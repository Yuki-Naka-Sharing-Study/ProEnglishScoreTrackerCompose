package com.example.proenglishscoretracker.record_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
fun IeltsRecordScreen(viewModel: EnglishInfoViewModel) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .clickable(interactionSource = interactionSource, indication = null) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16_dp))
        ) {
            var date by remember { mutableStateOf(fDate(2025, 1, 1)) }
            var showDatePicker by remember { mutableStateOf(false) }
            var readingScore by remember { mutableFloatStateOf(0.0F) }
            var listeningScore by remember { mutableFloatStateOf(0.0F) }
            var writingScore by remember { mutableFloatStateOf(0.0F) }
            var speakingScore by remember { mutableFloatStateOf(0.0F) }
            var overallScore by remember { mutableFloatStateOf(0.0F) }
            var memoText by remember { mutableStateOf("") }
            var readingMaxScoreError by remember { mutableStateOf(false) }
            var listeningMaxScoreError by remember { mutableStateOf(false) }
            var writingMaxScoreError by remember { mutableStateOf(false) }
            var speakingMaxScoreError by remember { mutableStateOf(false) }

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                ReadingImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                ReadingText("", modifier = Modifier.weight(1.1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                IeltsRLWSScorePicker(
                    modifier = Modifier.weight(1.2f),
                    readingScore,
                ) {
                    readingScore = it
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                if (readingScore >= 9.1) {
                    ErrorText("Readingスコアの上限は9.0です。")
                    readingMaxScoreError = true
                } else {
                    readingMaxScoreError = false
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                ListeningImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                ListeningText("", modifier = Modifier.weight(1.1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                IeltsRLWSScorePicker(
                    modifier = Modifier.weight(1.2f),
                    listeningScore,
                ) {
                    listeningScore = it
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                if (listeningScore >= 9.1) {
                    ErrorText("Listeningスコアの上限は9.0です。")
                    listeningMaxScoreError = true
                } else {
                    listeningMaxScoreError = false
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                WritingImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                WritingText("", modifier = Modifier.weight(1.1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                IeltsRLWSScorePicker(
                    modifier = Modifier.weight(1.2f),
                    writingScore,
                ) {
                    writingScore = it
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                if (writingScore >= 9.1) {
                    ErrorText("Writingスコアの上限は9.0です。")
                    writingMaxScoreError = true
                } else {
                    writingMaxScoreError = false
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                SpeakingImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                SpeakingText("", modifier = Modifier.weight(1.1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                IeltsRLWSScorePicker(
                    modifier = Modifier.weight(1.2f),
                    speakingScore,
                ) {
                    speakingScore = it
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                if (speakingScore >= 9.1) {
                    ErrorText("Speakingスコアの上限は9.0です。")
                    speakingMaxScoreError = true
                } else {
                    speakingMaxScoreError = false
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_64_dp)))
                OverallScoreText("")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
                IeltsOverallScoreView(
                    modifier = Modifier,
                    readingScore,
                    listeningScore,
                    writingScore,
                    speakingScore
                )
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    overallScore = readingScore + listeningScore + writingScore + speakingScore
                    val savableChecker = !readingMaxScoreError &&
                            !listeningMaxScoreError &&
                            !writingMaxScoreError &&
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
                                                viewModel.saveIeltsValues(
                                                    date.toString(),
                                                    readingScore,
                                                    listeningScore,
                                                    writingScore,
                                                    speakingScore,
                                                    overallScore,
                                                    memoText,
                                                    showAlert = { message ->
                                                        alertMessage = message
                                                        showSaved = ""
                                                    }
                                                )
                                                if (alertMessage == null) {
                                                    showSaved = "登録しました。"
                                                }
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
                                        Text("Readingスコア, Listeningスコア, Writingスコア, Speakingスコアのいずれかが0ですが登録しますか？")
                                    },
                                    contentColor = Color.Black,
                                    backgroundColor = Color(0xFFd3d3d3)
                                )
                            }
                            SaveButton(
                                onClick = {
                                    if (savableChecker) {
                                        if (
                                            readingScore == 0.0F ||
                                            listeningScore == 0.0F ||
                                            writingScore == 0.0F ||
                                            speakingScore == 0.0F
                                        )
                                        {
                                            showAlertDialogOfZero = true
                                        } else {
                                            viewModel.saveIeltsValues(
                                                date.toString(),
                                                readingScore,
                                                listeningScore,
                                                writingScore,
                                                speakingScore,
                                                overallScore,
                                                memoText,
                                                showAlert = { message ->
                                                    alertMessage = message
                                                    showSaved = ""
                                                }
                                            )
                                            if (alertMessage == null) {
                                                showSaved = "登録しました。"
                                            }
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
    }
}

@Preview(showBackground = true)
@Composable
private fun IeltsRecordScreenPreview(
    @PreviewParameter(PreviewParameterProvider::class)
    viewModel: EnglishInfoViewModel
) {
    ProEnglishScoreTrackerTheme {
        IeltsRecordScreen(viewModel = viewModel)
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
) {
    Box{
        Button(
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
private fun OverallScoreText(grade: String, modifier: Modifier = Modifier) {
    Text(
        text = "Overall",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun OverallScoreTextPreview() {
    ProEnglishScoreTrackerTheme {
        OverallScoreText("Overall")
    }
}

@Composable
private fun IeltsOverallScoreView(
    modifier: Modifier,
    readingScore: Float,
    listeningScore: Float,
    writingScore: Float,
    speakingScore: Float,
) {
    val overallScore = readingScore + listeningScore + writingScore + speakingScore

    Box(modifier = modifier) {
        Button(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 80.dp, height = 40.dp),
            onClick = { },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFf5f5f5)),
        ) {
            Text(
                text = overallScore.toString(),
                color = Color.Black
            )
        }
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
private fun MemoText(memoText: String, modifier: Modifier = Modifier) {
    Text(
        text = "メモ",
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
private fun IeltsRLWSScorePicker(
    modifier: Modifier = Modifier,
    ieltsRLWSScore: Float,
    onScoreConfirm: (Float) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var tempScore by remember { mutableStateOf(ieltsRLWSScore) }

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
                text = "%.1f".format(ieltsRLWSScore),
                color = Color.Black
            )
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFffffff)
                ),
                modifier = Modifier
                    .size(width = 240.dp, height = 320.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IeltsRLWSScorePickerView(
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
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF9C27B0)
                        ),
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
private fun IeltsRLWSScorePickerView(
    score: Float,
    onScoreChange: (Float) -> Unit
) {
    val index = ((score * 2).toInt()).coerceIn(0, 18)
    val state = rememberFWheelPickerState(initialIndex = index)

    LaunchedEffect(state.currentIndex) {
        onScoreChange(state.currentIndex / 2f)
    }

    FVerticalWheelPicker(
        modifier = Modifier.width(64.dp),
        count = 19, // 0.0 to 9.0 (0.5刻み)
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = state,
        focus = {
            FWheelPickerFocusVertical(
                dividerColor = Color.LightGray,
                dividerSize = 2.dp
            )
        },
    ) { index ->
        Text(
            "%.1f".format(index / 2f),
            color = Color.Black
        )
    }
}

@Composable
private fun IeltsRLWSTwoDigits(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
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

@Composable
private fun IeltsRLWSOneDigits(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
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

@Composable
private fun ErrorText(error: String) {
    Text(
        text = error,
        fontSize = 12.sp,
        color = Color.Red,
        maxLines = 1,
    )
}

@Composable
private fun ShowSavedText(saved: String, onTimeout: () -> Unit) {
    if (saved.isNotEmpty()) {
        Text(
            text = saved,
            fontSize = 16.sp,
            color = Color.Blue
        )
        LaunchedEffect(saved) {
            kotlinx.coroutines.delay(2000)
            onTimeout()
        }
    }
}
