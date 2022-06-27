package com.btc.wallet.services

import org.springframework.stereotype.Service
import arrow.core.Either
import com.btc.wallet.model.*
import arrow.core.computations.either
import com.btc.wallet.utils.DateTimeUtils
import com.btc.wallet.db.BTCTransaction
import com.btc.wallet.validators.RequestValidators
import com.btc.wallet.model.TransactionError

@Service
class TransactionServiceImpl(val validators: RequestValidators, val metadataService: MetadataService) :
    TransactionService {

    override fun saveTransaction(request: SaveTransactionRequest): SaveTransactionResponse {
        val result: Either<TransactionError, BTCTransaction> = either.eager {
            val validRequest = validators.validateSaveTransactionRequest(request).bind()
            val btcTransaction = BTCTransaction(
                date_time = validRequest.datetime,
                amount = validRequest.amount,
                created_at = DateTimeUtils.getCurrentDateTime()
            )
            // TODO: L4 publish to messaging queue
            metadataService.saveSingleTransaction(btcTransaction).bind()
        }

        val response = when (result) {
            is Either.Left -> SaveTransactionResponse(
                success = false,
                message = "Unable to save the transaction",
                error = result.value.error
            )
            is Either.Right -> SaveTransactionResponse(
                success = true,
                message = "Transaction at ${request.datetime} is saved.",
                error = null
            )
        }
        return response
    }

    override fun getWalletBalances(request: GetBalancesRequest): GetBalancesResponse {
        val result: Either<TransactionError, List<BTCTransaction>> = either.eager {
            val validRequest = validators.validateGetBalancesRequest(request).bind()
            val result = metadataService.getTransactionsBefore(validRequest.endDateTime).bind()
            buildHourlyBalances(validRequest, result)
        }

        val response = when (result) {
            is Either.Left -> GetBalancesResponse(transactions = listOf(), error = result.value.error)
            is Either.Right -> GetBalancesResponse(transactions = result.value, error = null)
        }
        return response
    }

    internal fun buildHourlyBalances(
        request: GetBalancesRequest,
        transactions: List<BTCTransaction>
    ): List<BTCTransaction> {
        val startDateTime = DateTimeUtils.parseToUTCDateTime(request.startDateTime)
        val endDateTime = DateTimeUtils.parseToUTCDateTime(request.endDateTime)
        val (beforeStart, afterStart) = transactions.partition {
            DateTimeUtils.parseToUTCDateTime(it.date_time).isBefore(startDateTime)
        }

        var totalAmount = beforeStart.sumOf { it.amount }
        val hourIntervals = DateTimeUtils.getHourIntervalsOfPeriod(startDateTime, endDateTime)
        val transactionList = hourIntervals.map { interval ->
            val transactionWithinPeriod = afterStart.filter {
                DateTimeUtils.parseToUTCDateTime(it.date_time)
                    .isAfter(interval.minusHours(1)) && DateTimeUtils.parseToUTCDateTime(it.date_time).isBefore(interval)
            }
            totalAmount += transactionWithinPeriod.sumOf { it.amount }
            BTCTransaction(date_time = DateTimeUtils.parseToString(interval), amount = totalAmount, created_at = null)
        }

        return transactionList
    }

}