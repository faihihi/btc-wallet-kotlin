package com.btc.wallet.services

import arrow.core.Either
import com.btc.wallet.db.BTCTransaction
import com.btc.wallet.model.TransactionError

interface MetadataService {
    fun saveSingleTransaction(transaction: BTCTransaction): Either<TransactionError, BTCTransaction>
    fun getTransactionsBefore(endDateTime: String): Either<TransactionError, List<BTCTransaction>>
}