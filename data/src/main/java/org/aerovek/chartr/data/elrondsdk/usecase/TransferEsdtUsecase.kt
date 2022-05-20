package org.aerovek.chartr.data.elrondsdk.usecase

import org.aerovek.chartr.data.model.elrond.esdt.EsdtConstants.ESDT_TRANSACTION_VALUE
import org.aerovek.chartr.data.model.elrond.account.Account
import org.aerovek.chartr.data.model.elrond.address.Address
import org.aerovek.chartr.data.model.elrond.network.NetworkConfig
import org.aerovek.chartr.data.model.elrond.transaction.Transaction
import org.aerovek.chartr.data.model.elrond.wallet.Wallet
import org.aerovek.chartr.data.util.ScUtils
import org.aerovek.chartr.data.util.toHex
import java.math.BigInteger

@Deprecated("DO NOT USE!!! This should be converted into its respective repository implementation")
class TransferEsdtUsecase internal constructor(
    private val sendTransactionUsecase: SendTransactionUsecase
) {

    fun execute(
        account: Account,
        wallet: Wallet,
        networkConfig: NetworkConfig,
        gasPrice: Long,
        extraGasLimit: Long? = null, // <an appropriate amount for the method call>
        receiver: Address,
        tokenIdentifier: String,
        valueToTransfer: BigInteger,
        funcName: String? = null,
        funcArgs: List<String> = emptyList(),
    ): Transaction {
        val args = mutableListOf(
            tokenIdentifier.toHex(),
            valueToTransfer.toHex()
        ).apply {
            if (funcName != null){
                add(funcName.toHex())
            }
            i