package com.btc.wallet.services

import com.btc.wallet.model.*

interface TransactionService {

    /**
     * Save BTC transaction and return status response
     *
     * @param request
     * @return SaveTransactionResponse
     */
    fun saveTransaction(request: SaveTransactionRequest): SaveTransactionResponse

    /**
     * Get wallet balances by period of time
     *
     * @param request
     * @return GetBalancesResponse
     */
    fun getWalletBalances(request: GetBalancesRequest): GetBalancesResponse
}