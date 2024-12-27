package com.example.proenglishscoretracker.record_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme
import java.util.Calendar

@Composable
fun ToeicRecordScreen(viewModel: EnglishInfoViewModel) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16_dp))
    ) {
        var selectedDate by rememberSaveable { mutableStateOf("") }
        var readingScore by rememberSaveable { mutableIntStateOf(0) }
        var listeningScore by rememberSaveable { mutableIntStateOf(0) }
        var memoText by rememberSaveable { mutableStateOf("") }

        //「ErrorText」系
        var selectedDateEmptyErrorText by rememberSaveable { mutableStateOf("") }
        var readingMaxScoreErrorText by rememberSaveable { mutableStateOf("") }
        var listeningMaxScoreErrorText by rememberSaveable { mutableStateOf("") }
        var readingScoreDivisionErrorText by rememberSaveable { mutableStateOf("") }
        var listeningScoreDivisionErrorText by rememberSaveable { mutableStateOf("") }

        //「Error」系
        val selectedDateEmptyError = selectedDate.isEmpty()
        val readingMaxScoreError = readingScore >= 496
        val listeningMaxScoreError = listeningScore >= 496
        val readingScoreDivisionError = readingScore % 5 != 0
        val listeningScoreDivisionError = listeningScore % 5 != 0

        Row {
            SelectDayText("")
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.space_24_dp)))
            Column {
                SelectDatePicker(LocalContext.current) { date->
                    selectedDate = date
                    selectedDateEmptyErrorText = ""
                }
                Text(selectedDate)
                if (selectedDate.isEmpty()) ErrorText(selectedDateEmptyErrorText)
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
            ReadingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            ReadingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            Column {
                ReadingScoreInputField(
                    placeholder = stringResource(id = R.string.toeic_reading_score),
                    value = readingScore,
                    onValueChange = { readingScore = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
            ListeningImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            ListeningText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            Column {
                ListeningScoreInputField(
                    placeholder = stringResource(id = R.string.toeic_listening_score),
                    value = listeningScore,
                    onValueChange = { listeningScore = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
            MemoText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            InputMemoRow(
                placeholder = stringResource(id = R.string.memo),
                value = memoText,
                onValueChange = { memoText = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        val savable =
            readingScore.toString().isNotBlank() &&
                    listeningScore.toString().isNotBlank() &&
                    !selectedDateEmptyError &&
                    !readingMaxScoreError &&
                    !listeningMaxScoreError &&
                    !readingScoreDivisionError &&
                    !listeningScoreDivisionError

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
                        // 修正後のコード（when文 → if文）
                        if (savable) {
                            selectedDateEmptyErrorText = ""
                            readingMaxScoreErrorText = ""
                            listeningMaxScoreErrorText = ""
                            readingScoreDivisionErrorText = ""
                            listeningScoreDivisionErrorText = ""
                            showSaved = "記録しました。"
                            viewModel.saveToeicValues(
                                readingScore,
                                listeningScore,
                                memoText
                            )
                        } else {
                            if (selectedDateEmptyError) {
                                selectedDateEmptyErrorText = "受験日が記入されていません。"
                            }
                            if (readingMaxScoreError) {
                                readingMaxScoreErrorText = "Readingスコアは496未満である必要があります。"
                            }
                            if (listeningMaxScoreError) {
                                listeningMaxScoreErrorText = "Listeningスコアは496未満である必要があります。"
                            }
                            if (readingScoreDivisionError) {
                                readingScoreDivisionErrorText = "Readingスコアはである5の倍数である必要があります。"
                            }
                            if (listeningScoreDivisionError) {
                                listeningScoreDivisionErrorText = "Listeningスコアはである5の倍数である必要があります。"
                            }
                            if (!readingScoreDivisionError) {
                                readingScoreDivisionErrorText = ""
                            }
                            if (!listeningScoreDivisionError) {
                                listeningScoreDivisionErrorText = ""
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
private fun SelectDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate =
                String.format("%04d年%02d月%02d日", selectedYear, selectedMonth + 1, selectedDay)
            onDateSelected(formattedDate)
        }, year, month, day
    )
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis
    Button(onClick = { datePickerDialog.show() }, colors = ButtonDefaults.buttonColors(
        containerColor = Color.Blue
    ), shape = RoundedCornerShape(8.dp)) {
        Text(
            text = "受験日を選択する",
            color = Color.White,
        )
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
private fun ReadingInputField(modifier: Modifier) {
    var number by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.space_52_dp)),
            value = number,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    number = newValue
                }
            },
            label = { Text("495") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReadingInputFieldPreview() {
    ProEnglishScoreTrackerTheme {
        ReadingInputField(modifier = Modifier)
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
private fun ListeningInputField(modifier: Modifier) {
    var number by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.space_52_dp)),
            value = number,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    number = newValue
                }
            },
            label = { Text("495") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListeningInputFieldPreview() {
    ProEnglishScoreTrackerTheme {
        ListeningInputField(modifier = Modifier)
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
    ){
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
private fun ReadingScoreInputField(placeholder: String, value: Int, onValueChange: (Int) -> Unit) {
    var isError by rememberSaveable { mutableStateOf(false) }
    var focusState by rememberSaveable { mutableStateOf(false) }
    val showError = value % 5 != 0 && !focusState

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        androidx.compose.material.OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.space_52_dp))
                .onFocusChanged {
                    focusState = it.isFocused
                    if (!it.isFocused && value % 5 != 0) {
                        isError = true
                    }
                },
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
        Spacer(modifier = Modifier.height(8.dp))
        if (showError) {
            ErrorText("Readingスコアは5の倍数である必要があります。")
        }
        if (value >= 496) {
            ErrorText("Readingスコアは496未満である必要があります。")
        }
    }
}

@Composable
private fun ListeningScoreInputField(placeholder: String, value: Int, onValueChange: (Int) -> Unit) {
    var isError by rememberSaveable { mutableStateOf(false) }
    var focusState by rememberSaveable { mutableStateOf(false) }
    val showError = value % 5 != 0 && !focusState

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        androidx.compose.material.OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.space_52_dp))
                .onFocusChanged {
                    focusState = it.isFocused
                    if (!it.isFocused && value % 5 != 0) {
                        isError = true
                    }
                },
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
        Spacer(modifier = Modifier.height(8.dp))
        if (showError) {
            ErrorText("Listeningスコアは5の倍数である必要があります。")
        }
        if (value >= 496) {
            ErrorText("Listeningスコアは496未満である必要があります。")
        }
    }
}

@Composable
private fun InputMemoRow(placeholder: String, value: String, onValueChange: (String) -> Unit = {}) {
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

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
private fun ShowSavedText(saved: String) {
    Text(
        text = saved, fontSize = 16.sp, color = Color.Green
    )
}