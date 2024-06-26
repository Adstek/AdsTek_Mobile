package com.adstek.drivers.onboarding.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adstek.data.remote.response.RegisterReponse
import com.adstek.data.remote.requests.LoginRequest
import com.adstek.data.remote.requests.VerifyEmail
import com.adstek.data.remote.requests.RegisterUserModel
import com.adstek.repository.AuthRepository
import com.adstek.extensions.BaseViewModel
import com.adstek.util.Constants
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.SignInResponse
import com.adstek.data.remote.response.Event
import com.adstek.data.remote.requests.ResendOTP
import com.adstek.data.remote.requests.ResetPassword
import com.adstek.data.remote.requests.StartResetPassword
import com.adstek.drivers.onboarding.events.OnboaringEvents
import com.adstek.util.SharedPref
import com.adstek.extensions.emitFlowResultsToEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPref: SharedPref
): BaseViewModel() {


    private val _registerCustomerResponse: MutableLiveData<Event<DataState<RegisterReponse>>> = MutableLiveData()
    val registerCustomerResponse: LiveData<Event<DataState<RegisterReponse>>> get() = _registerCustomerResponse


    private val _verifyEmailResponse: MutableLiveData<Event<DataState<Any>>> = MutableLiveData()
    val verifyEmailResponse: LiveData<Event<DataState<Any>>> get() = _verifyEmailResponse

  private val _resendOTPResponse: MutableLiveData<Event<DataState<Any>>> = MutableLiveData()
    val resendOTPResponse: LiveData<Event<DataState<Any>>> get() = _resendOTPResponse


    private val _startResetResponse: MutableLiveData<Event<DataState<Any>>> = MutableLiveData()
    val startResetResponse: LiveData<Event<DataState<Any>>> get() = _startResetResponse


    private val _confirmPasswordResetResponse: MutableLiveData<Event<DataState<Any>>> = MutableLiveData()
    val confirmPasswordResetResponse: LiveData<Event<DataState<Any>>> get() = _confirmPasswordResetResponse


    private val _loginResponse: MutableLiveData<Event<DataState<SignInResponse>>> = MutableLiveData()
    val loginResponse: LiveData<Event<DataState<SignInResponse>>> get() = _loginResponse


    private val _verifyPhoneResponse: MutableLiveData<Event<DataState<Any>>> = MutableLiveData()
    val verifyPhoneResponse: LiveData<Event<DataState<Any>>> get() = _verifyPhoneResponse



    fun handleEvent(event: OnboaringEvents) {
        when (event) {
            is OnboaringEvents.onSignInEvent -> login(event.loginRequest)
            is OnboaringEvents.onSignUpEvent -> registerDriver(event.registerUserModel)
            is OnboaringEvents.onVerifyEmail -> verifyEmail(event.verifyEmail)
            is OnboaringEvents.onStartResetPassword -> startResetPassword(event.startResetPassword)
            is OnboaringEvents.onResetPassword -> confirmResetPassword(event.resetPassword)
            else -> {}
        }
    }


    fun registerDriver(registerUserModel: RegisterUserModel) = emitFlowResultsToEvent(_registerCustomerResponse) {
        authRepository.registerDriver(registerUserModel)
    }

    fun verifyEmail(verifyEmail: VerifyEmail) = emitFlowResultsToEvent(_verifyEmailResponse) {
        authRepository.verifyEmail(verifyEmail)
    }

    fun resendOTP(resendOTP: ResendOTP) = emitFlowResultsToEvent(_resendOTPResponse) {
        authRepository.resendOTP(resendOTP)
    }

    fun startResetPassword(startResetPassword: StartResetPassword) = emitFlowResultsToEvent(_startResetResponse) {
        authRepository.startResetPassword(startResetPassword)
    }

    fun confirmResetPassword(resetPassword: ResetPassword) = emitFlowResultsToEvent(_confirmPasswordResetResponse) {
        authRepository.confirmResetPassword(resetPassword)
    }

    fun login(loginRequest: LoginRequest) = emitFlowResultsToEvent(_loginResponse) {
        authRepository.login(loginRequest)
    }

    fun saveFirstScreenRegistrationData(email: String, gender: String, firstName: String, lastName: String, profileImageUrl: String, nationality: String) {
        sharedPref.apply {
            setPref(Constants.KEY_EMAIL, email)
            setPref(Constants.KEY_GENDER, gender)
            setPref(Constants.KEY_FIRST_NAME, firstName)
            setPref(Constants.KEY_LAST_NAME, lastName)
            setPref(Constants.KEY_PROFILE_IMAGE, profileImageUrl)
            setPref(Constants.KEY_NATIONALITY, nationality)
        }
    }

    fun saveSecondScreenRegistrationData(numberPlate: String, nationalIdNumber: String, phoneNumber: String, idImage: String) {
        sharedPref.apply {
            setPref(Constants.KEY_NUMBER_PLATE, numberPlate)
            setPref(Constants.KEY_ID_NUMBER, nationalIdNumber)
            setPref(Constants.KEY_PHONE_NUMBER, phoneNumber)
            setPref(Constants.KEY_ID_IMAGE, idImage)
        }
    }



//    fun sendOtpOnPhoneNumber() = emitFlowResultsToEvent(_verifyPhoneResponse) {
//        authRepository.registerDriver()
//    }
}