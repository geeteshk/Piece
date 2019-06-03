/*
 * Copyright 2019 Geetesh Kalakoti <kalakotig@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geeteshk.piece.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.geeteshk.piece.R
import io.geeteshk.piece.adapter.GalleryAdapter
import io.geeteshk.piece.model.GalleryImage
import io.geeteshk.piece.model.RecyclerGridOptions
import io.geeteshk.piece.ui.GridSpacingItemDecoration
import java.io.File

@BindingAdapter("srcFile")
fun ImageView.loadImageFile(imageFile: File) {
    Glide.with(context)
        .load(imageFile)
        .centerCrop()
        .placeholder(R.drawable.image_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

@BindingAdapter("gridOptions")
fun RecyclerView.setupRecyclerGrid(props: RecyclerGridOptions) {
    layoutManager = GridLayoutManager(context, props.columns)
    addItemDecoration(GridSpacingItemDecoration(props.columns, props.padding, props.includeEdge))
}

@BindingAdapter("images")
fun RecyclerView.setImageFiles(galleryImages: LiveData<List<GalleryImage>>) {
    galleryImages.observe(getActivityContext()!!, Observer {
        if (adapter is GalleryAdapter) {
            (adapter as GalleryAdapter).setImages(it)
        } else {
            adapter = GalleryAdapter(it)
        }
    })
}