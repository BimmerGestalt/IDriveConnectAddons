package io.bimmergestalt.idriveconnectaddons.cdsgauge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import io.bimmergestalt.idriveconnectaddons.cdsgauge.databinding.MainBinding

class MainActivity : AppCompatActivity() {
	val viewModel by viewModels<MainModel>()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding = MainBinding.inflate(layoutInflater)
		binding.lifecycleOwner = this
		binding.data = viewModel
		setContentView(binding.root)
	}
}