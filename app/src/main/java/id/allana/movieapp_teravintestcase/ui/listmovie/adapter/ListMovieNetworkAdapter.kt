package id.allana.movieapp_teravintestcase.ui.listmovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.data.network.model.Movie
import id.allana.movieapp_teravintestcase.databinding.ItemMovieBinding

class ListMovieNetworkAdapter: RecyclerView.Adapter<ListMovieNetworkAdapter.ListMovieViewHolder>() {

    private var itemsNetwork: MutableList<Movie> = mutableListOf()

    fun setItems(data: List<Movie>) {
        itemsNetwork.clear()
        itemsNetwork.addAll(data)
    }

    class ListMovieViewHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Movie) {
            binding.apply {
                with(item) {
                    tvMovieTitle.text = title
                    tvMovieOverview.text = overview
                    tvMovieDateRelease.text = releaseDate
                    tvMovieOverview.text = overview
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListMovieViewHolder(binding)
    }

    override fun getItemCount(): Int = itemsNetwork.size

    override fun onBindViewHolder(holder: ListMovieViewHolder, position: Int) {
        holder.bindView(itemsNetwork[position])
    }
}