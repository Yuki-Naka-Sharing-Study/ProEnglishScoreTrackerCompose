package com.example.proenglishscoretracker

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.ComposeView
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme
import kotlin.getValue

class ToeflIbtRecordFragment : Fragment() {
    private val viewModel: EnglishInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                ToeflIbtRecordScreen(viewModel = viewModel)
            }
        }
        return composeView
    }
}

@Composable
fun ToeflIbtRecordScreen(viewModel: EnglishInfoViewModel) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16))
    ) {
        var overallScore by rememberSaveable { mutableStateOf("") }
        var readingScore by rememberSaveable { mutableStateOf("") }
        var listeningScore by rememberSaveable { mutableStateOf("") }
        var writingScore by rememberSaveable { mutableStateOf("") }
        var speakingScore by rememberSaveable { mutableStateOf("") }
        var memoText by rememberSaveable { mutableStateOf("") }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            SelectDayText("")
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            EnterScoreText("")
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            OverallScoreText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            InputRow(
                placeholder = stringResource(id = R.string.toefl_ibt_overall_score),
                value = overallScore,
                onValueChange = { overallScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            ReadingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            ReadingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            InputRow(
                placeholder = stringResource(id = R.string.toefl_ibt_reading_score),
                value = readingScore,
                onValueChange = { readingScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            ListeningText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            ListeningImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            InputRow(
                placeholder = stringResource(id = R.string.toefl_ibt_listening_score),
                value = listeningScore,
                onValueChange = { listeningScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            WritingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            WritingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            InputRow(
                placeholder = stringResource(id = R.string.toefl_ibt_writing_score),
                value = writingScore,
                onValueChange = { writingScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            SpeakingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            SpeakingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            InputRow(
                placeholder = stringResource(id = R.string.toefl_ibt_speaking_score),
                value = speakingScore,
                onValueChange = { speakingScore = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            MemoText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            InputRow(
                placeholder = stringResource(id = R.string.memo),
                value = memoText,
                onValueChange = { memoText = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

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
private fun ToeflIbtRecordScreenPreview(
    @PreviewParameter(PreviewParameterProvider::class)
    viewModel: EnglishInfoViewModel
) {
    ProEnglishScoreTrackerTheme {
        ToeflIbtRecordScreen(viewModel = viewModel)
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
            .size((dimensionResource(id = R.dimen.space_32)))
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
            .size((dimensionResource(id = R.dimen.space_32)))
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
            .size((dimensionResource(id = R.dimen.space_32)))
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
            .size((dimensionResource(id = R.dimen.space_32)))
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
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
        androidx.compose.material.OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.space_52)),
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
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
    }
}

@Composable
private fun SaveButton(
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Blue),
        enabled = enabled
    ) {
        Text("記録する", color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveButtonPreview() {
    ProEnglishScoreTrackerTheme {
        SaveButton()
    }
}
