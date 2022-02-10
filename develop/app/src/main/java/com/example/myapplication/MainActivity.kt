package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import io.github.sceneview.utils.setFullScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFullScreen(
            fullScreen = true, hideSystemBars = false,
            fitsSystemWindows = false, rootView = findViewById(R.id.rootView)
        )

        // Fragment 관리
        supportFragmentManager.commit {
            add(R.id.containerFragment,  MainFragment::class.java, Bundle())
        }
    }

}