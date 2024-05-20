package com.adstek.drivers.onboarding.events

import com.adstek.data.remote.models.LoginRequest
import com.adstek.data.remote.models.ResetPassword
import com.adstek.data.remote.models.VerifyEmail
import com.adstek.data.remote.models.auth.RegisterUserModel
import com.adstek.data.remote.models.auth.StartResetPassword

sealed class OnboaringEvents {
    data class onSignInEvent(val loginRequest: LoginRequest) : OnboaringEvents()
    data class onSignUpEvent(val registerUserModel: RegisterUserModel) : OnboaringEvents()
    data class onVerifyEmail(val verifyEmail: VerifyEmail) : OnboaringEvents()
    data class onStartResetPassword(val startResetPassword: StartResetPassword) : OnboaringEvents()
    data class onResetPassword(val resetPassword: ResetPassword) : OnboaringEvents()
    // Add more click events here if needed
}