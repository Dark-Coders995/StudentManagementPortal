package com.example.attendencecmru

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class attendenceView : AppCompatActivity() {

    private lateinit var mainref: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendence_view)


        val db= Firebase.firestore
        val auth= FirebaseAuth.getInstance()

        val nametf: TextView =findViewById(R.id.name1)

        val profileButton: ImageView =findViewById(R.id.profile1)



        mainref = db.collection("teachers")
        val documentRef = mainref.document(auth.currentUser!!.uid)
        documentRef.get()
            .addOnSuccessListener {

                val data = it.data
                val name = data?.get("name")
                nametf.text=name.toString()
            }

        profileButton.setOnClickListener{
            val i= Intent(this,Profile::class.java)
            i.putExtra("category",2)
            startActivity(i)
        }

        val table2: TableLayout =findViewById(R.id.total_show)


        val subid:TextView=findViewById(R.id.sub_id)

        val id = intent.getStringExtra("key")

        subid.text=id

        val tableLayout = findViewById<TableLayout>(R.id.total_show)
        val layoutParams = TableRow.LayoutParams(0, 90)

        layoutParams.bottomMargin = 10

        val tableRow1 = TableRow(this)
        layoutParams.weight = 2F

        tableRow1.layoutParams = layoutParams

        val textView1 = TextView(this)
        textView1.layoutParams = layoutParams

        tableRow1.addView(textView1)

        val textView2 = TextView(this)
        layoutParams.weight = 1F

        textView2.layoutParams = layoutParams

        tableRow1.addView(textView2)


        val textView3 = TextView(this)
        layoutParams.weight = 2F
        textView3.layoutParams = layoutParams
        tableRow1.addView(textView3)

        textView1.textSize = 18F
        textView1.gravity = Gravity.CENTER
        textView1.setTypeface(null, Typeface.BOLD)
        textView1.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        )
        textView1.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.grey
            )
        )
        textView1.setPadding(0, 2, 0, 2)
        textView1.setAllCaps(true)

        textView2.textSize = 18F
        textView2.gravity = Gravity.CENTER
        textView2.setTypeface(null, Typeface.BOLD)
        textView2.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        )
        textView2.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.grey
            )
        )
        textView2.setPadding(0, 2, 0, 2)
        textView2.setAllCaps(true)

        textView3.textSize = 18F
        textView3.gravity = Gravity.CENTER
        textView3.setTypeface(null, Typeface.BOLD)
        textView3.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        )
        textView3.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.grey
            )
        )
        textView3.setPadding(0, 2, 0, 2)
        textView3.setAllCaps(true)



        val docRef = db.collection("attendance")

        docRef.get().addOnSuccessListener { documents ->
            var k=1
            for (document in documents) {
                val stuid = document.id



                val ref = db.collection("attendance").document(stuid).collection("subjects")
                    .document(id.toString())

                ref.get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val data = snapshot.data
                        val attended = data?.get("attended").toString().toInt()
                        val total = intent.getIntExtra("key1", 1).toString()

                        Log.d("TAG", "Attended: $attended, Total: $total")

                        val sturef = db.collection("students").document(stuid).get().addOnSuccessListener { studentSnapshot ->
                            val studentData = studentSnapshot.data
                            val name = studentData?.get("name").toString()

                            textView1.text = name
                            textView2.text = "$attended/$total"

                            if (total == "0") {
                                textView3.text = "0"
                            } else {
                                val percentage = (attended.toFloat() / total.toFloat()) * 100
                                textView3.text = percentage.toInt().toString()+"%"
                            }
                        }.addOnFailureListener {
                            Log.d("TAG", "Failed to retrieve student data for document ID: $stuid")
                            Toast.makeText(this, "Failed to retrieve student data", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Subject document does not exist", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }.addOnFailureListener {
            Log.d("TAG", "Failed to retrieve attendance documents")
            Toast.makeText(this, "Failed to retrieve attendance documents", Toast.LENGTH_SHORT).show()
        }


        table2.addView(tableRow1)

    }
}