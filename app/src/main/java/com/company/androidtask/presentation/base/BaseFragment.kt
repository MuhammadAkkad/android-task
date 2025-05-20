package com.company.androidtask.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.company.androidtask.MainActivity
import com.company.androidtask.R
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel: VM

    @Inject
    lateinit var dialogManager: DialogManager

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

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    val mainActivity = activity as? MainActivity

                    when (uiState) {
                        UIState.Loading -> {
                            mainActivity?.setLoading(true)
                        }

                        UIState.Idle -> {
                            mainActivity?.setLoading(false)
                        }

                        is UIState.Error -> {
                            mainActivity?.setLoading(false)
                            mainActivity?.runOnUiThread {
                                dialogManager.showErrorDialog(
                                    titleResId = R.string.dialog_error_title_generic,
                                    description = "Error: ${uiState.message.takeIf { it.isNotEmpty() } ?: "An unknown error occurred"}",
                                    buttonTextResId = R.string.dialog_button_cancel,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigate(action: Int) {
        if (findNavController().currentDestination?.id != action) {
            findNavController().navigate(action)
        }
    }
}