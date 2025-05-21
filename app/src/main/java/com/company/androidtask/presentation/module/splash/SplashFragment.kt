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
            viewModel.loginState.collectLatest {
                it?.let {
                    navigate(R.id.action_SplashFragment_to_TasksFragment)
                }
            }
        }
    }

    override fun initResources() {
    }
}