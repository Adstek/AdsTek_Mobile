package com.adstek.drivers.onboarding

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.adstek.R
import com.adstek.data.remote.models.LoginRequest
import com.adstek.data.remote.models.auth.StartResetPassword
import com.adstek.databinding.FragmentSignInBinding
import com.adstek.drivers.onboarding.events.OnboaringEvents
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.extensions.navigateTo
import com.adstek.extensions.observeEventLiveData
import com.adstek.extensions.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

        observeEventLiveData(onBoardingViewModel.loginResponse, onError = {
            toast(getString(R.string.failed_signin))
        }){
            toast(getString(R.string.success_signin))
        }

        observeEventLiveData(onBoardingViewModel.startResetResponse, onError = {
            toast(it)
        }){
            toast("")
            navigateTo(SignInFragmentDirections.navigateToResetPassword())
        }
    }
    private fun onClicks() = with(binding){
        tvSignIn.setOnClickListener {
            navigateTo(SignInFragmentDirections.navigateToSignIn())
        }
        btnSignIn.onClick{
            loginUser()
        }
        tvForgetPin.setOnClickListener {
            val email = emailTextField.getFieldText()

            when{
                email.isEmpty() -> toast("Enter Email")
                else -> {

                    onBoardingViewModel.handleEvent(OnboaringEvents.onStartResetPassword(
                        StartResetPassword(email)
                    ))
                }
            }
        }
    }

    private fun loginUser() = with(binding){
        val email = emailTextField.getFieldText()
        val password = passwordTextField.getFieldText()

        when {
            email.isEmpty() -> toast("Enter Email")
            password.isEmpty() -> toast("Enter Password")
             else -> {
                 onBoardingViewModel.handleEvent(OnboaringEvents.onSignInEvent(LoginRequest(email, password)))
             }
        }

    }
}