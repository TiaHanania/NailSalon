package com.example.nailsalon

import AppointmentDbHelper
import android.app.*
import android.content.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.view.*
import androidx.recyclerview.widget.*
import com.example.nailsalon.AppointmentContract.AppointmentEntry.COLUMN_CUSTOMER_NAME
import com.example.nailsalon.AppointmentContract.AppointmentEntry.COLUMN_DATE
import com.example.nailsalon.AppointmentContract.AppointmentEntry.COLUMN_TIME
import com.example.nailsalon.AppointmentContract.AppointmentEntry.TABLE_NAME
import java.text.*
import java.util.*

class AppointmentActivity : AppCompatActivity() {

    private lateinit var selectDateButton: Button
    private lateinit var selectTimeButton: Button
    private lateinit var selectedDateTime: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        val mapLocation: TextView = findViewById(R.id.mapLocation)
        mapLocation.setOnClickListener {
            val location = "Your Location"
            val mapIntentUri = "geo:0,0?q=$location"
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapIntentUri))
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show()
            }
        }

        val optionSpinner: Spinner = findViewById(R.id.optionSpinner)
        val options1 = listOf("Normal color", "Gel color", "Gel extensions")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options1)
        optionSpinner.adapter = adapter

        val numColumns = 4

        val layoutManager = GridLayoutManager(this, numColumns)

        optionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = options1[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val appointmentButton: Button = findViewById(R.id.bookAppointmentButton)
        appointmentButton.setOnClickListener {
            val intent = Intent(this, LastPage::class.java)
            startActivity(intent)
    }

        val designSpinner: Spinner = findViewById(R.id.designSpinner)
        val design: TextView = findViewById(R.id.design)
        val options2 = listOf("French", "Ombre", "Drawing", "Chrome")

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, options2)
        designSpinner.adapter = adapter2

        designSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = options2[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val currentDate = Calendar.getInstance()
        calendarView.date = currentDate.timeInMillis

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            bookAppointment(selectedDate.time)
        }

        selectDateButton = findViewById(R.id.selectDateButton)
        selectTimeButton = findViewById(R.id.selectTimeButton)

        selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        selectTimeButton.setOnClickListener {
            showTimePickerDialog()
        }

        selectedDateTime = Calendar.getInstance()
    }

    private fun bookAppointment(selectedDate: Date) {
        val formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate)
        Toast.makeText(this, "Selected date: $formattedDate", Toast.LENGTH_SHORT).show()
    }

    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this@AppointmentActivity,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                selectedDateTime.set(Calendar.YEAR, year)
                selectedDateTime.set(Calendar.MONTH, monthOfYear)
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDateTime.time)
                selectDateButton.text = formattedDate
            },
            year,
            month,
            day
        )

        datePicker.datePicker.minDate = currentDate.timeInMillis
        datePicker.show()
    }

    private fun showTimePickerDialog() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            this@AppointmentActivity,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedDateTime.set(Calendar.MINUTE, minute)

                val formattedTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(selectedDateTime.time)
                selectTimeButton.text = formattedTime
            },
            hour,
            minute,
            false
        )

        timePicker.show()
    }

    private fun insertAppointment(customerName: String, date: String, time: String) {
        val dbHelper = AppointmentDbHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_CUSTOMER_NAME, customerName)
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
        }

        db.insert(TABLE_NAME, null, values)
    }

    private fun navigateToLastPage() {
        val intent = Intent(this, LastPage::class.java)
        startActivity(intent)
    }
}
