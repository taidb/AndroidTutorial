package com.eco.musicplayer.audioplayer.music.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eco.musicplayer.audioplayer.music.activity.model.Filter
import com.eco.musicplayer.audioplayer.music.activity.model.Item

class ItemViewModel : ViewModel() {
    // LiveData cho item được chọn
    private val _selectedItem = MutableLiveData<Item>()
    val selectedItem: LiveData<Item> get() = _selectedItem

    // LiveData cho filters
    private val _filters = MutableLiveData<Set<Filter>>(emptySet())
    val filters: LiveData<Set<Filter>> get() = _filters

    // LiveData cho filtered list
    private val _filteredList = MutableLiveData<List<Item>>()
    val filteredList: LiveData<List<Item>> get() = _filteredList

    // Danh sách gốc (trong thực tế có thể lấy từ repository)
    private val originalList = listOf(
        Item("Item 1", 1),
        Item("Item 2", 2),
        Item("Item 3", 3)
    )

    init {
        // Khởi tạo danh sách
        _filteredList.value = originalList
    }

    fun selectItem(item: Item) {
        _selectedItem.value = item
    }

    fun addFilter(filter: Filter) {
        val currentFilters = _filters.value ?: emptySet()
        _filters.value = currentFilters + filter
        applyFilters()
    }

    fun removeFilter(filter: Filter) {
        val currentFilters = _filters.value ?: emptySet()
        _filters.value = currentFilters - filter
        applyFilters()
    }

    private fun applyFilters() {
        val currentFilters = _filters.value ?: emptySet()
        var result = originalList

        // Áp dụng các filter (ví dụ đơn giản)
        currentFilters.forEach { filter ->
            result = result.filter { it.name.contains(filter.value, ignoreCase = true) }
        }

        _filteredList.value = result
    }
}