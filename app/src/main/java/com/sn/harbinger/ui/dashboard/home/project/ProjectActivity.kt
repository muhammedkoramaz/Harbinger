package com.sn.harbinger.ui.dashboard.home.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.sn.harbinger.R
import com.sn.harbinger.data.model.Project
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_project)
        val projectData = intent.getParcelableExtra<Project>("projectData")
        val bundle = bundleOf(
            ARG_PROJECT_DATA to projectData
        )
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.add_new_project_nav_host) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val graphId = when (intent.getStringExtra("flow")) {
            ProjectFlow.DETAIL_PROJECT.flow -> {
                R.navigation.detail_project_nav_graph
            }
            ProjectFlow.ADD_PROJECT.flow -> {
                R.navigation.add_new_project_nav_graph
            }
            else -> {
                R.navigation.contents_project_nav_graph
            }
        }
        val graph = navController.navInflater.inflate(graphId)
        navController.setGraph(graph, bundle)

    }

    companion object {
        const val ARG_PROJECT_DATA = "projectData"
    }
}