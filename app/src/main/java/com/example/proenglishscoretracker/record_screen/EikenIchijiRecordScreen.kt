package com.example.proenglishscoretracker.record_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpOffset
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
fun EikenIchijiRecordScreen(viewModel: EnglishInfoViewModel) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16_dp))
    ) {
        var selectedDate by rememberSaveable { mutableStateOf("") }

        // スコア系
        var cseScore by rememberSaveable { mutableIntStateOf(0) }
        var selectedCseScore by rememberSaveable { mutableIntStateOf(cseScore) }
        var readingScore by rememberSaveable { mutableIntStateOf(0) }
        var selectedReadingScore by rememberSaveable { mutableIntStateOf(readingScore) }
        var listeningScore by rememberSaveable { mutableIntStateOf(0) }
        var selectedListeningScore by rememberSaveable { mutableIntStateOf(listeningScore) }
        var writingScore by rememberSaveable { mutableIntStateOf(0) }
        var selectedWritingScore by rememberSaveable { mutableIntStateOf(writingScore) }
        var speakingScore by rememberSaveable { mutableIntStateOf(0) }
        var selectedSpeakingScore by rememberSaveable { mutableIntStateOf(speakingScore) }
        val grades = listOf("5級", "4級", "3級", "準2級", "2級", "準1級", "1級")
        var selectedGrade by rememberSaveable { mutableStateOf("") }
        var isSpeakingPickerVisible by rememberSaveable { mutableStateOf(false) }
        // グレードが選ばれたときにisSpeakingPickerVisibleを設定する
        isSpeakingPickerVisible = when (selectedGrade) {
            "3級", "準2級", "2級", "準1級", "1級" -> true
            else -> false
        }
        var memoText by rememberSaveable { mutableStateOf("") }
        var date by remember { mutableStateOf(fDate(2025, 1, 1)) }
        var showDatePicker by remember { mutableStateOf(false) }

        //「ErrorText」系
        var selectedDateEmptyErrorText by rememberSaveable { mutableStateOf("") }
        var cseMaxScoreErrorText by rememberSaveable { mutableStateOf("") }
        var readingMaxScoreErrorText by rememberSaveable { mutableStateOf("") }
        var listeningMaxScoreErrorText by rememberSaveable { mutableStateOf("") }
        var writingMaxScoreErrorText by rememberSaveable { mutableStateOf("") }

        //「Error」系
        val selectedDateEmptyError = selectedDate.isEmpty()
        val cseMaxScoreError = cseScore >= 2551
        val readingMaxScoreError = readingScore >= 851
        val listeningMaxScoreError = listeningScore >= 851
        val writingMaxScoreError = writingScore >= 851

        Row {
            SelectDayText("")
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.space_16_dp)))
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
            SelectGradeText(selectedGrade)
            DropdownMenuWithIcon(grades, onGradeSelected = { grade ->
                selectedGrade = grade
            })
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
            CSEScoreText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            EikenCseScorePicker(
                Modifier,
                cseScore,
                selectedCseScore,
                { selectedCseScore = it },
                { cseScore = selectedCseScore },
            )
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            if (cseScore >= 2551) {
                ErrorText("CSEスコアは2551未満である必要があります。")
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
            ReadingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            ReadingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            EikenRLWSScorePicker(
                Modifier,
                readingScore,
                selectedReadingScore,
                { selectedReadingScore = it },
                { readingScore = selectedReadingScore },
            )
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
            if (readingScore >= 851) {
                ErrorText("Readingスコアは851未満である必要があります。")
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
            ListeningImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            ListeningText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            EikenRLWSScorePicker(
                Modifier,
                listeningScore,
                selectedListeningScore,
                { selectedListeningScore = it },
                { listeningScore = selectedListeningScore },
            )
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
            if (listeningScore >= 851) {
                ErrorText("Listeningスコアは851未満である必要があります。")
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
            WritingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            WritingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            EikenRLWSScorePicker(
                Modifier,
                writingScore,
                selectedWritingScore,
                { selectedWritingScore = it },
                { writingScore = selectedWritingScore },
            )
        }

        Row {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
            if (writingScore >= 851) {
                ErrorText("Writingスコアは851未満である必要があります。")
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        SpeakingScoreArea(
            Modifier,
            isVisible = isSpeakingPickerVisible,
            speakingScore = speakingScore,
            onValueChange = { speakingScore = it },
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
            MemoText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            MemoInputField(
                placeholder = stringResource(id = R.string.memo),
                value = memoText,
                onValueChange = { memoText = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        var showSaved by remember { mutableStateOf("") }
        var showAlertDialogOfZeroCaseIchiji by remember { mutableStateOf(false) }
        var showAlertDialogOfZeroCaseNiji by remember { mutableStateOf(false) }
        var ZeroCaseIchiji by remember { mutableStateOf(false) }
        var result by remember { mutableStateOf("Result") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showAlertDialogOfZeroCaseIchiji) {
                    androidx.compose.material.AlertDialog(
                        onDismissRequest = {
                            result = "Dismiss"
                            showAlertDialogOfZeroCaseIchiji = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    result = "はい"
                                    showAlertDialogOfZeroCaseIchiji = false
                                    showSaved = "登録しました。"
                                    viewModel.saveEikenValues(
                                        cseScore,
                                        readingScore,
                                        listeningScore,
                                        writingScore,
                                        speakingScore,
                                        memoText
                                    )
                                }
                            ) {
                                Text("はい")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    result = "いいえ"
                                    showAlertDialogOfZeroCaseIchiji = false
                                }
                            ) {
                                Text("いいえ")
                            }
                        },
                        text = {
                            Text("CSEスコア,Readingスコア, Listeningスコア, Writingスコアのいずれかが０ですが登録しますか？")
                        },
                        contentColor = Color.Black,
                        backgroundColor = Color(0xFFd3d3d3)
                    )
                }
                if (showAlertDialogOfZeroCaseNiji) {
                    androidx.compose.material.AlertDialog(
                        onDismissRequest = {
                            result = "Dismiss"
                            showAlertDialogOfZeroCaseNiji = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    result = "はい"
                                    showAlertDialogOfZeroCaseNiji = false
                                    showSaved = "登録しました。"
                                    viewModel.saveEikenValues(
                                        cseScore,
                                        readingScore,
                                        listeningScore,
                                        writingScore,
                                        speakingScore,
                                        memoText
                                    )
                                }
                            ) {
                                Text("はい")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    result = "いいえ"
                                    showAlertDialogOfZeroCaseNiji = false
                                }
                            ) {
                                Text("いいえ")
                            }
                        },
                        text = {
                            Text("CSEスコア, Readingスコア, Listeningスコア, Writingスコア, Speakingスコアのいずれかが０ですが登録しますか？")
                        },
                        contentColor = Color.Black,
                        backgroundColor = Color(0xFFd3d3d3)
                    )
                }
                SaveButton(
                    onClick = {
                        if (
                            ZeroCaseIchiji &&
                            speakingScore == 0 &&
                            isSpeakingPickerVisible
                            )
                        {
                            showAlertDialogOfZeroCaseNiji = true
                        } else if (
                            cseScore == 0 ||
                            readingScore == 0 ||
                            listeningScore == 0 ||
                            writingScore == 0
                            )
                        {
                            showAlertDialogOfZeroCaseIchiji = true
                            ZeroCaseIchiji = true
                        } else {
                            showSaved = "登録しました。"
                            viewModel.saveEikenValues(
                                cseScore,
                                readingScore,
                                listeningScore,
                                writingScore,
                                speakingScore,
                                memoText
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                ShowSavedText(showSaved)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EikenIchijiRecordScreenPreview(
    @PreviewParameter(PreviewParameterProvider::class)
    viewModel: EnglishInfoViewModel
) {
    ProEnglishScoreTrackerTheme {
        EikenIchijiRecordScreen(viewModel = viewModel)
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
                containerColor = Color(0xFFd3d3d3)
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
                FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
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
                FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
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
                FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
            },
        ) { index ->
            listDayOfMonth.getOrNull(index)?.let { value ->
                Text(text = value.toString())
            }
        }
    }
}

@Composable
private fun SelectGradeText(grade: String, modifier: Modifier = Modifier) {
    Text(
        text = "受験級を選択",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectGradeTextPreview() {
    ProEnglishScoreTrackerTheme {
        SelectGradeText("受験級を選択")
    }
}

@Composable
private fun DropdownMenuWithIcon(
    grades: List<String>,
    onGradeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(8.dp)
            ) {
                Text(
                    text = grades[selectedIndex],
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(0.dp, 8.dp)
            ) {
                grades.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedIndex = index
                            onGradeSelected(item)
                            expanded = false
                        }
                    )
                }
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
private fun CSEScoreText(grade: String, modifier: Modifier = Modifier) {
    Text(
        text = "CSEスコア",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CSEScoreTextPreview() {
    ProEnglishScoreTrackerTheme {
        CSEScoreText("CSEスコア")
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
private fun EikenCseScorePicker(
    modifier: Modifier = Modifier,
    cseScore: Int,
    selectedCseScore: Int,
    onScoreChange: (Int) -> Unit,
    onConfirm: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

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
                text = "$cseScore",
                color = Color.Black
            )
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFd3d3d3)),
                modifier = Modifier
                    .size(width = 240.dp, height = 320.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    EikenCseScorePickerView(
                        score = selectedCseScore,
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
private fun EikenCseScorePickerView(
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    val thousand = score / 1000 // 1000の位
    val hundred = (score % 1000) / 100 // 100の位
    val ten = (score % 100) / 10 // 10の位
    val one = score % 10 // 10の位

    val thousandState = rememberSaveable { mutableIntStateOf(thousand) }
    val hundredState = rememberSaveable { mutableIntStateOf(hundred) }
    val tenState = rememberSaveable { mutableIntStateOf(ten) }
    val oneState = rememberSaveable { mutableIntStateOf(one) }

    LaunchedEffect(
        thousandState.intValue,
        hundredState.intValue,
        tenState.intValue,
        oneState.intValue) {
        onScoreChange(
            thousandState.intValue * 1000 +
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
        // 1000の位
        EikenCseFourDigits(thousandState)
        // 100の位
        EikenCseThreeDigits(hundredState)
        // 10の位
        EikenCseTwoDigits(tenState)
        // 1の位
        EiekenCseOneDigit(oneState)
    }
}

@Composable
private fun EikenCseFourDigits(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = 10,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun EikenCseThreeDigits(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = 10,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun EikenCseTwoDigits(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = 10,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun EiekenCseOneDigit(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = 10,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun EikenRLWSScorePicker(
    modifier: Modifier = Modifier,
    eikenRLWSScore: Int,
    selectedEikenRLWSScore: Int,
    onScoreChange: (Int) -> Unit,
    onConfirm: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

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
                text = "$eikenRLWSScore",
                color = Color.Black
            )
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFd3d3d3)),
                modifier = Modifier
                    .size(width = 240.dp, height = 320.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    EikenRLWSScorePickerView(
                        score = selectedEikenRLWSScore,
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
private fun EikenRLWSScorePickerView(
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    val hundred = score / 100 // 100の位
    val ten = (score % 100) / 10 // 10の位
    val one = score % 10 // 1の位

    // 状態管理のためにrememberを使う
    val hundredState = rememberSaveable { mutableIntStateOf(hundred) }
    val tenState = rememberSaveable { mutableIntStateOf(ten) }
    val oneState = rememberSaveable { mutableIntStateOf(one) }

    // スコア変更をトリガーする
    LaunchedEffect(hundredState.intValue, tenState.intValue, oneState.intValue) {
        onScoreChange(hundredState.intValue * 100 + tenState.intValue * 10 + oneState.intValue)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 100の位
        EikenThreeDigits(hundredState)
        // 10の位
        EikenTwoDigits(tenState)
        // 1の位
        EikenOneDigit(oneState)
    }
}

@Composable
private fun EikenThreeDigits(state: MutableIntState) {
    // FWheelPickerStateを利用してスクロール状態を管理
    val listState = rememberFWheelPickerState()

    // currentIndex の変化を監視
    LaunchedEffect(listState.currentIndex) {
        // listState.currentIndex が変わったときに state.intValue を更新
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(64.dp),
        count = 9,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun EikenTwoDigits(state: MutableIntState) {
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
            FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun EikenOneDigit(state: MutableIntState) {
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
            FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun SpeakingScoreArea(
    modifier: Modifier,
    isVisible: Boolean,
    speakingScore: Int,
    onValueChange: (Int) -> Unit
) {
    if (isVisible) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.space_36_dp))
            ) {
                SpeakingImageView(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.space_8_dp)))
                SpeakingText(
                    speakingText = "Speaking",
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.space_8_dp))
                )
                EikenRLWSScorePicker(
                    eikenRLWSScore = speakingScore,
                    selectedEikenRLWSScore = speakingScore,
                    onScoreChange = onValueChange,
                    onConfirm = { /* 確定時の処理 */ }
                )
            }
            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
                if (speakingScore >= 851) {
                    ErrorText("Speakingスコアは851未満である必要があります。")
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))
        }
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

@Composable
private fun SpeakingText(speakingText: String, modifier: Modifier = Modifier) {
    Text(
        text = "Speaking",
        modifier = modifier
    )
}

@Composable
private fun SpeakingScoreInputField(
    modifier: Modifier = Modifier,
    placeholder: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (value >= 851) ErrorText("")
        androidx.compose.material.OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.space_52_dp)),
            value = value.toString(),
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onValueChange(newValue.toInt())
                }
            },
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
    }
}

@Composable
private fun MemoInputField(placeholder: String, value: String, onValueChange: (String) -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
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

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
private fun ShowSavedText(saved: String) {
    Text(
        text = saved, fontSize = 16.sp, color = Color.Green
    )
}
