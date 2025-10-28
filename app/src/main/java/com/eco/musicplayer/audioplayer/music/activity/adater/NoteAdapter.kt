package com.eco.musicplayer.audioplayer.music.activity.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eco.musicplayer.audioplayer.music.activity.model.Note
import com.eco.musicplayer.audioplayer.music.databinding.ItemAddBinding
import com.eco.musicplayer.audioplayer.music.databinding.ItemNotesBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_ADD_NOTE = 0
        private const val TYPE_NORMAL_NOTE = 1
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    var onAddNoteClick: ((Note) -> Unit)? = null

    private var isGrifLayout = true

    fun setGridLayout(isGrid: Boolean) {
        isGrifLayout = isGrid
        notifyDataSetChanged()
    }

    inner class AddNoteViewHolder(private var binding: ItemAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewRoot.setOnClickListener {
                // Tạo một note mới mặc định
                val newNote = Note(
                    id = System.currentTimeMillis().toString(),
                    title = "Không có tiêu đề",
                    content = "Nội dung mới...",
                    time = getCurrentTime(),
                    isFixed = false
                )
                onAddNoteClick?.invoke(newNote)
            }
        }
    }

    inner class NoteViewHolder(val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_ADD_NOTE else TYPE_NORMAL_NOTE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_ADD_NOTE -> AddNoteViewHolder(ItemAddBinding.inflate(inflater, parent, false))
            else -> NoteViewHolder(ItemNotesBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemCount(): Int = 1 + differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddNoteViewHolder -> {
            }

            is NoteViewHolder -> {
                val note = differ.currentList[position - 1]
                holder.binding.apply {
                    tvTitle.text = note.title
                    tvSubtitle.text = note.content
                    tvTime.text = note.time

                    imgDot.visibility =
                        if (note.isFixed) android.view.View.VISIBLE else android.view.View.GONE

                    if (isGrifLayout) {
                        tvSubtitle.maxLines = 4
                    } else {
                        tvSubtitle.maxLines = 10
                    }
                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}