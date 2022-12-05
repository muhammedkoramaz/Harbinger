package com.sn.harbinger.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sn.harbinger.R
import com.sn.harbinger.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController = findNavController(R.id.dashboard_nav_host)
        initBottomBar()
    }

    private fun initBottomBar() {
        val popupMenu = PopupMenu(this, binding.root)
        popupMenu.inflate(R.menu.menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }
}