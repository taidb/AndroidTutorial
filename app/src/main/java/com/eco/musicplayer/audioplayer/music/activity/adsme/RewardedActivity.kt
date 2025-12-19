package com.eco.musicplayer.audioplayer.music.activity.adsme


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.activity.activity.AdDemoApp
import com.eco.musicplayer.audioplayer.music.databinding.ActivityRewardedBinding

class RewardedActivity : AppCompatActivity() {
    private lateinit var adManager: AdManager
    private lateinit var binding: ActivityRewardedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adManager = (application as AdDemoApp).adManager

        var coins = 100  // Số coins hiện tại

        binding.apply {
            tvTitle.text = "Rewarded Ad Demo"
            tvCoins.text = "Coins hiện tại: $coins"

            btnShowRewarded.setOnClickListener {
                updateStatus("Đang chuẩn bị quảng cáo có thưởng...")

                if (!adManager.showRewardedAd(this@RewardedActivity,
                        onRewardEarned = {
                            coins += 100
                            tvCoins.text = "Coins hiện tại: $coins"
                            updateStatus("Bạn đã nhận được 100 coins!")
                            Toast.makeText(this@RewardedActivity, "Chúc mừng! Bạn nhận được 100 coins", Toast.LENGTH_SHORT).show()
                        },
                        onAdDismissed = {
                            updateStatus("Đã xem xong quảng cáo")
                        }
                    )) {
                    adManager.loadRewardedAd(this@RewardedActivity)
                    updateStatus("Quảng cáo chưa sẵn sàng, đang tải...")
                    Toast.makeText(this@RewardedActivity, "Vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
                }
            }

            comeBack.setOnClickListener {
                finish()
            }

            updateStatus("Sẵn sàng xem Rewarded Ad")
        }
    }

    private fun updateStatus(message: String) {
        binding.tvStatus.text = "Trạng thái: $message"
    }
}