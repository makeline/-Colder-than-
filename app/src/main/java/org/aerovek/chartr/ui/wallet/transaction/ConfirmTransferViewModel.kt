/*
The MIT License (MIT)

Copyright (c) 2023-present Aerovek

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package org.aerovek.chartr.ui.wallet.transaction

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import org.aerovek.chartr.data.buildconfig.EnvironmentRepository
import org.aerovek.chartr.data.exceptions.ElrondException
import org.aerovek.chartr.data.model.elrond.address.Address
import org.aerovek.chartr.data.model.elrond.network.NetworkConfig
import org.aerovek.chartr.data.model.elrond.transaction.Transaction
import org.aerovek.chartr.data.model.elrond.wallet.Wallet
import org.aerovek.chartr.data.repository.elrond.AccountRepository
import org.aerovek.chartr.data.repository.elrond.ElrondNetworkRepository
import org.aerovek.chartr.data.repository.elrond.TransactionRepository
import org.aerovek.chartr.data.util.toHex
import org.aerovek.chartr.ui.AppConstants
import org.aerovek.chartr.ui.BaseViewModel
import org.aerovek.chartr.util.DispatcherProvider
import org.aerovek.chartr.util.FirebaseHelper
import java.lang.Exception
import java.math.BigInteger
import java.util.*

class ConfirmTransferViewModel(
    app: Application,
    private val networkRepository: ElrondNetworkRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val sharedPreferences: SharedPreferences,
    private val environmentRepository: EnvironmentRepository
    ) : BaseViewModel(app) {
    val transferAmount = MutableLiveData("")
    val usdAmount = MutableLiveData("")
    val recipientAddress = MutableLiveData("")
    val truncatedAddress = MutableLiveData("")
    val networkFee = MutableLiveData("")
    val closeButtonClicked = LiveEvent<Unit>()
    val showLoadingView = MutableLiveData(true)
    val showPinPad = LiveEvent<Unit>()
    val transactionComplete = LiveEvent<Unit>()
    val transactionFailed = LiveEvent<Unit>()

    private lateinit var transaction: Transaction

    private val senderAddress =
        Address.fromBech32(sharedPreferences.getString(AppConstants.UserPrefsKeys.WALLET_ADDRESS, null) ?: "")

    fun initialize(amount: String, usdAmountDisplay: String, asset: String) {
        viewModelScope.launch(dispatch