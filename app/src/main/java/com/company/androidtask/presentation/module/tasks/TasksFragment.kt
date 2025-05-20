package com.company.androidtask.presentation.module.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.androidtask.databinding.FragmentTasksBinding
import com.company.androidtask.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : BaseFragment<FragmentTasksBinding, TasksViewModel>() {

    override val viewModel: TasksViewModel by viewModels()

    private var taskAdapter: TaskAdapter? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTasksBinding {
        return FragmentTasksBinding.inflate(inflater, container, false)
    }

    override fun initResources() {
        taskAdapter = TaskAdapter()
        binding.rvTasks.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun initListeners() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.fetchTasks()
            binding.swipeToRefresh.isRefreshing = false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tasks.collectLatest { tasks ->
                taskAdapter?.setFullList(tasks)
            }
        }

        setupCustomSearchView()
    }

    private fun setupCustomSearchView() {
        binding.searchView.onSearch { query ->
            taskAdapter?.filter?.filter(query)
        }

        binding.searchView.onQrClick {
            Toast.makeText(
                requireContext(),
                "QR Code scanner clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}