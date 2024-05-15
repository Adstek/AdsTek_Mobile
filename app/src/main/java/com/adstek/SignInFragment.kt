package com.adstek

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.adstek.data.remote.models.LoginRequest
import com.adstek.databinding.FragmentSignInBinding
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.util.navigateTo
import com.adstek.util.observeEventLiveData
import com.adstek.util.toast
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

        observeEventLiveData(onBoardingViewModel.loginResponse, onError = {
            toast("Verification Failed")
        }){
            toast("Email Verified")
        }

    }
    private fun onClicks() = with(binding){
        tvSignIn.setOnClickListener {
            navigateTo(SignInFragmentDirections.navigateToSignIn())
        }
        btnSignIn.onClick{
            loginUser()

        }
    }

    private fun loginUser() = with(binding){
        val email = emailTextField.getFieldText()
        val password = passwordTextField.getFieldText()

        when {
            email.isEmpty() -> toast("Enter Email")
            password.isEmpty() -> toast("Enter Password")
             else -> {
                 val login = LoginRequest(email, password)
                 onBoardingViewModel.login(login)
             }
        }

    }
}