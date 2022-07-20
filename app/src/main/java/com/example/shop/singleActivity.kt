package com.example.shop

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONArray
import org.json.JSONObject

class singleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        val prefs : SharedPreferences = this.getSharedPreferences("shop", Context.MODE_PRIVATE)
        val prodname = findViewById<TextView>(R.id.p_name)
        val prodesc = findViewById<TextView>(R.id.p_desc)
        val prodcost = findViewById<TextView>(R.id.p_cost)
        val img = findViewById<ImageView>(R.id.img_url)

        val flashname = prefs.getString("prod_name","")
        val flashdesc = prefs.getString("prod_desc","")
        val flashcost = prefs.getString("prod_cost","")
        val flashimg = prefs.getString("img_url","")


        prodname.text = flashname
        prodesc.text = flashdesc
        prodcost.text = flashcost

        Glide.with(applicationContext).load(flashimg)
            .apply(RequestOptions().centerCrop())
            .into(img)

        val progressbar = findViewById(R.id.progressbar) as ProgressBar
        progressbar.visibility = View.GONE
        val phone = findViewById(R.id.phone) as EditText
        val pay = findViewById(R.id.pay) as Button
        pay.setOnClickListener{
            progressbar.visibility = View.VISIBLE
            //initialize loop j
            val client = AsyncHttpClient(88,443)
            //create a jsonobject
            val json = JSONObject()
            //convert data to json
            json.put("amount","10")
            json.put("phone",phone.text.toString())
            //create the body as a json entity
            val body = StringEntity(json.toString())
            //use loop j to post request
            client.post(this,"https://modcom.pythonanywhere.com/mpesa_payment",
                body,
                "application/json",
                object :JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        response: JSONObject?
                    ) {
                        Toast.makeText(applicationContext, "Paid successfully", Toast.LENGTH_LONG)
                            .show()
                        progressbar.visibility = View.GONE
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        throwable: Throwable?,
                        errorResponse: JSONObject?
                    ) {
                        Toast.makeText(applicationContext, "Error During Payment", Toast.LENGTH_LONG)
                            .show()
                        progressbar.visibility = View.GONE
                    }


                }
            )


        }






    }
}