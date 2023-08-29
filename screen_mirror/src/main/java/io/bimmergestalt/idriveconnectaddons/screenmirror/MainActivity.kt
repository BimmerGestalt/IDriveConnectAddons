package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.app.AppOpsManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import androidx.activity.viewModels
import io.bimmergestalt.idriveconnectaddons.screenmirror.databinding.ActivityMainBinding

const val TAG = "ScreenMirroring"

class MainActivity : AppCompatActivity() {
    val controller by lazy { MainController(this) }
    val viewModel by viewModels<MainModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.controller = controller
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        println("PROJECT_MEDIA permission: ${checkSelfPermission("PROJECT_MEDIA")}")
        val appOps = getSystemService(AppOpsManager::class.java)
        println("PROJECT_MEDIA appops: ${appOps.checkOpNoThrow("android:project_media", Process.myUid(), packageName)}")
    }
}