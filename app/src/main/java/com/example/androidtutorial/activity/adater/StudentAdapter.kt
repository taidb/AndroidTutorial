package com.example.androidtutorial.activity.adater

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtutorial.R
import com.example.androidtutorial.activity.model.Student
import com.example.androidtutorial.databinding.ItemStudentBinding

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