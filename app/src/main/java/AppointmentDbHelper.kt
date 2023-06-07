import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.nailsalon.Appointment
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

class AppointmentDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "appointments.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + AppointmentContract.AppointmentEntry.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AppointmentContract.AppointmentEntry.COLUMN_CUSTOMER_NAME + " TEXT NOT NULL, " +
                AppointmentContract.AppointmentEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                AppointmentContract.AppointmentEntry.COLUMN_TIME + " TEXT NOT NULL);"

        db.execSQL(SQL_CREATE_APPOINTMENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + AppointmentContract.AppointmentEntry.TABLE_NAME)
        onCreate(db)
    }

    fun insertAppointment(appointment: Appointment) {
        val db = writableDatabase

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val date = dateFormat.format(appointment.date)
        val time = timeFormat.format(appointment.date)

        val values = ContentValues().apply {
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
}