package com.example.mad_practical11_21012011040

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONObject
class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION){
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "persons.db"
        private const val TABLE_NAME = "person"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PERSON_NAME = "person_name"
        private const val COLUMN_PERSON_EMAIL_ID = "person_email_id"
        private const val COLUMN_PERSON_PHONE_NO = "person_phone_no"
        private const val COLUMN_PERSON_ADDRESS = "person_address"
        private const val COLUMN_PERSON_GPS_LAT = "person_lat"
        private const val COLUMN_PERSON_GPS_LONG = "person_long"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_PERSON_NAME + " TEXT,"
                + COLUMN_PERSON_EMAIL_ID + " TEXT,"
                + COLUMN_PERSON_PHONE_NO + " TEXT,"
                + COLUMN_PERSON_ADDRESS + " TEXT,"
                + COLUMN_PERSON_GPS_LAT + " REAL,"
                + COLUMN_PERSON_GPS_LONG + " REAL)")

        if (db != null) {
            db.execSQL(CREATE_TABLE)
        }
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        }
    }
    fun insertPerson(person: Person) : Long
    {
        val db =writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID,person.id)
        contentValues.put(COLUMN_PERSON_NAME,person.name)
        contentValues.put(COLUMN_PERSON_EMAIL_ID,person.emailId)
        contentValues.put(COLUMN_PERSON_PHONE_NO,person.phoneNo)
        contentValues.put(COLUMN_PERSON_ADDRESS,person.address)
        contentValues.put(COLUMN_PERSON_GPS_LAT,person.latitude)
        contentValues.put(COLUMN_PERSON_GPS_LONG,person.longitude)
        val count = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return count
    }
    fun deletePerson(personId: String) : Int
    {
        val db = writableDatabase
        val selection ="$COLUMN_ID = ?"
        val selectionArgs = arrayOf(personId)
        val count =db.delete(TABLE_NAME,selection,selectionArgs)
        db.close()
        return count
    }
    @SuppressLint("Range")
    fun getAllPersons() :ArrayList<Person>
    {
        val personList = arrayListOf<Person>()
        val db =readableDatabase
        var query = "SELECT * FROM $TABLE_NAME"
        var cursor : Cursor =db.rawQuery(query,null)
        while (cursor.moveToNext())
        {
            var id : String = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
            var name : String = cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME))
            var email : String = cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_EMAIL_ID))
            var phone : String = cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PHONE_NO))
            var address: String = cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_ADDRESS))

            var latitude : Double =
                cursor.getDouble(cursor.getColumnIndex(COLUMN_PERSON_GPS_LAT))
            var longitude: Double =
                cursor.getDouble(cursor.getColumnIndex(COLUMN_PERSON_GPS_LONG))
            val jsonObject = JSONObject()
            jsonObject.put("id", id)
            jsonObject.put("email", email)
            jsonObject.put("phone", phone)
            val profileJson = JSONObject()
            profileJson.put("name", name) // You'll need to fill in the actual name value here
            profileJson.put("address", address)
            val locationJson = JSONObject()
            locationJson.put("lat", latitude)
            locationJson.put("long", longitude)
            profileJson.put("location", locationJson)
            jsonObject.put("profile", profileJson)
            val person = Person(jsonObject)
            personList.add(person)
        }
        cursor.close()
        db.close()
        return personList
    }
}