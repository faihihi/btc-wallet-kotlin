package com.btc.wallet.services

import arrow.core.Either
import com.btc.wallet.db.BTCTransaction
import com.btc.wallet.db.TransactionRepository
import com.btc.wallet.model.TransactionError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MetadataServiceImpl : MetadataService {

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    override fun saveSingleTransaction(transaction: BTCTransaction): Either<TransactionError, BTCTransaction> {
        return Either.catch { transactionRepository.save(transaction) }
            .mapLeft { TransactionError(it.message ?: "Unable to save to DB") }
    }

    override fun getTransactionsBefore(endDateTime: String): Either<TransactionError, List<BTCTransaction>> {
        return Either.catch { transactionRepository.getBefore(endDateTime) }
            .mapLeft { TransactionError(it.message ?: "Unable to fetch from DB") }
    }
}