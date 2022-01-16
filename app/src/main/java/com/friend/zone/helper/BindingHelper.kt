package com.friend.zone.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.friend.zone.BuildConfig

class BindingHelper {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imageUrl", "imageCircle"], requireAll = false)
        fun bindLoadImageUrl(imageView: ImageView, imageUrl: String?, imageCircle: Boolean?) {
            imageUrl?.let {
                imageView.load("${BuildConfig.BASE_URL}$imageUrl") {
                    if (imageCircle == true) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }
    }
}