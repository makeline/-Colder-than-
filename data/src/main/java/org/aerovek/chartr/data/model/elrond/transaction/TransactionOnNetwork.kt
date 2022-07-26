package org.aerovek.chartr.data.model.elrond.transaction

import org.aerovek.chartr.data.model.elrond.address.Address
import java.math.BigInteger

data class TransactionOnNetwork(
    val sender: Address,
    val receiver: Address,
    val senderUsername: String?,
    val receiverUsername: String?,
    val nonce: Long,
    val value: BigInteger,
    val gasPrice: Long,
    val gasLimit: Long,
    val signature: String,
    val hash: String,
    val data: String?,
    val status: String,
    val timestamp: Long,
    val gasUsed: Long,
    val receiverShard: Long,