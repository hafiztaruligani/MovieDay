package com.hafiztaruligani.movieday.data.remote.dto.movie

import com.google.gson.annotations.SerializedName
import com.hafiztaruligani.movieday.BuildConfig
import com.hafiztaruligani.movieday.data.local.entity.GenreMovieCrossRef
import com.hafiztaruligani.movieday.data.local.entity.MovieGenreEntity
import com.hafiztaruligani.movieday.data.local.entity.MovieSimpleEntity
import com.hafiztaruligani.movieday.data.local.entity.pagingentity.MoviePagingEntity
import com.hafiztaruligani.movieday.domain.model.MovieSimple

data class MovieSimpleResponse(

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("original_language")
	val originalLanguage: String? = null,

	@field:SerializedName("original_title")
	val originalTitle: String? = null,

	@field:SerializedName("video")
	val video: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("genre_ids")
	val genreIds: List<Int>,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("popularity")
	val popularity: Double? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null
){

	fun toMovieSimpleEntity(genreData: List<MovieGenreEntity>): MovieSimpleEntity{
		return MovieSimpleEntity(
			movieId = id?:-1,
			title = title?:"-",
			releaseDate = releaseDate?:"-",
			voteAverage = voteAverage?:0.0,
			poster = posterPath?.let { BuildConfig.IMAGE_URL+it }?:"-",
			genresName = genreData.filter { genreIds.contains(it.genreId) }.map { it.name }.toString() // Make a list of genres name and convert into string
		)
	}

	fun toGenreMovieCrossRef(): List<GenreMovieCrossRef>{
		return genreIds.map {
			GenreMovieCrossRef(
				genreId = it,
				movieId = id?:-1
			)
		}
	}

	fun toMoviePagingEntity(genreData: List<MovieGenreEntity>): MoviePagingEntity{
		return MoviePagingEntity(
			movieId = id?:-1,
			title = title?:"-",
			releaseDate = releaseDate?:"-",
			voteAverage = voteAverage?:0.0,
			poster = posterPath?.let { BuildConfig.IMAGE_URL+it }?:"-",
			genresName = genreData.filter { genreIds.contains(it.genreId) }.map { it.name }.toString() // Make a list of genres name and convert into string
		)
	}


}