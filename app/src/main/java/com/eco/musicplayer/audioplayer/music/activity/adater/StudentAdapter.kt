package com.eco.musicplayer.audioplayer.music.activity.adater

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.model.Student
import com.eco.musicplayer.audioplayer.music.databinding.ItemStudentBinding

//adapter là 1 lớp trung gian giữa dữ liệu và giao diện hiện thị thường có : ListView ,RecyclerView ,ViewPager ,Spinner
// chuển đổi dữ liệu thành các view để hiện thị lên màn hình
//ListView: hiện thị 1 danh sách dữ liệu theo chiều dọc
//RecyclerView: hiện thị 1 danh sách dữ liệu theo chiều dọc và ngang có thể tái sự dụng View để tối ưu hiệu năng khi cuộn lên xuống
//ViewPager2: Hieenj thị các trang hoặc hình ảnh mà người dùng có thể vuốt sang trái/ phải hoặc tự động
class StudentAdapter(
    private var students: List<Student>,
    private val context: Context
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.name == newItem.name && oldItem.age == newItem.age && oldItem.address == newItem.address
        }


        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            ItemStudentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = differ.currentList[position]
        val formattedText = context.getString(
            R.string.student_list,
            student.name,
            student.age,
            student.address
        )
        holder.binding.txtStudent.text = formattedText
    }

}