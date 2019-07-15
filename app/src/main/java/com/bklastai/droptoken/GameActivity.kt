package com.bklastai.droptoken

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.bklastai.droptoken.controllers.BoardViewController
import com.bklastai.droptoken.model.GameEngine
import com.bklastai.droptoken.utils.*
import org.json.JSONArray

const val gameUrl = "https://w0ayb2ph1k.execute-api.us-west-2.amazonaws.com/production?moves=%s"
enum class TokenState {
    Empty,
    User,
    Computer
}

class GameActivity : AppCompatActivity() {
    var awaitingOpponentMove = false

    private var userStarts = false
    private val gameEngine = GameEngine(Array(4) { Array(4) { TokenState.Empty } })
    private lateinit var boardController: BoardViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(findViewById(R.id.activity_game_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        boardController = BoardViewController(findViewById(R.id.game_board))

        userStarts = intent?.extras?.getBoolean(EXTRA_USER_STARTS) ?: false
    }

    override fun onResume() {
        super.onResume()
        repopulateViews()
    }

    private fun repopulateViews() {
        val movesString = getStringPref(PREF_PREVIOUS_MOVES)
        if (movesString == null) {
            if (!userStarts) getOpponentsMove()
        } else {
            var player = if (userStarts) TokenState.User else TokenState.Computer
            val moves = JSONArray(movesString)
            for (i in 0 until moves.length()) {
                val column = moves[i] as Int
                recordMoveReturnTrueIfEndOfGame(gameEngine.getRowOfNewMove(column), column, player)
                player = if (player == TokenState.User) TokenState.Computer else TokenState.User
            }
        }
    }

    fun onTokenClicked(tokenView: View) {
        if (awaitingOpponentMove) return

        when (tokenView.id) {
            R.id.row0_column0 -> { maybeRecordMove(0, 0) }
            R.id.row0_column1 -> { maybeRecordMove(0, 1) }
            R.id.row0_column2 -> { maybeRecordMove(0, 2) }
            R.id.row0_column3 -> { maybeRecordMove(0, 3) }

            R.id.row1_column0 -> { maybeRecordMove(1, 0) }
            R.id.row1_column1 -> { maybeRecordMove(1, 1) }
            R.id.row1_column2 -> { maybeRecordMove(1, 2) }
            R.id.row1_column3 -> { maybeRecordMove(1, 3) }

            R.id.row2_column0 -> { maybeRecordMove(2, 0) }
            R.id.row2_column1 -> { maybeRecordMove(2, 1) }
            R.id.row2_column2 -> { maybeRecordMove(2, 2) }
            R.id.row2_column3 -> { maybeRecordMove(2, 3) }

            R.id.row3_column0 -> { maybeRecordMove(3, 0) }
            R.id.row3_column1 -> { maybeRecordMove(3, 1) }
            R.id.row3_column2 -> { maybeRecordMove(3, 2) }
            R.id.row3_column3 -> { maybeRecordMove(3, 3) }
        }
    }

    private fun maybeRecordMove(row: Int, column: Int) {
        if (awaitingOpponentMove) return

        // in a game extension, we would replace isMoveValid(column) with isMoveValid(row, column)
        if (gameEngine.isMoveValid(column)) {

            // in a game extension, we would pass row instead of gameEngine.getRowOfNewMove(column)
            if (recordMoveReturnTrueIfEndOfGame(gameEngine.getRowOfNewMove(column), column, TokenState.User)) return

            getOpponentsMove()
        } else {
            DialogUtil.showInvalidMoveDialog(this)
        }
    }

    private fun recordMoveReturnTrueIfEndOfGame(row: Int, column: Int, player: TokenState): Boolean {
        gameEngine.record(row, column, player)
        boardController.record(row, column)
        if (gameEngine.isWinningMove(row, column, player)) {
            DialogUtil.showEndOfGameDialog(player, this)
            return true
        } else if (gameEngine.noMovesLeft()) {
            DialogUtil.showDrawDialog(this)
            return true
        }
        return false
    }

    private fun getOpponentsMove() {
        val url = gameUrl.format(gameEngine.getMovesJsonArray().toString())
        val request = JsonArrayRequest(GET, url, null, Response.Listener<JSONArray> {
            awaitingOpponentMove = false
            if (it.length() == 0 || it[0] !is Int) return@Listener

            val computerMoveColumn = it[it.length()-1] as Int
            val computerMoveRow = gameEngine.getRowOfNewMove(computerMoveColumn)

            recordMoveReturnTrueIfEndOfGame(computerMoveRow, computerMoveColumn, TokenState.Computer)
        }, Response.ErrorListener {
            it.printStackTrace()
        })
        awaitingOpponentMove = true
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    fun resetGame() {
        gameEngine.reset()
        boardController.reset()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing && gameEngine.getMovesJsonArray().length() > 0) {
            putStringPref(PREF_PREVIOUS_MOVES, gameEngine.getMovesJsonArray().toString())
            putBooleanPref(PREF_USER_STARTED, userStarts)
            resetGame()
        }
    }
}