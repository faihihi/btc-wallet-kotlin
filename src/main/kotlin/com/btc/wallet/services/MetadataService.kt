package com.btc.wallet.services

import arrow.core.Either
import com.btc.wallet.db.BTCTransaction
import com.btc.wallet.model.TransactionError

interface MetadataService {

    /**
     * Save single transaction to DB
     *
     * @param transaction
     * @return
     */
    fun saveSingleTransaction(transaction: BTCTransaction): Either<TransactionError, BTCTransaction>

    /**
     * Get all transactions before a datetime from DB
     *
     * @param endDateTime
     * @return
     */
    fun getTransactionsBefore(endDateTime: String): Either<TransactionError, List<BTCTransaction>>
}