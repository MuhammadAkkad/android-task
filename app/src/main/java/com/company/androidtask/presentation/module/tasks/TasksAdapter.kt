package com.company.androidtask.presentation.module.tasks

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.androidtask.data.remote.model.TasksModel
import com.company.androidtask.databinding.ItemTaskBinding
import com.company.androidtask.presentation.common.orHyphen

class TaskAdapter : ListAdapter<TasksModel, TaskAdapter.TaskViewHolder>(TaskDiffCallback()),
    Filterable {

    private var fullList: List<TasksModel> = emptyList()

    fun setFullList(list: List<TasksModel>?) {
        fullList = list ?: emptyList()
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TasksModel) {
            binding.textViewTask.text = task.task.orHyphen()
            binding.textViewTitle.text = task.title.orHyphen()
            binding.textViewDescription.text = task.description.orHyphen()

            try {
                if (!task.colorCode.isNullOrEmpty()) {
                    val color = task.colorCode.toColorInt()
                    binding.colorIndicator.backgroundTintList = ColorStateList.valueOf(color)
                } else {
                    binding.colorIndicator.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                binding.colorIndicator.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            }
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<TasksModel>() {
        override fun areItemsTheSame(oldItem: TasksModel, newItem: TasksModel): Boolean {
            return oldItem.task == newItem.task
        }

        override fun areContentsTheSame(oldItem: TasksModel, newItem: TasksModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    fullList
                } else {
                    val query = constraint.toString().lowercase()
                    fullList.filter { task ->
                        task.task.orEmpty().lowercase().contains(query) ||
                                task.title.orEmpty().lowercase().contains(query) ||
                                task.description.orEmpty().lowercase().contains(query) ||
                                task.sort.orEmpty().lowercase().contains(query) ||
                                task.wageType.orEmpty().lowercase().contains(query) ||
                                task.businessUnitKey.orEmpty().lowercase().contains(query) ||
                                task.businessUnit.orEmpty().lowercase().contains(query) ||
                                task.parentTaskID.orEmpty().lowercase().contains(query) ||
                                task.preplanningBoardQuickSelect.orEmpty().lowercase()
                                    .contains(query) ||
                                task.colorCode.orEmpty().lowercase().contains(query) ||
                                task.workingTime.orEmpty().lowercase().contains(query)
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<TasksModel>)
            }
        }
    }
}
