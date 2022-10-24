package com.hafiztaruligani.movieday.data.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hafiztaruligani.movieday.domain.model.MovieDetail
import com.hafiztaruligani.movieday.util.convertIntoList
import com.hafiztaruligani.movieday.util.toStringDate
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

@Entity (tableName = "movie_detail")
data class MovieDetailEntity(
    @PrimaryKey (autoGenerate = false)
    val id: Int,
    val title: String,
    val releaseDate: String,
    val runtime: Int,
    val videos: String,
    val poster: String,
    val genresName: String,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int
){
    fun toMovieDetail() =
        MovieDetail(
            id = id,
            title = title,
            releaseDate = releaseDate.toStringDate(),
            runtime = getRuntimeFormatted(),
            videos = videos.convertIntoList(),
            poster = poster,
            genresName = genresName.convertIntoList(),
            overview = overview,
            voteAverage = getVoteAverageFormatted(),
            voteCount = getVoteCountFormatted(),
        )

    private fun getVoteCountFormatted(): String {
        return if(voteCount>0) voteCount.toString() else "-"
    }

    private fun getRuntimeFormatted(): String {
        return if(runtime>0) {
            "${runtime.div(60)}h ${runtime.mod(60)}m"
        }
        else "-"
    }

    private fun getVoteAverageFormatted(): String {
        return if (voteAverage>0.0)
            "${voteAverage.toBigDecimal().setScale(1, RoundingMode.CEILING)}/10"
        else "-"
    }



}