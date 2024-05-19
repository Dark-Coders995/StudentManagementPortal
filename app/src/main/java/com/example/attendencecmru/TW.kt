package com.example.attendencecmru

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class TW : AppCompatActivity() {
    private lateinit var tabbedlayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter

    private lateinit var mainref: CollectionReference
    private var category: Int = 1


    private lateinit var chatPopup: PopupWindow
    private lateinit var popupRecyclerView: RecyclerView
    private lateinit var popupMessageEditText: EditText
    private lateinit var popupSendButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tw)

        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()

        category = intent.getIntExtra("category", 1)


        val nametf: TextView = findViewById(R.id.name)
        val categorytf: TextView = findViewById(R.id.category)

        val profileButton: ImageView = findViewById(R.id.profile)



        if (category == 1) {
            mainref = db.collection("students")
            categorytf.text = "Student"
        } else if (category == 2) {
            mainref = db.collection("teachers")
            categorytf.text = "Teacher"
        } else if (category == 3) {


        }

        tabbedlayout = findViewById(R.id.tablayout)
        viewPager2 = findViewById(R.id.viewpager2)

        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)

        tabbedlayout.addTab(tabbedlayout.newTab().setText("Course Evaluation"))
        tabbedlayout.addTab(tabbedlayout.newTab().setText("Time Table"))

        viewPager2.adapter = adapter

        tabbedlayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabbedlayout.selectTab(tabbedlayout.getTabAt(position))
            }
        })


        val documentRef = mainref.document(auth.currentUser!!.uid)
        documentRef.get()
            .addOnSuccessListener {

                val data = it.data
                val name = data?.get("name")
                nametf.text = name.toString()
            }

        profileButton.setOnClickListener {
            val i = Intent(this, Profile::class.java)
            i.putExtra("category", category)
            startActivity(i)
        }
        // Chat popup

        val popupView = LayoutInflater.from(this).inflate(R.layout.chat_layout_popup, null)
        popupRecyclerView = popupView.findViewById(R.id.popupRecyclerView)
        popupMessageEditText = popupView.findViewById(R.id.popupMessageEditText)
        popupSendButton = popupView.findViewById(R.id.popupSendButton)

        chatPopup = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val messageList: MutableList<Message> = mutableListOf()

        popupRecyclerView.layoutManager = LinearLayoutManager(this)
        val messageAdapter = MessageAdapter(messageList)
        popupRecyclerView.adapter = messageAdapter


        val messagesRef = db.collectionGroup("messages")

        messagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, "Listen failed", exception)
                    return@addSnapshotListener
                }
                val newMessageList: MutableList<Message> = mutableListOf()
                messageList.clear()
                for (document in snapshot!!) {
                    val senderUid = document.getString("sender")
                    val content = document.getString("content")
                    val timestamp = document.getTimestamp("timestamp")

                    val messages = db.collection("messages")
                    if (senderUid != null) {
                        messages.document(senderUid)
                            .get()
                            .addOnSuccessListener { senderSnapshot ->
                                if (senderSnapshot.exists()) {
                                    val senderName = senderSnapshot.getString("name")
                                    val message = Message(senderName ?: senderUid, content, timestamp)
                                    newMessageList.add(message)
                                    // Update the adapter with the new list of messages
                                    messageAdapter.updateMessages(newMessageList)
                                } else {
                                    Log.d(TAG, "Sender with UID $senderUid not found")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error getting sender document", e)
                            }
                    }
                }
            }


        val chatFAB: FloatingActionButton = findViewById(R.id.chatFAB)
        chatFAB.setOnClickListener {
            showChatPopup()
        }

        popupSendButton.setOnClickListener {

            val message = popupMessageEditText.text.toString()


            if (message.isNotEmpty()) {

                val messageData = hashMapOf(
                    "sender" to auth.currentUser!!.uid,
                    "content" to message,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                db.collection("messages")
                    .add(messageData)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "Message sent with ID: ${documentReference.id}")
                        // Clear the message input field after sending
                        popupMessageEditText.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding message", e)
                        // Handle the error appropriately (show a toast, etc.)
                    }
            }
            popupMessageEditText.text.clear()
        }
    }

    fun getMyVariable(): Int {
        return category // won't compile because myVariable is not in scope
    }

    fun getref(): CollectionReference {
        return mainref
    }


    private fun showChatPopup() {
        // Show the chat popup at the bottom of the screen
        chatPopup.showAtLocation(findViewById(R.id.twLayout), Gravity.BOTTOM, 0, 0)
    }


}