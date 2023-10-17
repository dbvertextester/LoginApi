package com.dbvertex.loginapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.dbvertex.loginapi.databinding.ActivityMainBinding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit   var edemail: EditText
    lateinit    var edpass: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        edemail = binding.txtInputEmail.editText!!
        edpass = binding.txtPass.editText!!


        binding.btnLogin.setOnClickListener {
            val email = edemail.text.toString()
            val pass = edpass.text.toString()
            performLogin(email, pass)

        }
    }

    private fun performLogin(email: String, pass: String) {
        Log.d("performLogin","Enter in performLogin")

        var baseUrl = "https://work.dbvertex.com/dating/api/webservice/"
        val builder = OkHttpClient.Builder().addInterceptor(ApiKeyInterceptor())
        builder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val originalHttpUrl = chain.request().url()
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("x-api-key", "dating@123").build()
            request.url(url)
            chain.proceed(request.build())
        }

        val request = builder.build()

        var retrofit = Retrofit.Builder().baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create())

             .client(request)
                                 .build();
        Log.d("Retrofit","Enter in Retrofit")
          var apiServise = retrofit.create(ApiServise::class.java)
        val loginRequest = LoginRequest(email,pass)
        val apiKey: String = "dating@123" // Your API key
        val call = apiServise.login(apiKey, loginRequest)
        Log.d("Retrofit","Enter call  Retrofit")
        call.enqueue( object : Callback<LoginResponse>{


            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                 if (response.isSuccessful){
                     val token = response.body()?.token
                     val intent = Intent(this@MainActivity,MainPage::class.java)
                     startActivity(intent)
                 }else {
                      Log.d("onResponce",response.toString())

                 }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("onFailure",t.localizedMessage)
            }

        })

    }
    class ApiKeyInterceptor() : Interceptor {

        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header("Accept", "application/json")
            requestBuilder.header("x-api-key", "dating@123")


            return chain.proceed(requestBuilder.build())
        }
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<LoginResponse>) {

}
