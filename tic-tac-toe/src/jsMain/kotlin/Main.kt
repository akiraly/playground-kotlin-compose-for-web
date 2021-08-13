import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.Color.black
import org.jetbrains.compose.web.css.Color.lightgray
import org.jetbrains.compose.web.css.LineStyle.Companion.Solid
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

const val defaultCount = 3

fun main() {

    renderComposable(rootElementId = "root") {
        var count: Int by remember { mutableStateOf(defaultCount) }
        var game: TicTacToeState? by remember { mutableStateOf(null, policy = neverEqualPolicy()) }

        Div {
            NumberInput(value = count, min = 1, max = 5) {
                onChange {
                    count = it.value?.toInt() ?: defaultCount
                }
            }
            Button(attrs = {
                onClick {
                    game = TicTacToeState(count)
                }
            }) {
                Text("New Game")
            }
            game?.let { TicTacToe(it) { newState -> game = newState } }
        }
    }
}

data class TicTacToeState(val state: List<MutableList<Int>>) {
    constructor(count: Int) : this(List(count) { MutableList(count) { 0 } })

    private var next = 1

    fun click(rowIndex: Int, colIndex: Int) {
        state[rowIndex][colIndex] = next
        next *= -1
    }

    val finished: Boolean
        get() = winner != null

    val winner: Int?
        get() {
            val count = state.size
            return ((0 until count).asSequence().map { c -> state.asSequence().map { it[c] } }
                    + (0 until count).asSequence().map { c -> state[c].asSequence() } +
                    generateSequence {
                        state.asSequence().mapIndexed { index, list -> list[index] }
                    }.take(1) +
                    generateSequence {
                        state.asSequence().mapIndexed { index, list -> list[count - 1 - index] }
                    }.take(1))
                .map { it.toSet() }
                .find { it.size == 1 && !it.contains(0) }
                ?.first()
        }
}

@Composable
fun TicTacToe(state: TicTacToeState, onChange: (TicTacToeState) -> Unit) {
    val winner = state.winner

    Table({
        style {
            border(1.px, Solid, black)
        }
    }
    ) {
        state.state.forEachIndexed { rowIndex, row ->
            Tr {
                row.forEachIndexed { colIndex, value ->
                    Td({
                        style {
                            width(100.px)
                            height(100.px)
                            padding(0.px)
                            margin(0.px)
                            //border(1.px, Solid, black)
                            textAlign("center")
                            attr("vertical-align", "center")
                            if (rowIndex % 2 != colIndex % 2) backgroundColor(lightgray)
                        }
                        if (winner == null && value == 0) {
                            onClick {
                                state.click(rowIndex, colIndex)
                                onChange(state)
                            }
                        }
                    }) {
                        when {
                            value < 0 -> Text("X")
                            value > 0 -> Text("O")
                        }
                    }
                }
            }
        }
    }

    if (winner != null)
        Div({ style { fontWeight("bold") } }) {
            Text("${if (winner < 0) "X" else "O"} won!")
        }
}
