import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.cash.sqldelight.db.SqlDriver
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Send
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import udesc.eso.ddm.kotlin.TodoDatabase
import udesc.eso.ddm.kotlin.database.TodoEntity

@Composable
@ExperimentalMaterial3Api
@Preview
//TODO: Fix duplicated todos
fun App(driver: SqlDriver) {
    MaterialTheme {
        val homeIcon = remember { FeatherIcons.Home }
        var isCreatingTodo = remember { mutableStateOf(false) }
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
                if(!isCreatingTodo.value) {
                    TodoButton(action = { isCreatingTodo.value = !isCreatingTodo.value })
                }
            }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Column(
                ) {
                    val updatedList = handleTodoListing(driver)
                    TodoListComponent(updatedList)
                }
                AnimatedVisibility(
                    visible = isCreatingTodo.value,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
                            .fillMaxHeight().zIndex(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        createTodoInput(isCreatingTodo, driver)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoListComponent(todoList: List<TodoEntity>) {
    LazyColumn(
        userScrollEnabled = true
    ) {
        items(todoList.size) {
            TodoComponent(todoList[it])
        }
    }
}

@Composable
fun TodoComponent(todo: TodoEntity) {
    val isChecked = remember { mutableStateOf(false) }
    LazyRow(
        modifier = Modifier.padding(16.dp).background(Color.Transparent),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = !isChecked.value },
                    colors = customCheckBoxColors(),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = todo.title,
                    modifier = Modifier.padding(2.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }
}

@Composable
fun TodoButton(action: () -> Unit) {
    val plusIcon = remember { FeatherIcons.Plus }
    FloatingActionButton(
        containerColor = Color.Yellow,
        onClick = action,
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
fun customCheckBoxColors(): CheckboxColors {
    return CheckboxDefaults.colors(
        checkedColor = Color.Yellow,
        uncheckedColor = Color.LightGray,
        checkmarkColor = Color.Black,
    )
}
@Composable
fun customTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
       unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Black,
        focusedTextColor = Color.White
    )
}

fun handleTodoCreation(
    driver: SqlDriver,
    todoName: String,
    todoDescription: String,
    todoDueDate: String
) {
    val db = TodoDatabase(driver)
    try {
        db.todoDatabaseQueries.createTodo(
            title = todoName,
            description = todoDescription,
            created_at = LocalDate.toString(),
            dueDate = todoDueDate,
            isCompleted = 0
        )
    } catch (e: Exception) {
        println("Error inserting todo: ${e.message}")
    }
}

fun handleTodoListing(driver: SqlDriver): List<TodoEntity> {
    return try {
        val db = TodoDatabase(driver);
        db.todoDatabaseQueries.selectAllTodos().executeAsList()
    } catch (e: Exception) {
        println("Error on getting todos: ${e.message}")
        emptyList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createTodoInput(
    isActive: MutableState<Boolean>, driver: SqlDriver
) {
    val todoName = remember { mutableStateOf("") }
    val todoDescription = remember { mutableStateOf("") }
    val todoDueDate = remember { mutableStateOf("") }
    val dueDateState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { dueDateState.selectedDateMillis != null }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val sendIcon = remember { FeatherIcons.Send }
    val actionConfirm = {
        handleTodoCreation(
            driver,
            todoName.value,
            todoDescription.value,
            todoDueDate.value
        )
        isActive.value = false
    }
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = todoName.value,
            onValueChange = { newName -> todoName.value = newName },
            modifier = Modifier.background(Color.Transparent).fillMaxWidth(),
            colors = customTextFieldColors()
        )
        TextField(
            value = todoDescription.value,
            onValueChange = { newDescription -> todoDescription.value = newDescription },
            modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = { showDatePickerDialog = !showDatePickerDialog }, modifier = Modifier.background(Color.Yellow).width(200.dp).border(2.dp, Color.Black, RoundedCornerShape(6.dp)), shape = RoundedCornerShape(12.dp)) {
                Text("Definir prazo da tarefa", color = Color.Black)
            }
            Button(
                onClick = actionConfirm,
                modifier = Modifier.alignByBaseline(),

            ) {
                Text("CRIAR  ", color = Color.Black)
                Icon(imageVector = sendIcon, contentDescription = "send", modifier = Modifier.size(14.dp))
            }
        }

        if (showDatePickerDialog) {
            DatePickerDialog(
                onDismissRequest = { isActive.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePickerDialog = !showDatePickerDialog
                            var date = "No selection"
                            if (dueDateState.selectedDateMillis != null) {
                                date = dueDateState.selectedDateMillis.toString()
                            }
                            todoDueDate.value = date;
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("Confirmar")
                    }
                }
            ) {
                DatePicker(state = dueDateState)
            }
        }

    }
}