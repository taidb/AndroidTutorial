package com.example.androidtutorial.activity.activity

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.R
import com.example.androidtutorial.activity.Student
import com.example.androidtutorial.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.net.toUri
import com.example.androidtutorial.activity.StartDialogFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tag = "LifecycleDemo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Sự dụng databinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(tag, "============ onCreate() ============")
        // Kiểm tra và khôi phục trạng thái ở đây
        if (savedInstanceState != null) {
            val savedValue = savedInstanceState.getString("my_state")
            Log.d(tag, "onCreate(): Restored state: $savedValue")
        } else {
            Log.d(tag, "onCreate(): No saved state found.")
        }

        //Explicit intent
        binding.btnClick.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        //Implicit Intent
        //gửi thông báo đến trang khác
        binding.imageUser.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, binding.editText.text.toString())
            startActivity(intent)
        }

        val selectImageActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    val imageUri = intent?.data
                    binding.imageView.setImageURI(imageUri)
                }

            }

        //chuyển sang trang sang bộ sưu tập
        binding.btnimage.setOnClickListener {
            val intent = Intent(ACTION_PICK)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            selectImageActivityResult.launch(intent)
        }

        // chuyển sang trang web
        binding.btnweb.setOnClickListener {
            val url = "http://www.google.com"
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(url.toUri())
            startActivity(i)
        }

        //chuyển sang trang chụp ảnh:
        binding.btncamera.setOnClickListener {
            val takePictureActivityResult = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureActivityResult, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Log.e(tag, e.message.toString())
            }

        }

        // AlertDialog
        binding.btnAlertDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Đây là AlertDialog thông thường")
                .setPositiveButton("OK") { _, _ ->
                    // Do something.
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Do something else.
                }
//                    .setItems(arrayOf("Item One", "Item Two", "Item Three")) { _, _ ->
//                // hiện thị các mục danh sách lên }
//                .setMultiChoiceItems(
//                    arrayOf("Item One", "Item Two", "Item Three"),null
//                ){
//                    _,_,_ -> // hiện thị mục có nhiều lựa chọn
//                }
//                .setSingleChoiceItems(
//                    arrayOf("Item One", "Item Two", "Item Three"), 0
//                ) { _, _ ->
//                    // hiện thị mục có chỉ có 1 lựa chọn
//                }
                //.setView(R.layout.alertdialog)
                .show()
        }

        // Custom Dialog
        binding.btnCustomDialog.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_custom)
            dialog.setCancelable(true)
            dialog.show()
        }

        // BottomSheetDialog
        binding.btnBottomSheetDialog.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this)
            bottomSheet.setContentView(R.layout.layout_bottom_sheet)
            bottomSheet.show()
        }

        //DialogActivity
        binding.btnDialogActivity.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        //DialogFragment
        binding.btnDialogFragment.setOnClickListener {
            val dialogFragment = StartDialogFragment()
            dialogFragment.show(supportFragmentManager, "StartDialogFragment")
        }

        //truyền dữ liệu:
        binding.btnClick.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("data", binding.editText.text.toString())
            startActivity(intent)
        }

        binding.btnClick.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            val student = Student("Nguyen Van A", 2021, "Ha Noi")
            intent.putExtra("data1", student)
            startActivity(intent)
        }


    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.imageView.setImageBitmap(imageBitmap)

            }
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

    // Lưu trạng thái trước khi Activity có thể bị hủy bởi hệ thống
    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(tag, "============ onSaveInstanceState() ============")
        outState.putString("my_state", "Hello from saved state!")
        super.onSaveInstanceState(outState)
    }
}