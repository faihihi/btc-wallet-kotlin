package com.btc.wallet.controllers

import com.btc.wallet.model.*
import com.btc.wallet.services.TransactionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/wallet")
class TransactionController(private val transactionService: TransactionService) {

    @PostMapping("/save")
    fun saveTransaction(@RequestBody request: SaveTransactionRequest): SaveTransactionResponse {
        return transactionService.saveTransaction(request)
    }

    @PostMapping("/get")
    fun getWalletBalances(@RequestBody getBalancesRequest: GetBalancesRequest): GetBalancesResponse {
        return transactionService.getWalletBalances(getBalancesRequest)
    }

}