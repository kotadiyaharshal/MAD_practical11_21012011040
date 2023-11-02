package com.example.mad_practical11_21012011040

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

class HttpRequest {
    private val TAG = "HttpRequest"

    fun makeServiceCall(reqUrl: String?, token: String?=null): String? {
        var response: String? = null
        try {
            val url = URL(reqUrl)
            val conn = url.openConnection() as HttpURLConnection
            if (token != null)
            {
                conn.setRequestProperty("Authorization","Bearer $token")
                conn.setRequestProperty("Content-Type","application/json")
            }
            conn.requestMethod = "GET"
            response = convertStreamToString(BufferedInputStream(conn.inputStream))
        }
        catch (e : MalformedURLException)
        {
            Log.e(TAG, "MalformedURLException: " + e.message)
        }
        catch (e : ProtocolException)
        {
            Log.e(TAG, "ProtocolException: " + e.message)
        }
        catch (e : IOException)
        {
            Log.e(TAG, "IOException: " + e.message)
        }
        catch (e: Exception)
        {
            Log.e(TAG, "Exception: " + e.message)
        }
        return response
    }

    private fun convertStreamToString(`is`: InputStream):String
    {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String?=null
        try {
            while (reader.readLine().also { line = it } != null)
            {
                sb.append(line).append('\n')
            }
        }
        catch (e : IOException)
        {
            Log.i(TAG, "convertStreamToString: $line")
            e.printStackTrace()
        }
        finally {
            try {
                `is`.close()
            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }
}