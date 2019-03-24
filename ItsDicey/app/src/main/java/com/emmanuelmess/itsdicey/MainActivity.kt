package com.emmanuelmess.itsdicey

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.VISIBLE

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        diceView.onResultObtained = {
            touchToRestartTextView.visibility = VISIBLE
        }

        diceView.onUserRestarted = {
            touchToRestartTextView.visibility = GONE
        }
    }
}
