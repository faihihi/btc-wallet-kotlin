package com.btc.wallet

import com.btc.wallet.db.BTCTransaction
import com.btc.wallet.model.*

object TestData {

    const val defaultDateTimeStr         = "2019-10-05T14:45:11+07:00"
    const val defaultAmount              = 13.355

    val defaultBTCTransaction      = BTCTransaction(defaultDateTimeStr, defaultAmount, "")
    val saveTransactionRequest      = SaveTransactionRequest(defaultDateTimeStr, defaultAmount)
    val saveTransactionResponse     = SaveTransactionResponse(
        success = true,
        message = "Transaction at $defaultDateTimeStr is saved.",
        error = null
    )

    val getBalancesRequest      = GetBalancesRequest(
        startDateTime = defaultDateTimeStr,
        endDateTime = "2019-10-05T15:58:05+07:00"
    )
    val getBalancesResponse     = GetBalancesResponse(
        transactions = listOf(defaultBTCTransaction),
        error = null
    )
}