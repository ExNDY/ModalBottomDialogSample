package app.thirtyninth.modalbottomsheetsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import app.thirtyninth.modalbottomsheetsample.ui.theme.ModalBottomSheetSampleTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ModalBottomSheetSampleTheme {
                val systemUiController = rememberSystemUiController()

                DisposableEffect(systemUiController) {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = true,
                        isNavigationBarContrastEnforced = true
                    )
                    onDispose { Unit }
                }

                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.imePadding(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { localPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = localPadding)
                            .consumeWindowInsets(paddingValues = localPadding)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal,
                                ),
                            )
                            .systemBarsPadding(),
                    ) {
                        Greeting("Android")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        var isShowBottomSheet by rememberSaveable {
            mutableStateOf(false)
        }
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(key1 = isShowBottomSheet) {
            coroutineScope.launch {
                if (isShowBottomSheet) sheetState.expand() else sheetState.hide()
            }
        }

        if (isShowBottomSheet) {
            ModalDialog(
                sheetState = sheetState,
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.hide()
                        isShowBottomSheet = false
                    }
                },
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Box(modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.Red)
                    .clickable {
                        isShowBottomSheet = true
                    }
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        text = "CLICK ME",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDialog(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Box(modifier = Modifier.animateContentSize()) {
            SheetContent()
        }
    }
}

@Composable
fun SheetContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .imePadding(),
    ) {
        var field by rememberSaveable {
            mutableStateOf("")
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = field,
                    onValueChange = {
                        field = it
                    })
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn {
                    getList().forEach { item ->
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .background(Color.Red.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = item
                                )
                            }
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Same button on bottom")
            }
        }

    }
}

fun getList(): List<String> {
    return List(50) {
        "$it"
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ModalBottomSheetSampleTheme {
        Greeting("Android")
    }
}