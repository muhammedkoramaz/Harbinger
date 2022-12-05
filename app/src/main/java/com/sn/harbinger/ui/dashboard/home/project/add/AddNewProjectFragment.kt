package com.sn.harbinger.ui.dashboard.home.project.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sn.harbinger.R
import com.sn.harbinger.databinding.AddNewProjectFragmentBinding
import com.sn.harbinger.util.DateUtil
import com.sn.harbinger.util.State
import com.sn.harbinger.util.extensions.SnackBarType
import com.sn.harbinger.util.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddNewProjectFragment : Fragment() {

    private val vm: AddNewProjectViewModel by viewModels()
    private val binding by lazy { AddNewProjectFragmentBinding.inflate(layoutInflater) }
    private lateinit var navigator: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = requireActivity().findNavController(R.id.add_new_project_nav_host)
        initObserve()

        val startDateLayout = binding.tilStartDate
        val endDateLayout = binding.tilEndDate
        val startDateView = binding.detStartDate
        val endDateView = binding.detEndDate
        startDateView.listen()
        endDateView.listen()
        startDateView.minDate = DateUtil.getYesterday()
        endDateView.minDate = DateUtil.getToday()

        binding.btnAddProject.setOnClickListener {
            val projectName = binding.etProjectName.text.toString()
            val projectDes = binding.etProjectDesc.text.toString()
            val projectSDate = startDateView.text.toString()
            val projectEDate = endDateView.text.toString()

            if (projectName.isEmpty() || projectDes.isEmpty() ||
                projectSDate.isEmpty() || projectEDate.isEmpty()
            ) {
                showSnackBar(getString(R.string.fill_all_fields), SnackBarType.ERROR, view)
                return@setOnClickListener
            }

            if (startDateLayout.isErrorEnabled || endDateLayout.isErrorEnabled) {
                showSnackBar(getString(R.string.invalid_date_error),SnackBarType.ERROR, view)
                return@setOnClickListener
            }
            vm.addProject(projectName, projectDes, projectSDate, projectEDate)

        }
    }

    private fun initObserve() {
        vm.addProjectLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is State.Loading -> {

                }
                is State.Success -> {
                    val action =
                        AddNewProjectFragmentDirections.actionAddProjectSuccess(
                            binding.etProjectName.text.toString()
                        )
                    navigator.navigate(action)
                }
                is State.Error -> {
                    //Todo add project catch error
                }
            }
        }

    }

}