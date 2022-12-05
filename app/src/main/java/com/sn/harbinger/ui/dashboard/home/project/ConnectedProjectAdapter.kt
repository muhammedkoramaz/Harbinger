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
import com.sn.harbinger.databinding.ItemConnectProjectBinding
import com.sn.harbinger.databinding.ItemProjectBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ConnectedProjectAdapter(val listener: (Project) -> Unit) :
    RecyclerView.Adapter<ConnectedProjectAdapter.AutoCompleteViewHolder>() {

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
                R.layout.item_connect_project
        }
        throw RuntimeException("invalid object")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteViewHolder {
        return AutoCompleteViewHolder.create(
            LayoutInflater.from(parent.context),
            parent,
            viewType,
            listener
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
        val listener: (Project) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(data: Any?) {
            binding.setVariable(BR.item, data)
            binding.executePendingBindings()

            if (binding is ItemProjectBinding) {
                binding.itemProject.setOnClickListener {
                    // Todo go to project detail
                }

                binding.itemProject.setOnLongClickListener {
                    listener(binding.item!!)
                    true
                }

                val endDate = binding.item?.endDate ?: "0"
                binding.tvEndDate.text =
                    context.getString(R.string.remaining_days, remainingDay(endDate))

            }

            if (binding is ItemConnectProjectBinding) {
                binding.itemConnectProject.setOnClickListener {
                    // Todo(go to add connect project)
                }
            }

            if (binding is CustomHeaderBinding) {
                binding.tvHeader.text = context.getString(R.string.my_connected_projects)
            }

        }

        companion object {
            fun create(
                inflater: LayoutInflater?,
                parent: ViewGroup?,
                viewType: Int,
                listener: (Project) -> Unit
            ): AutoCompleteViewHolder {
                val binding =
                    DataBindingUtil.inflate<ViewDataBinding>(inflater!!, viewType, parent, false)
                return AutoCompleteViewHolder(binding, parent?.context!!, listener)
            }
        }

        private fun remainingDay(endDate: String): Long {
            val end = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(endDate)
            val millionSeconds = end!!.time - Calendar.getInstance().timeInMillis
            return TimeUnit.MILLISECONDS.toDays(millionSeconds)
        }

    }


}