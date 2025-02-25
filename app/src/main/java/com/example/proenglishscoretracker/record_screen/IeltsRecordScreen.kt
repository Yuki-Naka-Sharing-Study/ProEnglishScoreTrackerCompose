package com.example.proenglishscoretracker.record_screen

import androidx.compose.foundation.Image
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16_dp))
    ) {
        var date by remember { mutableStateOf(fDate(2025, 1, 1)) }
        var readingScore by remember { mutableFloatStateOf(0F) }
        var selectedReadingScore by remember { mutableFloatStateOf(readingScore) }
        var listeningScore by remember { mutableFloatStateOf(0F) }
        var selectedListeningScore by remember { mutableFloatStateOf(listeningScore) }
        var writingScore by remember { mutableFloatStateOf(0F) }
        var selectedWritingScore by remember { mutableFloatStateOf(writingScore) }
        var speakingScore by remember { mutableFloatStateOf(0F) }
        var selectedSpeakingScore by remember { mutableFloatStateOf(speakingScore) }
        var overallScore by remember { mutableFloatStateOf(0F) }
        var selectedOverallScore by remember { mutableFloatStateOf(overallScore) }
        var memoText by remember { mutableStateOf("") }
        var showDatePicker by remember { mutableStateOf(false) }

        //「ErrorText」系
        var selectedDateEmptyErrorText by remember { mutableStateOf("") }
        var overallMaxScoreErrorText by remember { mutableStateOf("") }
        var readingMaxScoreErrorText by remember { mutableStateOf("") }
        var listeningMaxScoreErrorText by remember { mutableStateOf("") }
        var writingMaxScoreErrorText by remember { mutableStateOf("") }
        var speakingMaxScoreErrorText by remember { mutableStateOf("") }
        var overallScoreDivisionErrorText by remember { mutableStateOf("") }
        var readingScoreDivisionErrorText by remember { mutableStateOf("") }
        var listeningScoreDivisionErrorText by remember { mutableStateOf("") }
        var writingScoreDivisionErrorText by remember { mutableStateOf("") }
        var speakingScoreDivisionErrorText by remember { mutableStateOf("") }

        //「Error」系
        val overallMaxScoreError = overallScore >= 36.1
        val readingMaxScoreError = readingScore >= 9.1
        val listeningMaxScoreError = listeningScore >= 9.1
        val writingMaxScoreError = writingScore >= 9.1
        val speakingMaxScoreError = speakingScore >= 9.1

        val overallScoreDivisionError = overallScore % 0.5 != 0.0
        val readingScoreDivisionError = readingScore % 0.5 != 0.0
        val listeningScoreDivisionError = listeningScore % 0.5 != 0.0
        val writingScoreDivisionError = writingScore % 0.5 != 0.0
        val speakingScoreDivisionError = speakingScore % 0.5 != 0.0

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

        var focusStateOfReading by remember { mutableStateOf(false) }
        val showReadingScoreDivisionError = readingScore % 0.5 != 0.0 && !focusStateOfReading

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            ReadingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            ReadingText("", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            Spacer(modifier = Modifier.weight(0.1f))
            IeltsRLWSScorePicker(
                modifier = Modifier.weight(1f),
                readingScore,
                selectedReadingScore,
                { selectedReadingScore = it },
                { readingScore = selectedReadingScore },
            )
            Spacer(modifier = Modifier.weight(3f))
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            if (readingScore >= 9.1) {
                ErrorText("Readingスコアは9.1未満である必要があります。")
            }
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            if (showReadingScoreDivisionError) {
                ErrorText(
                    "Readingスコアは0.5の倍数である必要があります。"
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        var focusStateOfListening by remember { mutableStateOf(false) }
        val showListeningScoreDivisionError = listeningScore % 0.5 != 0.0 && !focusStateOfListening

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            ListeningImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            ListeningText("", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            Spacer(modifier = Modifier.weight(0.1f))
            IeltsRLWSScorePicker(
                modifier = Modifier.weight(1f),
                listeningScore,
                selectedListeningScore,
                { selectedListeningScore = it },
                { listeningScore = selectedListeningScore },
            )
            Spacer(modifier = Modifier.weight(3f))
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            if (listeningScore >= 9.1) {
                ErrorText("Listeningスコアは9.1未満である必要があります。")
            }
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            if (showListeningScoreDivisionError) {
                ErrorText(
                    "Listeningスコアは0.5の倍数である必要があります。"
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        var focusStateOfWriting by remember { mutableStateOf(false) }
        val showWritingScoreDivisionError = writingScore % 0.5 != 0.0 && !focusStateOfWriting

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            WritingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            WritingText("", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            Spacer(modifier = Modifier.weight(0.1f))
            IeltsRLWSScorePicker(
                modifier = Modifier.weight(1f),
                writingScore,
                selectedWritingScore,
                { selectedWritingScore = it },
                { writingScore = selectedWritingScore },
            )
            Spacer(modifier = Modifier.weight(3f))
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            if (writingScore >= 9.1) {
                ErrorText("Writingスコアは9.1未満である必要があります。")
            }
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            if (showWritingScoreDivisionError) {
                ErrorText(
                    "Writingスコアは0.5の倍数である必要があります。"
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        var focusStateOfSpeaking by remember { mutableStateOf(false) }
        val showSpeakingScoreDivisionError = speakingScore % 0.5 != 0.0 && !focusStateOfSpeaking

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            SpeakingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            SpeakingText("", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            Spacer(modifier = Modifier.weight(0.1f))
            IeltsRLWSScorePicker(
                modifier = Modifier.weight(1f),
                speakingScore,
                selectedSpeakingScore,
                { selectedSpeakingScore = it },
                { speakingScore = selectedSpeakingScore },
            )
            Spacer(modifier = Modifier.weight(3f))
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_52_dp)))
            if (speakingScore >= 9.1) {
                ErrorText("Speakingスコアは9.1未満である必要があります。")
            }
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_52_dp)))
            if (showSpeakingScoreDivisionError) {
                ErrorText(
                    "Speakingスコアは0.5の倍数である必要があります。"
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        var focusStateOfOverall by remember { mutableStateOf(false) }
        val showOverallScoreDivisionError = overallScore % 0.5 != 0.0 && !focusStateOfOverall

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_64_dp)))
            OverallScoreText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            IeltsOverallScorePicker(
                Modifier,
                overallScore,
                selectedOverallScore,
                { selectedOverallScore = it },
                { overallScore = selectedOverallScore },
            )
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_64_dp)))
            if (overallScore >= 36.1) {
                ErrorText("Overallスコアは36.1未満である必要があります。")
            }
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_64_dp)))
            if (showOverallScoreDivisionError) {
                ErrorText(
                    "Overallスコアは0.5の倍数である必要があります。"
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            MemoText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            MemoInputField(
                placeholder = stringResource(id = R.string.memo),
                value = memoText,
                onValueChange = {
                    memoText = it
                }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_64_dp)))

        val savable = overallScore.toString().isNotBlank() &&
                readingScore.toString().isNotBlank() &&
                listeningScore.toString().isNotBlank() &&
                writingScore.toString().isNotBlank() &&
                speakingScore.toString().isNotBlank() &&
                !overallMaxScoreError &&
                !readingMaxScoreError &&
                !listeningMaxScoreError &&
                !writingMaxScoreError &&
                !speakingMaxScoreError &&
                !overallScoreDivisionError &&
                !readingScoreDivisionError &&
                !listeningScoreDivisionError &&
                !writingScoreDivisionError &&
                !speakingScoreDivisionError

        var showSaved by remember { mutableStateOf("") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SaveButton(
                    onClick = {
                        if (savable) {
                            selectedDateEmptyErrorText = ""
                            overallMaxScoreErrorText = ""
                            readingMaxScoreErrorText = ""
                            listeningMaxScoreErrorText = ""
                            writingMaxScoreErrorText = ""
                            speakingMaxScoreErrorText = ""
                            overallScoreDivisionErrorText = ""
                            readingScoreDivisionErrorText = ""
                            listeningMaxScoreErrorText = ""
                            writingScoreDivisionErrorText = ""
                            speakingScoreDivisionErrorText = ""
                            showSaved = "記録しました。"
//                            viewModel.saveIeltsValues(
//                                overallScore,
//                                readingScore,
//                                listeningScore,
//                                writingScore,
//                                speakingScore,
//                                memoText
//                            )
                        } else {
                            if (overallMaxScoreError) {
                                overallMaxScoreErrorText = "Overallスコアは36.1未満である必要があります。"
                            }
                            if (readingMaxScoreError) {
                                readingMaxScoreErrorText = "Readingスコアは9.1未満である必要があります。"
                            }
                            if (listeningMaxScoreError) {
                                listeningMaxScoreErrorText = "Listeningスコアは9.1未満である必要があります。"
                            }
                            if (writingMaxScoreError) {
                                writingMaxScoreErrorText = "Writingスコアは9.1未満である必要があります。"
                            }
                            if (speakingMaxScoreError) {
                                speakingMaxScoreErrorText = "Speakingスコアは9.1未満である必要があります。"
                            }
                            if (overallScoreDivisionError) {
                                overallScoreDivisionErrorText = "Overallスコアは0.5の倍数である必要があります。"
                            }
                            if (readingScoreDivisionError) {
                                readingScoreDivisionErrorText = "Readingスコアは0.5の倍数である必要があります。"
                            }
                            if (listeningScoreDivisionError) {
                                listeningScoreDivisionErrorText = "Listeningスコアは0.5の倍数である必要があります。"
                            }
                            if (writingScoreDivisionError) {
                                writingScoreDivisionErrorText = "Writingスコアは0.5の倍数である必要があります。"
                            }
                            if (speakingScoreDivisionError) {
                                speakingScoreDivisionErrorText = "Speakingスコアは0.5の倍数である必要があります。"
                            }
                            if (!overallMaxScoreError) {
                                overallMaxScoreErrorText = ""
                            }
                            if (!readingMaxScoreError) {
                                readingMaxScoreErrorText = ""
                            }
                            if (!listeningMaxScoreError) {
                                listeningMaxScoreErrorText = ""
                            }
                            if (!writingMaxScoreError) {
                                writingMaxScoreErrorText = ""
                            }
                            if (!speakingMaxScoreError) {
                                speakingMaxScoreErrorText = ""
                            }
                            if (!overallScoreDivisionError) {
                                overallScoreDivisionErrorText = ""
                            }
                            if (!readingScoreDivisionError) {
                                readingScoreDivisionErrorText = ""
                            }
                            if (!listeningScoreDivisionError) {
                                listeningScoreDivisionErrorText = ""
                            }
                            if (!writingScoreDivisionError) {
                                writingScoreDivisionErrorText = ""
                            }
                            if (!speakingScoreDivisionError) {
                                speakingScoreDivisionErrorText = ""
                            }
                        }
                    },
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                ShowSavedText(showSaved)
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
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF9C27B0)
                    ),
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
private fun IeltsOverallScorePicker(
    modifier: Modifier = Modifier,
    ieltsOverallScore: Float,
    selectedIeltsOverallScore: Float,
    onScoreChange: (Float) -> Unit,
    onConfirm: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            modifier = Modifier.align(Alignment.TopCenter),
            onClick = { showDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFf5f5f5)),
        ) {
            Text(
                text = "$ieltsOverallScore",
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
                    verticalArrangement = Arrangement.Center
                ) {
                    IeltsOverallScorePickerView(
                        score = selectedIeltsOverallScore,
                        onScoreChange = onScoreChange
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            onConfirm()
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
private fun IeltsOverallScorePickerView(
    score: Float,
    onScoreChange: (Float) -> Unit
) {
    val ten = score / 10.0
    val one = (score % 10.0) / 1.0

    val tenState = remember { mutableFloatStateOf(ten.toFloat()) }
    val oneState = remember { mutableFloatStateOf(one.toFloat()) }

    LaunchedEffect(tenState.floatValue, oneState.floatValue) {
        onScoreChange((tenState.floatValue * 10.0 + oneState.floatValue).toFloat())
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement
            .SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 10の位
        IeltsOverallTwoDigits(tenState)
        // 1の位
        IeltsOverallOneDigit(oneState)
    }
}

@Composable
private fun IeltsOverallTwoDigits(state: MutableFloatState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.floatValue = listState.currentIndex.toFloat()
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(64.dp),
        count = 4,
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
private fun IeltsOverallOneDigit(state: MutableFloatState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.floatValue = listState.currentIndex.toFloat()
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
private fun IeltsRLWSScorePicker(
    modifier: Modifier = Modifier,
    ieltsRLWSScore: Float,
    selectedIeltsRLWSScore: Float,
    onScoreChange: (Float) -> Unit,
    onConfirm: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            modifier = Modifier.align(Alignment.TopCenter),
            onClick = { showDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFf5f5f5)),
        ) {
            Text(
                text = "$ieltsRLWSScore",
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
                    verticalArrangement = Arrangement.Center
                ) {
                    IeltsRLWSScorePickerView(
                        score = selectedIeltsRLWSScore,
                        onScoreChange = onScoreChange
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            onConfirm()
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
    val scoreState = remember { mutableFloatStateOf(score) }

    LaunchedEffect(scoreState.floatValue) {
        onScoreChange((scoreState.floatValue))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement
            .SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IeltsRLWSScore(scoreState)
    }
}

@Composable
private fun IeltsRLWSScore(state: MutableFloatState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.floatValue = listState.currentIndex.toFloat()
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
private fun MemoInputField(placeholder: String, value: String, onValueChange: (String) -> Unit = {}) {
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
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
    }
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
private fun ShowSavedText(saved: String) {
    Text(
        text = saved,
        fontSize = 16.sp,
        color = Color.Blue
    )
}
