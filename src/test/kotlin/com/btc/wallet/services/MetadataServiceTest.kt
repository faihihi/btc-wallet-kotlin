package com.btc.wallet.services

import arrow.core.Either
import com.btc.wallet.TestData.defaultBTCTransaction
import com.btc.wallet.TestData.defaultDateTimeStr
import com.btc.wallet.db.TransactionRepository
import com.btc.wallet.model.TransactionError
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MetadataServiceTest {

    @MockK
    private lateinit var mockTransactionRepository: TransactionRepository

    @InjectMockKs
    private lateinit var metadataService: MetadataServiceImpl

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
        clearAllMocks()
    }

    @Nested
    inner class SaveSingleTransaction {
        @Test
        fun `should return correctly`() {
            every { mockTransactionRepository.save(any()) } returns defaultBTCTransaction

            val result = metadataService.saveSingleTransaction(defaultBTCTransaction)
            verify(exactly = 1) { mockTransactionRepository.save(any()) }
            assert(result.isRight())
            assertEquals(Either.Right(defaultBTCTransaction), result)
        }

        @Test
        fun `should return error when db throw error`() {
            every { mockTransactionRepository.save(any()) } throws Exception("unable to save")

            val expectedError = Either.Left(TransactionError("unable to save"))
            val result = metadataService.saveSingleTransaction(defaultBTCTransaction)
            verify(exactly = 1) { mockTransactionRepository.save(any()) }
            assert(result.isLeft())
            assertEquals(expectedError, result)
        }
    }

    @Nested
    inner class GetTransactionsBefore {
        @Test
        fun `should return correctly`() {
            every { mockTransactionRepository.getBefore(any()) } returns listOf(defaultBTCTransaction)

            val result = metadataService.getTransactionsBefore(defaultDateTimeStr)
            verify(exactly = 1) { mockTransactionRepository.getBefore(any()) }
            assert(result.isRight())
            assertEquals(Either.Right(listOf(defaultBTCTransaction)), result)
        }

        @Test
        fun `should return error when db throw error`() {
            every { mockTransactionRepository.getBefore(any()) } throws Exception("unable to fetch")

            val expectedError = Either.Left(TransactionError("unable to fetch"))
            val result = metadataService.getTransactionsBefore(defaultDateTimeStr)
            verify(exactly = 1) { mockTransactionRepository.getBefore(any()) }
            assert(result.isLeft())
            assertEquals(expectedError, result)
        }
    }
}