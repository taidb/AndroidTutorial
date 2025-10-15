package com.example.androidtutorial.activity.layout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androidtutorial.activity.adater.NoteAdapter
import com.example.androidtutorial.activity.model.Note
import com.example.androidtutorial.databinding.ActivityS2NotesBinding

class S2NotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityS2NotesBinding
    private val noteAdapter = NoteAdapter()
    private val notes = mutableListOf<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityS2NotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadData()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewNotes.apply {
            // Sử dụng StaggeredGridLayoutManager với 2 cột
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            adapter = noteAdapter
        }
    }

    private fun loadData() {
        notes.addAll(
            listOf(
                Note(
                    id = "1",
                    title = "Không có tiêu đề",
                    content = "Nội dung ghi chú dài để test chiều cao item với nhiều dòng văn bản khác nhau...",
                    time = "11:58",
                    isFixed = true
                ),
                Note(
                    id = "2",
                    title = "Ghi chú 2",
                    content = "Nội dung ngắn",
                    time = "09:30",
                    isFixed = false
                ),
                Note(
                    id = "3",
                    title = "Hôm nay",
                    content = "Đi siêu thị, mua hoa quả, rau củ và đồ dùng cá nhân cho gia đình",
                    time = "14/10",
                    isFixed = false
                ),
                Note(
                    id = "4",
                    title = "Ý tưởng",
                    content = "Làm app note có hiệu ứng đẹp với animation và chuyển động mượt mà",
                    time = "11:07",
                    isFixed = true
                ),
                Note(
                    id = "5",
                    title = "Công việc",
                    content = "Hoàn thành báo cáo tuần, gửi email cho sếp, chuẩn bị meeting",
                    time = "15:20",
                    isFixed = false
                ),
                Note(
                    id = "6",
                    title = "Học tập",
                    content = "Ôn tập Android Development, RecyclerView, Kotlin Coroutines, Jetpack Compose",
                    time = "08:45",
                    isFixed = false
                )
            )
        )
        noteAdapter.differ.submitList(notes.toList())

    }

    private fun setupClickListeners() {
        noteAdapter.onAddNoteClick = { newNote ->
            // Thêm note mới vào đầu danh sách
            notes.add(0, newNote)

            // Cập nhật RecyclerView
            noteAdapter.differ.submitList(notes.toList())
        }
    }

}