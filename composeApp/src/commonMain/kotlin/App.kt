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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import app.cash.sqldelight.db.SqlDriver
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bookmark
import compose.icons.feathericons.Home
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Send
import compose.icons.feathericons.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import repository.TodoRepositoryProtocol

import udesc.eso.ddm.kotlin.TodoDatabase
import udesc.eso.ddm.kotlin.database.TodoEntity

@Composable
@ExperimentalMaterial3Api
@Preview
fun App(repository: TodoRepositoryProtocol) {
    MaterialTheme {
        val homeIcon = remember { FeatherIcons.Home }
        val userIcon = remember { FeatherIcons.User }
        val bookMarkIcon = remember { FeatherIcons.Bookmark }
        var isCreatingTodo = remember { mutableStateOf(false) }
        val todoListState = remember { mutableStateOf<List<TodoEntity>>(listOf()) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            repository.getAllTodos().collect { todos ->
                todoListState.value = todos
            }
        }
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
                        Icon(imageVector = userIcon, contentDescription = "user Icon")
                        Icon(imageVector = homeIcon, contentDescription = "home Icon")
                        Icon(imageVector = bookMarkIcon, contentDescription = "bookmark Icon")
                    }
                }
            },
            floatingActionButton = {
                if (!isCreatingTodo.value) {
                    TodoButton(action = { isCreatingTodo.value = !isCreatingTodo.value })
                }
            }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Column(
                    Modifier.padding(8.dp).fillMaxHeight()
                ) {
                    Column() {
                        Text("Tarefas para fazer", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        TodoListComponent(
                            todoListState.value.filter { it.isCompleted == 0L },
                            repository
                        )
                    }
                    Column() {
                        Text("Tarefas completas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        TodoListComponent(
                            todoListState.value.filter { it.isCompleted == 1L },
                            repository
                        )
                    }
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
                        TodoInput(isCreatingTodo, repository)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoListComponent(todoList: List<TodoEntity>, repository: TodoRepositoryProtocol) {
    LazyColumn(
        userScrollEnabled = true
    ) {
        items(todoList.size) {
            TodoComponent(todoList[it], repository)
        }
    }
}

@Composable
fun TodoComponent(todo: TodoEntity, repository: TodoRepositoryProtocol) {
    val isChecked = remember(todo.isCompleted) { mutableStateOf(todo.isCompleted == 1L) }
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
                    onCheckedChange = {
                        isChecked.value = it
                        changeTodoStatus(repository, todo)
                    },
                    colors = customCheckBoxColors(),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = todo.title,
                    modifier = Modifier.padding(2.dp),
                    color = if (isChecked.value || todo.isCompleted == 1L) Color.Gray else Color.Black,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (isChecked.value || todo.isCompleted == 1L) TextDecoration.LineThrough else null
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
        unfocusedContainerColor = Color.White,
        focusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        cursorColor = Color.Black,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoInput(
    isActive: MutableState<Boolean>, repository: TodoRepositoryProtocol
) {
    val todoName = remember { mutableStateOf("") }
    val todoDescription = remember { mutableStateOf("") }
    val todoDueDate = remember { mutableStateOf("") }
    val dueDateState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { dueDateState.selectedDateMillis != null }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val sendIcon = remember { FeatherIcons.Send }
    val actionConfirm = {
        createTodo(
            repository,
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
            modifier = Modifier.background(Color.White).fillMaxWidth(),
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
            TextButton(
                onClick = { showDatePickerDialog = !showDatePickerDialog },
                modifier = Modifier.background(Color.Yellow).width(200.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(6.dp)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Definir prazo da tarefa", color = Color.Black)
            }
            Button(
                onClick = actionConfirm,
                modifier = Modifier.alignByBaseline(),

                ) {
                Text("CRIAR  ", color = Color.Black)
                Icon(
                    imageVector = sendIcon,
                    contentDescription = "send",
                    modifier = Modifier.size(14.dp)
                )
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

fun changeTodoStatus(
    repository: TodoRepositoryProtocol,
    todo: TodoEntity
) {
    if (todo.isCompleted == 0L) {
        repository.completeTodo(todo.id)
    } else {
        repository.unCompleteTodo(todo.id)
    }
}

fun createTodo(
    repository: TodoRepositoryProtocol, title: String, description: String, dueDate: String
) {
    repository.registerTodo(title, description, dueDate)
}

