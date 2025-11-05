package com.eco.musicplayer.audioplayer.music.activity.event

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.model.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

//EventBus là một cơ chế truyền dữ liệu và sự kiện giữa các component
// trong Android (Activity, Fragment, ViewModel, Service…) mà không cần truyền trực
// tiếp qua Interface hay BroadcastReceiver. -> Dễ gửi sự kiện từ bất cứ đâu trong ứng dụng.
class EventBusTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_event_bus)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent){
        findViewById<TextView>(R.id.tvText).text = event.message
    }

}