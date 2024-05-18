package com.adstek.drivers.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adstek.data.remote.models.VerifyEmail
import com.adstek.databinding.FragmentVerifyEmailBinding
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.util.SharedPref
import com.adstek.extensions.observeEventLiveData
import com.adstek.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VerifyEmailFragment : Fragment() {
    private lateinit var binding: FragmentVerifyEmailBinding

    @Inject
    lateinit var sharedPref: SharedPref;
    private val args: VerifyEmailFragmentArgs by navArgs()
    private var userId = ""

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyEmailBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            args.let {
                tvEmail.text = it.email
                userId = it.userId.toString()
            }
            btnSignIn.onClick{
                onClicks()
            }

        }

        observeEventLiveData(onBoardingViewModel.verifyEmailResponse, onError = {
            toast("OTP Invalid")
        }) {response ->
            toast("Email Verified")
        }
    }

    private fun onClicks() = with(binding){

        val otp = otplTextField.getFieldText()

        when{
            otp.isEmpty() -> toast("Enter the OTP")
            else -> {
                    val verifyEmail = VerifyEmail(userId.toInt(), otp.toInt())
                    onBoardingViewModel.verifyEmail(verifyEmail)
            }
        }

        back.btnBack.setOnClickListener  {
            findNavController().popBackStack()
        }
    }
}