package com.example.bai1_20215386

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerProvince: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerProvince = findViewById(R.id.spinner_province)

        val provinces = loadProvincesFromJson()
        val provinceNames = provinces.map { it.name }

        // Đổ dữ liệu vào Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinceNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProvince.adapter = adapter
    }

    private fun loadProvincesFromJson(): List<Province> {
        val inputStream = assets.open("provinces.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Province>>() {}.type
        return Gson().fromJson(reader, type)
    }
}


class MainActivity : AppCompatActivity() {

    private lateinit var etMssv: EditText
    private lateinit var etName: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSelectDate: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var spinnerWard: Spinner
    private lateinit var spinnerDistrict: Spinner
    private lateinit var spinnerProvince: Spinner
    private lateinit var cbSport: CheckBox
    private lateinit var cbMovies: CheckBox
    private lateinit var cbMusic: CheckBox
    private lateinit var cbTerms: CheckBox
    private lateinit var btnSubmit: Button
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo các view
        etMssv = findViewById(R.id.et_mssv)
        etName = findViewById(R.id.et_name)
        rgGender = findViewById(R.id.rg_gender)
        etEmail = findViewById(R.id.et_email)
        etPhone = findViewById(R.id.et_phone)
        btnSelectDate = findViewById(R.id.btn_select_date)
        tvSelectedDate = findViewById(R.id.tv_selected_date)
        spinnerWard = findViewById(R.id.spinner_ward)
        spinnerDistrict = findViewById(R.id.spinner_district)
        spinnerProvince = findViewById(R.id.spinner_province)
        cbSport = findViewById(R.id.cb_sport)
        cbMovies = findViewById(R.id.cb_movies)
        cbMusic = findViewById(R.id.cb_music)
        cbTerms = findViewById(R.id.cb_terms)
        btnSubmit = findViewById(R.id.btn_submit)

        // Thiết lập DatePicker cho nút chọn ngày sinh
        btnSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Xử lý sự kiện khi nhấn nút Submit
        btnSubmit.setOnClickListener {
            handleSubmit()
        }
    }

    // Hàm hiển thị DatePickerDialog để chọn ngày sinh
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                tvSelectedDate.text = selectedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    // Hàm xử lý logic khi nhấn nút Submit
    private fun handleSubmit() {
        val mssv = etMssv.text.toString().trim()
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        // Kiểm tra lựa chọn giới tính
        val selectedGenderId = rgGender.checkedRadioButtonId
        val gender = when (selectedGenderId) {
            R.id.rb_male -> "Nam"
            R.id.rb_female -> "Nữ"
            else -> ""
        }

        // Kiểm tra tính hợp lệ của form
        if (mssv.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || gender.isEmpty() || selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }
        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Vui lòng đồng ý với các điều khoản", Toast.LENGTH_SHORT).show()
            return
        }

        // Lấy nơi ở từ Spinner
        val ward = spinnerWard.selectedItem.toString()
        val district = spinnerDistrict.selectedItem.toString()
        val province = spinnerProvince.selectedItem.toString()

        // Lấy sở thích
        val hobbies = mutableListOf<String>()
        if (cbSport.isChecked) hobbies.add("Thể thao")
        if (cbMovies.isChecked) hobbies.add("Điện ảnh")
        if (cbMusic.isChecked) hobbies.add("Âm nhạc")

        // Hiển thị thông tin nhập lên Toast
        val result = """
            MSSV: $mssv
            Họ tên: $name
            Giới tính: $gender
            Email: $email
            SĐT: $phone
            Ngày sinh: $selectedDate
            Địa chỉ: $ward, $district, $province
            Sở thích: ${hobbies.joinToString(", ")}
        """.trimIndent()
        Toast.makeText(this, result, Toast.LENGTH_LONG).show()
    }
}
