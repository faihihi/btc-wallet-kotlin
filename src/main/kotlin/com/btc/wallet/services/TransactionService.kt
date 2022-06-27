package com.btc.wallet.services

import com.btc.wallet.model.*

interface TransactionService {
    fun saveTransaction(request: SaveTransactionRequest): SaveTransactionResponse
    fun getWalletBalances(request: GetBalancesRequest): GetBalancesResponse
}