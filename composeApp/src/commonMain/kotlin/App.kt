import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import model.TodoDataClass
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import mytodolistapp2.composeapp.generated.resources.Res
import mytodolistapp2.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {

    MaterialTheme {
        val todo1 = TodoDataClass(
            id = 1,
            title = "Create zist",
            description = "Criar",
            dueDate = LocalDate.toString(),
            created_at = LocalDate.toString(),
            updated_at = null
        )
        val todoList: MutableList<TodoDataClass> = mutableListOf(todo1)
        var todoListState: MutableList<TodoDataClass> =
            remember { mutableStateListOf<TodoDataClass>(todo1) }
        Column {
            TodoListComponent(todoListState)
        }
    }
}

@Composable
fun TodoListComponent(todoList: MutableList<TodoDataClass>) {
    LazyColumn(

    ) {
        items(todoList.size) {
            TodoComponent(todoList)
        }
    }
}

@Composable
fun TodoComponent(todoList: MutableList<TodoDataClass>) {
    var isChecked = remember { mutableStateOf(false) }
    todoList.forEach { it ->
        LazyRow(
            modifier = Modifier.padding(16.dp).background(Color.Transparent),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = !isChecked.value })
                Text(text = it.title, modifier = Modifier.padding(2.dp), color = Color.Black)
            }
        }
    }
}

