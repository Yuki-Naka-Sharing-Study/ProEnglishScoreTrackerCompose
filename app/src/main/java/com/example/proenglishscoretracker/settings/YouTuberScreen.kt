package com.example.proenglishscoretracker.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proenglishscoretracker.R

@Composable
fun YoutuberScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("登録推奨YouTuber")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                backgroundColor = Color.Blue,
                contentColor = Color.White,
            )
        },
    ) { innerPadding ->
        YoutuberContentScreen(innerPadding)
    }
}

@Composable
fun YoutuberContentScreen(
    innerPadding: PaddingValues
) {
    val items = listOf(
        ExpandableItem(
            R.drawable.youtuber_atsu,
            "Atsu",

            "必ず登録すべき情報発信者。\n" +
                    "純ジャパでありながらも \n" +
                    "以下の英語資格を保有している。 \n" +
                    "間違い無く純ジャパ最強。 \n" +
                    "ーーーーーーーーーーーーーーーーーーーーーーーー \n" +
                    "IELTS Academic 8.5 (L9.0 R9.0 W7.5 S8.0) \n" +
                    "実用英語技能検定１級 \n" +
                    "TOEIC 990点（満点） (L495 R495) \n" +
                    "TOEFL iBT 114点(L30 R28 S27 W29) \n" +
                    "Versant 80点満点"),

        ExpandableItem(
            R.drawable.youtuber_osaru,
            "英語コーチ-イングリッシュおさる",

            "勉強法のアンチパターンを積極的に紹介している。 \n" +
                    "遠回りしないために登録すべき。 \n"),
    )

    val expandedState = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn {
        items(items) { item ->
            val isExpanded = expandedState[item.name] ?: false

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        expandedState[item.name] = !isExpanded
                    }
                    .background(
                        Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector =
                        if (isExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse"
                    )
                }
                if (isExpanded) {
                    Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                        Text(
                            text = item.description
                        )
                    }
                }
            }
        }
    }
}

data class ExpandableItem(
    @DrawableRes val iconRes: Int,
    val name: String,
    val description: String
)