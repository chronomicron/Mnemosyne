/**
 * Hosts the MindSteps Compose app and its home, matching-pairs, and pattern-game screens.
 * Game interactions report session outcomes to the local statistics logger.
 */
package org.mnemosyne.app

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.mnemosyne.app.data.GameStatsLogger

private val Ink = Color(0xFF24343A)
private val Teal = Color(0xFF286C68)
private val Cream = Color(0xFFFFFBF4)
private val SoftBlue = Color(0xFFE9F1F2)
private val SoftGold = Color(0xFFF5E8C9)
private val SoftGreen = Color(0xFFE1EEE4)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Cream, contentColor = Ink) {
                    MnemosyneApp()
                }
            }
        }
    }
}

private enum class Screen { Home, Match, Pattern }

@Composable
private fun MnemosyneApp() {
    var screen by remember { mutableStateOf(Screen.Home) }
    when (screen) {
        Screen.Home -> HomeScreen(
            onMatch = { screen = Screen.Match },
            onPattern = { screen = Screen.Pattern }
        )
        Screen.Match -> MatchGame(onBack = { screen = Screen.Home })
        Screen.Pattern -> PatternGame(onBack = { screen = Screen.Home })
    }
}

@Composable
private fun HomeScreen(onMatch: () -> Unit, onPattern: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mnemosyne", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("A little time together", style = MaterialTheme.typography.titleLarge, color = Teal)
        Spacer(Modifier.height(20.dp))
        Text("Choose something to do. There is no score and no rush.", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(28.dp))
        GameChoice(
            title = "Find the matching pairs",
            description = "Turn over cards and find two that look alike.",
            symbol = "☀️",
            onClick = onMatch
        )
        Spacer(Modifier.height(16.dp))
        GameChoice(
            title = "Continue the pattern",
            description = "Choose what you think comes next.",
            symbol = "🌿",
            onClick = onPattern
        )
        Spacer(Modifier.height(24.dp))
        Text("You can stop or take a break whenever you like.", style = MaterialTheme.typography.bodyMedium, color = Teal)
    }
}

@Composable
private fun GameChoice(title: String, description: String, symbol: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(116.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SoftBlue, contentColor = Ink)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            Text(symbol, fontSize = 36.sp)
            Column {
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(description, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Start)
            }
        }
    }
}

private data class MemoryCard(val id: Int, val face: String)

@Composable
private fun MatchGame(onBack: () -> Unit) {
    val faces = listOf("🌼", "🐦", "🍎")
    var round by remember { mutableIntStateOf(0) }
    val appContext = LocalContext.current.applicationContext
    val sessionId = remember(round) { GameStatsLogger.startSession(appContext, "matching_pairs") }
    val sessionStartedAt = remember(round) { SystemClock.elapsedRealtime() }
    val cards = remember(round) { (faces + faces).shuffled().mapIndexed { i, face -> MemoryCard(i, face) } }
    val open = remember(round) { mutableStateListOf<Int>() }
    val matched = remember(round) { mutableStateListOf<Int>() }
    var locked by remember(round) { mutableStateOf(false) }
    val complete = matched.size == cards.size
    val latestComplete = rememberUpdatedState(complete)

    DisposableEffect(sessionId) {
        onDispose {
            GameStatsLogger.finishSession(
                appContext,
                sessionId,
                SystemClock.elapsedRealtime() - sessionStartedAt,
                if (latestComplete.value) "completed" else "stopped",
            )
        }
    }

    LaunchedEffect(complete) {
        if (complete) {
            GameStatsLogger.finishSession(
                appContext,
                sessionId,
                SystemClock.elapsedRealtime() - sessionStartedAt,
                "completed",
            )
        }
    }

    LaunchedEffect(open.toList()) {
        if (open.size == 2 && !locked) {
            val first = cards[open[0]]
            val second = cards[open[1]]
            if (first.face == second.face) {
                GameStatsLogger.recordTask(appContext, sessionId, succeeded = true)
                matched.add(first.id)
                matched.add(second.id)
                open.clear()
            } else {
                GameStatsLogger.recordTask(appContext, sessionId, succeeded = false)
                locked = true
                delay(1100)
                open.clear()
                locked = false
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        GameHeader(
            title = "Matching pairs",
            onBack = {
                GameStatsLogger.finishSession(
                    appContext,
                    sessionId,
                    SystemClock.elapsedRealtime() - sessionStartedAt,
                    if (complete) "completed" else "stopped",
                )
                onBack()
            },
        )
        Text("Find two of the same picture. Take your time.", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(cards, key = { _, card -> card.id }) { _, card ->
                val isVisible = card.id in open || card.id in matched
                Button(
                    onClick = {
                        if (!locked && !isVisible && open.size < 2) open.add(card.id)
                    },
                    enabled = !locked && !isVisible && open.size < 2,
                    modifier = Modifier.fillMaxWidth().height(96.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVisible) SoftGreen else SoftGold,
                        disabledContainerColor = if (isVisible) SoftGreen else SoftGold,
                        contentColor = Ink,
                        disabledContentColor = Ink
                    )
                ) {
                    Text(if (isVisible) card.face else "•", fontSize = if (isVisible) 36.sp else 42.sp)
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            if (complete) "Lovely. You found them all."
            else if (matched.isNotEmpty()) "Nice matching! Keep going, or take a break."
            else "Turn over two cards and see what you find.",
            style = MaterialTheme.typography.titleMedium,
            color = Teal
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                GameStatsLogger.finishSession(
                    appContext,
                    sessionId,
                    SystemClock.elapsedRealtime() - sessionStartedAt,
                    if (complete) "completed" else "stopped",
                )
                round++
            },
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(if (complete) "Play again" else "Start over")
        }
    }
}

private data class PatternRound(val sequence: List<String>, val choices: List<String>, val answer: String)

private val patterns = listOf(
    PatternRound(listOf("🌼", "🍃", "🌼"), listOf("🍃", "🌼", "🐦"), "🍃"),
    PatternRound(listOf("☀️", "🌙", "☀️"), listOf("🌙", "☀️", "⭐"), "🌙"),
    PatternRound(listOf("🍎", "🍐", "🍎"), listOf("🍐", "🍎", "🍊"), "🍐")
)

@Composable
private fun PatternGame(onBack: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    val appContext = LocalContext.current.applicationContext
    val sessionId = remember { GameStatsLogger.startSession(appContext, "continue_pattern") }
    val sessionStartedAt = remember { SystemClock.elapsedRealtime() }
    var chosen by remember(round) { mutableStateOf<String?>(null) }
    val puzzle = patterns[round % patterns.size]

    DisposableEffect(sessionId) {
        onDispose {
            GameStatsLogger.finishSession(
                appContext,
                sessionId,
                SystemClock.elapsedRealtime() - sessionStartedAt,
                "stopped",
            )
        }
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        GameHeader(
            title = "Continue the pattern",
            onBack = {
                GameStatsLogger.finishSession(
                    appContext,
                    sessionId,
                    SystemClock.elapsedRealtime() - sessionStartedAt,
                    "stopped",
                )
                onBack()
            },
        )
        Text("Look at the pictures. What might come next?", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(36.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            puzzle.sequence.forEach { Text(it, fontSize = 44.sp, modifier = Modifier.padding(8.dp)) }
            Text("…", fontSize = 36.sp, color = Teal)
        }
        Spacer(Modifier.height(32.dp))
        puzzle.choices.forEach { choice ->
            Button(
                onClick = {
                    if (chosen == null) {
                        chosen = choice
                        GameStatsLogger.recordTask(appContext, sessionId, choice == puzzle.answer)
                    }
                },
                enabled = chosen == null,
                modifier = Modifier.fillMaxWidth().height(72.dp).padding(bottom = 8.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (chosen == choice) SoftGreen else SoftBlue,
                    contentColor = Ink
                )
            ) { Text(choice, fontSize = 34.sp) }
        }
        Spacer(Modifier.height(16.dp))
        if (chosen != null) {
            Text(
                if (chosen == puzzle.answer) "That fits nicely!" else "Thanks for choosing. Want to try another?",
                style = MaterialTheme.typography.titleMedium,
                color = Teal
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { round++; chosen = null }) { Text("Another pattern") }
        }
    }
}

@Composable
private fun GameHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextButton(onClick = onBack) { Text("‹  Games", fontSize = 18.sp) }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}
