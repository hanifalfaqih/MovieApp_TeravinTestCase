package id.allana.movieapp_teravintestcase.ui.listmovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.allana.movieapp_teravintestcase.data.local.MovieEntity
import id.allana.movieapp_teravintestcase.databinding.ItemMovieBinding

class ListMovieLocalAdapter: RecyclerView.Adapter<ListMovieLocalAdapter.ListMovieLocalViewHolder>() {

    private var itemsLocal: MutableList<MovieEntity> = mutableListOf()
    fun setItemsFromLocal(data: List<MovieEntity>) {
        itemsLocal.clear()
        itemsLocal.addAll(data)
    }

    class ListMovieLocalViewHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: MovieEntity) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMovieLocalViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListMovieLocalViewHolder(binding)
    }

    override fun getItemCount(): Int = itemsLocal.size

    override fun onBindViewHolder(holder: ListMovieLocalViewHolder, position: Int) {
        holder.bindView(itemsLocal[position])
    }
}