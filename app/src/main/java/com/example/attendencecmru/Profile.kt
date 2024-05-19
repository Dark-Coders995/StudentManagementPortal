package com.example.attendencecmru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.text.Typography.section

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val auth = FirebaseAuth.getInstance()

        val db = Firebase.firestore


        val tf1: TextView = findViewById(R.id.profileName)
        val tf2: TextView = findViewById(R.id.profilenumber)
        val tf3: TextView = findViewById(R.id.profileEmail)
        val tf4: TextView = findViewById(R.id.profileUSN)
        val tf5: TextView = findViewById(R.id.profileSection)
        val tf6: TextView = findViewById(R.id.profilesem)
        val tf7: TextView = findViewById(R.id.profilecourse)

        val button: Button = findViewById(R.id.signout)

        val category = intent.getIntExtra("category", 1)

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

        if (category == 1) {
            val mainref = db.collection("students")

            val doc = mainref.document(auth.currentUser!!.uid)

            doc.get()
                .addOnSuccessListener {
                    val data = it.data
                    val name = data?.get("name")
                    val number = data?.get("contact_number")
                    val emailInfo = data?.get("email")
                    val usn = data?.get("usn")
                    val section = data?.get("section")
                    val sem = data?.get("semester")
                    val course = data?.get("course")


                    tf1.text = name.toString()
                    tf2.text = number.toString()
                    tf3.text = emailInfo.toString()
                    tf4.text = usn.toString()
                    tf5.text = section.toString()
                    tf6.text = sem.toString()
                    tf7.text = course.toString()
                }

        } else {
            val mainref = db.collection("teachers")

            val doc = mainref.document(auth.currentUser!!.uid)

            doc.get()
                .addOnSuccessListener {
                    val data = it.data
                    val name = data?.get("name")
                    val number = data?.get("contact_number")
                    val emailInfo = data?.get("email")
                    val id = data?.get("teacherid")
                    val department = data?.get("department")

                    val s:TextView=findViewById(R.id.semtf)
                    s.visibility=View.GONE

                    val s1:TextView=findViewById(R.id.course)
                    s1.text="Department"

                    val i1:ImageView=findViewById(R.id.categorypic)
                    i1.visibility=View.GONE

                    tf1.text = name.toString()
                    tf2.text = number.toString()
                    tf3.text = emailInfo.toString()
                    tf5.text = id.toString()
                    tf7.text = department.toString()




                }


        }
    }
}