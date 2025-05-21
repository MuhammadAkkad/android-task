package com.company.androidtask.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.company.androidtask.MainActivity
import com.company.androidtask.R
import com.company.androidtask.presentation.common.extensions.collectFlowOnStart
import com.company.androidtask.presentation.common.helper.DialogHelper
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel: VM

    @Inject
    lateinit var dialogHelper: DialogHelper

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun initListeners()

    abstract fun initResources()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        initResources()
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeUiState() {
        viewModel.uiState.collectFlowOnStart { uiState ->
            val mainActivity = activity as? MainActivity
            mainActivity?.setLoadingVisibility(uiState)
            if (uiState is UIState.Error) {
                mainActivity?.runOnUiThread {
                    dialogHelper.showDialog(
                        title = getString(R.string.dialog_error_title_generic),
                        message = "Error: ${uiState.message.takeIf { it.isNotEmpty() } ?: "An unknown error occurred"}",
                        positiveButtonText = getString(R.string.dialog_button_try_again),
                        onPositiveButtonClick = {
                            findNavController().navigate(
                                R.id.SplashFragment,
                                null,
                                androidx.navigation.navOptions {
                                    popUpTo(findNavController().graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            )
                        },
                        negativeButtonText = getString(R.string.exit),
                        onNegativeButtonClick = { mainActivity.finish() }
                    )
                }
            }
        }
    }

    fun navigate(action: Int) {
        if (findNavController().currentDestination?.id != action) {
            findNavController().navigate(action)
        }
    }

    fun navigateBackWithResult() {
        findNavController().popBackStack()
    }

    fun navigateBack() {
        findNavController().navigateUp()
    }
}