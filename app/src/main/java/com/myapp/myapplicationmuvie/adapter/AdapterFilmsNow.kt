package com.myapp.myapplicationmuvie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapp.myapplicationmuvie.R

class AdapterFilmsNow(private val filmListener: (com.myapp.myapplicationmuvie.database.Model) -> Unit) :
    ListAdapter<com.myapp.myapplicationmuvie.database.Model, AdapterFilmsNow.MyViewHolder>(FilmDiffCallback()) {

    class MyViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val year: TextView? = view.findViewById(R.id.yearFilmView)
        val name: TextView? = view.findViewById(R.id.nameFilmView)
        private val posterView: ImageView? = view.findViewById(R.id.logoFilmView)

        fun bind(
            model: com.myapp.myapplicationmuvie.database.Model
        ) {
            year?.text = model.year
            name?.text = model.name
            Glide.with(posterView!!.context).load(model.poster ).error(R.drawable.ic_baseline_broken_image).centerCrop().into(posterView)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ilist_item_home, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val model = getItem(position)
        holder.itemView.setOnClickListener { filmListener(model) }

        return holder.bind(model)
    }
}

class FilmDiffCallback : DiffUtil.ItemCallback<com.myapp.myapplicationmuvie.database.Model>() {
    override fun areItemsTheSame(oldItem: com.myapp.myapplicationmuvie.database.Model, newItem: com.myapp.myapplicationmuvie.database.Model): Boolean {
        return oldItem.name == newItem.name && oldItem.description == newItem.description
                && oldItem.id == oldItem.id

    }

    override fun areContentsTheSame(oldItem: com.myapp.myapplicationmuvie.database.Model, newItem: com.myapp.myapplicationmuvie.database.Model): Boolean {
        return oldItem != newItem
    }
}
