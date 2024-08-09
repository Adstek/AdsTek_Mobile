package com.adstek.home.ui.profile.wallet

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adstek.MainActivity
import com.adstek.R
import com.adstek.data.remote.requests.PaymentWalletRequest
import com.adstek.data.remote.response.WalletResponse
import com.adstek.databinding.FragmentAddMobileMoneyBinding
import com.adstek.extensions.observeLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.extensions.toast
import com.adstek.home.ui.dialog.PWAlertDialog
import com.adstek.home.ui.profile.wallet.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMobileMoneyFragment : Fragment() {
    private lateinit var binding: FragmentAddMobileMoneyBinding

    private val walletViewModel: WalletViewModel by viewModels()
    private val args: AddMobileMoneyFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMobileMoneyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupServiceProviders()

        if (args.wallet == null){
            binding.tvTitle.text = "Add Mobile Money Account"
            binding.addMomoBtn.onClick {
                addMobileMoney(isEdit = false)
            }
        } else {
            binding.tvTitle.text = "Edit Mobile Money Account"
            populateData(args.wallet!!)
            binding.addMomoBtn.onClick {
                addMobileMoney(isEdit = true)
            }
        }

            binding.btnDeactive.setButtonColor(Color.GRAY)
            binding.btnDeactive.onClick  {
                showDeleteDialog()
            }


        observeLiveData(walletViewModel.deleteWalletResponse, onError = {
            toast("Please try again")
        }){
            toast("Momo Account Deleted Successfully")
            popBackStackOrFinish()

        }

        observeLiveData(walletViewModel.editWalletResponse, onError = {
            toast("Please try again")
        }){
            toast("Momo Account Edited Successfully")
            popBackStackOrFinish()

        }

        observeLiveData(walletViewModel.addWalletResponse, onError = {
            toast("Please try again")
        }){
            toast("Momo Account Added Successfully")
            popBackStackOrFinish()
        }

        binding.back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }
    }

    private fun populateData(wallet: WalletResponse)= with(binding){
        wallet.accountName?.let { accountName.setFieldText(it) }
        wallet.phoneNumber?.let { phoneNumber.setFieldText(it) }
        dropDownServiceType.getDropDownAutoText().setText(wallet.bankName, false)

    }

    private fun setupServiceProviders() {
        val idTypes = resources.getStringArray(R.array.service_providers)
        binding.dropDownServiceType.setDropdownList(idTypes)
        binding.dropDownServiceType.getDropDownAutoText().setDropDownBackgroundResource(R.color.white)
    }

    private fun addMobileMoney(isEdit: Boolean) = with(binding) {
        val momoName = accountName.getFieldText()
        val serviceProvider = dropDownServiceType.getDropDownAutoText().text
        val phoneNumber = phoneNumber.getFieldText()

        when {
            momoName.isEmpty() -> toast("Enter Name on  Phone Number")
            serviceProvider.isEmpty() -> toast("Select Service Provider")
            phoneNumber.isEmpty() -> toast("Enter Phone Number")

            else -> {
                val paymentWalletRequest = PaymentWalletRequest(bankName = serviceProvider.toString(), bankCode = "", accountName = momoName, email = "", accountNumber = phoneNumber, phoneNumber = phoneNumber, paymentType = "mobile_money"  )
                if (isEdit){
                    walletViewModel.editWallet(args.wallet?.id.toString(),paymentWalletRequest)
                } else {
                    walletViewModel.addWallet(paymentWalletRequest)
                }
            }
        }

    }


    private fun showDeleteDialog() {
        val builder = PWAlertDialog(
            requireContext(),
            "Delete Wallet",
            "Do you want to delete your momo account??"
        )
        builder
            .setPositiveButton(R.string.ok) { _, _ ->
                walletViewModel.deleteWallet(args.wallet?.id.toString())
            }
            .setNegativeButton(R.string.cancel, null)
        builder.create().show()
    }
}