package com.aboust.develop_guide.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aboust.develop_guide.R

data class Item(val id: Long, val icon: String?, val name: String, val clazz: Class<*>)


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
