package com.sn.harbinger.ui.dashboard.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sn.harbinger.R
import com.sn.harbinger.data.model.Project
import com.sn.harbinger.databinding.BottomSheetBinding
import com.sn.harbinger.databinding.HomeFragmentBinding
import com.sn.harbinger.ui.auth.AuthActivity
import com.sn.harbinger.ui.dashboard.home.project.ProjectActivity
import com.sn.harbinger.ui.dashboard.home.project.ConnectedProjectAdapter
import com.sn.harbinger.ui.dashboard.home.project.ProjectFlow
import com.sn.harbinger.ui.dashboard.home.project.ProjectsAdapter
import com.sn.harbinger.util.State
import com.sn.harbinger.util.extensions.gone
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val vm: HomeViewModel by viewModels()
    private val binding: HomeFragmentBinding by lazy {
        HomeFragmentBinding.inflate(layoutInflater)
    }
    private val projectsAdapter by lazy {
        ProjectsAdapter()
    }
    private val connectedProjectsAdapter by lazy {
        ConnectedProjectAdapter { project -> }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = vm
        initObserve()
        initializeRecycler()
        adaptersClicks()
        errorViewClicks()
    }

    override fun onResume() {
        super.onResume()
        vm.projectsRequests()
    }

    private fun initObserve() {
        vm.myProjectsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Loading -> {
                    binding.shimmerMyProjects.startShimmer()
                }
                is State.Success -> {
                    projectsAdapter.setData(vm.returnFullList(response.data.result?.projects))
                    vm.myProjectsError.set(false)
                    binding.shimmerMyProjects.stopShimmer()
                    binding.shimmerMyProjects.gone()
                }
                is State.Error -> {
                    vm.myProjectsError.set(true)
                    binding.shimmerMyProjects.stopShimmer()
                    binding.shimmerMyProjects.gone()
                    if (response.throwable is HttpException) {
                        if (response.throwable.code() == 401) {
                            showSessionAlert()
                        }
                    }
                }
            }
        }
        vm.connectedProjectsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Loading -> {
                    binding.shimmerConnectedProjects.startShimmer()
                }
                is State.Success -> {
                    connectedProjectsAdapter.setData(vm.returnFullList(response.data.result?.projects))
                    vm.connectedProjectsError.set(false)
                    binding.shimmerConnectedProjects.stopShimmer()
                    binding.shimmerConnectedProjects.gone()
                }
                is State.Error -> {
                    vm.connectedProjectsError.set(true)
                    binding.shimmerConnectedProjects.stopShimmer()
                    binding.shimmerConnectedProjects.gone()
                    if (response.throwable is HttpException) {
                        if (response.throwable.code() == 401) {
                            showSessionAlert()
                        }
                    }
                }
            }
        }
        vm.deletedProjectsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Success -> {
                    vm.projectsRequests()
                }
                is State.Error -> {
                    // Todo catch error
                }
                else -> {
                }
            }
        }
    }

    private fun initializeRecycler() {
        val rcvProjectsLayoutManager = GridLayoutManager(requireContext(), 2)
        rcvProjectsLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) {
                    2
                } else 1
            }
        }

        val rcvConnectedLayoutManager = GridLayoutManager(requireContext(), 2)
        rcvConnectedLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) {
                    2
                } else 1
            }
        }

        binding.rcvProjects.apply {
            setHasFixedSize(true)
            adapter = projectsAdapter
            layoutManager = rcvProjectsLayoutManager
        }
        binding.rcvConnectedProjects.apply {
            setHasFixedSize(true)
            adapter = connectedProjectsAdapter
            layoutManager = rcvConnectedLayoutManager
        }
    }

    private fun errorViewClicks() {
        binding.myProjectErrorView.onClick = {
            // send my projects request
        }

        binding.connectedProjectErrorView.onClick = {
            // send connected projects request
        }
    }

    private fun adaptersClicks() {
        projectsAdapter.addProjectClick = {
            val intent = Intent(requireContext(), ProjectActivity::class.java)
            intent.putExtra("flow", ProjectFlow.ADD_PROJECT.flow)
            startActivity(intent)
        }
        projectsAdapter.onLongClick = { project ->
            showBottomSheet(project)
        }
        projectsAdapter.onClick = { project ->
            val intent = Intent(requireContext(), ProjectActivity::class.java)
            intent.putExtra("flow", ProjectFlow.CONTENTS_PROJECT.flow)
            intent.putExtra("projectData", project)
            startActivity(intent)
        }
    }

    private fun showSessionAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.alert_session_title))
        builder.setMessage(getString(R.string.alert_session_msg))
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, _ ->
            dialog.dismiss()
            vm.removeToken()
            requireActivity().finish()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }
        builder.show()
    }

    private fun showBottomSheet(project: Project) {
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val bindingSheet = DataBindingUtil.inflate<BottomSheetBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.bottom_sheet,
            null,
            false
        )
        bottomSheetDialog.setContentView(bindingSheet.root)
        bindingSheet.ivClose.setOnClickListener { bottomSheetDialog.dismiss() }
        bindingSheet.btnProjectDetail.setOnClickListener {
            bottomSheetDialog.dismiss()
            val intent = Intent(requireContext(), ProjectActivity::class.java)
            intent.putExtra("flow", ProjectFlow.DETAIL_PROJECT.flow)
            intent.putExtra("projectData", project)
            startActivity(intent)
        }
        bindingSheet.btnProjectDelete.setOnClickListener {
            bottomSheetDialog.dismiss()
            vm.deleteProject(project.projectID.toString())
        }
        bottomSheetDialog.show()

    }

}