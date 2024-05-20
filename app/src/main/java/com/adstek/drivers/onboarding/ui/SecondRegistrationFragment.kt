package com.adstek.drivers.onboarding.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.adstek.R
import com.adstek.databinding.FragmentSecondRegistrationBinding
import com.adstek.drivers.onboarding.events.OnboaringEvents
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.util.Constants
import com.adstek.util.SharedPref
import com.adstek.extensions.loadFromFile
import com.adstek.extensions.navigateTo
import com.adstek.extensions.observeEventLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.extensions.setCustomFocusChangeListener
import com.adstek.extensions.setCustomFocusChangeListenerForDropdown
import com.adstek.extensions.toast
import com.adstek.util.view.removeView
import com.adstek.util.view.removeViews
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class SecondRegistrationFragment : Fragment() {
  private lateinit var binding: FragmentSecondRegistrationBinding
    private var idCardUrl = ""

    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()

    @Inject
    lateinit var sharedPref: SharedPref;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setupDropdown()
        setupFocusChangeListeners()
        onEventClicks()

        observeEventLiveData(onBoardingViewModel.registerCustomerResponse, onError = {
            toast(it)
        }) { response ->
            navigateTo(
                SecondRegistrationFragmentDirections.navigateToVerifyEmail(
                    email = sharedPref.getPref(Constants.KEY_EMAIL, ""),
                    userId = response?.user_id
                )
            )
        }
    }

    private fun setupDropdown() {
        val idTypes = resources.getStringArray(R.array.id_type)
        binding.dropDownIDType.setDropdownList(idTypes)
    }

    private fun setupFocusChangeListeners() = with(binding) {
        setCustomFocusChangeListenerForDropdown(
            dropDownIDType.getDropDownAutoText(),
            {dropDownIDType.getSelectedValue() },
            Constants.KEY_ID_TYPE,
            sharedPref
        )
        setCustomFocusChangeListener(
            idCardNumberField.getTextInputEditText(),
            { idCardNumberField.getFieldText() },
            Constants.KEY_ID_NUMBER,
            idCardNumberField.getTextInputLayout(),
            sharedPref
        )
        setCustomFocusChangeListener(
            idNumberTextField.getTextInputEditText(),
            {idNumberTextField.getFieldText() },
            Constants.KEY_NUMBER_PLATE,
            idNumberTextField.getTextInputLayout(),
            sharedPref
        )
        setCustomFocusChangeListener(
            phoneTextField.getTextInputEditText(),
            {phoneTextField.getFieldText()},
            Constants.KEY_PHONE_NUMBER,
            phoneTextField.getTextInputLayout(),
            sharedPref
        )
    }

    override fun onResume() {
        super.onResume()
        reloadForm()
    }

    private fun reloadForm() = with(binding){
        val idCardType = sharedPref.getPref(Constants.KEY_ID_TYPE, "")
        val idCardNumber = sharedPref.getPref(Constants.KEY_ID_NUMBER, "")
        val numberPlate = sharedPref.getPref(Constants.KEY_NUMBER_PLATE, "")
        val phoneNumber = sharedPref.getPref(Constants.KEY_PHONE_NUMBER, "")
        val idImage = sharedPref.getPref(Constants.KEY_ID_IMAGE, "")

        dropDownIDType.getDropDownAutoText().setText(idCardType, false)
        idCardNumberField.setFieldText(idCardNumber)
        idNumberTextField.setFieldText(numberPlate)
        phoneTextField.setFieldText(phoneNumber)

        loadFromFile(requireContext(), idCardImgView, idImage)

        if (idImage.isNotEmpty()){
            if (idImage.isNotEmpty()){
                root.removeViews(tvUploadText,tvTypeUpload, tvUploadIcon)
            }
        }

    }

    private fun onEventClicks() = with(binding){
        back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }
        uploadBtn.onClick {
            updateUserRegister()
        }
        uploadIDCard.setOnClickListener {
            selectImage()
        }
    }


    private fun updateUserRegister() = with(binding) {
        val idCardType = dropDownIDType.getSelectedValue()
        val idNumber = idNumberTextField.getFieldText()
        val numberPlate = phoneTextField.getFieldText()
        val phoneNumber = phoneTextField.getFieldText()
        val password = passwordTextField.getFieldText()
        val confirmPassword = passwordConfirmTextField.getFieldText()

        val profileImage = sharedPref.getPref(Constants.KEY_PROFILE_IMAGE, "")

        when {
            password.isEmpty() -> toast("Password cannot be blank")
            confirmPassword.isEmpty() -> toast("Confirm Password cannot be blank")
            password != confirmPassword -> toast("Password do not match")
            else -> {
                val registerUserModel = sharedPref.userModel.copy (
                    nationalIdNumber = idNumber.takeIf { it.isNotEmpty() },
                    numberPlate = numberPlate.takeIf { it.isNotEmpty() },
                    nationaldType = idCardType?.takeIf { it.isNotEmpty()},
                    phoneNumber = phoneNumber.takeIf { it.isNotEmpty()},
                    nationalDImage = idCardUrl,
                    profileImage = profileImage,
                    password = password
                )
                onBoardingViewModel.handleEvent(OnboaringEvents.onSignUpEvent(registerUserModel))
            }
        }

    }

    private fun selectImage(cameraOnly: Boolean = false, galleryOnly: Boolean = false) {

        val imagePicker = ImagePicker.with(this)
            .cropSquare()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .saveDir(File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ImagePicker"))

        if (cameraOnly) {
            imagePicker.cameraOnly()
        }
        if (galleryOnly){
            imagePicker.galleryOnly()
        }
        imagePicker.start(100)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewLifecycleOwner.lifecycleScope.launch {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    data.data?.let { uri ->
                        idCardUrl = uri.toString()
                        loadFromFile(requireContext(), binding.idCardImgView, idCardUrl)
                        sharedPref.setPref(Constants.KEY_ID_IMAGE, uri.toString())
                        binding.apply {
                            root.removeViews(tvUploadText, tvTypeUpload, tvUploadIcon)
                        }
                        }
                    }
                }
            }
        }
    }


