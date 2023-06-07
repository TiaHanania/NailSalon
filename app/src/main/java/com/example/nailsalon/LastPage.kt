package com.example.nailsalon

import AppointmentDbHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class LastPage : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var num: EditText
    private lateinit var confirm: Button
    private lateinit var delete: Button
    private lateinit var dbHelper: AppointmentDbHelper
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_page)
        initView()
        dbHelper = AppointmentDbHelper(this)

        val intent = intent
        selectedDate = intent.getStringExtra("date")
        selectedTime = intent.getStringExtra("time")

        confirm.setOnClickListener {
            addClient()
        }

        if (selectedDate != null && selectedTime != null) {
            val appointment = Appointment(-1, "", "", selectedDate!!, selectedTime!!)
            // Pass the appointment object to the addClient() method
            confirm.setOnClickListener {
                addClient(appointment)
            }
        }

        delete.setOnClickListener {
            val appointmentId = 123 // Replace with your actual appointment ID
            val rowsDeleted = dbHelper.deleteAppointment(appointmentId)

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Appointment deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete appointment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addClient() {
        // Implement your logic to add a client without the appointment information
    }

    private fun initView() {
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        num = findViewById(R.id.num)
        confirm = findViewById(R.id.confirm)
        delete = findViewById(R.id.btnDelete)
    }

    private fun addClient(appointment: Appointment) {
        val edName = name.text.toString()
        val edEmail = email.text.toString()
        val edNum = num.text.toString()

        if (edName.isEmpty() || edEmail.isEmpty() || edNum.isEmpty()) {
            Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        dbHelper.insertAppointment(appointment)
        Toast.makeText(this, "Client added successfully", Toast.LENGTH_SHORT).show()

        name.text.clear()
        email.text.clear()
        num.text.clear()
    }
}

data class Appointment(
    val id: Int,
    val customerName: String,
    val date: String,
    val time: String,
    val selectedTime: String
)