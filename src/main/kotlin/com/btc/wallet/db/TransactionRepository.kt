package com.btc.wallet.db

import com.btc.wallet.db.DBConstants.DATE_TIME
import com.btc.wallet.db.DBConstants.tableName
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.data.cassandra.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : CassandraRepository<BTCTransaction, String> {

    @Query("SELECT * FROM $tableName WHERE $DATE_TIME > ?0 AND $DATE_TIME < ?1 ALLOW FILTERING;")
    fun getByPeriod(start: String, end: String): List<BTCTransaction>

    @Query("SELECT * FROM $tableName WHERE $DATE_TIME < ?0 ALLOW FILTERING;")
    fun getBefore(end: String): List<BTCTransaction>
}