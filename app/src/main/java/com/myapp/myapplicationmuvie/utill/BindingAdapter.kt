package com.myapp.myapplicationmuvie.utill

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.myapplicationmuvie.R
import com.myapp.myapplicationmuvie.database.Model

@BindingAdapter("textName")
fun TextView.setNames(item: String) {
    let {
        text = item
    }
}

@BindingAdapter("textYear")
fun TextView.setYear(item: String) {
    let {
        text = item
    }
}

@BindingAdapter("imagePoster")
fun ImageView.setPoster(item: String) {
    let {
        Glide.with(it.context).load(item).centerCrop().into(it)
    }
}

@BindingAdapter("setPosterDetail")
fun ImageView.posterFilm(item: Model) {
    let {
        Glide.with(it.context).load(item.poster).error(R.drawable.ic_baseline_broken_image)
            .centerCrop().into(it)
    }
}

@BindingAdapter("setDescriptionDetail")
fun TextView.descriptionFilm(item: Model) {
    let {
        text = item.description
    }
}

@BindingAdapter("setRatingDetail")
fun TextView.ratingFilm(item: Model) {
    val string = item.ratings
    let {
        text =  "${context.getString(R.string.rating)} $string"
    }
}

@BindingAdapter("setBackground")
fun FloatingActionButton.background(item: Any?) {
    let {
        if (item == null) it.setImageDrawable(resources.getDrawable(R.drawable.plus))
        else it.setImageDrawable(resources.getDrawable(R.drawable.minus))
    }
}