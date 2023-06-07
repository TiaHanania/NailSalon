package com.example.nailsalon

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.text.SimpleDateFormat
import java.util.*

class AppointmentContract private constructor() {
    object AppointmentEntry : BaseColumns {
        const val TABLE_NAME = "appointments"
        const val COLUMN_ID = "id"
        const val COLUMN_CUSTOMER_NAME = "customer_name"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
    }
}

class AppointmentDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_APPOINTMENTS_TABLE =
            ("CREATE TABLE " + AppointmentContract.AppointmentEntry.TABLE_NAME + " ("
                    + AppointmentContract.AppointmentEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + AppointmentContract.AppointmentEntry.COLUMN_CUSTOMER_NAME + " TEXT NOT NULL, "
                    + AppointmentContract.AppointmentEntry.COLUMN_DATE + " TEXT NOT NULL, "
                    + AppointmentContract.AppointmentEntry.COLUMN_TIME + " TEXT NOT NULL);")
        db.execSQL(SQL_CREATE_APPOINTMENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + AppointmentContract.AppointmentEntry.TABLE_NAME)
        onCreate(db)
    }

    fun insertAppointment(customerName: String, date: String, time: String) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(AppointmentContract.AppointmentEntry.COLUMN_CUSTOMER_NAME, customerName)
            put(AppointmentContract.AppointmentEntry.COLUMN_DATE, date)
            put(AppointmentContract.AppointmentEntry.COLUMN_TIME, time)
        }

        val result = db.insert(AppointmentContract.AppointmentEntry.TABLE_NAME, null, values)
    }

    fun deleteAppointment(appointmentId: Int): Int {
        val db = writableDatabase

        return db.delete(
            AppointmentContract.AppointmentEntry.TABLE_NAME,
            "${AppointmentContract.AppointmentEntry.COLUMN_ID} = ?",
            arrayOf(appointmentId.toString())
        )
    }

    data class Appointment(
        val num: Int,
        val name: String,
        val email: String,
        val date: String,
        val time: String
    )

    fun readAllData(): List<Appointment> {
        val appointmentsList = ArrayList<Appointment>()

        val db = this.readableDatabase
        val query = "SELECT * FROM ${AppointmentContract.AppointmentEntry.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val idColumnIndex = cursor.getColumnIndex(AppointmentContract.AppointmentEntry.COLUMN_ID)
            val customerNameColumnIndex = cursor.getColumnIndex(AppointmentContract.AppointmentEntry.COLUMN_CUSTOMER_NAME)
            val dateColumnIndex = cursor.getColumnIndex(AppointmentContract.AppointmentEntry.COLUMN_DATE)
            val timeColumnIndex = cursor.getColumnIndex(AppointmentContract.AppointmentEntry.COLUMN_TIME)

            do {
                val id = if (idColumnIndex != -1) cursor.getInt(idColumnIndex) else -1
                val customerName = if (customerNameColumnIndex != -1) cursor.getString(customerNameColumnIndex) else ""
                val date = if (dateColumnIndex != -1) cursor.getString(dateColumnIndex) else ""
                val time = if (timeColumnIndex != -1) cursor.getString(timeColumnIndex) else ""

                val appointment = Appointment(id, customerName, "", date, time)
                appointmentsList.add(appointment)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return appointmentsList
    }

    companion object {
        private const val DATABASE_NAME = "appointment.db"
        private const val DATABASE_VERSION = 1
    }
}
