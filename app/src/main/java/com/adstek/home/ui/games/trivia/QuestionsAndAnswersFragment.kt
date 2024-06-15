package com.adstek.home.ui.games.trivia

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.adstek.data.remote.requests.InteractRequest
import com.adstek.data.remote.response.TriviaResult
import com.adstek.databinding.FragmentQuestionsAndAnswersBinding
import com.adstek.extensions.navigateTo
import com.adstek.extensions.observeLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.extensions.toast
import com.adstek.home.AdsActivity
import com.adstek.home.ui.games.trivia.viewmodel.TriviaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionsAndAnswersFragment : Fragment() {
    private lateinit var binding: FragmentQuestionsAndAnswersBinding

    private val triviaViewModel: TriviaViewModel by activityViewModels()

    private var currentQuestionIndex = 0
    private var questions: MutableList<TriviaResult>? = null
    private var isQuizCompleted = false

//    private var countDownTimer: CountDownTimer? = null

    override fun onStart() {
        super.onStart()
        currentQuestionIndex = 0
        questions?.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuestionsAndAnswersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData(triviaViewModel.triviaResponse) {trivia ->
            trivia?.message?.results?.let { results ->
                if (results.isNotEmpty()) {
                    questions = results.toMutableList()
                    currentQuestionIndex = 0
                    displayQuestion(questions!![currentQuestionIndex])
                }
            }
        }


        observeLiveData(triviaViewModel.interactResponse, enableProgressBar = false, onError = {}) { response ->
            requireActivity().runOnUiThread {
                if (isQuizCompleted && !response?.message?.video.isNullOrEmpty()) {
                    startActivity(Intent(requireActivity(), AdsActivity::class.java).apply {
                        putExtra("VIDEO_LINK", response?.message?.video)
                    })
                    isQuizCompleted = false // Reset the flag after showing the ad
                }
            }
            if (isQuizCompleted) {
                questions?.clear()
                triviaViewModel.getTrivia()
            }
        }

        triviaViewModel.getTrivia()
    }

//    private fun startTimer() {
//        countDownTimer = object : CountDownTimer(5000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                binding.tvTimer.text = "${millisUntilFinished / 1000}"
//            }
//
//            override fun onFinish() {
//                Toast.makeText(context, "Time's up!", Toast.LENGTH_SHORT).show()
//                moveToNextQuestion()
//            }
//        }.start()
//    }


    private fun displayQuestion(result: TriviaResult) {
        binding.tvQuestion.text = result.question
        binding.optionA.setText(result.options.first() .option_a ?: "N/A")
        binding.optionB.setText(result.options.first() .option_b ?: "N/A")
        binding.optionC.setText(result.options.first() .option_c ?: "N/A")
        binding.optionD.setText(result.options.first() .option_d ?: "N/A")

        val correctAnswer = result.options[0].answer
        val optionButtons = listOf(binding.optionA, binding.optionB, binding.optionC, binding.optionD)
        for (button in optionButtons) {
            button.setOnClickListener {
                if (button.getText() == correctAnswer) {
                    button.setbackgroundcolor("#098f5a","#FFFFFFFF")
                } else {
                    button.setbackgroundcolor("#E91E63","#FFFFFFFF")
                }
//                countDownTimer
                moveToNextQuestionWithDelay()

            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Do not clear questions if the quiz is completed
        if (!isQuizCompleted) {
            questions?.clear()
            currentQuestionIndex = 0
        }
    }
    private fun moveToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < (questions?.size ?: 0)) {
            displayQuestion(questions!![currentQuestionIndex])
//            startTimer() // Restart the timer for the next question

        } else {
            isQuizCompleted = true
            val interactRequest = InteractRequest(true)
            triviaViewModel.interactWithApi(interactRequest)
        }
    }

    private fun moveToNextQuestionWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            resetButtonColors()
            moveToNextQuestion()
        }, 1000) // 1 second delay
    }

    private fun resetButtonColors() {
        binding.optionA.setbackgroundcolor("#FFFFFFFF", "#FF000000")
        binding.optionB.setbackgroundcolor("#FFFFFFFF", "#FF000000")
        binding.optionC.setbackgroundcolor("#FFFFFFFF", "#FF000000")
        binding.optionD.setbackgroundcolor("#FFFFFFFF", "#FF000000")
    }
}