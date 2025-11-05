package com.eco.musicplayer.audioplayer.music.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.eco.musicplayer.audioplayer.music.activity.model.Item
import com.eco.musicplayer.audioplayer.music.activity.viewmodel.ItemViewModel
import com.eco.musicplayer.audioplayer.music.databinding.Fragment1Binding

class Fragment1 : Fragment() {
    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClick.setOnClickListener {
            val newItem = Item("Dữ liệu từ Fragment1", System.currentTimeMillis().toInt())
            viewModel.selectItem(newItem)
        }

        // Observer cho filteredList
        viewModel.filteredList.observe(viewLifecycleOwner) { items ->
            // Cập nhật RecyclerView hoặc UI khác
            binding.tvItemCount.text = "Số lượng items: ${items.size}"
        }

        // Observer cho filters
        viewModel.filters.observe(viewLifecycleOwner) { filters ->
            binding.tvFilterCount.text = "Số lượng filters: ${filters.size}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}