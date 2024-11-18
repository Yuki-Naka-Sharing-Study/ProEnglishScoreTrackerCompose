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

class EikenNijiRecordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                EikenNijiRecordScreen()
            }
        }
        return composeView
    }
}

@Composable
fun EikenNijiRecordScreen() {
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
            SelectGradeText("")
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
            CSEScoreText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_16)))
            CSEScoreInputField(
                modifier = Modifier
                    .weight(1f),
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
            SpeakingInputField(
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
            ShortSpeechText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            EachSectionField(
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
            InteractionText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            EachSectionField(
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
            GrammarAndVocabularyText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            EachSectionField(
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
            PronunciationText("")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
            EachSectionField(
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
private fun EikenNijiRecordScreenPreview() {
    ProEnglishScoreTrackerTheme {
        EikenNijiRecordScreen()
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
private fun CSEScoreInputField(modifier: Modifier) {
    var number by remember { mutableStateOf("") }
    OutlinedTextField(
        value = number,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                number = newValue
            }
        },
        label = { Text("3400") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Preview(showBackground = true)
@Composable
private fun CSEScoreInputFieldPreview() {
    ProEnglishScoreTrackerTheme {
        CSEScoreInputField(modifier = Modifier)
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
private fun SpeakingInputField(modifier: Modifier) {
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
private fun SpeakingInputFieldPreview() {
    ProEnglishScoreTrackerTheme {
        SpeakingInputField(modifier = Modifier)
    }
}

@Composable
private fun EachSectionField(modifier: Modifier) {
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
            label = { Text("10") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EachSectionFieldPreview() {
    ProEnglishScoreTrackerTheme {
        EachSectionField(modifier = Modifier)
    }
}

@Composable
fun ShortSpeechText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Short Speech",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ShortSpeechTextPreview() {
    ProEnglishScoreTrackerTheme {
        ShortSpeechText("Short Speech")
    }
}

@Composable
fun InteractionText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Interaction",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun InteractionTextPreview() {
    ProEnglishScoreTrackerTheme {
        InteractionText("Interaction")
    }
}

@Composable
fun GrammarAndVocabularyText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Grammar And Vocabulary",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GrammarAndVocabularyTextPreview() {
    ProEnglishScoreTrackerTheme {
        GrammarAndVocabularyText("Grammar And Vocabulary")
    }
}

@Composable
fun PronunciationText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Pronunciation",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PronunciationTextPreview() {
    ProEnglishScoreTrackerTheme {
        PronunciationText("Pronunciation")
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
