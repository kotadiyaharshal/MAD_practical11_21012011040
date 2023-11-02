package com.example.mad_practical11_21012011040

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable
class PersonAdapter(private val context: Context, private val array: ArrayList<Person>):
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(){
    lateinit var databaseHelper: DatabaseHelper
    init {
        // Initialize the databaseHelper here
        databaseHelper = DatabaseHelper(context)
    }
    inner class PersonViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val nameTxt : TextView = itemView.findViewById(R.id.name)
        val emailTxt : TextView = itemView.findViewById(R.id.emailid)
        val phoneTxt : TextView = itemView.findViewById(R.id.mobile)
        val addressTxt : TextView = itemView.findViewById(R.id.address)
        val mapBtn : ImageView = itemView.findViewById(R.id.btnLocation)
        val deleteBtn : ImageView = itemView.findViewById(R.id.btnDelete)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return PersonViewHolder(view)
    }
    override fun getItemCount(): Int {
        return array.size
    }
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = array[position]
        holder.nameTxt.text = person.name
        holder.emailTxt.text = person.emailId
        holder.phoneTxt.text = person.phoneNo
        holder.addressTxt.text = person.address
        val obj = person as Serializable
        databaseHelper.insertPerson(person)
        holder.mapBtn.setOnClickListener {
            Intent(this@PersonAdapter.context, MapsActivity::class.java).apply {
                putExtra("Object",obj)
                this@PersonAdapter.context.startActivity(this)
            }
        }
        holder.deleteBtn.setOnClickListener {
            var count = databaseHelper.deletePerson(person.id)
            if(count > 0)
            {
                Toast.makeText(this.context, "${person.name}'s details deleted successfully!",
                    Toast.LENGTH_SHORT).show()
                array.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }
}