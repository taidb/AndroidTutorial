package com.eco.musicplayer.audioplayer.music.activity.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import com.eco.musicplayer.audioplayer.music.activity.activity.permission.Api30To32Activity
import com.eco.musicplayer.audioplayer.music.activity.activity.permission.Api33PlusActivity
import com.eco.musicplayer.audioplayer.music.activity.activity.permission.PreApi30Activity
import com.eco.musicplayer.audioplayer.music.activity.broadcast.Broadcast
import com.eco.musicplayer.audioplayer.music.activity.broadcast.CustomBroadcast
import com.eco.musicplayer.audioplayer.music.activity.event.EventBusTest
import com.eco.musicplayer.audioplayer.music.activity.model.MessageEvent
import com.eco.musicplayer.audioplayer.music.activity.model.Staff
import com.eco.musicplayer.audioplayer.music.activity.model.Student
import com.eco.musicplayer.audioplayer.music.activity.model.Teacher
import com.eco.musicplayer.audioplayer.music.activity.network.isNetworkAvailable
import com.eco.musicplayer.audioplayer.music.activity.network.isWifeEnabled
import com.eco.musicplayer.audioplayer.music.databinding.ActivityMain4Binding
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
//Trước Android 11 (API < 30)	Hộp thoại yêu cầu quyền của hệ thống sẽ luôn hiển thị mỗi
// lần bạn yêu cầu, trừ khi người dùng chọn rõ ràng tùy chọn "Don't ask again" (Không hỏi lại).
//Android 11 trở lên (API >= 30)	Nếu người dùng nhấn "Từ chối" hai lần (không có hộp kiểm
// "Don't ask again" hiển thị nữa), hệ thống sẽ ngầm hiểu là "không hỏi lại".
// Các yêu cầu tiếp theo sẽ tự động bị từ chối ngay lập tức mà không hiển thị hộp thoại hệ thống.
// Truy cập bộ nhớ/ảnh/video/audio:
//Trước Android 13: Sử dụng quyền chung READ_EXTERNAL_STORAGE hoặc WRITE_EXTERNAL_STORAGE.
//Android 13 (API 33) trở lên: Cần các quyền chi tiết hơn như READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_AUDIO.
// Điều này giúp người dùng chỉ cấp quyền truy cập vào loại tệp cụ thể mà ứng dụng cần.
//Thông báo (Notifications):
//Trước Android 13: Quyền thông báo được cấp mặc định khi cài đặt.
//Android 13 (API 33) trở lên: Yêu cầu quyền thời gian chạy POST_NOTIFICATIONS. Người dùng có thể từ chối quyền này.
//Vị trí, Camera, Microphone: Android 11 giới thiệu quyền một lần (one-time permissions),
class MainActivity4 : AppCompatActivity() {
    private lateinit var binding: ActivityMain4Binding
    private lateinit var broadcast: Broadcast



    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //loadDenialCount()
        setupClickListeners()

        // Kiểm tra trạng thái quyền khi khởi động
       // updatePermissionStatus()
    }

    private fun setupClickListeners() {
        transferObject1()
        transferObject2()
        transferObject3()
        isCheckNetwork()
        navigationPage(ListStudentActivity::class.java)
        useBroacastReceiver()
        useFragment()

        binding.btnVersionSdk1.setOnClickListener {
            val intent = Intent(this, PreApi30Activity::class.java)
            startActivity(intent)
        }

        binding.btnVersionSdk2.setOnClickListener {
            val intent = Intent(this, Api30To32Activity::class.java)
            startActivity(intent)
        }

        binding.btnVersionSdk3.setOnClickListener {
            val intent = Intent(this, Api33PlusActivity::class.java)
            startActivity(intent)
        }
        binding.btnEventBus.setOnClickListener {
            startActivity(Intent(this, EventBusTest::class.java))

            Handler(Looper.getMainLooper()).postDelayed({
                EventBus.getDefault().post(MessageEvent("Hello from MainActivity4"))
            }, 200)
        }
        binding.btnService.setOnClickListener {
            val intent = Intent(this, ServiceActivity::class.java)
            startActivity(intent)
        }
        binding.btnFragment1.setOnClickListener {
            val intent = Intent(this, DemoFragment::class.java)
            startActivity(intent)
        }


    }

    private fun isCheckNetwork() {
        binding.checkNetwork.setOnClickListener {
            when {
                isWifeEnabled(applicationContext) -> {
                    Toast.makeText(this, "WiFi turned on", Toast.LENGTH_LONG).show()
                }
                isNetworkAvailable(applicationContext) -> {
                    Toast.makeText(this, "Network is available", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun transferObject1() {
        binding.btnParcelable.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            val student = Student("Nguyen Van A", 2021, "Ha Noi")
            intent.putExtra("student", student)
            startActivity(intent)
        }
    }

    private fun transferObject2() {
        binding.btnSerializable.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            val staff = Staff(1, "Nguyen Van A", 23, 555.666)
            intent.putExtra("staff", staff)
            startActivity(intent)
        }
    }

    private fun transferObject3() {
        binding.btnTransferJson.setOnClickListener {
            val teacher = Teacher("Nguyen Van A", 46)
            val gson = Gson()
            val jsonString = gson.toJson(teacher)
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("jsonString", jsonString)
            startActivity(intent)
        }
    }

    private fun navigationPage(activityClass: Class<out Activity>) {
        binding.btnViewmodel.setOnClickListener {
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun useBroacastReceiver() {
        broadcast = Broadcast()
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction("android.net.wifi.WIFI_STATE_CHANGED")
            addAction("android.net.wifi.STATE_CHANGE")
            addAction("android.net.conn.CONNECTIVITY_CHANGE")
            addAction("test.Broadcast")
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ cần flag
            registerReceiver(
                broadcast,
                intentFilter,
                Context.RECEIVER_EXPORTED  // hoặc RECEIVER_NOT_EXPORTED nếu broadcast nội bộ
            )
        } else {
            // Android 12 trở xuống
            registerReceiver(broadcast, intentFilter)
        }


        binding.btnBroadcastReceiver.setOnClickListener {
            val intent = Intent(this, CustomBroadcast::class.java)
            startActivity(intent)
            val intent2 = Intent("test.Broadcast")
            intent2.setPackage(packageName)
            sendBroadcast(intent2)
        }
    }

    private fun useFragment() {
        binding.btnFragment.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
            unregisterReceiver(broadcast)
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcast)
    }

}