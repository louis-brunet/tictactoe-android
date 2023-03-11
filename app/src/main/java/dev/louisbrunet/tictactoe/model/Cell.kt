package dev.louisbrunet.tictactoe.model

//data class Cell(val row: Int, val column: Int, val content: Player? = null) {
data class Cell(
    val content: Player? = null,
    val isWinningCell: Boolean = false,
) {
    fun isEmpty(): Boolean {
        return this.content == null
    }
}