package com.adstek.home.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adstek.R
import com.adstek.databinding.FragmentPrivacyPolicyBinding
import com.adstek.extensions.popBackStackOrFinish
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@AndroidEntryPoint
class PrivacyPolicyFragment : Fragment() {
    private lateinit var bindings: FragmentPrivacyPolicyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindings = FragmentPrivacyPolicyBinding.inflate(inflater)
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindings.termsOfUse.text = readTermsOfUse(R.raw.privacy_policy)
        bindings.btnBack.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }

    }

    private fun readTermsOfUse(content: Int): String {
        val inputStream = resources.openRawResource(content)

        var text = ""
        var reader: BufferedReader? = null

        try {
            reader = BufferedReader(InputStreamReader(inputStream))
            text = reader.readLines().joinToString("\n")
        } catch (e: IOException) {
            Timber.tag("termsOfUse").d("error: ${e.localizedMessage}")
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                Timber.tag("termsOfUse").d("error: ${e.localizedMessage}")
            }
        }

        return text
    }
}