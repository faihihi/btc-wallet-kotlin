package com.btc.wallet.db

object DBConstants {
    const val tableName = "btc_transaction"
    const val DATE_TIME = "date_time"  // Transaction date and time in UTC 0
    const val AMOUNT = "amount"     // Transaction amount
    const val CREATED_AT = "created_at" // Date and time that the transaction is stored in DB
    const val DATE = "date"       // Transaction date in UTC 0
    const val HOUR = "hour"       // Transaction hour in UTC 0
}