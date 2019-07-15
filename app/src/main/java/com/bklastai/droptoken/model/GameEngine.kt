package com.bklastai.droptoken.model

import android.util.Log
import com.bklastai.droptoken.TokenState
import org.json.JSONArray
import java.lang.IllegalStateException
import kotlin.math.abs

class GameEngine(private var gameState: Array<Array<TokenState>>) {
    companion object {
        var moves = JSONArray()
    }

    fun noMovesLeft(): Boolean {
        return moves.length() == 16
    }

    fun isWinningMove(row: Int, column: Int, player: TokenState): Boolean {
        return filledColumn(column, player) || filledRow(row, player) || filledDiagonal(row, column, player)
    }

    private fun filledColumn(newMoveColumn: Int, player: TokenState): Boolean {
        for (i in 0..3) {
            if (peek(i, newMoveColumn) != player) return false
        }
        Log.i("TESTIN", "filledColumn")
        return true
    }

    private fun filledRow(row: Int, player: TokenState): Boolean {
        for (i in 0..3) {
            if (peek(row, i) != player) return false
        }
        Log.i("TESTIN", "filledRow, row = $row")
        return true
    }

    private fun filledDiagonal(row: Int, column: Int, player: TokenState): Boolean {
        return filledDiagonalTopDown(row, column, player) || filledDiagonalBottomUp(row, column, player)
    }

    private fun filledDiagonalTopDown(row: Int, column: Int, player: TokenState): Boolean {
        if (row != column) return false
        for (i in 0..3) {
            val nextColumn = (column + i) % 4
            val nextRowUp = (row + i) % 4
            if (peek(nextRowUp, nextColumn) != player) return false
        }
        Log.i("TESTIN", "filledDiagonalTopDown")
        return true
    }

    private fun filledDiagonalBottomUp(row: Int, column: Int, player: TokenState): Boolean {
        if (abs(column - 3) != row) return false
        var nextRowDown = row + 1
        for (i in 0..3) {
            val nextColumn = (column + i) % 4
            nextRowDown--
            if (nextRowDown < 0) nextRowDown = 3
            if (peek(nextRowDown, nextColumn) != player) return false
        }
        Log.i("TESTIN", "filledDiagonalBottomUp")
        return true
    }

    fun getRowOfNewMove(newMoveColumn: Int): Int {
        for (i in 3 downTo 0) {
            if (peek(i, newMoveColumn) == TokenState.Empty) return i
        }
        return -1
    }

    fun isMoveValid(newMoveColumn: Int): Boolean {
        for (i in 3 downTo 0) {
            if (peek(i, newMoveColumn) == TokenState.Empty) return true
        }
        return false
    }

    // for game extension pack (if game allowed picking any slot, not just bottom of the column)
    fun isMoveValid(newMoveRow: Int, newMoveColumn: Int): Boolean {
        if (newMoveRow !in 0..3 || newMoveColumn !in 0..3) {
            return false
        }
        return peek(newMoveRow, newMoveColumn) == TokenState.Empty
    }

    fun record(row: Int, column: Int, state: TokenState) {
        if (row !in 0..3 || column !in 0..3) {
            throw IllegalStateException("Illegal move, row = $row, column = $column")
        }
        moves.put(column)
        gameState[row][column] = state
    }

    private fun peek(row: Int, column: Int): TokenState {
        return gameState[row][column]
    }

    fun reset() {
        moves = JSONArray()
        gameState = Array(4) {Array(4) { TokenState.Empty } }
    }

    fun getMovesJsonArray(): JSONArray {
        return moves
    }
}