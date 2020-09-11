package com.myapp.myapplicationmuvie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.database.Favorite
import com.myapp.myapplicationmuvie.databinding.ListItemFavoriteBinding

class AdapterFavoriteFilm(private val listFavorites: ArrayList<Favorite>, private val callBack: ListenerCallback): RecyclerView.Adapter<FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val dataBinding: ListItemFavoriteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.list_item_favorite, parent, false)
        return FavoriteViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.dataBinding.also {
            it.viewModel = listFavorites[position]
            it.callback = callBack
        }
    }

    override fun getItemCount() = listFavorites.size

    fun setData(newFavorite: ArrayList<Favorite>) {
        val diffCallback = FavoriteDiff(newFavorite, listFavorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listFavorites.clear()
        listFavorites.addAll(newFavorite)
        diffResult.dispatchUpdatesTo(this)
    }
}

class ListenerCallback(val block: (Favorite) -> Unit) {
    fun onClick(film: Favorite) = block(film)

}

class FavoriteViewHolder(var dataBinding: ListItemFavoriteBinding): RecyclerView.ViewHolder(dataBinding.root) {
}

class FavoriteDiff(private val newList: ArrayList<Favorite>, private val oldList: ArrayList<Favorite>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}

