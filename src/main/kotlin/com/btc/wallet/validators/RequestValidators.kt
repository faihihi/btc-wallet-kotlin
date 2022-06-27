package com.btc.wallet.validators

import arrow.core.*
import com.btc.wallet.utils.DateTimeUtils
import com.btc.wallet.model.*
import org.springframework.stereotype.Service

@Service
class RequestValidators {

    fun validateSaveTransactionRequest(request: SaveTransactionRequest): Either<TransactionError, SaveTransactionRequest> {
        return when {
            request.datetime.isEmpty() -> emptyValidationErr("datetime")
            !validDateTime(request.datetime) -> invalidDateTimeFormatErr("datetime")
            else -> Either.Right(request)
        }
    }

    fun validateGetBalancesRequest(request: GetBalancesRequest): Either<TransactionError, GetBalancesRequest> {
        return when {
            request.startDateTime.isEmpty() || request.endDateTime.isEmpty() -> emptyValidationErr("startDateTime or endDateTime")
            !validDateTime(request.startDateTime) || !validDateTime(request.endDateTime) -> invalidDateTimeFormatErr("startDateTime or endDateTime")
            !validStartEndDateTime(
                request.startDateTime,
                request.endDateTime
            ) -> Either.Left(TransactionError("Request Validation Error: startDateTime is after endDateTime"))
            else -> Either.Right(request)
        }
    }

    private fun validDateTime(value: String): Boolean {
        return try {
            DateTimeUtils.parseToUTCDateTime(value)
            true
        } catch (_: Throwable) {
            false
        }
    }

    private fun validStartEndDateTime(start: String, end: String): Boolean {
        return try {
            DateTimeUtils.parseToUTCDateTime(start).isBefore(DateTimeUtils.parseToUTCDateTime(end))
        } catch (_: Throwable) {
            false
        }
    }

    private fun emptyValidationErr(fieldName: String): Either.Left<TransactionError> =
        Either.Left(TransactionError("$fieldName cannot be empty"))

    private fun invalidDateTimeFormatErr(fieldName: String): Either.Left<TransactionError> =
        Either.Left(TransactionError("Request Validation Error: $fieldName is in the wrong format, please use this format yyyy-MM-dd'T'HH:mm:ssZ (ex. 2019-10-05T14:45:11+07:00)"))
}