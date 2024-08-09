package com.adstek.home.ui.profile.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adstek.databinding.FragmentMobileMoneyBinding
import com.adstek.extensions.navigateTo
import com.adstek.extensions.observeLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.home.ui.profile.wallet.adapter.BankWalletAdapter
import com.adstek.home.ui.profile.wallet.adapter.MomoWalletAdapter
import com.adstek.home.ui.profile.wallet.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileMoneyFragment : Fragment() {
    private lateinit var binding: FragmentMobileMoneyBinding
    private val walletViewModel: WalletViewModel by activityViewModels()


    private val momoWalletAdapter by lazy {
        MomoWalletAdapter(
            onItemClick = {wallet->
                navigateTo(MobileMoneyFragmentDirections.actionMobileMoneyFragmentToAddMobileMoneyFragment(wallet))
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMobileMoneyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMomoAdapter()


        binding.back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }
        binding.add?.setOnClickListener {
            navigateTo(MobileMoneyFragmentDirections.actionMobileMoneyFragmentToAddMobileMoneyFragment())
        }

        observeLiveData(walletViewModel.walletResponse, onError = {
            binding.emptyAccount.visibility = View.VISIBLE
            binding.rvWallet.visibility = View.GONE
        }){
            it?.let { it1 -> momoWalletAdapter.submitList(it.filter { it.paymentType != "bank" }) }
            if (it != null) {
                if (it.isEmpty()){
                    binding.emptyAccount.visibility = View.VISIBLE
                    binding.rvWallet.visibility = View.GONE
                } else {
                    binding.emptyAccount.visibility = View.GONE
                    binding.rvWallet.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        walletViewModel.getWallets()
    }



    private fun setUpMomoAdapter() = with(binding){
        rvWallet.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            adapter = momoWalletAdapter
        }
    }



}