package com.adstek.drivers.onboarding.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.adstek.databinding.FragmentFirstRegistrationBinding
import com.adstek.drivers.onboarding.viewModel.OnBoardingViewModel
import com.adstek.util.Constants
import com.adstek.util.SharedPref
import com.adstek.extensions.loadFromFile
import com.adstek.extensions.navigateTo
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.util.view.removeView
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class FirstRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentFirstRegistrationBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private  var profileImageUrl = ""

    @Inject
    lateinit var sharedPref: SharedPref


    private val onBoardingViewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstRegistrationBinding.inflate(inflater)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickActions()



        binding.dropDownGenderTextField.setDropdownList(arrayOf("male", "female"))
        binding.dropDownNationalityTextField.setDropdownList(arrayOf("Ghanaian"))


        binding.firstNameTextField.getTextInputEditText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                if (binding.firstNameTextField.getFieldText().isNotEmpty()){
                    sharedPref.setPref(Constants.KEY_FIRST_NAME, binding.firstNameTextField.getFieldText())
                }
                binding.firstNameTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#EFF1F3")
            } else {
                binding.firstNameTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#FFFFFF")

            }
        }

        binding.lastNameTextField.getTextInputEditText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                if (binding.lastNameTextField.getFieldText().isNotEmpty()) {
                    sharedPref.setPref(Constants.KEY_LAST_NAME, binding.lastNameTextField.getFieldText())
                }
                binding.lastNameTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#EFF1F3")
            } else {
                binding.lastNameTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#FFFFFF")

            }
        }

        binding.emailTextField.getTextInputEditText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                if (binding.emailTextField.getFieldText().isNotEmpty()){
                    sharedPref.setPref(Constants.KEY_EMAIL, binding.emailTextField.getFieldText())
                }
                binding.emailTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#EFF1F3")
            } else {
                binding.emailTextField.getTextInputLayout() .boxBackgroundColor = Color.parseColor("#FFFFFF")

            }
        }

        binding.dropDownGenderTextField.getDropDownAutoText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){

                if (binding.dropDownGenderTextField.getSelectedValue()?.isNotEmpty() == true){
                    val gender = binding.dropDownGenderTextField.getSelectedValue()
                    sharedPref.setPref(Constants.KEY_GENDER, gender)
                }
            }
        }

        binding.dropDownNationalityTextField.getDropDownAutoText().setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                if (binding.dropDownNationalityTextField.getSelectedValue()?.isNotEmpty() == true){
                    sharedPref.setPref(Constants.KEY_NATIONALITY, binding.dropDownNationalityTextField.getSelectedValue())
                }
            }
        }
    }

    private fun onClickActions() = with(binding){

        continueBtn.onClick{
            navigateTo(FirstRegistrationFragmentDirections.navigateToSecondFragment())
        }

        back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }
        uploadImage.setOnClickListener {
            selectImage(false, false)

            sharedPref.setPref(Constants.KEY_GENDER,  binding.dropDownGenderTextField.getSelectedValue())
            sharedPref.setPref(Constants.KEY_NATIONALITY, binding.dropDownNationalityTextField.getSelectedValue())

        }

    }

    override fun onResume() {
        super.onResume()
        reloadForm()
    }

    private fun reloadForm() = with(binding){
        val lastName = sharedPref.getPref(Constants.KEY_LAST_NAME, "")
        val firstName = sharedPref.getPref(Constants.KEY_FIRST_NAME, "")
        val profilePicture = sharedPref.getPref(Constants.KEY_PROFILE_IMAGE, "")
        val email = sharedPref.getPref(Constants.KEY_EMAIL, "")
        val gender = sharedPref.getPref(Constants.KEY_GENDER, "")
        val nationality = sharedPref.getPref(Constants.KEY_NATIONALITY, "")

        dropDownGenderTextField.setSelectedValue(gender)
        dropDownNationalityTextField.setSelectedValue(nationality)
        firstNameTextField.setFieldText(firstName)
        lastNameTextField.setFieldText(lastName)
        emailTextField.setFieldText(email)
        loadFromFile(requireContext(), binding.profileImgView, profilePicture)

        if (profilePicture.isNotEmpty()){
            binding.tvUploadText.removeView()
            binding.tvTypeUpload.removeView()
            binding.tvUploadIcon.removeView()
        }

    }

    private fun updateUserRegisterSharedPref() = with(binding) {
        onBoardingViewModel.saveFirstScreenRegistrationData(
            emailTextField.getFieldText(),
            dropDownGenderTextField.getSelectedValue().toString(),
            firstNameTextField.getFieldText(),
            lastNameTextField.getFieldText(),
            profileImageUrl,
            dropDownNationalityTextField.getSelectedValue().toString()
        )
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
                        profileImageUrl = uri.toString()
                        loadFromFile(requireContext(), binding.profileImgView, profileImageUrl)
                        sharedPref.setPref(Constants.KEY_PROFILE_IMAGE,  uri.toString())

                        binding.tvUploadText.removeView()
                        binding.tvTypeUpload.removeView()
                        binding.tvUploadIcon.removeView()

                    }
                }
            }
        }
    }
}