package com.myapp.myapplicationmuvie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.myapplicationmuvie.database.FilmWithPoster
import com.myapp.myapplicationmuvie.database.Films
import com.myapp.myapplicationmuvie.repository.Repository
import com.myapp.myapplicationmuvie.databinding.ItemListBinding

class AdapterFilmsNow(val filmListener: FilmClickListener): ListAdapter<Repository,AdapterFilmsNow.MyViewHolder>(FilmDiffCallback()) {

    class MyViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            film: Films?,
            poster: FilmWithPoster?,
            filmListener: FilmClickListener
        ) {
            binding.filmInfo = film
            binding.filmPoster = poster
            binding.click = filmListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterFilmsNow.MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterFilmsNow.MyViewHolder, position: Int) {

        val film = getItem(position).listFilm.value?.get(position)
        val poster = getItem(position).listPoster.value?.get(position)

        return holder.bind(film, poster, filmListener)
    }
}

class FilmClickListener(val clickListener: (id: Int) -> Unit) {
    fun onClick(films: Films) = clickListener(films.id)

}

class FilmDiffCallback: DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean{
        return oldItem.listFilm.value!![0].name == newItem.listFilm.value!![0].name

    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.listFilm.value == newItem.listFilm.value
    }
}
