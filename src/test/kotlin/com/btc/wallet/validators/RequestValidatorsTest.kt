package com.btc.wallet.validators

import arrow.core.Either
import com.btc.wallet.TestData.getBalancesRequest
import com.btc.wallet.TestData.saveTransactionRequest
import com.btc.wallet.model.TransactionError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RequestValidatorsTest {

    private val validators = RequestValidators()

    @Nested
    inner class ValidateSaveTransactionRequest {
        @Test
        fun `validate valid request`() {
            val result = validators.validateSaveTransactionRequest(saveTransactionRequest)
            assert(result.isRight())
            assertEquals(Either.Right(saveTransactionRequest), result)
        }

        @Test
        fun `invalidate when datetime is empty`() {
            val expectedError = Either.Left(TransactionError("datetime cannot be empty"))
            val result = validators.validateSaveTransactionRequest(saveTransactionRequest.copy(datetime = ""))
            assert(result.isLeft())
            assertEquals(expectedError, result)
        }

        @Test
        fun `invalidate when datetime format is invalid`() {
            val expectedError =
                Either.Left(TransactionError("Request Validation Error: datetime is in the wrong format, please use this format yyyy-MM-dd'T'HH:mm:ssZ (ex. 2019-10-05T14:45:11+07:00)"))
            val result = validators.validateSaveTransactionRequest(saveTransactionRequest.copy(datetime = "26-06-2022"))
            assert(result.isLeft())
            assertEquals(expectedError, result)
        }
    }

    @Nested
    inner class ValidateGetBalancesRequest {
        @Test
        fun `validate valid request`() {
            val result = validators.validateGetBalancesRequest(getBalancesRequest)
            assert(result.isRight())
            assertEquals(Either.Right(getBalancesRequest), result)
        }

        @Test
        fun `invalidate when datetime is empty`() {
            val expectedError = Either.Left(TransactionError("startDateTime or endDateTime cannot be empty"))
            val result = validators.validateGetBalancesRequest(getBalancesRequest.copy(startDateTime = ""))
            assert(result.isLeft())
            assertEquals(expectedError, result)
        }

        @Test
        fun `invalidate when startDateTime is not before endDateTime`() {
            val expectedError =
                Either.Left(TransactionError("Request Validation Error: startDateTime is after endDateTime"))
            val result =
                validators.validateGetBalancesRequest(getBalancesRequest.copy(startDateTime = "2019-10-06T15:58:05+07:00"))
            assert(result.isLeft())
            assertEquals(expectedError, result)
        }
    }

}