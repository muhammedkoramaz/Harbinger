package com.sn.harbinger.ui.dashboard.home.project

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException
import androidx.databinding.ViewDataBinding
import androidx.databinding.DataBindingUtil
import com.sn.harbinger.R
import androidx.databinding.library.baseAdapters.BR
import com.sn.harbinger.data.model.Project
import com.sn.harbinger.databinding.CustomHeaderBinding
import com.sn.harbinger.databinding.ItemAddProjectBinding
import com.sn.harbinger.databinding.ItemProjectBinding
import com.sn.harbinger.util.DateUtil

class ProjectsAdapter :
    RecyclerView.Adapter<ProjectsAdapter.AutoCompleteViewHolder>() {

    var onLongClick: ((Project) -> Unit)? = null
    var onClick: ((Project) -> Unit)? = null
    var addProjectClick: ((Unit) -> Unit)? = null
    private var myProjects = mutableListOf<Any>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Any>) {
        myProjects.clear()
        myProjects.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = myProjects[position]
        if (item is Project) {
            return R.layout.item_project
        } else if (item is String) {
            return if (item == "header")
                R.layout.custom_header
            else
                R.layout.item_add_project
        }
        throw RuntimeException("invalid object")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteViewHolder {
        return AutoCompleteViewHolder.create(
            LayoutInflater.from(parent.context),
            parent,
            viewType,
            onLongClick,
            onClick,
            addProjectClick
        )
    }

    override fun onBindViewHolder(holder: AutoCompleteViewHolder, position: Int) {
        holder.bindItem(myProjects[position])
    }

    override fun getItemCount(): Int {
        return myProjects.size
    }

    class AutoCompleteViewHolder(
        private val binding: ViewDataBinding,
        val context: Context,
        private val onLongClick: ((Project) -> Unit)?,
        private val onClick: ((Project) -> Unit)?,
        private val addProjectClick: ((Unit) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(data: Any?) {
            binding.setVariable(BR.item, data)
            binding.executePendingBindings()

            if (binding is ItemProjectBinding) {

                binding.itemProject.setOnLongClickListener {
                    onLongClick?.invoke(binding.item!!)
                    true
                }

                binding.itemProject.setOnClickListener {
                    onClick?.invoke(binding.item!!)
                }

                val endDate = binding.item?.endDate ?: "0"
                binding.tvEndDate.text =
                    context.getString(R.string.remaining_days, DateUtil.remainingDay(endDate))

            }

            if (binding is ItemAddProjectBinding) {
                binding.itemAddProject.setOnClickListener {
                    addProjectClick?.invoke(Unit)
                }
            }

            if (binding is CustomHeaderBinding) {
                binding.tvHeader.text = context.getString(R.string.my_projects)
            }

        }

        companion object {
            fun create(
                inflater: LayoutInflater?,
                parent: ViewGroup?,
                viewType: Int,
                onLongClick: ((Project) -> Unit)?,
                onClick: ((Project) -> Unit)?,
                addProjectClick: ((Unit) -> Unit)?
            ): AutoCompleteViewHolder {
                val binding =
                    DataBindingUtil.inflate<ViewDataBinding>(inflater!!, viewType, parent, false)
                return AutoCompleteViewHolder(
                    binding,
                    parent?.context!!,
                    onLongClick,
                    onClick,
                    addProjectClick
                )
            }
        }

    }
}