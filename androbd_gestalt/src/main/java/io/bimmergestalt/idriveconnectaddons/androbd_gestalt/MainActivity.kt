package io.bimmergestalt.idriveconnectaddons.androbd_gestalt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.bimmergestalt.idriveconnectaddons.androbd_gestalt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = MainModel
        setContentView(binding.root)
    }
}