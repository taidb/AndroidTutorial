package com.eco.musicplayer.audioplayer.music.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eco.musicplayer.audioplayer.music.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
       private lateinit var binding: FragmentLoginBinding
       override fun onCreateView(
              inflater: LayoutInflater,
              container: ViewGroup?,
              savedInstanceState: Bundle?
       ): View? {
              binding =FragmentLoginBinding.inflate(layoutInflater)
              return binding.root
       }
}