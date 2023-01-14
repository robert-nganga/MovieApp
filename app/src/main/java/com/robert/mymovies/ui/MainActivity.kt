package com.robert.mymovies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.robert.mymovies.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MoviesFragmentViewModel by viewModels()
    private lateinit var toolBar: MaterialToolbar
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = findViewById(R.id.toolBar)
        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)

        //Get the NavHostFragment
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFrag.navController


        // Define AppBar Configuration
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // Connect ToolBar with NavController
        toolBar.setupWithNavController(navController, appBarConfiguration)

        //Connect NavigationView with NavController
        navigationView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if(drawerLayout.isOpen){
            drawerLayout.close()
        }else{
            super.onBackPressed()
        }
    }
}