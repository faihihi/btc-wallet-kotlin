package com.btc.wallet.controllers

import com.btc.wallet.TestData.getBalancesRequest
import com.btc.wallet.TestData.getBalancesResponse
import com.btc.wallet.TestData.saveTransactionRequest
import com.btc.wallet.TestData.saveTransactionResponse
import com.btc.wallet.services.TransactionService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
class TransactionControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val mapper: ObjectMapper
) {

    @MockkBean
    lateinit var transactionService: TransactionService

    @Test
    fun `should call saveTransaction correctly`() {
        every { transactionService.saveTransaction(saveTransactionRequest) } returns saveTransactionResponse

        val result = mockMvc.perform(
            post("/wallet/save").content(mapper.writeValueAsString(saveTransactionRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val responseJson = result.response.contentAsString
        assertEquals(mapper.writeValueAsString(saveTransactionResponse), responseJson)
    }

    @Test
    fun `should call getWalletBalances correctly`() {
        every { transactionService.getWalletBalances(getBalancesRequest) } returns getBalancesResponse

        val result = mockMvc.perform(
            post("/wallet/get").content(mapper.writeValueAsString(getBalancesRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        val responseJson = result.response.contentAsString
        assertEquals(mapper.writeValueAsString(getBalancesResponse), responseJson)
    }
}