/**
 * Defines local game-session records, their Room queries, and the MindSteps SQLite database.
 * Database changes must preserve saved sessions through explicit migrations.
 */
package org.mnemosyne.app.data

import androidx.room3.ColumnInfo
import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import android.content.Context

@Entity(
    tableName = "game_sessions",
    indices = [Index(value = ["game_id"]), Index(value = ["started_at_epoch_millis"])],
)
data class GameSessionEntity(
    @PrimaryKey
    @ColumnInfo(name = "session_id")
    val sessionId: String,
    @ColumnInfo(name = "game_id")
    val gameId: String,
    @ColumnInfo(name = "started_at_epoch_millis")
    val startedAtEpochMillis: Long,
    @ColumnInfo(name = "finished_at_epoch_millis")
    val finishedAtEpochMillis: Long? = null,
    @ColumnInfo(name = "duration_millis")
    val durationMillis: Long? = null,
    @ColumnInfo(name = "finish_status")
    val finishStatus: String? = null,
    @ColumnInfo(name = "tasks_attempted")
    val tasksAttempted: Int = 0,
    @ColumnInfo(name = "tasks_succeeded")
    val tasksSucceeded: Int = 0,
    @ColumnInfo(name = "tasks_missed")
    val tasksMissed: Int = 0,
)

data class GameUsageSummary(
    val gameId: String,
    val playCount: Int,
    val completedPlayCount: Int,
    val tasksAttempted: Int,
    val tasksSucceeded: Int,
    val tasksMissed: Int,
)

@Dao
interface GameSessionDao {
    @Insert
    suspend fun insertSession(session: GameSessionEntity)

    @Query(
        """
        UPDATE game_sessions
        SET tasks_attempted = tasks_attempted + 1,
            tasks_succeeded = tasks_succeeded + CASE WHEN :succeeded THEN 1 ELSE 0 END,
            tasks_missed = tasks_missed + CASE WHEN :succeeded THEN 0 ELSE 1 END
        WHERE session_id = :sessionId
        """,
    )
    suspend fun recordTask(sessionId: String, succeeded: Boolean)

    @Query(
        """
        UPDATE game_sessions
        SET finished_at_epoch_millis = :finishedAtEpochMillis,
            duration_millis = :durationMillis,
            finish_status = :finishStatus
        WHERE session_id = :sessionId AND finish_status IS NULL
        """,
    )
    suspend fun finishSession(
        sessionId: String,
        finishedAtEpochMillis: Long,
        durationMillis: Long,
        finishStatus: String,
    )

    @Query(
        """
        SELECT game_id AS gameId,
               COUNT(*) AS playCount,
               SUM(CASE WHEN finish_status = 'completed' THEN 1 ELSE 0 END) AS completedPlayCount,
               SUM(tasks_attempted) AS tasksAttempted,
               SUM(tasks_succeeded) AS tasksSucceeded,
               SUM(tasks_missed) AS tasksMissed
        FROM game_sessions
        GROUP BY game_id
        ORDER BY game_id
        """,
    )
    suspend fun getUsageSummary(): List<GameUsageSummary>

    @Query("SELECT * FROM game_sessions ORDER BY started_at_epoch_millis DESC")
    suspend fun getSessionsByDate(): List<GameSessionEntity>
}

@Database(entities = [GameSessionEntity::class], version = 1)
abstract class MnemosyneDatabase : RoomDatabase() {
    abstract fun gameSessionDao(): GameSessionDao

    companion object {
        @Volatile
        private var instance: MnemosyneDatabase? = null

        fun get(context: Context): MnemosyneDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder<MnemosyneDatabase>(
                    context.applicationContext,
                    "mnemosyne-local.db",
                )
                    .setDriver(AndroidSQLiteDriver())
                    .build()
                    .also { instance = it }
            }
    }
}
