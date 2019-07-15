package com.bklastai.droptoken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import com.bklastai.droptoken.utils.*
import kotlinx.android.synthetic.main.activity_main.*

// intent extras
const val EXTRA_USER_STARTS = "com.bklastai.droptoken.EXTRA_USER_STARTS"

// shared prefs
const val PREF_EXIT_APP = "com.bklastai.droptoken.PREF_EXIT_APP"
const val PREF_PREVIOUS_MOVES = "com.bklastai.droptoken.PREF_PREVIOUS_MOVES"
const val PREF_USER_STARTED = "com.bklastai.droptoken.PREF_USER_STARTED"
const val APP_LEVEL_SHARED_PREFS = "com.bklastai.droptoken.APP_LEVEL_SHARED_PREFS"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        button_user_will_start.setOnClickListener {
            startGame(true, false)
        }

        button_computer_will_start.setOnClickListener {
            startGame(false, false)
        }
    }

    private fun startGame(userStarts: Boolean, playPreviousGame: Boolean) {
        val gameIntent = Intent(this, GameActivity::class.java)
        if (!playPreviousGame) {
            gameIntent.putExtra(EXTRA_USER_STARTS, userStarts)
            removePref(PREF_PREVIOUS_MOVES)
        } else {
            gameIntent.putExtra(EXTRA_USER_STARTS, getBooleanPref(PREF_USER_STARTED) == true)
        }
        startActivity(gameIntent)
    }

    override fun onResume() {
        super.onResume()
        maybeShowPreviousGameOption()
        maybeExitApp()
    }

    private fun maybeExitApp() {
        if (containsPref(PREF_EXIT_APP)) {
            removePref(PREF_EXIT_APP)
            finish()
        }
    }

    private fun maybeShowPreviousGameOption() {
        if (containsPref(PREF_PREVIOUS_MOVES)) {
            choose_old_game_prompt.visibility = VISIBLE
            activity_main_button_play_previous_game.visibility = VISIBLE
            activity_main_button_play_previous_game.setOnClickListener {
                startGame(false, true)
            }
        }
    }
}
