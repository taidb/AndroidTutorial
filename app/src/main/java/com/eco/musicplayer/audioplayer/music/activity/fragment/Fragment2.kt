package com.eco.musicplayer.audioplayer.music.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.eco.musicplayer.audioplayer.music.activity.model.Filter
import com.eco.musicplayer.audioplayer.music.activity.viewmodel.ItemViewModel
import com.eco.musicplayer.audioplayer.music.databinding.Fragment2Binding

class Fragment2 : Fragment() {
    private var _binding: Fragment2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Fragment2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnTranser.setOnClickListener {
            val filterText = binding.etFilter.text.toString()
            val action = Fragment2Directions.actionFragment1ToFragment2(filterText)
            view.findNavController().navigate(action)
        }


        // Observer cho selectedItem
//        viewModel.selectedItem.observe(viewLifecycleOwner) { item ->
//            binding.tvSelectedItem.text = "Item được chọn: ${item.name} - ID: ${item.id}"
       // }

        // Thêm filter
        binding.btnAddFilter.setOnClickListener {
            val filterText = binding.etFilter.text.toString()
            if (filterText.isNotEmpty()) {
                val newFilter = Filter("name", filterText)
                viewModel.addFilter(newFilter)
                binding.etFilter.text.clear()
            }
        }

        // Xóa filter
        binding.btnClearFilters.setOnClickListener {
            viewModel.filters.value?.forEach { filter ->
                viewModel.removeFilter(filter)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}