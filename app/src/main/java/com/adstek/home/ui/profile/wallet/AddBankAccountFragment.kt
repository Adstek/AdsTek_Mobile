package com.adstek.home.ui.profile.wallet

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adstek.R
import com.adstek.data.remote.requests.LoginRequest
import com.adstek.data.remote.requests.PaymentWalletRequest
import com.adstek.data.remote.response.WalletResponse
import com.adstek.databinding.FragmentAddBankAccountBinding
import com.adstek.drivers.onboarding.SignInFragmentDirections
import com.adstek.drivers.onboarding.events.OnboaringEvents
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.extensions.navigateTo
import com.adstek.extensions.observeEventLiveData
import com.adstek.extensions.observeLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.extensions.toast
import com.adstek.home.ui.dialog.PWAlertDialog
import com.adstek.home.ui.profile.wallet.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBankAccountFragment : Fragment() {
    private lateinit var binding: FragmentAddBankAccountBinding

    private val walletViewModel: WalletViewModel by viewModels()
    private val args: AddBankAccountFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentAddBankAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.wallet == null){
            binding.tvTitle.text = "Add Bank Account"
            binding.btnAdd.onClick {
                addBankAccountWallet(isEdit = false)
            }
        } else {
            binding.tvTitle.text = "Edit Bank Account"
            populateData(args.wallet!!)
            binding.btnAdd.onClick {
                addBankAccountWallet(isEdit = true)
            }
        }

        binding.back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }

            binding.btnDeactive.setButtonColor(Color.GRAY)
            binding.btnDeactive.onClick  {
                showDeleteDialog()
            }

        observeLiveData(walletViewModel.addWalletResponse, onError = {
            toast("Please try again")
        }){
            toast("Bank Account Added Successfully")
            popBackStackOrFinish()

        }

        observeLiveData(walletViewModel.editWalletResponse, onError = {
            toast("Please try again")
        }){
            toast("Bank Account Edited Successfully")
            popBackStackOrFinish()

        }

        observeLiveData(walletViewModel.deleteWalletResponse, onError = {
            toast("Please try again")
        }){
            toast("Bank Account Deleted Successfully")
            popBackStackOrFinish()

        }
    }

    private fun showDeleteDialog() {
        val builder = PWAlertDialog(
            requireContext(),
            "Delete Wallet",
            "Do you want to delete your bank account??"
        )
        builder
            .setPositiveButton(R.string.ok) { _, _ ->
                walletViewModel.deleteWallet(args.wallet?.id.toString())
            }
            .setNegativeButton(R.string.cancel, null)
        builder.create().show()
    }
    private fun populateData(wallet: WalletResponse)= with(binding){
         wallet.accountName?.let { accountName.setFieldText(it) }
         bankName.setFieldText(wallet.bankName)
        wallet.code?.let { bankCode.setFieldText(it) }
        wallet.accountNumber?.let { accountNumber.setFieldText(it) }
        wallet.email?.let { email.setFieldText(it) }
        wallet.phoneNumber?.let { phoneNumber.setFieldText(it) }
    }

    private fun addBankAccountWallet(isEdit: Boolean) = with(binding){
        val bankAccountName = accountName.getFieldText()
        val bankName = bankName.getFieldText()
        val bankCode = bankCode.getFieldText()
        val bankAccountNumber = accountNumber.getFieldText()
        val email = email.getFieldText()
        val phoneNumber = phoneNumber.getFieldText()

        when {
            email.isEmpty() -> toast("Enter Email")
            bankAccountName.isEmpty() -> toast("Enter Name on Account")
            bankName.isEmpty() -> toast("Enter Bank Name")
            bankCode.isEmpty() -> toast("Enter Bank Code")
            bankAccountNumber.isEmpty() -> toast("Enter Bank Account Number")
            phoneNumber.isEmpty() -> toast("Enter Phonr Number")
            else -> {
                val paymentWalletRequest = PaymentWalletRequest(bankName = bankName, bankCode = bankCode, accountName = bankAccountName, email = email, accountNumber = bankAccountNumber, phoneNumber = phoneNumber, paymentType = "bank"  )
                if (isEdit){
                    walletViewModel.editWallet(args.wallet?.id.toString(),paymentWalletRequest)
                } else {
                    walletViewModel.addWallet(paymentWalletRequest)
                }
            }
        }

    }
}