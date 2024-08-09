package com.adstek.home.ui.profile.wallet.repository

import com.adstek.api.AdsTekApi
import com.adstek.data.remote.requests.InteractRequest
import com.adstek.data.remote.requests.PaymentWalletRequest
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.DidInteractResponse
import com.adstek.data.remote.response.TriviaQuestions
import com.adstek.data.remote.response.WalletResponse
import com.adstek.util.SharedPref
import com.adstek.extensions.makeNetworkRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val adsTekApi: AdsTekApi,
    private val sharedPrefFunctions: SharedPref
) {

    fun addPaymentWallet(paymentWallet: PaymentWalletRequest): Flow<DataState<Any>> {
        return makeNetworkRequest { adsTekApi.addPaymentWallet(paymentWallet) }
    }

    fun getWallets(): Flow<DataState<List<WalletResponse>>> {
        return makeNetworkRequest { adsTekApi.getAllWallet() }
    }

    fun deleteWallet(walletId: String): Flow<DataState<Any>> {
        return makeNetworkRequest { adsTekApi.deleteWallet(walletId) }
    }

    fun editWallet(walletId: String, paymentWallet: PaymentWalletRequest): Flow<DataState<Any>> {
        return makeNetworkRequest { adsTekApi.editWallet(walletId, paymentWallet) }
    }
}