package com.adstek.home.ui.profile.wallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adstek.data.remote.requests.InteractRequest
import com.adstek.data.remote.requests.PaymentWalletRequest
import com.adstek.extensions.BaseViewModel
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.DidInteractResponse
import com.adstek.data.remote.response.TriviaQuestions
import com.adstek.data.remote.response.WalletResponse
import com.adstek.extensions.emitFlowResults
import com.adstek.util.SharedPref
import com.adstek.home.ui.games.trivia.repository.TriviaRepository
import com.adstek.home.ui.profile.wallet.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val sharedPref: SharedPref
): BaseViewModel() {


    private val _addWalletResponse: MutableLiveData<DataState<Any>> = MutableLiveData()
    val addWalletResponse: LiveData<DataState<Any>> get() = _addWalletResponse

    private val _deleteWalletResponse: MutableLiveData<DataState<Any>> = MutableLiveData()
    val deleteWalletResponse: LiveData<DataState<Any>> get() = _deleteWalletResponse

    private val _editWalletResponse: MutableLiveData<DataState<Any>> = MutableLiveData()
    val editWalletResponse: LiveData<DataState<Any>> get() = _editWalletResponse

    private val _walletResponse: MutableLiveData<DataState<List<WalletResponse>>> = MutableLiveData()
    val walletResponse: LiveData<DataState<List<WalletResponse>>> get() = _walletResponse


    fun addWallet(paymentWalletRequest: PaymentWalletRequest) = emitFlowResults(_addWalletResponse) {
        walletRepository.addPaymentWallet(paymentWalletRequest)
    }

    fun deleteWallet(walletId: String) = emitFlowResults(_deleteWalletResponse) {
        walletRepository.deleteWallet(walletId)
    }

    fun editWallet(walletId: String, paymentWallet: PaymentWalletRequest) = emitFlowResults(_editWalletResponse) {
        walletRepository.editWallet(walletId, paymentWallet)
    }

    fun getWallets() = emitFlowResults(_walletResponse) {
        walletRepository.getWallets()
    }



}