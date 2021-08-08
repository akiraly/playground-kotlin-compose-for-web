import androidx.compose.runtime.*
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
}

@Composable
fun TicTacToe(state: TicTacToeState, onChange: (TicTacToeState) -> Unit) {
    println(state)
    Table {
        state.state.forEachIndexed { rowIndex, row ->
            Tr {
                row.forEachIndexed { colIndex, value ->
                    Td(attrs = {
                        onClick { event ->
                            state.click(rowIndex, colIndex)
                            onChange(state)
                        }
                    }) {
                        if (value < 0) Text("X")
                        else if (value > 0) Text("O")
                    }
                }
            }
        }
    }
}
