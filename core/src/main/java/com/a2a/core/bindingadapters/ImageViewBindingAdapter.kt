package com.a2a.core.bindingadapters

  import android.widget.ImageView
import androidx.databinding.BindingAdapter
 import com.bumptech.glide.Glide

@BindingAdapter("setImage")
fun setImage(imageView: ImageView, url: String?) {
    if (url.isNullOrEmpty().not()){
        Glide.with(imageView.context).load(url).centerCrop()
             .into(imageView)
    }

}
