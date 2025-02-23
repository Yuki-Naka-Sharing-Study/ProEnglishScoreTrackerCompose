package com.example.proenglishscoretracker.record_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
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
fun ToeicSwRecordScreen(viewModel: EnglishInfoViewModel) {
    val focusManager = LocalFocusManager.current
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
            var writingScore by remember { mutableIntStateOf(0) }
            var speakingScore by remember { mutableIntStateOf(0) }
            var memoText by remember { mutableStateOf("") }

            //「Error」系
            var writingMaxScoreError = writingScore >= 201
            var speakingMaxScoreError = speakingScore >= 201

            Row {
                SelectDayText("")
                Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.space_24_dp)))
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
                WritingImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                WritingText("", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                TOEICSWScorePicker(
                    modifier = Modifier.weight(1.2f),
                    writingScore
                ) {
                    writingScore = it
                    viewModel.setWritingScore(it)
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
                if (writingScore >= 201) {
                    ErrorText("Writingスコアは201未満である必要があります。")
                    writingMaxScoreError = true
                } else {
                    writingMaxScoreError = false
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
                SpeakingImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                SpeakingText("", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                TOEICSWScorePicker(
                    modifier = Modifier.weight(1.2f),
                    speakingScore
                ) {
                    speakingScore = it
                    viewModel.setSpeakingScore(it)
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
                if (speakingScore >= 201) {
                    ErrorText("Speakingスコアは201未満である必要があります。")
                    speakingMaxScoreError = true
                } else {
                    speakingMaxScoreError = false
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
                MemoText("")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
                MemoInputField(
                    placeholder = stringResource(id = R.string.memo),
                    value = memoText,
                    onValueChange = {
                        memoText = it
                        viewModel.setMemoText(it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_64_dp)))

            val savableChecker = !writingMaxScoreError &&
                    !speakingMaxScoreError
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
                                        viewModel.saveToeicSwValues(
                                            date.toString(),
                                            writingScore,
                                            speakingScore,
                                            memoText,
                                            showAlert = { message ->
                                                alertMessage = message
                                                showSaved = ""
                                            }
                                        )
                                        if (alertMessage == null) {
                                            showSaved = "登録しました。"
                                        }
                                        viewModel.setWritingScore(0)
                                        viewModel.setSpeakingScore(0)
                                        viewModel.setMemoText("")
                                        writingScore = 0
                                        speakingScore = 0
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
                                Text("WritingスコアもしくはSpeakingスコアが0ですが登録しますか？")
                            },
                            contentColor = Color.Black,
                            backgroundColor = Color(0xFFd3d3d3)
                        )
                    }
                    SaveButton(
                        onClick = {
                            if (savableChecker) {
                                if (writingScore == 0 || speakingScore == 0) {
                                    showAlertDialogOfZero = true
                                } else {
                                    viewModel.saveToeicSwValues(
                                        date.toString(),
                                        writingScore,
                                        speakingScore,
                                        memoText,
                                        showAlert = { message ->
                                            alertMessage = message
                                            showSaved = ""
                                        }
                                    )
                                    if (alertMessage == null) {
                                        showSaved = "登録しました。"
                                    }
                                    viewModel.setWritingScore(0)
                                    viewModel.setSpeakingScore(0)
                                    viewModel.setMemoText("")
                                    writingScore = 0
                                    speakingScore = 0
                                    memoText = ""
                                }
                            }
                        },
                        isEnabled = savableChecker
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
private fun ToeicSwScreenPreview(
    @PreviewParameter(PreviewParameterProvider::class)
    viewModel: EnglishInfoViewModel
) {
    ProEnglishScoreTrackerTheme {
        ToeicSwRecordScreen(viewModel = viewModel)
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
private fun SelectDatePicker(
    date: FDate,
    onShowDatePickerChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .navigationBarsPadding()
    ) {
        // 年月日を表示するボタン
        Button(
            modifier = Modifier.align(Alignment.TopCenter),
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
                verticalArrangement = Arrangement.Center
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
private fun WritingText(writingText: String, modifier: Modifier = Modifier) {
    Text(
        text = "Writing",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun WritingTextPreview() {
    ProEnglishScoreTrackerTheme {
        WritingText("Writing")
    }
}

@Composable
private fun WritingImageView(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.writing),
        contentDescription = "",
        modifier = modifier
            .size((dimensionResource(id = R.dimen.space_32_dp)))
            .aspectRatio(1f)
    )
}

@Preview(showBackground = true)
@Composable
private fun WritingImageViewPreview() {
    ProEnglishScoreTrackerTheme {
        WritingImageView(modifier = Modifier)
    }
}

@Composable
private fun SpeakingText(speakingText: String, modifier: Modifier = Modifier) {
    Text(
        text = "Speaking",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun SpeakingTextPreview() {
    ProEnglishScoreTrackerTheme {
        SpeakingText("Speaking")
    }
}

@Composable
private fun SpeakingImageView(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.speaking),
        contentDescription = "",
        modifier = modifier
            .size((dimensionResource(id = R.dimen.space_32_dp)))
            .aspectRatio(1f)
    )
}

@Preview(showBackground = true)
@Composable
private fun SpeakingImageViewPreview() {
    ProEnglishScoreTrackerTheme {
        SpeakingImageView(modifier = Modifier)
    }
}

@Composable
private fun TOEICSWScorePicker(
    modifier: Modifier = Modifier,
    toeicRLScore: Int,
    onScoreConfirm: (Int) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var tempScore by remember { mutableIntStateOf(toeicRLScore) }

    Box(modifier = modifier) {
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
                    TOEICSWScorePickerView(
                        score = tempScore,
                        onScoreChange = { tempScore = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            onScoreConfirm(tempScore)
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
private fun TOEICSWScorePickerView(
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
        onScoreChange(hundredState.intValue * 100 +
                tenState.intValue * 10 +
                oneState.intValue
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement
            .SpaceEvenly,
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
        count = 3,
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
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_32_dp)))
        androidx.compose.material.OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.space_52_dp)),
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
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
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
    onClick: () -> Unit,
    isEnabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClick,
            colors =
            if (isEnabled) ButtonDefaults.buttonColors(Color.Blue)
            else ButtonDefaults.buttonColors(Color.LightGray),
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

private fun showToast(context: android.content.Context, message: String) {
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
