package dev.louisbrunet.tictactoe.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import dev.louisbrunet.tictactoe.model.Cell
import dev.louisbrunet.tictactoe.model.Game
import dev.louisbrunet.tictactoe.model.Player


/** GAME SCREEN */
@Composable
fun GameScreen() {
    var game by rememberSaveable { mutableStateOf(Game()) }
    val currentPlayer by game.currentPlayer
    val rows = game.rows
    val onCellClick: (Int, Int) -> Unit = game::play

    val text =
        if (game.isOver.value) "Game over !${if (currentPlayer != null) " $currentPlayer wins." else " It's a tie."}"
        else "It is $currentPlayer's turn to play"


    val grid = @Composable { GameGrid(rowStateLists = rows, onCellClick = onCellClick)}
    val statusText = @Composable {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.h5
        )
    }
    val buttonRow = @Composable {
        Row (
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { game = Game() /*TODO*/ },
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "Reset this game",
                    style = MaterialTheme.typography.button,
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            ) { /*TODO*/ }
        }
    }

    // don't need to observe orientation change because
    // activity is recreated on rotation ?
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            GameScreenLayoutLandscape(
                grid = grid,
                statusText = statusText,
                buttonRow = buttonRow,
            )
        }
        else -> {
            GameScreenLayoutPortrait(
                grid = grid,
                statusText = statusText,
                buttonRow = buttonRow,
            )
        }
    }
}

/**
 * Layout for portrait mode : one big column
 */
@Composable
fun GameScreenLayoutPortrait(
    grid: @Composable () -> Unit,
    statusText: @Composable () -> Unit,
    buttonRow: @Composable () -> Unit
) {
    Column {
        Column(modifier = Modifier.weight(1f)) {
            grid()
            statusText()
        }

        buttonRow()
    }
}

/**
 * Layout for landscape mode :
 * grid in one column, the rest in another column
 */
@Composable
fun GameScreenLayoutLandscape(
    grid: @Composable () -> Unit,
    statusText: @Composable () -> Unit,
    buttonRow: @Composable () -> Unit
) {
    Row {
        Column(modifier = Modifier.fillMaxHeight().aspectRatio(1f)) {
            grid()
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.weight(1f)) {
                statusText()
            }
            buttonRow()
        }
    }
}

@Composable
fun GameGrid(
    rowStateLists: List<SnapshotStateList<Cell>>,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        rowStateLists.forEachIndexed { rowIndex, rowStateList ->
            GameRow(rowStateList, rowIndex, onCellClick)
        }
    }
}

@Composable
fun GameRow(
    rowStateList: SnapshotStateList<Cell>,
    rowIndex: Int,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        rowStateList.forEachIndexed { columnIndex, cell ->
            GameCell(cell) {
                onCellClick(rowIndex, columnIndex)
            }
        }
    }
}

@Composable
fun RowScope.GameCell(
    cell: Cell,
    onCellClick: () -> Unit
) {
    val img: ImageVector? = when (cell.content) {
        Player.X -> Icons.Outlined.Close
        Player.O -> Icons.Outlined.Circle
        else -> null
    }

    Button(
        onClick = onCellClick,
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f),
//        shape = MaterialTheme.shapes.medium,
        border = ButtonDefaults.outlinedBorder,
        colors = ButtonDefaults.outlinedButtonColors()
    ) {
        if (img != null) {
            if (cell.isWinningCell) {
                Icon(
                    imageVector = img,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colors.secondary//WinnningCellIconTint
                )
            } else {
                Icon(
                    imageVector = img,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
