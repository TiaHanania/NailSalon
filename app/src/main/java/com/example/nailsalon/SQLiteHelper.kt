import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "clients.db"
        private const val DATABASE_VERSION = 1
        private const val TBLClient = "Client"
        private const val NUM = "number"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val DATE = "date"
        private const val TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CLIENTS_TABLE = ("CREATE TABLE " + TBLClient + "("
                + NUM + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT,"
                + EMAIL + " TEXT,"
                + DATE + " TEXT,"
                + TIME + " TEXT" + ")")
        db.execSQL(CREATE_CLIENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TBLClient")
        onCreate(db)
    }

    fun insertClient(client: Client) {
        val db = writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NAME, client.name)
        contentValues.put(EMAIL, client.email)
        contentValues.put(DATE, client.date)
        contentValues.put(TIME, client.time)

        db.insert(TBLClient, null, contentValues)
        db.close()
    }

    fun insertAppointment(appointment: Appointment) {
        val db = writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NAME, appointment.name)
        contentValues.put(EMAIL, appointment.email)
        contentValues.put(DATE, appointment.date)
        contentValues.put(TIME, appointment.time)

        db.insert(TBLClient, null, contentValues)
        db.close()
    }

    data class Client(
        val name: String,
        val email: String,
        val date: String,
        val time: String
    )

    data class Appointment(
        val name: String,
        val email: String,
        val date: String,
        val time: String
    )
}

fun main() {
    val context: Context? = null // Replace with your actual context

    val sqliteHelper = SQLiteHelper(context!!)
    val client = SQLiteHelper.Client(
        name = "John Doe",
        email = "john.doe@example.com",
        date = "2023-05-18",
        time = "10:00 AM"
    )
    sqliteHelper.insertClient(client)

    val appointment = SQLiteHelper.Appointment(
        name = "John Doe",
        email = "john.doe@example.com",
        date = "2023-05-18",
        time = "10:00 AM"
    )
    sqliteHelper.insertAppointment(appointment)
}
