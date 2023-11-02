package com.example.mad_practical11_21012011040

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView : RecyclerView
    lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        var toolBar : Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolBar)

        val fetchBtn : FloatingActionButton = findViewById(R.id.btnSwap)

        recyclerView = findViewById(R.id.recyclerView)
        fetchBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data = HttpRequest().makeServiceCall(
                        "https://api.json-generator.com/templates/qjeKFdjkXCdK/data",
                        "rbn0rerl1k0d3mcwgw7dva2xuwk780z1hxvyvrb1"
                    )
                    withContext(Dispatchers.Main) {
                        try {
                            if(data != null)
                            {
                                runOnUiThread{getPersonDetailsFromJson(data)}
                            }
                        }
                        catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_button1 -> {
                Toast.makeText(this@MainActivity, "Clicked on item at menu!", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_button2 -> {
                var personList: ArrayList<Person> = databaseHelper.getAllPersons()
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = PersonAdapter(this, personList)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getPersonDetailsFromJson(sJson: String?)
    {
        val personList = ArrayList<Person>()
        try {
            val jsonArray = JSONArray(sJson)
            for(i in 0 until jsonArray.length())
            {
                val jsonObject = jsonArray[i] as JSONObject
                val person = Person(jsonObject)
                personList.add(person)
            }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter =PersonAdapter(this, personList)
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }
    }
}