package com.example.myapplication

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var msgBtn: ImageButton
    var targetMsg = "this is target message"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar).apply {
            title = "application"
        })

        supportFragmentManager.beginTransaction().replace(R.id.containerFragment, MainFragment()).commit()

//        msgBtn = findViewById<ImageButton>(R.id.addButton)
//        msgBtn.setOnClickListener{
////            val nextIntent = Intent(this, InputFragment::class.java)
////            startActivity(nextIntent)
//            supportFragmentManager.beginTransaction().replace(R.id.inputFragment, InputFragment()).addToBackStack(null).commit()
//        }

//        if (intent.hasExtra("message")){
//            val txt = intent.getStringExtra("message")
////            renderableTxt.setText(txt)
//            Toast.makeText(this, "전달 받음: "+txt, Toast.LENGTH_SHORT).show()
//        }

//        supportFragmentManager.commit {
//            add(R.id.containerFragment, MainFragment::class.java, Bundle())
//        }

    }

}