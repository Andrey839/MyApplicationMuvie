package com.myapp.myapplicationmuvie.utill

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.myapp.myapplicationmuvie.database.Films
import com.myapp.myapplicationmuvie.database.Poster

@BindingAdapter("textName")
fun TextView.setName(item: Films){
    let {
        text = item.name
    }
}

@BindingAdapter("textYear")
fun TextView.setYear(item: Films){
    let {
        text = item.year.toString()
    }
}

@BindingAdapter("imagePoster")
fun ImageView.setPoster(item: Poster){
    let {
        Glide.with(it.context).load(item.poster).circleCrop().into(it)
    }
}