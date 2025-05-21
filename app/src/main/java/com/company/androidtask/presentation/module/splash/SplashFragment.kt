package com.company.androidtask.presentation.module.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.company.androidtask.R
import com.company.androidtask.databinding.FragmentSplashBinding
import com.company.androidtask.presentation.base.BaseFragment
import com.company.androidtask.presentation.common.extensions.collectFlow
import dagger.hilt.android.AndroidEntryPoint

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
        viewModel.loginState.collectFlow {
            it?.let {
                navigate(R.id.action_SplashFragment_to_TasksFragment)
            }
        }
    }

    override fun initResources() {}
}