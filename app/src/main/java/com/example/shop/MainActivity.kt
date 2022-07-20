package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    lateinit var productList : ArrayList<Product>
    lateinit var progress : ProgressBar
    lateinit var recyclerView: RecyclerView //variable and its view type e.g edittext,textviews,buttons,recyclerviews,imageviews
    lateinit var recyclerAdapter: RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Find the recyclerview and the progress bar from the MainActivity XML
        recyclerView = findViewById(R.id.recycler)
        progress = findViewById(R.id.progressBar)

        //progress.visibility  = View.VISIBLE

        //pass the list of products to the adapter
        recyclerAdapter = RecyclerAdapter(applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        //Use loop J to get access of the API..It is a library that works with http methods e.g GEt and POST.


        val client = AsyncHttpClient(true,80,443)

        client.get(
            this, "https://modcom.pythonanywhere.com/api/all",
            null,
        "application/json",//API is in json format
            object : JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?
                ) {

                  val gson = GsonBuilder().create()
                    val list = gson.fromJson(response.toString(),Array<Product>::class.java).toList()
                    recyclerAdapter.setProductListItems(list)
                    progress.visibility = View.GONE


                }
                //Incase
                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONArray?
                ) {
                    Toast.makeText(applicationContext,"no products on sale"+statusCode, Toast.LENGTH_LONG).show()
                    progress.visibility = View.GONE
                }

            }

        )

        //now put the adapter to recycler view
        recyclerView.adapter = recyclerAdapter
    }
}