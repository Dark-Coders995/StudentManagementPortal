package com.example.attendencecmru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button:Button=findViewById(R.id.loginButton)

        val auth=FirebaseAuth.getInstance()
        val db= Firebase.firestore




        val usersRef1 = db.collection("students")
        val usersRef2 = db.collection("teachers")

        val tf1:EditText=findViewById(R.id.edittext_email)
        val tf2:EditText=findViewById(R.id.edit_textpass)


        button.setOnClickListener {
            val email=tf1.text.toString()
            val pass=tf2.text.toString()


            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                Toast.makeText(this,"credentials cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            if(email == "Admin" && pass =="admin")
            {
                startActivity(Intent(this, AdminActivity:: class.java))
            }

            auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {

                        val userId = auth.currentUser!!.uid


                        usersRef1.document(userId).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {

                                    Toast.makeText(this,"login success", Toast.LENGTH_SHORT).show()
                                    val i=Intent(this,TW::class.java)
                                    i.putExtra("category",1)

                                    startActivity(i)
                                }
                            }
                            .addOnFailureListener { e ->
                                pass
                            }

                        usersRef2.document(userId).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {

                                    Toast.makeText(this,"login success", Toast.LENGTH_SHORT).show()
                                    val i=Intent(this,TW::class.java)
                                    i.putExtra("category",2)
                                    startActivity(i)
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this,it.exception?.message,Toast.LENGTH_LONG).show()
                                Toast.makeText(
                                    this,
                                    "Data not present please contact admin",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    else{
                        Toast.makeText(this,it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}