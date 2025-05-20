package com.company.androidtask.presentation.modules.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import com.company.androidtask.databinding.FragmentTasksBinding
import com.company.androidtask.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : BaseFragment<FragmentTasksBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTasksBinding {
        return FragmentTasksBinding.inflate(inflater, container, false)
    }

    override fun initListeners() {
    }

    override fun initResrouces() {

    }
}