package com.company.androidtask.presentation.module.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.androidtask.R
import com.company.androidtask.databinding.FragmentTasksBinding
import com.company.androidtask.presentation.base.BaseFragment
import com.company.androidtask.presentation.common.extensions.collectFlow
import com.company.androidtask.presentation.module.qr.QrScannerFragment
import dagger.hilt.android.AndroidEntryPoint

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
        setupCustomSearchView()

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.fetchTasks()
            binding.swipeToRefresh.isRefreshing = false
        }

        viewModel.tasks.collectFlow { tasks ->
            taskAdapter?.setList(tasks)
        }

        setFragmentResultListener(QrScannerFragment.QR_SCAN_REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == QrScannerFragment.QR_SCAN_REQUEST_KEY) {
                val scannedQrText = bundle.getString(QrScannerFragment.QR_SCAN_RESULT_KEY)
                scannedQrText?.let {
                    binding.searchView.setQuery(it)
                }
            }
        }
    }

    private fun setupCustomSearchView() {
        binding.searchView.onSearch { query ->
            taskAdapter?.filter?.filter(query)
        }

        binding.searchView.onQrClick {
            navigate(R.id.action_TasksFragment_to_QrFragment)
        }
    }
}