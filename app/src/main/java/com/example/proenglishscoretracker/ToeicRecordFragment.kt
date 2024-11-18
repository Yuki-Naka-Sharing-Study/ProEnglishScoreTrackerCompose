package com.example.proenglishscoretracker

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme

class ToeicRecordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                ToeicRecordScreen()
            }
        }
        return composeView
    }
}

@Composable
fun ToeicRecordScreen() {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.space_16))
    ) {

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
            ReadingText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            ReadingImageView()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            ReadingInputField(
                modifier = Modifier
                    .weight(1f)
                    .width(dimensionResource(id = R.dimen.space_16))
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
            ListeningInputField(
                modifier = Modifier
                    .weight(1f)
                    .width(dimensionResource(id = R.dimen.space_16))
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            MemoText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            MemoTextField(
                modifier = Modifier
                    .weight(1f)
                    .width(dimensionResource(id = R.dimen.space_16))
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_16)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SaveButton()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ToeicRecordScreenPreview() {
    ProEnglishScoreTrackerTheme {
        ToeicRecordScreen()
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
private fun ReadingInputField(modifier: Modifier) {
    var number by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.space_52)),
            value = number,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    number = newValue
                }
            },
            label = { Text("850") },
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
private fun ListeningInputField(modifier: Modifier) {
    var number by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(dimensionResource(id = R.dimen.space_52)),
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
                .height(dimensionResource(id = R.dimen.space_52)),
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
private fun SaveButton(
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Blue)
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
