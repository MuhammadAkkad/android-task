package com.company.androidtask.presentation.module.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.company.androidtask.R
import com.company.androidtask.databinding.FragmentSplashBinding
import com.company.androidtask.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun initListeners() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isAuthorized.collectLatest {
                it?.let { isAuthorized ->
                    if (isAuthorized)
                        navigate(R.id.action_SplashFragment_to_TasksFragment)
                    else
                        dialogManager.showErrorDialog(
                            titleResId = R.string.dialog_error_title_default,
                            descriptionResId = R.string.dialog_error_description_default,
                            buttonTextResId = R.string.dialog_button_cancel,
                        )
                }
            }
        }
    }

    override fun initResources() {
    }
}