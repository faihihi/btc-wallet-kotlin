package com.btc.wallet.db

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table("btc_transaction")
data class BTCTransaction(
    val date_time: String,
    val amount: Double,
    @PrimaryKey
    @JsonIgnore
    val created_at: String?
)