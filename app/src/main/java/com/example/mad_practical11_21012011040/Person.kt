package com.example.mad_practical11_21012011040

import org.json.JSONObject
import java.io.Serializable

class Person (
    var id:String,
    var name:String,
    var emailId:String,
    var phoneNo: String,
    var address :String,
    var latitude:Double,
    var longitude:Double):Serializable {
    constructor(jsonObject:JSONObject):this ("","","","","",90.0,09.8){
        id = jsonObject.getString("id")
        emailId = jsonObject.getString("email")
        phoneNo = jsonObject.getString("phone")
        val profileJson = jsonObject.getJSONObject("profile")
        name = profileJson.getString("name")
        address = profileJson.getString("address")
        val locationJson = profileJson.getJSONObject("location")
        latitude = locationJson.getDouble("lat")
        longitude = locationJson.getDouble("long")
    }

}