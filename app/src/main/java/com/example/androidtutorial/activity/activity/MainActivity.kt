package com.example.androidtutorial.activity.activity
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.R
import com.example.androidtutorial.activity.model.Student
import com.example.androidtutorial.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.net.toUri
import com.example.androidtutorial.activity.activity.permission.PermissionActivity
import com.example.androidtutorial.activity.dialog.StartDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//Intent Là thông điệp (message) – gửi đi để yêu cầu một hành động nào đó (mở Activity, chọn ảnh, gửi dữ liệu, v.v.)
//Intent Filter	Là bộ lọc (filter) – khai báo trong AndroidManifest.xml để nói với hệ thống rằng: nó có thể nhận và xử lý loại Intent nào đó.

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.extras?.getParcelable("data2", Bitmap::class.java)
            } else {
                TODO("VERSION.SDK_INT < TIRAMISU")
            }
            binding.imageView.setImageBitmap(imageBitmap)
        }
    }

    private val txtdataback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val data = it.data?.getStringExtra("EXTRA_DATA")
            binding.txtBackData.text = data
        }
    }

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
        //truyền dữ liệu:
        setupIntentBasic()

        //dùng bundle : để nhận nhiều dữ liệu
        setupIntentBundle()

//        binding.btnNavigate.setOnClickListener {
//            val intent = Intent(this, MainActivity2::class.java).apply {
//               Xóa stack hiện tại và tạo stack mới thường sự dụng Khi login xong, muốn xóa màn Login khỏi stack
//            Khi quay về trang chủ, muốn clear hết stack: FLAG_ACTIVITY_CLEAR_TOP
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            startActivity(intent)
//        }

        //Implicit Intent
        //gửi thông báo đến trang khác
        setupImplicitIntent()

        //chuyển sang trang sang bộ sưu tập
        setupPickImage()

        // chuyển sang trang web
        setupOpenWeb()

        //chuyển sang trang chụp ảnh:
        setupCamera()

        // AlertDialog
        setupAlertDialog()

        // Custom Dialog
        setupCustomDialog()

        // BottomSheetDialog
        setupBottomSheetDialog()

        //DialogActivity
        setupDialogActivity()

        //DialogFragment
        setupDialogFragment()

        //date & time
        setupDatePickerDialog()
        setupTimePickerDialog()

        //viewmodel
        setupViewModelNavigation()

        //permission
        setupPermissionActivity()

    }

    private fun navigationPage(activityClass: Class<out Activity>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    //Intent
    private fun setupIntentBasic() {
        binding.btnClick.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("data", binding.editTextName.text.toString())
            txtdataback.launch(intent)
        }
    }

    private fun setupIntentBundle() {
        binding.btnClick2.setOnClickListener {
            val intent = Intent(this, TestIntent::class.java)
            val bundle = Bundle().apply {
                putString("data3", "Hello ")
                putInt("data4", 10)
                putString("data5", "Hello")
            }
            intent.putExtras(bundle)
            startActivity(intent)

        }
    }

    private fun setupImplicitIntent() {
        binding.imageUser.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, binding.editTextName.text.toString())
            }
            startActivity(intent)
        }
    }

    private fun setupPickImage() {
        binding.btnImage.setOnClickListener {
            val intent = Intent(ACTION_PICK).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                type = "image/*"
            }
            takePictureLauncher.launch(intent)
        }
    }

    //Intent web, camera
    private fun setupOpenWeb() {
        binding.btnWeb.setOnClickListener {
            val url = "http://www.google.com"
            val i = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(i)
        }
    }

    private fun setupCamera() {
        binding.btnCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureLauncher.launch(intent)
        }
    }

    //Dialog
    private fun setupAlertDialog() {
        binding.btnAlertDialog.setOnClickListener {
            val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.alertdialog, binding.root as ViewGroup, false)
            val username = dialogView.findViewById<EditText>(R.id.username)
            val password = dialogView.findViewById<EditText>(R.id.password)
            AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Đây là AlertDialog thông thường")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    // Do something.
                    Toast.makeText(
                        this,
                        "Username: ${username.text}\nPassword: ${password.text}",
                        Toast.LENGTH_SHORT
                    ).show()
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
                //
                .show()
        }

    }

    private fun setupCustomDialog() {
        binding.btnCustomDialog.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_custom)
            dialog.setCancelable(true)
            dialog.show()
        }
    }

    private fun setupBottomSheetDialog() {
        binding.btnBottomSheetDialog.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this)
            bottomSheet.setContentView(R.layout.layout_bottom_sheet)
            bottomSheet.show()
        }
    }

    private fun setupDialogActivity() {
        binding.btnDialogActivity.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            val student = Student("Nguyen Van A", 2021, "Ha Noi")
            intent.putExtra("data1", student)
            startActivity(intent)
        }
    }

    private fun setupDialogFragment() {
        binding.btnDialogFragment.setOnClickListener {
            val dialogFragment = StartDialogFragment()
            dialogFragment.show(supportFragmentManager, "StartDialogFragment")
        }
    }

    // DATE & TIME PICKER
    private fun setupDatePickerDialog() {
        binding.btnDialogTimePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.tvReceiveData.text = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun setupTimePickerDialog() {
        binding.btnDatePickerDialog.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                    calendar.set(Calendar.MINUTE, selectedMinute)
                    binding.tvReceiveData.text =
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                }
            TimePickerDialog(
                this,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    //VIEWMODEL & PERMISSION
    private fun setupViewModelNavigation() {
        binding.btnClick3.setOnClickListener {
            navigationPage(ListStudentActivity::class.java)
        }
    }

    private fun setupPermissionActivity() {
        binding.btnClick4.setOnClickListener {
            navigationPage(PermissionActivity::class.java)
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