package com.sn.harbinger.ui.dashboard.home.project.contents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.sn.harbinger.data.model.Project
import com.sn.harbinger.databinding.ProjectContentsFragmentBinding
import com.sn.harbinger.ui.dashboard.home.project.NotesAdapter
import com.sn.harbinger.util.SwipeToDelete
import com.sn.harbinger.util.DateUtil
import com.sn.harbinger.util.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectContentsFragment : Fragment() {

    private lateinit var projectData: Project
    private val vm: ProjectContentsViewModel by viewModels()
    private val binding: ProjectContentsFragmentBinding by lazy {
        ProjectContentsFragmentBinding.inflate(
            layoutInflater
        )
    }

    private val notesAdapter by lazy {
        NotesAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initializeRecycler()
        projectData = arguments?.get("projectData") as Project
        binding.project = projectData

        vm.getNote(projectData.projectID.toString())
    }


    private fun initializeRecycler() {
        binding.rcvNotes.adapter = notesAdapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDelete(notesAdapter,requireContext()))
        itemTouchHelper.attachToRecyclerView(binding.rcvNotes)
    }

    private fun initObserve() {
        vm.getNoteLiveDate.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Loading -> {

                }
                is State.Success -> {
                    val data = response.data.result?.notes
                    notesAdapter.setItems(data!!)
                    binding.tvRemainingDay.text =
                        DateUtil.remainingDay(projectData.endDate!!).toString()
                    binding.tvRemainingTask.text = vm.calculateTaskSize(data)
                }
                is State.Error -> {

                }
            }
        }
    }


}