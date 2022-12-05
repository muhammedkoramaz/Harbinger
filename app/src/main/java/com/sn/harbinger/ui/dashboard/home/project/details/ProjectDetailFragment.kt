package com.sn.harbinger.ui.dashboard.home.project.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sn.harbinger.R
import com.sn.harbinger.data.model.Project
import com.sn.harbinger.databinding.ProjectDetailFragmentBinding
import com.sn.harbinger.util.DateUtil
import com.sn.harbinger.util.State
import com.sn.harbinger.util.extensions.SnackBarType
import com.sn.harbinger.util.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProjectDetailFragment : Fragment() {
    private val vm: ProjectDetailViewModel by viewModels()
    private val binding by lazy { ProjectDetailFragmentBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        binding.project = arguments?.get("projectData") as Project
        binding.vm = vm
        binding.tvEdit.setOnClickListener {
            if (vm.isEnabled.get())
                vm.isEnabled.set(false)
            else
                vm.isEnabled.set(true)
        }

        val endDateLayout = binding.tilEndDate
        val endDateView = binding.detEndDate
        endDateView.minDate = DateUtil.getToday()
        endDateView.listen()

        binding.btnSaveProject.setOnClickListener {

            val projectName = binding.etProjectName.text.toString()
            val projectDes = binding.etProjectDesc.text.toString()
            val projectSDate = binding.etStartDate.text.toString()
            val projectEDate = endDateView.text.toString()

            if (projectName.isEmpty() || projectDes.isEmpty() ||
                projectSDate.isEmpty() || projectEDate.isEmpty()
            ) {
                showSnackBar(getString(R.string.fill_all_fields), SnackBarType.ERROR, view)
                return@setOnClickListener
            }

            if (endDateLayout.isErrorEnabled) {
                showSnackBar(getString(R.string.invalid_date_error), SnackBarType.ERROR, view)
                return@setOnClickListener
            }

            val project = arguments?.get("projectData") as Project
            vm.updateProject(
                project.projectID.toString(),
                projectName,
                projectDes,
                projectSDate,
                projectEDate
            )
        }

    }

    private fun initObserve() {
        vm.updateProjectLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Loading -> {

                }
                is State.Success -> {
                    showSnackBar(getString(R.string.success), SnackBarType.SUCCESS, requireView())
                }
                is State.Error -> {
                    //todo show snack bar
                }
            }
        }

    }

}