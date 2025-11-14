package com.eco.musicplayer.audioplayer.music.activity.activity.permission

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityApi34PlusBinding

class Api34PlusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApi34PlusBinding
    private val requestMediaPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.filterValues { it }.keys
        val denied = result.filterValues { !it }.keys
        when {
            granted.containsAll(
                listOf(Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO)
            ) -> {
                showStatus("Cấp phép truy cập chọn nhiều anhr hoặc video")
            }

            granted.contains(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) -> {
                showStatus("Cấp phép truy cập chọn 1 ảnh hoặc video")
                openPhotoPicker()
            }

            denied.isNotEmpty() -> {
                showStatus("Từ chối truy cập ảnh và video")
            }

        }
    }

    private val photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                showStatus("Ảnh/video đã chọn: $uri")
            } else {
                showStatus("Không có ảnh/video nào được chọn")
            }

        }

    private fun openPhotoPicker() {
          if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
              photoPickerLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
              )
          }else{
              showStatus("Thiết bị ko hỗ trợ")
          }
    }

    private fun showStatus(message: String) {
        binding.tvTitle.text = message

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApi34PlusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRequest.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestMediaPermission.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                    )
                )
            }else{
                showStatus("Chưa có quyền 14")
            }
        }
        binding.btnRequestCamera.setOnClickListener {
            openPhotoPicker()
        }
    }
}