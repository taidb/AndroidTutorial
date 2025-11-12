package com.eco.musicplayer.audioplayer.music.activity.adater

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eco.musicplayer.audioplayer.music.activity.designlayout.LoginActivity
import com.eco.musicplayer.audioplayer.music.activity.fragment.LoginFragment
import com.eco.musicplayer.audioplayer.music.activity.fragment.SignupFragment

class AuthPagerAdapter(activity: LoginActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount()=2

    override fun createFragment(position: Int): Fragment {
        return if (position ==0) LoginFragment() else SignupFragment()
    }
}