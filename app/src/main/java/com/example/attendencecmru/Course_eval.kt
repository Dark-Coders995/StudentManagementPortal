package com.example.attendencecmru
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter

class Course_eval : Fragment() {

    private var scannedText: String? = null // Member variable to store scanned result
    private var isScanning: Boolean = false
    private var completeScaning: Boolean = false// Member variable to track scanning state

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == 123) {
            completeScaning=false
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    // QR code scanning cancelled
                } else {
                    scannedText = result.contents
                    completeScaning=true// Store the scanned result in the member variable
                    // Handle the scanned text here or update UI
                }
            }
            else {
                super.onActivityResult(requestCode, resultCode, data)
            }

        isScanning = false // Set scanning state to false after result is obtained
        }
//    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_course_eval, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val imageView:ImageView=view.findViewById(R.id.qrdisplay)

                if (imageView.visibility==View.VISIBLE) {
                    // Perform your task here, for example, save the data and then exit the fragment
                    // ...
                    imageView.visibility=View.GONE

                    val tableLayout = view.findViewById<TableLayout>(R.id.table_layout)
                    tableLayout.visibility=View.VISIBLE

                    // Call onBackPressed() to trigger the default behavior
                   // isEnabled = false

                } else if (isScanning) {
                    isScanning = false // Set scanning state to false if back button is pressed during scanning
                } else{
                    requireActivity().onBackPressed() // Perform default back button behavior
                }

//                if (isScanning) {
//                    isScanning = false // Set scanning state to false if back button is pressed during scanning
//                } else {
//                    requireActivity().onBackPressed() // Perform default back button behavior
//                }
            }
        })

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Course_eval.REQUEST_LOCATION_PERMISSION
            )
        }

        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()


        val tf2: TextView = view.findViewById(R.id.middletf)
        val tf3: TextView = view.findViewById(R.id.righttf)

        val parentActivity = activity as TW
        val category = parentActivity.getMyVariable()


        if (category == 1) {
            var counter = 1

            db.collection("attendance")
                .document(auth.currentUser!!.uid)
                .collection("subjects")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Get the attendance data for the subject

                        val subjectName = document.getString("subject_name")
                        val attended = document.getLong("attended")
                        var total:Long = 0
                        val subjecjtid=document.id



                        val id = document.id.toString()

                        val tableLayout = view.findViewById<TableLayout>(R.id.table_layout)
                        val layoutParams = TableRow.LayoutParams(0, 90)

                        layoutParams.bottomMargin = 10

                        val tableRow = TableRow(context)
                        layoutParams.weight = 2F

                        tableRow.layoutParams = layoutParams

                        val textView1 = TextView(context)
                        textView1.id = counter
                        counter++
                        textView1.layoutParams = layoutParams

                        tableRow.addView(textView1)

                        val textView2 = TextView(context)
                        textView2.id = counter
                        counter++
                        layoutParams.weight = 1F

                        textView2.layoutParams = layoutParams

                        tableRow.addView(textView2)


                        val textView3 = TextView(context)
                        textView3.id = counter
                        counter++
                        layoutParams.weight = 2F
                        textView3.layoutParams = layoutParams

                        textView1.textSize = 18F
                        textView1.gravity = Gravity.CENTER
                        textView1.setTypeface(null, Typeface.BOLD)
                        textView1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView1.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
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
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView2.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
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
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView3.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView3.setPadding(0, 2, 0, 2)
                        textView3.setAllCaps(true)


                        tableRow.addView(textView3)

                        val docReff = db.collection("subjects").document(subjecjtid)
                        docReff.get()
                            .addOnSuccessListener { document ->

                                val teacherid = document.get("teacher_id")
                                val docRef = db.collection("total_teacher_attendance").document(
                                    teacherid.toString()
                                ).collection("subjects").document(subjecjtid)

                                docRef.get()
                                    .addOnSuccessListener { document ->
                                        if (document != null) {
                                            total = document.getLong("total_classes")!!
                                            textView1.text = subjectName.toString()
                                            textView2.text = attended.toString() + "/" + total.toString()
                                            if (total.toString() == "0")
                                                textView3.text = "0"
                                            else {

                                                val percentage = (attended!!.toFloat() / total!!.toFloat()) * 100
                                                textView3.text = percentage.toInt().toString()+"%"
                                            }
                                        }
                                    }
                            }

                        tableLayout.addView(tableRow)


                    }
                }
                .addOnFailureListener {
                    // Handle any errors that occur during the query
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                }

            val scanqr:FloatingActionButton=view.findViewById(R.id.fab)

            scanqr.visibility=View.VISIBLE

            scanqr.setOnClickListener{
                scanQRCode()
//                // Show a success message
//                Toast.makeText(
//                    requireContext(),
//                    "QR code scanned successfully!",
//                    Toast.LENGTH_SHORT
//                ).show()
                if(completeScaning) {
                    val scannedData = scannedText
                    val separator = "\n" // or "," or any other character you used as a separator
                    val parts = scannedData?.split(separator)
                    if (parts!!.size == 2) {
                        val text = parts[0]
                        val locationString = parts[1]


                        // Check if the scanned location matches the current location
                        val currentLocation = getLocation()
                        if (locationString == currentLocation) {
                            // Show a success message
                            Toast.makeText(
                                requireContext(),
                                "QR code scanned successfully!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val dbs = Firebase.firestore
                            val docRef = db.collection("attendence").document(auth.currentUser!!.uid).collection("subject")
                                .document(text)

                            docRef.update("attended", FieldValue.increment(1))
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Attendence Marked Relogin to view updated attendence",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireContext(),
                                        "Error try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }




                        } else {
                            // Show an error message
                            Toast.makeText(
                                requireContext(),
                                "QR code does not contain current location",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Show an error message for invalid QR code data
                        Toast.makeText(requireContext(), "Invalid QR code data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


            }


        } else if (category == 2) {


            tf3.text = "Semester"
            tf2.text = "Total"

            val qr:ImageView=view.findViewById(R.id.qr)

            val layoutParams1 = TableRow.LayoutParams(0,    138)
            layoutParams1.weight=1F
            qr.layoutParams=layoutParams1


            var counter = 100

            val teacherId = auth.currentUser!!.uid

            val teacherAttendanceRef = db.collection("total_teacher_attendance")
                .document(teacherId)
                .collection("subjects")

            teacherAttendanceRef.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {


                        // Get the attendance data for the subject

                        val subjectName = document.getString("name_of_subject")
                        val semester = document.getString("semester")
                        val total = document.getLong("total_classes")


                        val id = document.id.toString()

                        val tableLayout = view.findViewById<TableLayout>(R.id.table_layout)
                        val layoutParams = TableRow.LayoutParams(0, 90)

                        layoutParams.bottomMargin = 10

                        val tableRow = TableRow(context)
                        layoutParams.weight = 2F



                        tableRow.layoutParams = layoutParams

                        val textView1 = TextView(context)
                        textView1.id = counter
                        counter++
                        textView1.layoutParams = layoutParams

                        tableRow.addView(textView1)

                        val textView2 = TextView(context)
                        textView2.id = counter
                        counter++
                        layoutParams.weight = 1F

                        textView2.layoutParams = layoutParams

                        tableRow.addView(textView2)


                        val textView3 = TextView(context)
                        textView3.id = counter
                        counter++
                        layoutParams.weight = 2F
                        textView3.layoutParams = layoutParams

                        textView1.textSize = 15F
                        textView1.gravity = Gravity.CENTER
                        textView1.setTypeface(null, Typeface.BOLD)
                        textView1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView1.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView1.setPadding(0, 2, 0, 2)
                        textView1.setAllCaps(true)

                        textView2.textSize = 15F
                        textView2.gravity = Gravity.CENTER
                        textView2.setTypeface(null, Typeface.BOLD)
                        textView2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView2.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView2.setPadding(0, 2, 0, 2)
                        textView2.setAllCaps(true)

                        textView3.textSize = 15F
                        textView3.gravity = Gravity.CENTER
                        textView3.setTypeface(null, Typeface.BOLD)
                        textView3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView3.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView3.setPadding(0, 2, 0, 2)
                        textView3.setAllCaps(true)


                        tableRow.addView(textView3)

                        val imageButton = ImageButton(context)
                        layoutParams.weight=1F
                        imageButton.layoutParams=layoutParams

                        imageButton.scaleType=ImageView.ScaleType.CENTER_INSIDE
                        imageButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
                        imageButton.setPadding(0, 2, 0, 2)
                        imageButton.setImageResource(R.drawable.ic_baseline_qr_code_24)


                        tableRow.addView(imageButton)


                        textView1.text = subjectName.toString()
                        textView3.text = semester.toString()
                        textView2.text = total.toString()





                        imageButton.setOnClickListener {
                            tableLayout.visibility=View.GONE
                            val qrimage:ImageView=view.findViewById(R.id.qrdisplay)
                            qrimage.visibility=View.VISIBLE



                            val text = "${document.id}"
                            val location = getLocation()

                            // Combine the text and location
                            val combinedText = "$text\n$location"

                            // Generate the QR code
                            val width = 500
                            val height = 500
                            val qrCode = generateQRCode(combinedText, width, height)

                            val qrCodeImageView:ImageView= view.findViewById(R.id.qrdisplay)
                            qrCodeImageView.setImageBitmap(qrCode )

                            val docRef = db.collection("total_teacher_attendance").document(
                                teacherId
                            ).collection("subjects").document(document.id)

                            docRef.update("total_classes", FieldValue.increment(1))
                                .addOnSuccessListener{
                                    textView2.text=(textView2.text.toString().toInt() +1).toString()
                                }


                        }


                        tableRow.setOnClickListener {
                            val intent = Intent(requireContext(), attendenceView::class.java)
                            intent.putExtra("key", id);
                            val t=textView2.text.toString().toInt()
                            intent.putExtra("key1",t );
                            requireContext().startActivity(intent);

                        }




//
//                        tableRow.setOnClickListener {
//                            tableLayout.visibility=View.GONE
//
//                            val table2:TableLayout=view.findViewById(R.id.total_show)
//
//                            table2.visibility=View.VISIBLE
//
//                            val subid:TextView=view.findViewById(R.id.sub_id)
//
//                            subid.text=id
//
//                            val tableLayout = view.findViewById<TableLayout>(R.id.total_show)
//                            val layoutParams = TableRow.LayoutParams(0, 90)
//
//                            layoutParams.bottomMargin = 10
//
//                            val tableRow1 = TableRow(context)
//                            layoutParams.weight = 2F
//
//                            tableRow.layoutParams = layoutParams
//
//                            val textView1 = TextView(context)
//                            textView1.id = counter
//                            counter++
//                            textView1.layoutParams = layoutParams
//
//                            tableRow1.addView(textView1)
//
//                            val textView2 = TextView(context)
//                            textView2.id = counter
//                            counter++
//                            layoutParams.weight = 1F
//
//                            textView2.layoutParams = layoutParams
//
//                            tableRow1.addView(textView2)
//
//
//                            val textView3 = TextView(context)
//                            textView3.id = counter
//                            counter++
//                            layoutParams.weight = 2F
//                            textView3.layoutParams = layoutParams
//                            tableRow1.addView(textView3)
//
//                            textView1.textSize = 18F
//                            textView1.gravity = Gravity.CENTER
//                            textView1.setTypeface(null, Typeface.BOLD)
//                            textView1.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color.black
//                                )
//                            )
//                            textView1.setBackgroundColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color.grey
//                                )
//                            )
//                            textView1.setPadding(0, 2, 0, 2)
//                            textView1.setAllCaps(true)
//
//                            textView2.textSize = 18F
//                            textView2.gravity = Gravity.CENTER
//                            textView2.setTypeface(null, Typeface.BOLD)
//                            textView2.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color.black
//                                )
//                            )
//                            textView2.setBackgroundColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color.grey
//                                )
//                            )
//                            textView2.setPadding(0, 2, 0, 2)
//                            textView2.setAllCaps(true)
//
//                            textView3.textSize = 18F
//                            textView3.gravity = Gravity.CENTER
//                            textView3.setTypeface(null, Typeface.BOLD)
//                            textView3.setTextColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color.black
//                                )
//                            )
//                            textView3.setBackgroundColor(
//                                ContextCompat.getColor(
//                                    requireContext(),
//                                    R.color.grey
//                                )
//                            )
//                            textView3.setPadding(0, 2, 0, 2)
//                            textView3.setAllCaps(true)
//
//                            table2.addView(tableRow1)
//
//                            val docRef = db.collection("attendence")
//
//                            docRef.get().addOnSuccessListener {documents->
//                                for(document in documents) {
//                                    val stuid = document.id
//                                    val ref= db.collection("attendence").document(stuid).collection("subjects").document(subid.toString())
//
//                                    ref.get().addOnSuccessListener{
//                                        val data=it.data
//                                        val attended = data?.get("attended").toString().toInt()
//                                        val total = textView2.text.toString().toInt()
//
//                                        val sturef=db.collection("students").document(stuid).get().addOnSuccessListener {
//                                            val data=it.data
//                                            val name = data?.get("name").toString()
//                                            val usn=data?.get("usn").toString()
//
//                                            textView1.text="$usn\n$name"
//                                            textView2.text="$attended/$total"
//
//                                            if (total.toString() == "0")
//                                                textView3.text = "0"
//                                            else {
//
//                                                val percentage = (attended!!.toFloat() / total!!.toFloat()) * 100
//                                                textView3.text = percentage.toString()
//                                            }
//
//                                        }
//
//
//                                    }
//
//                                }
//                            }
//



              //          }



//                        tableLayout.addView(tableRow)

//                        imageButton.setOnClickListener {
//                            tableLayout.visibility=View.GONE
//                            val qrimage:ImageView=view.findViewById(R.id.qrdisplay)
//                            qrimage.visibility=View.VISIBLE
//
//
//
//                            val text = "${document.id}"
//                            val location = getLocation()
//
//                            // Combine the text and location
//                            val combinedText = "$text\n$location"
//
//                            // Generate the QR code
//                            val width = 500
//                            val height = 500
//                            val qrCode = generateQRCode(combinedText, width, height)
//
//                            val qrCodeImageView:ImageView= view.findViewById(R.id.qrdisplay)
//                            qrCodeImageView.setImageBitmap(qrCode )
//
//                            val docRef = db.collection("total_teacher_attendance").document(
//                                teacherId
//                            ).collection("subjects").document(document.id)
//
//                            docRef.update("total_classes", FieldValue.increment(1))
//                                .addOnSuccessListener{
//                                    textView2.text=(textView2.text.toString().toInt() +1).toString()
//                                }
//
//
//
//
//
//                        }

                        tableLayout.addView(tableRow)

                    }


                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }

        }
        }

    @SuppressLint("MissingPermission")
    private fun getLocation(): String {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if location permission is granted

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Course_eval.REQUEST_LOCATION_PERMISSION
            )
            return "Location permission not granted"
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                Course_eval.REQUEST_LOCATION_PERMISSION
            )
            return "Location permission not granted"
        }

        // Get the last known location from the location manager
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        // Set up location listener to get the current location
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Do something with the new location
                locationManager.removeUpdates(this)
            }

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        // Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        // Return the location string
        return if (lastKnownLocation != null) {
            "Latitude: ${lastKnownLocation.latitude}, Longitude: ${lastKnownLocation.longitude}"
        } else {
            Toast.makeText(context, "ReLogin location not available rigth now invalid QR", Toast.LENGTH_LONG).show()
            "Location not available"
        }
    }

    private fun generateQRCode(text: String, width: Int, height: Int): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }

    private fun scanQRCode() {
        isScanning = true // Set scanning state to true before initiating QR code scanning
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setOrientationLocked(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan QR Code")
        integrator.setBeepEnabled(false)

        integrator.initiateScan()

    }
    companion object {
        const val REQUEST_LOCATION_PERMISSION = 100
    }




}


