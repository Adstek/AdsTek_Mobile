package com.adstek.drivers.onboarding.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adstek.R
import com.adstek.databinding.FragmentVerifyPhoneBinding
import com.adstek.util.Constants
import com.adstek.util.view.removeView
import com.adstek.util.view.showView
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyPhoneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class VerifyPhoneFragment : Fragment() {
  private lateinit var binding: FragmentVerifyPhoneBinding
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerifyPhoneBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCounter()
    }

    private fun setupCounter() = with(binding) {
        countDownTimer = object : CountDownTimer(
            Constants.ONE_MINUTE_COUNTER,
            Constants.COUNTER_INTERVAL
        ) {
            override fun onTick(millisRemain: Long) {

                val remainSeconds = millisRemain / Constants.COUNTER_INTERVAL
                resendText.removeView()
                resendCounter.apply {
                    showView()
                    text = String.format(getString(R.string.resend_otp_in), remainSeconds)
                }
            }

            override fun onFinish() {
                resendCounter.removeView()
                resendText.showView()
                countDownTimer.cancel()
            }
        }
        countDownTimer.start()
    }

}