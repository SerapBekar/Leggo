package com.serapbekar.fprojectsb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.serapbekar.fprojectsb.common.gone
import com.serapbekar.fprojectsb.common.viewBinding
import com.serapbekar.fprojectsb.common.visible
import com.serapbekar.fprojectsb.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {            //Bottom Navigation bağlama
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            NavigationUI.setupWithNavController(bottomNav, navController)

            navController.addOnDestinationChangedListener{ _, destination, _ ->            //NAvigation gizleme
                when(destination.id) {
                    R.id.homeFragment,
                    R.id.searchFragment,
                    R.id.cartFragment,
                    R.id.favoritesFragment
                    -> {
                        bottomNav.visible()
                    }
                    else -> {
                        bottomNav.gone() // tek bir material alır. }
                    }
                }
            }
        }
    }
}