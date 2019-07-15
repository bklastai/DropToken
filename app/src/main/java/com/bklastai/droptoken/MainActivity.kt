package com.bklastai.droptoken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

const val USER_STARTS = "com.bklastai.droptoken.USER_STARTS"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_game_toolbar))

        activity_main_button_user_will_start.setOnClickListener {
            val gameIntent = Intent(this, GameActivity::class.java).apply {
                putExtra(USER_STARTS, true)
            }
            startActivity(gameIntent)
        }

        activity_main_button_computer_will_start.setOnClickListener {
            val gameIntent = Intent(this, GameActivity::class.java).apply {
                putExtra(USER_STARTS, false)
            }
            startActivity(gameIntent)
        }
    }
}
