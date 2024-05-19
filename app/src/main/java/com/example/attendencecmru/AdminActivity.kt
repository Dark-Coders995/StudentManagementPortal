package com.example.attendencecmru
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_page)

        val spinner: Spinner = findViewById(R.id.spinner_selection)
        val teacherDetailsLabel: TextView = findViewById(R.id.teacher_details_label)
        val teacherNameEditText: EditText = findViewById(R.id.teacher_name_edittext)
        val teacherDepartmentEditText: EditText = findViewById(R.id.teacher_department_edittext)
        val studentDetailsLabel: TextView = findViewById(R.id.student_details_label)
        val studentNameEditText: EditText = findViewById(R.id.student_name_edittext)
        val studentCourseEditText: EditText = findViewById(R.id.student_course_edittext)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> { // Teacher selected
                        teacherDetailsLabel.visibility = View.VISIBLE
                        teacherNameEditText.visibility = View.VISIBLE
                        teacherDepartmentEditText.visibility = View.VISIBLE
                        studentDetailsLabel.visibility = View.GONE
                        studentNameEditText.visibility = View.GONE
                        studentCourseEditText.visibility = View.GONE
                    }
                    1 -> { // Student selected
                        teacherDetailsLabel.visibility = View.GONE
                        teacherNameEditText.visibility = View.GONE
                        teacherDepartmentEditText.visibility = View.GONE
                        studentDetailsLabel.visibility = View.VISIBLE
                        studentNameEditText.visibility = View.VISIBLE
                        studentCourseEditText.visibility = View.VISIBLE
                    }
                    // Handle other selections if needed
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
}
