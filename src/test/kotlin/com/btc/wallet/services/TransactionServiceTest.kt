package com.btc.wallet.services

import arrow.core.Either
import com.btc.wallet.TestData.defaultBTCTransaction
import com.btc.wallet.TestData.getBalancesRequest
import com.btc.wallet.TestData.saveTransactionRequest
import com.btc.wallet.TestData.saveTransactionResponse
import com.btc.wallet.db.BTCTransaction
import com.btc.wallet.model.GetBalancesRequest
import com.btc.wallet.model.GetBalancesResponse
import com.btc.wallet.model.SaveTransactionResponse
import com.btc.wallet.model.TransactionError
import com.btc.wallet.validators.RequestValidators
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TransactionServiceTest {

    @MockK
    private lateinit var mockMetadataService: MetadataService

    @MockK
    private lateinit var mockValidators: RequestValidators

    @InjectMockKs
    private lateinit var transactionService: TransactionServiceImpl

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
        clearAllMocks()
    }

    @Nested
    inner class SaveTransaction {
        @Test
        fun `should make all calls and return correctly`() {
            every { mockValidators.validateSaveTransactionRequest(saveTransactionRequest) } returns Either.Right(
                saveTransactionRequest
            )
            every { mockMetadataService.saveSingleTransaction(any()) } returns Either.Right(defaultBTCTransaction)

            val result = transactionService.saveTransaction(saveTransactionRequest)
            verify(exactly = 1) { mockValidators.validateSaveTransactionRequest(any()) }
            verify(exactly = 1) { mockMetadataService.saveSingleTransaction(any()) }
            assertEquals(saveTransactionResponse, result)
        }

        @Test
        fun `should return response with error when validator failed`() {
            every { mockValidators.validateSaveTransactionRequest(saveTransactionRequest) } returns Either.Left(
                TransactionError("invalid request")
            )

            val expectedResponse = SaveTransactionResponse(false, "Unable to save the transaction", "invalid request")

            val result = transactionService.saveTransaction(saveTransactionRequest)
            verify(exactly = 1) { mockValidators.validateSaveTransactionRequest(any()) }
            assertEquals(expectedResponse, result)
        }
    }

    @Nested
    inner class GetWalletBalances {
        @Test
        fun `should make all calls and return correctly`() {
            every { mockValidators.validateGetBalancesRequest(getBalancesRequest) } returns Either.Right(
                getBalancesRequest
            )
            every { mockMetadataService.getTransactionsBefore(any()) } returns Either.Right(listOf(defaultBTCTransaction))

            val expectedResponse =
                GetBalancesResponse(listOf(BTCTransaction("2019-10-05T08:00:00+0000", 13.355, null)), null)
            val result = transactionService.getWalletBalances(getBalancesRequest)
            verify(exactly = 1) { mockValidators.validateGetBalancesRequest(any()) }
            verify(exactly = 1) { mockMetadataService.getTransactionsBefore(any()) }
            assertEquals(expectedResponse, result)
        }

        @Test
        fun `should return response with error when repo throw error`() {
            every { mockValidators.validateGetBalancesRequest(getBalancesRequest) } returns Either.Right(
                getBalancesRequest
            )
            every { mockMetadataService.getTransactionsBefore(any()) } returns Either.Left(TransactionError("some DB error"))

            val expectedResponse = GetBalancesResponse(listOf(), "some DB error")
            val result = transactionService.getWalletBalances(getBalancesRequest)
            verify(exactly = 1) { mockValidators.validateGetBalancesRequest(any()) }
            verify(exactly = 1) { mockMetadataService.getTransactionsBefore(any()) }
            assertEquals(expectedResponse, result)
        }
    }

    @Nested
    inner class BuildHourlyBalances {
        @Test
        fun `should build hourly balances correctly`() {
            val request = GetBalancesRequest("2019-10-05T10:58:05+00:00", "2019-10-05T15:20:05+00:00")
            val transactions = listOf(
                BTCTransaction("2019-10-04T10:08:05+00:00", 25.50, null),
                BTCTransaction("2019-10-04T20:28:05+00:00", 300.0, null),
                BTCTransaction("2019-10-05T10:50:05+00:00", 15.25, null),
                BTCTransaction("2019-10-05T12:30:05+00:00", 100.0, null),
                BTCTransaction("2019-10-06T00:58:05+00:00", 150.0, null)
            )

            val expectedList = listOf(
                BTCTransaction("2019-10-05T11:00:00+0000", 340.75, null),
                BTCTransaction("2019-10-05T12:00:00+0000", 340.75, null),
                BTCTransaction("2019-10-05T13:00:00+0000", 440.75, null),
                BTCTransaction("2019-10-05T14:00:00+0000", 440.75, null),
                BTCTransaction("2019-10-05T15:00:00+0000", 440.75, null)
            )

            val result = transactionService.buildHourlyBalances(request, transactions)
            assertEquals(expectedList, result)
        }

        @Test
        fun `should correct list when transactions are empty`() {
            val request = GetBalancesRequest("2019-10-05T10:58:05+00:00", "2019-10-05T15:20:05+00:00")
            val transactions = listOf<BTCTransaction>()

            val expectedList = listOf(
                BTCTransaction("2019-10-05T11:00:00+0000", 0.0, null),
                BTCTransaction("2019-10-05T12:00:00+0000", 0.0, null),
                BTCTransaction("2019-10-05T13:00:00+0000", 0.0, null),
                BTCTransaction("2019-10-05T14:00:00+0000", 0.0, null),
                BTCTransaction("2019-10-05T15:00:00+0000", 0.0, null)
            )

            val result = transactionService.buildHourlyBalances(request, transactions)
            assertEquals(expectedList, result)
        }

        @Test
        fun `should correct list when request period is within an hour`() {
            val request = GetBalancesRequest("2019-10-05T10:58:05+00:00", "2019-10-05T10:59:05+00:00")
            val transactions = listOf(
                BTCTransaction("2019-10-04T10:08:05+00:00", 25.50, null),
                BTCTransaction("2019-10-04T20:28:05+00:00", 300.0, null),
                BTCTransaction("2019-10-05T10:50:05+00:00", 15.25, null),
                BTCTransaction("2019-10-05T12:30:05+00:00", 100.0, null),
                BTCTransaction("2019-10-06T00:58:05+00:00", 150.0, null)
            )

            val expectedList = listOf(
                BTCTransaction("2019-10-05T11:00:00+0000", 340.75, null)
            )

            val result = transactionService.buildHourlyBalances(request, transactions)
            assertEquals(expectedList, result)
        }
    }
}