package io.bimmergestalt.idriveconnectaddons.cdsdetails

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import io.bimmergestalt.idriveconnectaddons.cdsdetails.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	val mainModel by viewModels<MainModel>()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding = ActivityMainBinding.inflate(layoutInflater)
		binding.lifecycleOwner = this
		binding.viewModel = mainModel
		setContentView(binding.root)

		if (ContextCompat.checkSelfPermission(this, "bimmergestalt.permission.CDS_personal") != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(arrayOf("bimmergestalt.permission.CDS_personal"), 0)
		}
	}
}