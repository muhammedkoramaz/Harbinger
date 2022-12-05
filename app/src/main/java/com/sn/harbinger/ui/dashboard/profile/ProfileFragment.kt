package com.sn.harbinger.ui.dashboard.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sn.harbinger.R
import com.sn.harbinger.data.model.Image
import com.sn.harbinger.databinding.ImageBottomSheetBinding
import com.sn.harbinger.databinding.ProfileFragmentBinding
import com.sn.harbinger.ui.auth.AuthActivity
import com.sn.harbinger.util.IOUtil
import com.sn.harbinger.util.State
import com.sn.harbinger.util.extensions.gone
import com.sn.harbinger.util.extensions.loadImageWithGlideBase64
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var imageBottomSheetDialog: BottomSheetDialog
    private lateinit var profileImage: Bitmap
    private val vm: ProfileViewModel by viewModels()
    private val binding by lazy {
        ProfileFragmentBinding.inflate(layoutInflater)
    }

    private val requestStoragePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                chooseGallery()
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                takePhoto()
            }
        }

    private val selectImageFromGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { imageUri ->
            if (Build.VERSION.SDK_INT > 28) {
                profileImage = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireContext().contentResolver,
                        imageUri
                    )
                )
                val request = Image(image = IOUtil.bitmapConvertToBase64(profileImage))
                vm.addUserImage(request)
            } else {
                profileImage = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    imageUri
                )
                val request = Image(image = IOUtil.bitmapConvertToBase64(profileImage))
                vm.addUserImage(request)
            }
        }
    }

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                profileImage = result.data?.extras?.get("data") as Bitmap
                val request = Image(image = IOUtil.bitmapConvertToBase64(profileImage))
                vm.addUserImage(request)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()

        binding.img.setOnClickListener {
            showSelectImageBottomSheet()
        }

        binding.btnLogout.onClick = {
            //todo maybe it is necessary to forward the exit information to the service
            vm.removeToken()
            activity?.finish()
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun chooseGallery() {
        selectImageFromGalleryLauncher.launch("image/*")
    }

    private fun takePhoto() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoLauncher.launch(takePhotoIntent)
    }

    private fun checkStoragePermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                chooseGallery()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                //todo show toast permission is needed
            }
            else -> {
                requestStoragePermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                //todo show toast permission is needed
            }
            else -> {
                requestCameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private fun showSelectImageBottomSheet() {
        imageBottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val bindingSheet = DataBindingUtil.inflate<ImageBottomSheetBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.image_bottom_sheet,
            null,
            false
        )
        imageBottomSheetDialog.setContentView(bindingSheet.root)
        bindingSheet.ivClose.setOnClickListener { imageBottomSheetDialog.dismiss() }
        bindingSheet.btnSelectPhoto.setOnClickListener {
            imageBottomSheetDialog.dismiss()
            checkStoragePermission()
        }
        bindingSheet.btnTakePhoto.setOnClickListener {
            imageBottomSheetDialog.dismiss()
            checkCameraPermission()

        }

        if (vm.noProfileImage.get()) {
            bindingSheet.btnDeletePhoto.gone()
        } else {
            bindingSheet.btnDeletePhoto.setOnClickListener {
                imageBottomSheetDialog.dismiss()
                val image = Image("")
                vm.addUserImage(image)
            }
        }

        imageBottomSheetDialog.show()
    }


    private fun initObserve() {
        vm.userInfoLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Success -> {
                    val image = response.data.result?.image
                    vm.setNoImageData(image)
                    binding.img.loadImageWithGlideBase64(image.toString())
                }
                is State.Error -> {
                    // todo show error maybe
                }
                else -> {

                }
            }
        }
        vm.imageUploadLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Success -> {
                    val image = response.data.result?.image
                    vm.setNoImageData(image)
                    binding.img.loadImageWithGlideBase64(image.toString())
                }
                is State.Error -> {
                    // todo show error
                }
                else -> {
                }
            }
        }

    }

}