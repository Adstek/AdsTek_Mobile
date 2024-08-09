package com.adstek.home.ui.profile.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adstek.R
import com.adstek.databinding.FragmentBankAccountBinding
import com.adstek.extensions.navigateTo
import com.adstek.extensions.observeLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.home.ui.profile.wallet.adapter.BankWalletAdapter
import com.adstek.home.ui.profile.wallet.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BankAccountFragment : Fragment() {
    private lateinit var binding: FragmentBankAccountBinding
    private val walletViewModel: WalletViewModel by activityViewModels()


    private val bankWalletAdapter by lazy {
        BankWalletAdapter(
            onItemClick = {item ->
                navigateTo(BankAccountFragmentDirections.actionBankAccountFragmentToAddBankAccountFragment(item))
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {
        // Inflate the layout for this fragment
        binding = FragmentBankAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBankAdapter()

        observeLiveData(walletViewModel.walletResponse, onError =  {
            binding.emptyAccount.visibility = View.VISIBLE
        }){ walletResponses ->
            val bankAccount = walletResponses?.filter { it.paymentType == "bank" }
            bankAccount?.let {
                bankWalletAdapter.submitList(bankAccount)
            }
            if (bankAccount != null) {
                if (bankAccount.isEmpty()){
                    binding.emptyAccount.visibility = View.VISIBLE
                    binding.rvWallet.visibility = View.GONE
                } else {
                    binding.emptyAccount.visibility = View.GONE
                    binding.rvWallet.visibility = View.VISIBLE
                }
            }
        }
        binding.addBankAccount.setOnClickListener {
            navigateTo(BankAccountFragmentDirections.actionBankAccountFragmentToAddBankAccountFragment(null))
        }

        binding.back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }
    }

    override fun onResume() {
        super.onResume()
        walletViewModel.getWallets()
    }

    private fun setUpBankAdapter() = with(binding){
        rvWallet.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            adapter = bankWalletAdapter
        }
    }



}