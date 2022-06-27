package com.btc.wallet.model

import com.btc.wallet.db.BTCTransaction

data class GetBalancesResponse(val transactions: List<BTCTransaction>, val error: String?)
