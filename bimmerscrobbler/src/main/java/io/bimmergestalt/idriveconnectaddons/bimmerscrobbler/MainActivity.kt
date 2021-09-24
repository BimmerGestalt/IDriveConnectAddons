package io.bimmergestalt.idriveconnectaddons.bimmerscrobbler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import io.bimmergestalt.idriveconnectaddons.bimmerscrobbler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	val installedScrobblersViewModel by viewModels<InstalledScrobblersViewModel>()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val binding = ActivityMainBinding.inflate(layoutInflater)
		binding.lifecycleOwner = this
		binding.installedScrobblers = installedScrobblersViewModel
		binding.viewModel = MainModel
		setContentView(binding.root)
	}
}