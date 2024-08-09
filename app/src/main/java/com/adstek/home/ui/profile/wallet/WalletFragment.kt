package com.adstek.home.ui.profile.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adstek.R
import com.adstek.data.local.ProfileScreenModel
import com.adstek.data.local.model.WalletTypeModel
import com.adstek.databinding.FragmentWalletBinding
import com.adstek.extensions.navigateTo
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.home.ui.profile.ProfileAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : Fragment() {
    private lateinit var binding: FragmentWalletBinding

    private val walletAdapter by lazy { WalletAdapter {
            when(it.title) {
                "Mobile wallets" -> {
                    navigateTo(WalletFragmentDirections.actionWalletFragmentToMobileMoneyFragment())
                }
                "Bank Account" -> {
                    navigateTo(WalletFragmentDirections.actionWalletFragmentToBankAccountFragment())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEvaluationAdapter()
        walletAdapter.submitList(getWalletList())

        binding.back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }
    }

    private fun getWalletList(): List<WalletTypeModel>{
        val list = mutableListOf<WalletTypeModel>()
        list.add(WalletTypeModel(R.drawable.outline_account_balance_24, "Bank Account"))
        list.add(WalletTypeModel(R.drawable.outline_send_to_mobile_24, "Mobile wallets"))
        return list
    }

    private fun setUpEvaluationAdapter() {
        binding.rvWallet.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            adapter = walletAdapter
        }
    }

}