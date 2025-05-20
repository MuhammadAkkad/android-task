package com.company.androidtask.presentation.modules.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.company.androidtask.R
import com.company.androidtask.databinding.FragmentSplashBinding
import com.company.androidtask.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun initListeners() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isAuthorized.collectLatest { isAuthorized ->
                if (isAuthorized)
                    navigate(R.id.action_SplashFragment_to_TasksFragment)
                else {
                    Toast.makeText(requireContext(),"Unable to make connection, please try again later.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initResources() {
    }
}