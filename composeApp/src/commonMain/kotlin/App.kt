import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import compose.icons.feathericons.Plus
import kotlinx.datetime.LocalDate
import model.TodoDataClass
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import mytodolistapp2.composeapp.generated.resources.Res
import mytodolistapp2.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
@Composable
@ExperimentalMaterial3Api
@Preview
fun App() {

    MaterialTheme {
        val homeIcon = remember { FeatherIcons.Home }

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
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Minhas Tarefas", color = Color.Black, fontWeight = FontWeight.Bold)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TopAppBarColors(
                        containerColor = Color.Yellow,
                        scrolledContainerColor = Color.Black,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.Black,
                        actionIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                BottomAppBar(containerColor = Color.Transparent) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize().padding(12.dp).background(
                            Color.Yellow,
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp,
                                bottomEnd = 12.dp,
                                bottomStart = 12.dp
                            )
                        )
                    ) {
                        Icon(imageVector = homeIcon, contentDescription = "")
                        Icon(imageVector = homeIcon, contentDescription = "")
                        Icon(imageVector = homeIcon, contentDescription = "")
                    }
                }
            },
            floatingActionButton = {
                CreateTodoButton()
            }) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TodoListComponent(todoListState)
            }
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                    Checkbox(
                        checked = isChecked.value,
                        onCheckedChange = { isChecked.value = !isChecked.value },
                        colors = customCheckBoxColors(),
                        modifier = Modifier.size(32.dp)
                    )
                    Text(text = it.title, modifier = Modifier.padding(2.dp), color = Color.Black, fontWeight = FontWeight.Medium)
                }

            }
        }
    }
}

@Composable
fun CreateTodoButton() {
    val plusIcon = remember { FeatherIcons.Plus }
    FloatingActionButton(
        containerColor = Color.Yellow,
        onClick = {},
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = plusIcon,
            modifier = Modifier.size(32.dp).padding(4.dp),
            contentDescription = "",
            tint = Color.Black
        )
    }
}

@Composable
fun customCheckBoxColors(): androidx.compose.material3.CheckboxColors {
    return CheckboxDefaults.colors(
        checkedColor = Color.Yellow,
        uncheckedColor = Color.LightGray,
        checkmarkColor = Color.Black,
    )
}
