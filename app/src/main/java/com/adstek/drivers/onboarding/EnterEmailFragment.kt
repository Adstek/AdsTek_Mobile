package com.adstek.drivers.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adstek.data.remote.models.VerifyEmail
import com.adstek.databinding.FragmentEnterEmailBinding
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.util.SharedPref
import com.adstek.util.navigateTo
import com.adstek.util.observeEventLiveData
import com.adstek.util.popBackStackOrFinish
import com.adstek.util.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EnterEmailFragment : Fragment() {
    private lateinit var binding: FragmentEnterEmailBinding

    @Inject
    lateinit var sharedPref: SharedPref;
    private val args: EnterEmailFragmentArgs by navArgs()
    private var userId = ""

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterEmailBinding.inflate(inflater)
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