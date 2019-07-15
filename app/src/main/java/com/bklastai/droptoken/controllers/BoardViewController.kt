package com.bklastai.droptoken.controllers

import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.DrawableRes
import androidx.core.view.children
import com.bklastai.droptoken.R
import kotlinx.android.synthetic.main.game_board.view.*

class BoardViewController(private val board: ViewGroup) {
    companion object {
        private var movesCount = 0
    }

    fun record(row: Int, column: Int) {
        movesCount++
        when {
            row == 0 && column == 0 -> { board.row0_column0.setBackgroundResource(getImageRes()) }
            row == 0 && column == 1 -> { board.row0_column1.setBackgroundResource(getImageRes()) }
            row == 0 && column == 2 -> { board.row0_column2.setBackgroundResource(getImageRes()) }
            row == 0 && column == 3 -> { board.row0_column3.setBackgroundResource(getImageRes()) }

            row == 1 && column == 0 -> { board.row1_column0.setBackgroundResource(getImageRes()) }
            row == 1 && column == 1 -> { board.row1_column1.setBackgroundResource(getImageRes()) }
            row == 1 && column == 2 -> { board.row1_column2.setBackgroundResource(getImageRes()) }
            row == 1 && column == 3 -> { board.row1_column3.setBackgroundResource(getImageRes()) }

            row == 2 && column == 0 -> { board.row2_column0.setBackgroundResource(getImageRes()) }
            row == 2 && column == 1 -> { board.row2_column1.setBackgroundResource(getImageRes()) }
            row == 2 && column == 2 -> { board.row2_column2.setBackgroundResource(getImageRes()) }
            row == 2 && column == 3 -> { board.row2_column3.setBackgroundResource(getImageRes()) }

            row == 3 && column == 0 -> { board.row3_column0.setBackgroundResource(getImageRes()) }
            row == 3 && column == 1 -> { board.row3_column1.setBackgroundResource(getImageRes()) }
            row == 3 && column == 2 -> { board.row3_column2.setBackgroundResource(getImageRes()) }
            row == 3 && column == 3 -> { board.row3_column3.setBackgroundResource(getImageRes()) }
        }
    }

    @DrawableRes private fun getImageRes(): Int {
        return if (movesCount % 2 == 0) R.drawable.token_team2 else R.drawable.token_team1
    }

    fun reset() {
        movesCount = 0
        for (i in board.children) {
            (i as ImageButton).setBackgroundResource(R.drawable.token_empty)
        }
    }
}