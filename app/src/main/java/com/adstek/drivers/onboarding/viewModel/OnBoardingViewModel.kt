package com.adstek.drivers.onboarding.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adstek.data.remote.RegisterReponse
import com.adstek.data.remote.models.LoginRequest
import com.adstek.data.remote.models.VerifyEmail
import com.adstek.data.remote.models.auth.RegisterUserModel
import com.adstek.repository.AuthRepository
import com.adstek.extensions.BaseViewModel
import com.adstek.util.Constants
import com.adstek.data.remote.DataState
import com.adstek.data.remote.models.Event
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


    private val _loginResponse: MutableLiveData<Event<DataState<Any>>> = MutableLiveData()
    val loginResponse: LiveData<Event<DataState<Any>>> get() = _loginResponse


    private val _verifyPhoneResponse: MutableLiveData<Event<DataState<Any>>> = MutableLiveData()
    val verifyPhoneResponse: LiveData<Event<DataState<Any>>> get() = _verifyPhoneResponse


    fun registerDriver(registerUserModel: RegisterUserModel) = emitFlowResultsToEvent(_registerCustomerResponse) {
        authRepository.registerDriver(registerUserModel)
    }

    fun verifyEmail(verifyEmail: VerifyEmail) = emitFlowResultsToEvent(_verifyEmailResponse) {
        authRepository.verifyEmail(verifyEmail)
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