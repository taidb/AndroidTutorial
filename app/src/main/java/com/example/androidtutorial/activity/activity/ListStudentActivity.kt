package com.example.androidtutorial.activity.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtutorial.activity.adater.StudentAdapter
import com.example.androidtutorial.activity.model.DataClass.students
import com.example.androidtutorial.activity.util.Resource
import com.example.androidtutorial.activity.viewmodel.StudentViewModel
import com.example.androidtutorial.databinding.ActivityListStudentBinding
import kotlinx.coroutines.launch

class ListStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStudentBinding
    private val viewModel by viewModels<StudentViewModel>()
    private val studentAdapter by lazy { StudentAdapter(students, this) }
    private val tag = "LifecycleDemo"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        Log.d("LifecycleDemo", "activity được tạo")
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.students.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.progressbarStudent.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressbarStudent.visibility = View.INVISIBLE
                            it.data?.let { students -> studentAdapter.differ.submitList(students) }
                        }

                        is Resource.Error -> {
                            binding.progressbarStudent.visibility = View.INVISIBLE
                        }

                        else -> Unit
                    }

                }
            }
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.error.collect {
                        Toast.makeText(this@ListStudentActivity, it, Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvStudent.apply {
            layoutManager =
                LinearLayoutManager(this@ListStudentActivity, RecyclerView.VERTICAL, false)
            adapter = studentAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "============ onStart() ============")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "============ onResume() ============")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "============ onPause() ============")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "============ onStop() ============")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "============ onDestroy() ============")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "============ onRestart() ============")
    }


}