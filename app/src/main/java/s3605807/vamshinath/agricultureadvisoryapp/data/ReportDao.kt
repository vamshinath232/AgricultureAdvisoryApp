package s3605807.vamshinath.agricultureadvisoryapp.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface ReportDao {

    @Insert
    suspend fun insertReport(report: Report)

    @Query("SELECT * FROM reports ORDER BY id DESC")
    suspend fun getAllReports(): List<Report>

    @Delete
    suspend fun deleteReport(report: Report)
}


@Database(entities = [Report::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}

object DatabaseProvider {
    private var db: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (db == null) {
            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "plant_reports_db"
            ).build()
        }
        return db!!
    }
}
