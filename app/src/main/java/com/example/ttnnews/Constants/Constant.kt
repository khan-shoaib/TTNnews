package com.example.ttnnews.Constants

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.ttnnews.model.CategoryModel


class Constant {



    companion object {
        fun getCatergoryData(): java.util.ArrayList<CategoryModel> {
            val myArrayList = ArrayList<CategoryModel>()
            myArrayList.add(CategoryModel("Business"))
            myArrayList.add(CategoryModel("General"))
            myArrayList.add(CategoryModel("Entertainment"))
            myArrayList.add(CategoryModel("Health"))
            myArrayList.add(CategoryModel("Science"))
            myArrayList.add(CategoryModel("Sports"))
            myArrayList.add(CategoryModel("Technology"))
            return  myArrayList;
        }
        val API_KEY = "8f4f5d6c37ee3540afb179e31b506364"
         val SOURCENAME = "sourcename"
         val URL = "url"

        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
            return false
        }


    }




}