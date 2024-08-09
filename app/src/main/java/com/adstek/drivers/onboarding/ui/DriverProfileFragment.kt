package com.adstek.drivers.onboarding.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adstek.MainActivity
import com.adstek.R
import com.adstek.data.local.ProfileScreenModel
import com.adstek.databinding.FragmentDriverProfileBinding
import com.adstek.extensions.navigateTo
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.home.ui.dialog.PWAlertDialog
import com.adstek.home.ui.profile.ProfileAdapter
import com.adstek.util.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DriverProfileFragment : Fragment() {
    private lateinit var binding: FragmentDriverProfileBinding

    @Inject
    lateinit var sharedPref: SharedPref

    private val profileAdapter by lazy { ProfileAdapter {
        when(it.title){
            "View Wallets" -> {
                navigateTo(DriverProfileFragmentDirections.actionDriverProfileFragmentToWalletFragment())
            }
            "Profile Information" -> {
                navigateTo(DriverProfileFragmentDirections.actionDriverProfileFragmentToProfileFragment())
            }
            "Privacy Policy" -> {
                navigateTo(DriverProfileFragmentDirections.actionDriverProfileFragmentToPrivacyPolicyFragment())
            }
            "Contact Information" -> {
                navigateTo(DriverProfileFragmentDirections.actionDriverProfileFragmentToContactUSFragment())
            }
            "Terms of Use" -> {
                navigateTo(DriverProfileFragmentDirections.actionDriverProfileFragmentToTermsOfUseFragment())
            }
            "Logout" -> {
                showLogoutDialog()
            }

        }
    }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDriverProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEvaluationAdapter()
        profileAdapter.submitList(getProfileList())

        binding.back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }

    }

    private fun getProfileList(): List<ProfileScreenModel>{
        val list = mutableListOf<ProfileScreenModel>()
        list.add(ProfileScreenModel(R.drawable.outline_person_24, "Profile Information"))
        list.add(ProfileScreenModel(R.drawable.outline_person_24, "KYC Information"))
        list.add(ProfileScreenModel(R.drawable.outline_wallet_24, "View Wallets"))
        list.add(ProfileScreenModel(R.drawable.outline_contact_support_24, "Contact Information"))
        list.add(ProfileScreenModel(R.drawable.outline_assignment_24, "Terms of Use"))
        list.add(ProfileScreenModel(R.drawable.outline_privacy_tip_24, "Privacy Policy"))
        list.add(ProfileScreenModel(R.drawable.outline_logout_24, "Logout"))
        return list
    }

    private fun showLogoutDialog() {
        val builder = PWAlertDialog(
            requireContext(),
            "Logout",
            "Do you want to logout?"
        )
        builder
            .setPositiveButton(R.string.ok) { _, _ ->
                sharedPref.clearSharedPref()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton(R.string.cancel, null)
        builder.create().show()
    }


    private fun setUpEvaluationAdapter() {
        binding.rvProfile.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            adapter = profileAdapter
        }
        }
    }