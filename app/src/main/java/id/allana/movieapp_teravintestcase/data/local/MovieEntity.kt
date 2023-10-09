package id.allana.movieapp_teravintestcase.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "overview_movie")
    val overview: String,

    @ColumnInfo(name = "title_movie")
    val title: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,
)