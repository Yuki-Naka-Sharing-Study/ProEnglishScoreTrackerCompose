package com.example.proenglishscoretracker.record_screen

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import kotlinx.coroutines.delay

@Composable
fun EikenRecordScreen(viewModel: EnglishInfoViewModel) {
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
            val grades = listOf("5級", "4級", "3級", "準2級", "2級", "準1級", "1級")
            var selectedGrade by remember { mutableStateOf("") }
            var readingScore by remember { mutableIntStateOf(0) }
            var listeningScore by remember { mutableIntStateOf(0) }
            var writingScore by remember { mutableIntStateOf(0) }
            var speakingScore by remember { mutableIntStateOf(0) }
            var cseScore by remember { mutableIntStateOf(0) }
            var memoText by remember { mutableStateOf("") }
            var isWritingAndSpeakingPickerVisible by remember { mutableStateOf(false) }
            isWritingAndSpeakingPickerVisible = when (selectedGrade) {
                "3級", "準2級", "2級", "準1級", "1級" -> true
                else -> false
            }
            var isSpeakingVisible by remember { mutableStateOf(true) }
            var readingMaxScoreError by remember { mutableStateOf(false) }
            var listeningMaxScoreError by remember { mutableStateOf(false) }
            var writingMaxScoreError by remember { mutableStateOf(false) }
            var speakingMaxScoreError by remember { mutableStateOf(false) }

            SelectDayText("")

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
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

            SelectGradeText("")

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            DropdownMenuWithIcon(
                grades = grades,
                selectedGrade = selectedGrade,
                onGradeSelected = { grade ->
                    selectedGrade = grade
                    viewModel.setEikenGrade(selectedGrade)
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            EnterScoreText("")

            if (isWritingAndSpeakingPickerVisible) {
                SpeakingSwitchArea(
                    Modifier,
                    isSpeakingVisible = isSpeakingVisible,
                    onCheckedChange = { isSpeakingVisible = it }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                ReadingImageView()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                ReadingText("", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = Modifier.weight(0.3f))
                EikenRLWSScorePicker(
                    modifier = Modifier.weight(1.3f),
                    readingScore,
                ) {
                    readingScore = it
                    viewModel.setEikenReadingScore(it)
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                if (readingScore >= 851) {
                    ErrorText("Readingスコアは851未満である必要があります。")
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
                ListeningText("", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = Modifier.weight(0.3f))
                EikenRLWSScorePicker(
                    modifier = Modifier.weight(1.3f),
                    listeningScore,
                ) {
                    listeningScore = it
                    viewModel.setEikenListeningScore(it)
                }
                Spacer(modifier = Modifier.weight(3f))
            }

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                if (listeningScore >= 851) {
                    ErrorText("Listeningスコアは851未満である必要があります。")
                    listeningMaxScoreError = true
                } else {
                    listeningMaxScoreError = false
                }
            }

            WritingScoreArea(
                Modifier,
                isVisible = isWritingAndSpeakingPickerVisible,
                writingScore = writingScore,
                onValueChange = {
                    writingScore = it
                    viewModel.setEikenWritingScore(it)
                }
            )

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                if (writingScore >= 851) {
                    writingMaxScoreError = true
                } else {
                    writingMaxScoreError = false
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

            SpeakingScoreArea(
                Modifier,
                isVisible = isWritingAndSpeakingPickerVisible && isSpeakingVisible,
                speakingScore = speakingScore,
                onValueChange = {
                    speakingScore = it
                    viewModel.setEikenSpeakingScore(it)
                }
            )

            Row {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_24_dp)))
                if (speakingScore >= 851) {
                    speakingMaxScoreError = true
                } else {
                    speakingMaxScoreError = false
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_64_dp)))
                CSEScoreText("")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = Modifier.weight(0.1f))
                EikenCseScoreView(
                    modifier = Modifier,
                    readingScore,
                    listeningScore,
                    writingScore,
                    speakingScore
                )
                Spacer(modifier = Modifier.weight(3f))
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
                        viewModel.setEikenMemoText(it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_64_dp)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    cseScore = readingScore + listeningScore + writingScore + speakingScore
                    var showAlertDialogOfZeroCaseRL by remember { mutableStateOf(false) }
                    var zeroCaseRL by remember { mutableStateOf(false) }
                    var showAlertDialogOfZeroCaseRLW by remember { mutableStateOf(false) }
                    var showAlertDialogOfZeroCaseRLWS by remember { mutableStateOf(false) }

                    val savableChecker = !readingMaxScoreError &&
                            !listeningMaxScoreError &&
                            !writingMaxScoreError &&
                            !speakingMaxScoreError

                    var showSaved by remember { mutableStateOf("") }
                    var result by remember { mutableStateOf("Result") }
                    var alertMessage by remember { mutableStateOf<String?>(null) }

                    if (alertMessage != null) {
                        AlertDialog(
                            onDismissRequest = { alertMessage = null },
                            confirmButton = {
                                TextButton(
                                    onClick = { alertMessage = null }
                                ) {
                                    Text(
                                        text = "OK",
                                        color = Color.White
                                    )
                                }
                            },
                            text = {
                                Text(
                                    text = alertMessage!!,
                                    color = Color.Red
                                )
                            },
                            backgroundColor = Color.White,
                            contentColor = Color.Black
                        )
                    }
                    if (showAlertDialogOfZeroCaseRL) {
                        AlertDialog(
                            onDismissRequest = {
                                result = "Dismiss"
                                showAlertDialogOfZeroCaseRL = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        result = "はい"
                                        showAlertDialogOfZeroCaseRL = false
                                        viewModel.saveEikenValues(
                                            date.toString(),
                                            selectedGrade,
                                            readingScore,
                                            listeningScore,
                                            writingScore,
                                            speakingScore,
                                            cseScore,
                                            memoText,
                                            showAlert = { message ->
                                                alertMessage = message
                                                showSaved = ""
                                            }
                                        )
                                        if (alertMessage == null) {
                                            showSaved = "登録しました。"
                                        }
                                        viewModel.setEikenGrade("")
                                        viewModel.setEikenReadingScore(0)
                                        viewModel.setEikenListeningScore(0)
                                        viewModel.setEikenWritingScore(0)
                                        viewModel.setEikenReadingScore(0)
                                        viewModel.setEikenMemoText("")
                                        readingScore = 0
                                        listeningScore = 0
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
                                        showAlertDialogOfZeroCaseRL = false
                                    }
                                ) {
                                    Text("いいえ")
                                }
                            },
                            text = {
                                Text("Readingスコア, Listeningスコアのいずれかが0ですが登録しますか？")
                            },
                            contentColor = Color.Black,
                            backgroundColor = Color(0xFFd3d3d3)
                        )
                    }
                    if (showAlertDialogOfZeroCaseRLW) {
                        AlertDialog(
                            onDismissRequest = {
                                result = "Dismiss"
                                showAlertDialogOfZeroCaseRLW = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        result = "はい"
                                        showAlertDialogOfZeroCaseRLW = false
                                        viewModel.saveEikenValues(
                                            date.toString(),
                                            selectedGrade,
                                            readingScore,
                                            listeningScore,
                                            writingScore,
                                            speakingScore,
                                            cseScore,
                                            memoText,
                                            showAlert = { message ->
                                                alertMessage = message
                                                showSaved = ""
                                            }
                                        )
                                        if (alertMessage == null) {
                                            showSaved = "登録しました。"
                                        }
                                        viewModel.setEikenGrade("")
                                        viewModel.setEikenGrade("")
                                        viewModel.setEikenReadingScore(0)
                                        viewModel.setEikenListeningScore(0)
                                        viewModel.setEikenWritingScore(0)
                                        viewModel.setEikenSpeakingScore(0)
                                        viewModel.setEikenMemoText("")
                                        readingScore = 0
                                        listeningScore = 0
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
                                        showAlertDialogOfZeroCaseRLW = false
                                    }
                                ) {
                                    Text("いいえ")
                                }
                            },
                            text = {
                                Text("Readingスコア, Listeningスコア, Writingスコアのいずれかが0ですが登録しますか？")
                            },
                            contentColor = Color.Black,
                            backgroundColor = Color(0xFFd3d3d3)
                        )
                    }
                    if (showAlertDialogOfZeroCaseRLWS) {
                        AlertDialog(
                            onDismissRequest = {
                                result = "Dismiss"
                                showAlertDialogOfZeroCaseRLWS = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        result = "はい"
                                        showAlertDialogOfZeroCaseRLWS = false
                                        viewModel.saveEikenValues(
                                            date.toString(),
                                            selectedGrade,
                                            readingScore,
                                            listeningScore,
                                            writingScore,
                                            speakingScore,
                                            cseScore,
                                            memoText,
                                            showAlert = { message ->
                                                alertMessage = message
                                                showSaved = ""
                                            }
                                        )
                                        if (alertMessage == null) {
                                            showSaved = "登録しました。"
                                        }
                                        viewModel.setEikenGrade("")
                                        viewModel.setEikenReadingScore(0)
                                        viewModel.setEikenListeningScore(0)
                                        viewModel.setEikenWritingScore(0)
                                        viewModel.setEikenSpeakingScore(0)
                                        viewModel.setEikenMemoText("")
                                        readingScore = 0
                                        listeningScore = 0
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
                                        showAlertDialogOfZeroCaseRLWS = false
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
                                if (selectedGrade == "") {
                                    alertMessage = "受験級が選択されていません。"
                                } else if (
                                    zeroCaseRL &&
                                    writingScore == 0 &&
                                    isWritingAndSpeakingPickerVisible &&
                                    !isSpeakingVisible
                                ) {
                                    showAlertDialogOfZeroCaseRLW = true
                                } else if (
                                    zeroCaseRL &&
                                    writingScore == 0 &&
                                    speakingScore == 0 &&
                                    isWritingAndSpeakingPickerVisible
                                ) {
                                    showAlertDialogOfZeroCaseRLWS = true
                                } else if (
                                    readingScore == 0 ||
                                    listeningScore == 0
                                ) {
                                    showAlertDialogOfZeroCaseRL = true
                                    zeroCaseRL = true
                                } else {
                                    viewModel.saveEikenValues(
                                        date.toString(),
                                        selectedGrade,
                                        readingScore,
                                        listeningScore,
                                        writingScore,
                                        speakingScore,
                                        cseScore,
                                        memoText,
                                        showAlert = { message ->
                                            alertMessage = message
                                            showSaved = ""
                                        }
                                    )
                                    if (alertMessage == null) {
                                        showSaved = "登録しました。"
                                    }
                                    viewModel.setEikenGrade("")
                                    viewModel.setEikenReadingScore(0)
                                    viewModel.setEikenListeningScore(0)
                                    viewModel.setEikenWritingScore(0)
                                    viewModel.setEikenSpeakingScore(0)
                                    viewModel.setEikenMemoText("")
                                    readingScore = 0
                                    listeningScore = 0
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
                            title = { Text(
                                text = "エラー",
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            ) },
                            text = { Text(
                                text = alertMessage!!,
                                color = Color.Black
                            ) },
                            confirmButton = {
                                Button(onClick = {
                                    alertMessage = null
                                    showSaved = ""
                                }) {
                                    Text(
                                        text = "OK",
                                        color = Color.White
                                    )
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
private fun EikenIchijiRecordScreenPreview(
    @PreviewParameter(PreviewParameterProvider::class)
    viewModel: EnglishInfoViewModel
) {
    ProEnglishScoreTrackerTheme {
        EikenRecordScreen(viewModel = viewModel)
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
    selectedGrade: String,
    onGradeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                    text = selectedGrade.ifEmpty { "" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector =
                    if (expanded) Icons.Default.ArrowDropUp
                    else Icons.Default.ArrowDropDown,
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
                grades.forEach { grade ->
                    DropdownMenuItem(
                        text = { Text(text = grade) },
                        onClick = {
                            onGradeSelected(grade)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SpeakingSwitchArea(
    modifier: Modifier,
    isSpeakingVisible: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.space_16_dp)))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(id = R.dimen.space_16_dp))
        ) {
            Switch(
                checked = isSpeakingVisible,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF9C27B0),
                    uncheckedThumbColor = Color(0xFFBDBDBD),
                    checkedTrackColor = Color(0xFF9C27B0),
                    uncheckedTrackColor = Color(0xFFBDBDBD)
                )
            )
            Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            Text(text = "二次試験受験済")
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
private fun EikenCseScoreView(
    modifier: Modifier,
    readingScore: Int,
    listeningScore: Int,
    writingScore: Int,
    speakingScore: Int,
) {
    val cseScore = readingScore + listeningScore + writingScore + speakingScore

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
                text = cseScore.toString(),
                color = Color.Black
            )
        }
    }
}

@Composable
private fun EikenRLWSScorePicker(
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
                    EikenRLWSScorePickerView(
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
private fun EikenRLWSScorePickerView(
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    val hundred = score / 100
    val ten = (score % 100) / 10
    val one = score % 10

    val hundredState = remember { mutableIntStateOf(hundred) }
    val tenState = remember { mutableIntStateOf(ten) }
    val oneState = remember { mutableIntStateOf(one) }

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
        EikenThreeDigits(hundredState)
        EikenTwoDigits(tenState)
        EikenOneDigit(oneState)
    }
}

@Composable
private fun EikenThreeDigits(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(64.dp),
        count = 9,
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
private fun EikenTwoDigits(state: MutableIntState) {
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
private fun EikenOneDigit(state: MutableIntState) {
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
private fun WritingScoreArea(
    modifier: Modifier,
    isVisible: Boolean,
    writingScore: Int,
    onValueChange: (Int) -> Unit,
) {
    if (isVisible) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.space_16_dp)))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.space_24_dp))
            ) {
                WritingImageView()
                Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                WritingText(
                    writingText = "Writing",
                    modifier = modifier.weight(1f)
                )
                Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = modifier.weight(0.3f))
                EikenRLWSScorePicker(
                    modifier = modifier.weight(1.2f),
                    writingScore,
                    onScoreConfirm = onValueChange
                )
                Spacer(modifier = modifier.weight(3f))
            }
            Row {
                Spacer(modifier = modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
                if (writingScore >= 851) {
                    ErrorText("WritingScoreスコアは851未満である必要があります。")
                }
            }
        }
    }
}

@Composable
private fun SpeakingScoreArea(
    modifier: Modifier,
    isVisible: Boolean,
    speakingScore: Int,
    onValueChange: (Int) -> Unit,
) {
    if (isVisible) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.space_24_dp))
            ) {
                SpeakingImageView()
                Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                SpeakingText(
                    speakingText = "Speaking",
                    modifier = modifier.weight(1f)
                )
                Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
                Spacer(modifier = modifier.weight(0.3f))
                EikenRLWSScorePicker(
                    modifier = modifier.weight(1.2f),
                    speakingScore,
                    onScoreConfirm = onValueChange
                )
                Spacer(modifier = modifier.weight(3f))
            }
            Row {
                Spacer(modifier = modifier.padding(start = dimensionResource(id = R.dimen.space_36_dp)))
                if (speakingScore >= 851) {
                    ErrorText("SpeakingScoreスコアは851未満である必要があります。")
                }
            }
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.space_16_dp)))
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
        OutlinedTextField(
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
            delay(2000) // 2秒間待機
            onTimeout()
        }
    }
}
