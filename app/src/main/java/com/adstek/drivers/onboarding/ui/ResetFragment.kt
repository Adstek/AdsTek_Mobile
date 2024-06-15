package com.adstek.drivers.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.adstek.data.remote.requests.ResetPassword
import com.adstek.databinding.FragmentResetBinding
import com.adstek.drivers.onboarding.events.OnboaringEvents
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.extensions.observeEventLiveData
import com.adstek.extensions.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResetFragment : Fragment() {
    private lateinit var binding: FragmentResetBinding

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignIn.onClick {
            resetPassword()
        }

        observeEventLiveData(onBoardingViewModel.confirmPasswordResetResponse,
            onError = {
                toast("Invalid Credentials")
            })
        {
            toast("Password Reset is successful")
        }
    }


    private fun resetPassword() = with(binding){
        val oldPassword = tempPasswordTextField.getFieldText()
        val newPassword = newPasswordTextField.getFieldText()
        val confirmPassword = confirmPasswordTextField.getFieldText()

        when{
            oldPassword.isEmpty() -> toast("Temporary password cannot be blank")
            newPassword.isEmpty() -> toast("New password cannot be blank")
            confirmPassword.isEmpty() -> toast("Confirm password cannot be blank")
            confirmPassword != newPassword -> toast("Password does not match")
            else -> {
                onBoardingViewModel.handleEvent(OnboaringEvents.onResetPassword(ResetPassword(oldPassword, confirmPassword)))
            }
        }

        back.btnBack.setOnClickListener  {
            findNavController().popBackStack()
        }
    }
}