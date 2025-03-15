package com.example.proenglishscoretracker.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
        YouTubersItem(
            R.drawable.youtuber_atsu,
            "Atsu",

            "必ず登録すべき情報発信者。純ジャパ最強。\n" +
                    "英検一級, TOEIC満点, TOEFL/IELTSほぼ満点。 \n",

            "https://www.youtube.com/@atsueigo"
        ),

        YouTubersItem(
            R.drawable.youtuber_osaru,
            "イングリッシュおさる",

            "勉強法のアンチパターンを積極的に紹介している。 \n" +
                    "遠回りしないために登録すべき。 \n",

            "https://www.youtube.com/@englishosaru/videos"
        ),

        YouTubersItem(
            R.drawable.youtuber_moritetsu,
            "Morite2 English Channel",

            "TOEICや様々な大学の英語試験の解説をしている。 \n" +
                    "受験生は登録すべき。 \n",

            "https://www.youtube.com/@Morite2Channel/videos"
        ),

        YouTubersItem(
            R.drawable.youtuber_takeda_juku,
            "大人のための武田塾English",

            "TOEFLやIELTSの学習ルートを紹介している。 \n" +
                    "留学をしたい人はは登録すべき。 \n",

            "https://www.youtube.com/@english3963"
        ),

        YouTubersItem(
            R.drawable.youtuber_daijiro,
            "だいじろー Daijiro",

            "イギリス英語の発音の紹介が多めのYouTuber。 \n" +
                    "イギリスが好きな人は登録すべき。 \n",

            "https://www.youtube.com/@daijirojp/videos"
        ),

        YouTubersItem(
            R.drawable.youtuber_koala,
            "こあらの学校　英語の時間",

            "「shopとstoreの違い」といったように \n" +
                    "違いを解説している動画が多い。 \n",

            "https://www.youtube.com/@KoalaEnglish180/videos"
        ),

        YouTubersItem(
            R.drawable.youtuber_kazu,
            "Kazu Languages Shorts\n",

            "マルチリンガル。 \n" +
                    "英語以外に10ケ国語以上も話せる超人。 \n",

            "https://www.youtube.com/@KazuLanguagesShorts/videos"
        ),

        )
    val context = LocalContext.current

    LazyColumn {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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
                        Image(
                            painter = painterResource(id = R.drawable.youtube_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { openYoutubeLink(context, item.url) }
                        )
                    }
                    Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                        Text(text = item.description)
                    }
                }
            }
        }
    }
}

data class YouTubersItem(
    @DrawableRes val iconRes: Int,
    val name: String,
    val description: String,
    val url: String
)

private fun openYoutubeLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}
