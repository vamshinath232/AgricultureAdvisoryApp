package s3605807.vamshinath.agricultureadvisoryapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plant: String,
    val disease: String,
    val confidence: Float,
    val imageUri: String,
    val date: String,
    val notes: String = ""
)

