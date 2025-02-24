package com.example.proenglishscoretracker.record_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme
import com.example.proenglishscoretracker.wheel_picker.CurrentIndex
import com.example.proenglishscoretracker.wheel_picker.FVerticalWheelPicker
import com.example.proenglishscoretracker.wheel_picker.FWheelPickerFocusVertical
import com.example.proenglishscoretracker.wheel_picker.rememberFWheelPickerState
import com.sd.lib.date.FDate
import com.sd.lib.date.FDateSelector
import com.sd.lib.date.fCurrentDate
import com.sd.lib.date.fDate
import com.sd.lib.date.selectDayOfMonthWithIndex
import com.sd.lib.date.selectMonthWithIndex
import com.sd.lib.date.selectYearWithIndex

@Composable
fun ToeicRecordScreen(viewModel: EnglishInfoViewModel) {
    val focusManager = LocalFocusManager.current
    // 以下の変数「interactionSource」は「rememberSaveable」だとクラッシュする。
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = interactionSource, indication = null) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16_dp))
        ) {
            var date by remember { mutableStateOf(fDate(2025, 1, 1)) }
            var showDatePicker by remember { mutableStateOf(false) }
            var readingScore by remember { mutableIntStateOf(0) }
            var listeningScore by remember { mutableIntStateOf(0) }
            var memoText by remember { mutableStateOf("") }

            SelectDayText("")

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                CalendarImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
                SelectDatePicker(
                    date = date,
                    onShowDatePickerChange = { showDatePicker = it }
                )
                if (showDatePicker) {
                    DatePicker(
                        date = date,
                        onDone = {
                            showDatePicker = false
                            if (it != null) {
                                date = it
                            }
                        },
                        onDismissRequest = {
                            showDatePicker = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row {
                EnterScoreText("")
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
                ReadingImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                ReadingText("", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                TOEICRLScorePicker(
                    modifier = Modifier.weight(1.2f),
                    readingScore,
                ) {
                    readingScore = it
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
                ListeningImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                ListeningText("", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                TOEICRLScorePicker(
                    modifier = Modifier.weight(1.2f),
                    listeningScore,
                ) {
                    listeningScore = it
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            MemoText("")

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                MemoInputField(
                    placeholder = stringResource(id = R.string.memo),
                    value = memoText,
                    onValueChange = {
                        memoText = it
                        viewModel.setEikenMemoText(it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_64_dp)))

            var showSaved by remember { mutableStateOf("") }
            var showAlertDialogOfZero by remember { mutableStateOf(false) }
            var result by remember { mutableStateOf("Result") }
            var alertMessage by remember { mutableStateOf<String?>(null) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showAlertDialogOfZero) {
                        AlertDialog(
                            onDismissRequest = {
                                result = "Dismiss"
                                showAlertDialogOfZero = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        result = "はい"
                                        showAlertDialogOfZero = false
                                        viewModel.saveToeicValues(
                                            date.toString(),
                                            readingScore,
                                            listeningScore,
                                            memoText,
                                            showAlert = { message ->
                                                alertMessage = message
                                                showSaved = ""
                                            }
                                        )
                                        if (alertMessage == null) {
                                            showSaved = "登録しました。"
                                        }
                                        readingScore = 0
                                        listeningScore = 0
                                        memoText = ""
                                    }
                                ) {
                                    Text("はい")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        result = "いいえ"
                                        showAlertDialogOfZero = false
                                    }
                                ) {
                                    Text("いいえ")
                                }
                            },
                            text = {
                                Text("ReadingスコアもしくはListeningスコアが0ですが登録しますか？")
                            },
                            contentColor = Color.Black,
                            backgroundColor = Color(0xFFd3d3d3)
                        )
                    }
                    SaveButton(
                        onClick = {
                            if (readingScore == 0 || listeningScore == 0) {
                                showAlertDialogOfZero = true
                            } else {
                                viewModel.saveToeicValues(
                                    date.toString(),
                                    readingScore,
                                    listeningScore,
                                    memoText,
                                    showAlert = { message ->
                                        alertMessage = message
                                        showSaved = ""
                                    }
                                )
                                if (alertMessage == null) {
                                    showSaved = "登録しました。"
                                }
                                readingScore = 0
                                listeningScore = 0
                                memoText = ""
                            }
                        }
                    )
                    if (alertMessage != null) {
                        AlertDialog(
                            onDismissRequest = {
                                alertMessage = null
                                showSaved = ""
                            },
                            title = { Text("エラー") },
                            text = { Text(alertMessage!!) },
                            confirmButton = {
                                Button(onClick = {
                                    alertMessage = null
                                    showSaved = ""
                                }) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                    if (alertMessage == null && showSaved.isNotEmpty()) {
                        ShowSavedText(saved = showSaved, onTimeout = { showSaved = "" })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ToeicRecordScreenPreview(
    @PreviewParameter(PreviewParameterProvider::class)
    viewModel: EnglishInfoViewModel
) {
    ProEnglishScoreTrackerTheme {
        ToeicRecordScreen(viewModel = viewModel)
    }
}

@Composable
private fun SelectDayText(day: String, modifier: Modifier = Modifier) {
    Text(
        text = "受験日を選択",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectDayTextPreview() {
    ProEnglishScoreTrackerTheme {
        SelectDayText("受験日を選択")
    }
}

@Composable
private fun CalendarImageView(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.calendar),
        contentDescription = "",
        modifier = modifier
            .size((dimensionResource(id = R.dimen.space_32_dp)))
            .aspectRatio(1f)
    )
}

@Composable
private fun SelectDatePicker(
    date: FDate,
    onShowDatePickerChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { onShowDatePickerChange(true) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFf5f5f5)),
        ) {
            Text(
                text = date.toString(),
                color = Color.Black
            )
        }
    }
}

@Composable
private fun DatePicker(
    modifier: Modifier = Modifier,
    date: FDate,
    onDone: (FDate?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val selector = remember {
        FDateSelector(
            startDate = fDate(2016, 1, 1),
            endDate = fCurrentDate(),
        )
    }
    val state by selector.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(selector, date) {
        selector.setDate(date)
    }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFffffff)
            ),
            modifier = Modifier
                .size(
                    width = 240.dp,
                    height = 280.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                DatePickerView(
                    listYear = state.listYear,
                    listMonth = state.listMonth,
                    listDayOfMonth = state.listDayOfMonth,
                    indexOfYear = state.indexOfYear,
                    indexOfMonth = state.indexOfMonth,
                    indexOfDayOfMonth = state.indexOfDayOfMonth,
                    onYearIndexChange = {
                        selector.selectYearWithIndex(it)
                    },
                    onMonthIndexChange = {
                        selector.selectMonthWithIndex(it)
                    },
                    onDayOfMonthIndexChange = {
                        selector.selectDayOfMonthWithIndex(it)
                    },
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = { onDone(selector.date) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF9C27B0)),
                ) {
                    Text(
                        text = "確定",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun DatePickerView(
    modifier: Modifier = Modifier,
    listYear: List<Int>,
    listMonth: List<Int>,
    listDayOfMonth: List<Int>,
    indexOfYear: Int,
    indexOfMonth: Int,
    indexOfDayOfMonth: Int,
    onYearIndexChange: suspend (Int) -> Unit,
    onMonthIndexChange: suspend (Int) -> Unit,
    onDayOfMonthIndexChange: suspend (Int) -> Unit,
) {
    if (indexOfYear < 0) return
    if (indexOfMonth < 0) return
    if (indexOfDayOfMonth < 0) return

    val yearState = rememberFWheelPickerState(indexOfYear)
    val monthState = rememberFWheelPickerState(indexOfMonth)
    val dayOfMonthState = rememberFWheelPickerState(indexOfDayOfMonth)

    yearState.CurrentIndex(onYearIndexChange)
    monthState.CurrentIndex(onMonthIndexChange)
    dayOfMonthState.CurrentIndex(onDayOfMonthIndexChange)

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        // Year
        FVerticalWheelPicker(
            modifier = Modifier.width(64.dp),
            state = yearState,
            count = listYear.size,
            focus = {
                FWheelPickerFocusVertical(
                    dividerColor = Color.LightGray,
                    dividerSize = 2.dp
                )
            },
        ) { index ->
            listYear.getOrNull(index)?.let { value ->
                Text(text = value.toString())
            }
        }

        // Month
        FVerticalWheelPicker(
            modifier = Modifier.width(64.dp),
            state = monthState,
            count = listMonth.size,
            focus = {
                FWheelPickerFocusVertical(
                    dividerColor = Color.LightGray,
                    dividerSize = 2.dp
                )
            },
        ) { index ->
            listMonth.getOrNull(index)?.let { value ->
                Text(text = value.toString())
            }
        }

        // Day of month
        FVerticalWheelPicker(
            modifier = Modifier.width(64.dp),
            state = dayOfMonthState,
            count = listDayOfMonth.size,
            focus = {
                FWheelPickerFocusVertical(
                    dividerColor = Color.LightGray,
                    dividerSize = 2.dp
                )
            },
        ) { index ->
            listDayOfMonth.getOrNull(index)?.let { value ->
                Text(text = value.toString())
            }
        }
    }
}

@Composable
private fun EnterScoreText(grade: String, modifier: Modifier = Modifier) {
    Text(
        text = "スコアを記入",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun EnterScoreTextPreview() {
    ProEnglishScoreTrackerTheme {
        EnterScoreText("スコアを記入")
    }
}

@Composable
private fun ReadingText(readingText: String, modifier: Modifier = Modifier) {
    Text(
        text = "Reading",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ReadingTextPreview() {
    ProEnglishScoreTrackerTheme {
        ReadingText("Reading")
    }
}

@Composable
private fun ReadingImageView(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.reading),
        contentDescription = "",
        modifier = modifier
            .size((dimensionResource(id = R.dimen.space_32_dp)))
            .aspectRatio(1f)
    )
}

@Preview(showBackground = true)
@Composable
private fun ReadingImageViewPreview() {
    ProEnglishScoreTrackerTheme {
        ReadingImageView(modifier = Modifier)
    }
}

@Composable
private fun TOEICRLScorePicker(
    modifier: Modifier = Modifier,
    toeicRLScore: Int,
    onScoreConfirm: (Int) -> Unit, // スコア更新用のコールバック
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var tempScore by rememberSaveable { mutableIntStateOf(toeicRLScore) } // 一時的なスコア保持用

    Box(modifier = modifier) {
        // スコア入力ボタン
        Button(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 80.dp, height = 40.dp),
            onClick = { showDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFf5f5f5)),
        ) {
            Text(
                text = "$toeicRLScore",
                color = Color.Black
            )
        }
    }

    // スコア入力ダイアログ
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFffffff)),
                modifier = Modifier
                    .size(width = 240.dp, height = 320.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    // スコア選択のWheel Picker
                    TOEICRLScorePickerView(
                        score = tempScore,
                        onScoreChange = { tempScore = it } // 一時的なスコアを更新
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 確定ボタン
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            onScoreConfirm(tempScore) // 確定時にスコアを親に渡す
                            showDialog = false
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF9C27B0)),
                    ) {
                        Text(
                            text = "確定",
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TOEICRLScorePickerView(
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    val hundred = score / 100 // 100の位
    val ten = (score % 100) / 10 // 10の位
    val one = score % 10 // 1の位

    // 状態管理のためにrememberを使う
    val hundredState = remember { mutableIntStateOf(hundred) }
    val tenState = remember { mutableIntStateOf(ten) }
    val oneState = remember { mutableIntStateOf(one) }

    // スコア変更をトリガーする
    LaunchedEffect(
        hundredState.intValue,
        tenState.intValue,
        oneState.intValue
    ) {
        onScoreChange(
            hundredState.intValue * 100 +
                    tenState.intValue * 10 +
                    oneState.intValue
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 100の位
        ThreeDigits(hundredState)
        // 10の位
        TwoDigits(tenState)
        // 1の位
        OneDigit(oneState)
    }
}

@Composable
private fun ThreeDigits(state: MutableIntState) {
    // FWheelPickerStateを利用してスクロール状態を管理
    val listState = rememberFWheelPickerState()

    // currentIndex の変化を監視
    LaunchedEffect(listState.currentIndex) {
        // listState.currentIndex が変わったときに state.intValue を更新
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(64.dp),
        count = 5,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(
                dividerColor = Color.LightGray,
                dividerSize = 2.dp
            )
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun TwoDigits(state: MutableIntState) {
    // FWheelPickerStateを利用してスクロール状態を管理
    val listState = rememberFWheelPickerState()

    // currentIndex の変化を監視
    LaunchedEffect(listState.currentIndex) {
        // listState.currentIndex が変わったときに state.intValue を更新
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(64.dp),
        count = 10,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(
                dividerColor = Color.LightGray,
                dividerSize = 2.dp
            )
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

// 三桁目の数字は0か5のみしか入力不可
@Composable
private fun OneDigit(state: MutableIntState) {
    val items = listOf(0, 5)

    // FWheelPickerStateを利用してスクロール状態を管理
    val listState = rememberFWheelPickerState()
    // currentIndex の変化を監視
    LaunchedEffect(listState.currentIndex) {
        // currentIndex が items のサイズを超えないことを確認
        val currentItemIndex = listState.currentIndex.coerceIn(0, items.size - 1)
        // listState.currentIndex が変わったときに state.intValue を更新
        state.intValue = items[currentItemIndex]
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(64.dp),
        count = items.size,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(
                dividerColor = Color.LightGray,
                dividerSize = 2.dp
            )
        },
    ) { index ->
        Text(
            items[index].toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun ListeningText(listeningText: String, modifier: Modifier = Modifier) {
    Text(
        text = "Listening",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ListeningTextPreview() {
    ProEnglishScoreTrackerTheme {
        ListeningText("Listening")
    }
}

@Composable
private fun ListeningImageView(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.listening),
        contentDescription = "",
        modifier = modifier
            .size((dimensionResource(id = R.dimen.space_32_dp)))
            .aspectRatio(1f)
    )
}

@Preview(showBackground = true)
@Composable
private fun ListeningImageViewPreview() {
    ProEnglishScoreTrackerTheme {
        ListeningImageView(modifier = Modifier)
    }
}

@Composable
private fun MemoText(memoText: String, modifier: Modifier = Modifier) {
    Text(
        text = "Memo",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun MemoTextPreview() {
    ProEnglishScoreTrackerTheme {
        MemoText("Memo")
    }
}

@Composable
private fun MemoTextField(modifier: Modifier) {
    var text by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.space_52_dp)),
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            label = { Text("MEMO") },
            placeholder = { Text("") },
            maxLines = 5,
            singleLine = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoTextFieldPreview() {
    ProEnglishScoreTrackerTheme {
        MemoTextField(modifier = Modifier)
    }
}

@Composable
private fun MemoInputField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material.OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.memo_height_dp)),
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(fontSize = dimensionResource(id = R.dimen.space_16_sp).value.sp),
                    color = Color.Gray
                )
            },
            shape = RoundedCornerShape(10),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.space_24_dp)))
    }
}

@Composable
private fun ErrorText(error: String) {
    Text(
        text = error,
        fontSize = 12.sp,
        maxLines = 1,
        color = Color.Red
    )
}

@Composable
private fun SaveButton(
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color.Blue),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(stringResource(id = R.string.record), color = Color.White)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun SaveButtonPreview() {
//    ProEnglishScoreTrackerTheme {
//        SaveButton()
//    }
//}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
private fun ShowSavedText(saved: String, onTimeout: () -> Unit) {
    if (saved.isNotEmpty()) {
        Text(
            text = saved,
            fontSize = 16.sp,
            color = Color.Green
        )

        // メッセージを非表示にするためのタイマーを設定
        LaunchedEffect(saved) {
            kotlinx.coroutines.delay(2000) // 2秒間待機
            onTimeout()
        }
    }
}
