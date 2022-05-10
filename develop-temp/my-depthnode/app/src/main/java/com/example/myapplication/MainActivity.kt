package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.ux.ArFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var btn_map: Button
    lateinit var btn_memo: Button

    private lateinit var arFragment: ArFragment
    private val arSceneView get() = arFragment.arSceneView
    private val scene get() = arSceneView.scene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_memo = findViewById(R.id.memoButton)
        btn_memo.setOnClickListener{
            val intent = Intent(this, MemoActivity::class.java)
            startActivity(intent)
            //supportFragmentManager.beginTransaction().replace(R.id.containerFragment, MainFragment()).addToBackStack(null).commit()
        }

        btn_map = findViewById(R.id.mapButton)
        btn_map.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            //supportFragmentManager.beginTransaction().replace(R.id.containerFragment, MapFragment()).addToBackStack(null).commit()
        }
    }
}


