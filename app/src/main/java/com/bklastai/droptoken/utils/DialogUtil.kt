package com.bklastai.droptoken.utils

import androidx.appcompat.app.AlertDialog
import com.bklastai.droptoken.GameActivity
import com.bklastai.droptoken.PREF_EXIT_APP
import com.bklastai.droptoken.R
import com.bklastai.droptoken.TokenState

class DialogUtil {
    companion object {
        @JvmStatic fun showEndOfGameDialog(winner: TokenState, activity: GameActivity) {
            AlertDialog.Builder(activity)
                .setTitle(if (winner == TokenState.User) R.string.end_of_game_dialog_you_won else
                    R.string.end_of_game_dialog_you_lost)
                .setMessage(R.string.end_of_game_dialog_message)
                .setPositiveButton(R.string.play_again) { _, _ ->
                    activity.resetGame()
                }
                .setNegativeButton(R.string.end_game) { _, _ ->
                    activity.resetGame()
                    activity.finish()
                }
                .setNeutralButton(R.string.exit_app) { _, _ ->
                    activity.resetGame()
                    activity.putBooleanPref(PREF_EXIT_APP, true)
                    activity.finish()
                }
                .setOnCancelListener { activity.resetGame() }
                .show()
        }

        @JvmStatic  fun showInvalidMoveDialog(activity: GameActivity) {
            AlertDialog.Builder(activity)
                .setTitle(R.string.invalid_move_dialog_title)
                .setMessage(R.string.invalid_move_dialog_message)
                .setPositiveButton(R.string.invalid_move_dialog_ok, null)
                .show()
        }

        @JvmStatic  fun showDrawDialog(activity: GameActivity) {
            AlertDialog.Builder(activity)
                .setTitle(R.string.draw_dialog_title)
                .setMessage(R.string.end_of_game_dialog_message)
                .setPositiveButton(R.string.play_again) { _, _ ->
                    activity.resetGame()
                }
                .setNegativeButton(R.string.end_game) { _, _ ->
                    activity.resetGame()
                    activity.finish()
                }
                .setNeutralButton(R.string.exit_app) { _, _ ->
                    activity.resetGame()
                    activity.putBooleanPref(PREF_EXIT_APP, true)
                    activity.finish()
                }
                .setOnCancelListener { activity.resetGame() }
                .show()
        }
    }
}