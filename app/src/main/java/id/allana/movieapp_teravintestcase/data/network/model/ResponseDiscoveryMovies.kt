package id.allana.movieapp_teravintestcase.data.network.model

import com.google.gson.annotations.SerializedName

data class ResponseDiscoveryMovies(

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("results")
	val results: List<Movie>,

	@field:SerializedName("total_results")
	val totalResults: Int
)

data class Movie(

	@field:SerializedName("overview")
	val overview: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("poster_path")
	val posterPath: String,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("id")
	val id: Int,
)
