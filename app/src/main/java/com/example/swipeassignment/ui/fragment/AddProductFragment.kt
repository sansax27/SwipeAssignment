package com.example.swipeassignment.ui.fragment

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.swipeassignment.R
import com.example.swipeassignment.data.UIStatus
import com.example.swipeassignment.databinding.FragmentAddProductBinding
import com.example.swipeassignment.viewmodel.AddProductFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    private val viewModel by viewModels<AddProductFragmentViewModel>()

    private val binding: FragmentAddProductBinding by lazy {
        FragmentAddProductBinding.inflate(layoutInflater)
    }

    private var imgPath: String? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                requireContext().contentResolver.query(uri, null, null, null, null).use { cursorData ->
                    cursorData?.let { cursor ->
                        cursor.moveToFirst()
                        imgPath = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                        val file = File(requireContext().filesDir, imgPath!!)
                        val inputStream = requireContext().contentResolver.openInputStream(uri)
                        val outputStream = FileOutputStream(file)
                        var read = 0
                        val maxBufferSize = 1 * 1024 * 1024
                        val bytesAvailable: Int = inputStream?.available() ?: 0
                        //int bufferSize = 1024;
                        val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                        val buffers = ByteArray(bufferSize)
                        while (inputStream?.read(buffers).also {
                                if (it != null) {
                                    read = it
                                }
                            } != -1) {
                            outputStream.write(buffers, 0, read)
                        }
                        inputStream?.close()
                        outputStream.close()
                        binding.uploadImgIv.setImageURI(it)
                        binding.uploadImgCv.visibility = View.VISIBLE
                    }
                }

            }
        }


    private val getRequestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getContent.launch("image/*")
            } else {
                Toast.makeText(activity, getString(R.string.give_image_permission_string), Toast.LENGTH_LONG).show()
            }
        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.addProductDataLiveData.observe(this) {
            when(it) {
                is UIStatus.Success -> {
                    binding.addProductPb.visibility = View.GONE
                    binding.addContentRoot.visibility = View.VISIBLE
                    Toast.makeText(context, it.data.message, Toast.LENGTH_LONG).show()
                }
                is UIStatus.Loading -> {
                    binding.addProductPb.visibility = View.VISIBLE
                    binding.addContentRoot.visibility = View.GONE
                }
                is UIStatus.Error -> {
                    binding.addProductPb.visibility = View.GONE
                    binding.addContentRoot.visibility = View.VISIBLE
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                is UIStatus.Empty -> {}
            }
        }
        binding.uploadImgBt.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                getRequestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                getContent.launch("image/*")
            }
        }
        binding.enterProductTypeSpinner.apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, resources.getStringArray(R.array.product_type_array)).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_item)
            }
        }
        binding.addProductDataBt.setOnClickListener {
            if (binding.enterProductNameEt.text.isEmpty() || binding.enterProductNameEt.text.isNullOrBlank() ||
                binding.enterProductPriceEt.text.isEmpty() || binding.enterProductPriceEt.text.isNullOrBlank() ||
                binding.enterProductTaxEt.text.isEmpty() || binding.enterProductTaxEt.text.isNullOrBlank()) {
                Toast.makeText(context, getString(R.string.please_enter_product_data_string), Toast.LENGTH_LONG).show()
            } else {
                Timber.e("Image Path "+imgPath)
                if (imgPath != null) {
                    val img = File(requireContext().filesDir, imgPath!!)
                    val requestFile =
                        img.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val imgBody = MultipartBody.Part.createFormData("files[]", img.name, requestFile)
                    Timber.e("With Image")
                    viewModel.addProductData(binding.enterProductNameEt.text.toString(),
                        binding.enterProductTypeSpinner.selectedItem as String, binding.enterProductPriceEt.text.toString(),
                        binding.enterProductTaxEt.text.toString(), imgBody)

                } else {
                    Timber.e("Without Image")
                    viewModel.addProductData(binding.enterProductNameEt.text.toString(),
                        binding.enterProductTypeSpinner.selectedItem as String, binding.enterProductPriceEt.text.toString(),
                        binding.enterProductTaxEt.text.toString(), null)
                }

            }
        }
        super.onViewCreated(view, savedInstanceState)
    }


}