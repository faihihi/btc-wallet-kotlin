package com.btc.wallet.model

data class SaveTransactionResponse(val success: Boolean,
                                   val message: String,
                                   val error: String?)
