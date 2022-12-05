package com.sn.harbinger.ui.dashboard.home.project.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.sn.harbinger.databinding.AddProjectSuccessFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProjectSuccessFragment : Fragment() {

    private lateinit var binding: AddProjectSuccessFragmentBinding
    private val args: AddProjectSuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddProjectSuccessFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvProjectName.text = args.argsProjectName
        binding.btnFirst.setOnClickListener {
            requireActivity().finish()
        }
        binding.btnSecond.setOnClickListener {
            // Todo go to project detail
        }

    }


}