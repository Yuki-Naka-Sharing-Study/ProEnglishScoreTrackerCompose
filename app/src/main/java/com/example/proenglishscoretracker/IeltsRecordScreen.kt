package com.example.proenglishscoretracker

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun IeltsRecordScreen(viewModel: EnglishInfoViewModel) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16_dp))
    ) {
        var selectedDate by remember { mutableStateOf("") }
        var overallScore by rememberSaveable { mutableStateOf("") }
        var readingScore by rememberSaveable { mutableStateOf("") }
        var listeningScore by rememberSaveable { mutableStateOf("") }
        var writingScore by rememberSaveable { mutableStateOf("") }
        var speakingScore by rememberSaveable { mutableStateOf("") }
        var memoText by rememberSaveable { mutableStateOf("") }

        Row {
            SelectDayText("")
            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.space_24_dp)))
            Column {
                SelectDatePicker(LocalContext.current) { date->
                    selectedDate = date
                }
                Text("受験日: $selectedDate")
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
            OverallScoreText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            InputRow(
                placeholder = stringResource(id = R.string.ielts_overall_score),
                value = overallScore,
                onValueChange = { overallScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_52_dp)))
            ReadingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            ReadingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            InputRow(
                placeholder = stringResource(id = R.string.ielts_reading_score),
                value = readingScore,
                onValueChange = { readingScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_52_dp)))
            ListeningText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            ListeningImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            InputRow(
                placeholder = stringResource(id = R.string.ielts_listening_score),
                value = listeningScore,
                onValueChange = { listeningScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_52_dp)))
            WritingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            WritingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            InputRow(
                placeholder = stringResource(id = R.string.ielts_writing_score),
                value = writingScore,
                onValueChange = { writingScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_52_dp)))
            SpeakingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8_dp)))
            SpeakingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            InputRow(
                placeholder = stringResource(id = R.string.ielts_speaking_score),
                value = speakingScore,
                onValueChange = { speakingScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.space_32_dp)))
            MemoText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16_dp)))
            InputRow(
                placeholder = stringResource(id = R.string.memo),
                value = memoText,
                onValueChange = { memoText = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16_dp)))

        val isButtonEnabled = overallScore.isNotBlank() &&
                readingScore.isNotBlank() &&
                listeningScore.isNotBlank() &&
                writingScore.isNotBlank() &&
                speakingScore.isNotBlank() &&
                memoText.isNotBlank()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SaveButton(
                onClick = {
                    viewModel.saveToeflIbtValues(
                        overallScore,
                        readingScore,
                        listeningScore,
                        writingScore,
                        speakingScore,
                        memoText
                    )
                },
                enabled = isButtonEnabled
            )
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
private fun SelectDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate =
                String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
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
private fun InputRow(placeholder: String, value: String, onValueChange: (String) -> Unit = {}) {
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
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    var showMessage by remember { mutableStateOf(false) }

    Button(
        onClick = { showMessage = true },
        colors = ButtonDefaults.buttonColors(Color.Blue),
        enabled = enabled
    ) {
        Text("記録する", color = Color.White)
    }

    if (showMessage) {
        Text("記録しました。")
        LaunchedEffect(Unit) {
            delay(2000)
            showMessage = false
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveButtonPreview() {
    ProEnglishScoreTrackerTheme {
        SaveButton()
    }
}
