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
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.util.Constants
import com.adstek.util.SharedPref
import com.adstek.extensions.loadFromFile
import com.adstek.extensions.navigateTo
import com.adstek.extensions.observeEventLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.extensions.toast
import com.adstek.util.view.removeView
import com.github.dhaval2404.imagepicker.ImagePicker
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

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;


        val idTypes = resources.getStringArray(R.array.id_type)
        binding.dropDownIDType.setDropdownList(idTypes)
        onClicks()

        observeEventLiveData(onBoardingViewModel.registerCustomerResponse, onError = {
            toast(it)
        }) {response ->
            navigateTo(SecondRegistrationFragmentDirections.navigateToVerifyEmail(email = sharedPref.getPref(Constants.KEY_EMAIL, ""), userId =response?.user_id))
        }

        binding.dropDownIDType.getDropDownAutoText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                if (binding.dropDownIDType.getSelectedValue()?.isNotEmpty() == true){
                    sharedPref.setPref(Constants.KEY_ID_TYPE, binding.dropDownIDType.getSelectedValue())
                }
            }
        }

        binding.idCardNumberField.getTextInputEditText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                if (binding.idCardNumberField.getFieldText().isNotEmpty()){
                    sharedPref.setPref(Constants.KEY_ID_NUMBER, binding.idCardNumberField.getFieldText())
                    binding.idCardNumberField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#EFF1F3")
                } else {
                    binding.idCardNumberField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#FFFFFF")
                }

            }
        }

        binding.idNumberTextField.getTextInputEditText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                if (binding.idNumberTextField.getFieldText().isNotEmpty()){
                    sharedPref.setPref(Constants.KEY_NUMBER_PLATE, binding.idNumberTextField.getFieldText())
                }
                binding.idNumberTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#EFF1F3")
            } else {
                binding.idNumberTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#FFFFFF")
            }
        }

        binding.phoneTextField.getTextInputEditText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                if (binding.phoneTextField.getFieldText().isNotEmpty()){
                    sharedPref.setPref(Constants.KEY_PHONE_NUMBER, binding.phoneTextField.getFieldText())
                    binding.phoneTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#EFF1F3")
                } else {
                    binding.phoneTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#FFFFFF")
                }
            }
        }
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

        loadFromFile(requireContext(), binding.idCardImgView, idImage)

        if (idImage.isNotEmpty()){
            if (idImage.isNotEmpty()){
                binding.tvUploadText.removeView()
                binding.tvTypeUpload.removeView()
                binding.tvUploadIcon.removeView()

            }
        }

    }


    private fun onClicks() = with(binding){
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

        val profileImage = sharedPref.getPref(Constants.KEY_PROFILE_IMAGE, "")


        val confirmPassword = passwordConfirmTextField.getFieldText()

        when {
            password != confirmPassword -> toast("Password do not match")
            else -> {
//                onBoardingViewModel.saveSecondScreenRegistrationData(
//                    numberPlate = numberPlate,
//                    nationalIdNumber = idNumber,
//                    phoneNumber = phoneNumber,
//                    idImage = idCardUrl
//                )
                    onBoardingViewModel.registerDriver(
                    sharedPref.userModel.copy (
                        nationalIdNumber = idNumber.takeIf { it.isNotEmpty() },
                        numberPlate = numberPlate.takeIf { it.isNotEmpty() },
                        nationaldType = idCardType?.takeIf { it.isNotEmpty()},
                        phoneNumber = phoneNumber.takeIf { it.isNotEmpty()},
                        nationalDImage = idCardUrl,
                        profileImage = profileImage,
                        password = password
                    )
                )
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
                                binding.tvUploadText.removeView()
                                binding.tvTypeUpload.removeView()
                                binding.tvUploadIcon.removeView()
                        }
                    }
                }
            }
        }
    }


