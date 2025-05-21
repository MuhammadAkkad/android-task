package com.company.androidtask.presentation.module.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.company.androidtask.R
import com.company.androidtask.databinding.FragmentQrBinding
import com.company.androidtask.presentation.base.BaseFragment
import com.company.androidtask.presentation.base.BaseViewModel
import com.company.androidtask.presentation.common.helper.PermissionHelper
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QrScannerFragment : BaseFragment<FragmentQrBinding, BaseViewModel>() {

    override val viewModel: BaseViewModel by viewModels()

    @Inject
    lateinit var permissionHelper: PermissionHelper

    private lateinit var cameraProvider: ProcessCameraProvider

    private lateinit var previewView: PreviewView

    private var imageAnalysis: ImageAnalysis? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQrBinding {
        return FragmentQrBinding.inflate(inflater, container, false)
    }

    override fun initListeners() {}

    override fun initResources() {
        previewView = binding.previewView
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        permissionHelper.requestCameraPermission {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()

                val preview = buildPreviewUseCase()
                imageAnalysis = buildImageAnalysisUseCase()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)

            } catch (e: Exception) {
                dialogHelper.showDialog(
                    title = "QRScanner",
                    message = "Error initializing camera: ${e.message}",
                    negativeButtonText = getString(R.string.go_back),
                    onNegativeButtonClick = { navigateBack() }
                )
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun buildPreviewUseCase(): Preview {
        return Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun buildImageAnalysisUseCase(): ImageAnalysis {
        val barcodeScanner = BarcodeScanning.getClient()

        return ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().also {
                it.setAnalyzer(ContextCompat.getMainExecutor(requireContext())) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        barcodeScanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    if (barcode.valueType == Barcode.TYPE_TEXT) {
                                        val qrText = barcode.rawValue
                                        qrText?.let { text ->
                                            setFragmentResult(QR_SCAN_REQUEST_KEY, Bundle().apply {
                                                putString(QR_SCAN_RESULT_KEY, text)
                                            })
                                            cameraProvider.unbindAll()
                                            barcodeScanner.close()
                                            navigateBackWithResult()
                                        }
                                        return@addOnSuccessListener
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                dialogHelper.showDialog(
                                    title = getString(R.string.qr_error),
                                    message = e.message ?: getString(R.string.qr_error_description),
                                    negativeButtonText = getString(R.string.go_back),
                                    onNegativeButtonClick = { navigateBack() }
                                )
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                    }
                }
            }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageAnalysis?.setAnalyzer(ContextCompat.getMainExecutor(requireContext())) {}
        imageAnalysis = null
    }

    companion object {
        const val QR_SCAN_REQUEST_KEY = "qr_scan_request"
        const val QR_SCAN_RESULT_KEY = "qr_scan_result"
    }
}