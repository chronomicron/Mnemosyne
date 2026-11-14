/**
 * Queues local-only game-session and task-result writes in the order the player generated them.
 * Game screens call startSession, recordTask, and finishSession to persist activity history.
 */
package org.mnemosyne.app.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID

object GameStatsLogger {
    private val writeScope = CoroutineScope(SupervisorJob() + Dispatchers.IO.limitedParallelism(1))

    fun startSession(context: Context, gameId: String): String {
        val sessionId = UUID.randomUUID().toString()
        val dao = MnemosyneDatabase.get(context).gameSessionDao()
        writeScope.launch {
            dao.insertSession(
                GameSessionEntity(
                    sessionId = sessionId,
                    gameId = gameId,
                    startedAtEpochMillis = System.currentTimeMillis(),
                ),
            )
        }
        return sessionId
    }

    fun recordTask(context: Context, sessionId: String, succeeded: Boolean) {
        val dao = MnemosyneDatabase.get(context).gameSessionDao()
        writeScope.launch { dao.recordTask(sessionId, succeeded) }
    }

    fun finishSession(context: Context, sessionId: String, durationMillis: Long, status: String) {
        val dao = MnemosyneDatabase.get(context).gameSessionDao()
        writeScope.launch {
            dao.finishSession(
                sessionId = sessionId,
                finishedAtEpochMillis = System.currentTimeMillis(),
                durationMillis = durationMillis.coerceAtLeast(0L),
                finishStatus = status,
            )
        }
    }
}
