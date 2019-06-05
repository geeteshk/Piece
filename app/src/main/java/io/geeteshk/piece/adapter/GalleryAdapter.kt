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

package io.geeteshk.piece.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.geeteshk.piece.R
import io.geeteshk.piece.model.GalleryImage
import kotlinx.android.synthetic.main.item_gallery_image.view.*
import java.io.File

class GalleryAdapter(private var imageFiles: List<GalleryImage> = ArrayList(), private val listener: (File) -> Unit)
    : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(image: GalleryImage) {
            Glide.with(view.context)
                .load(image.file)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view.imageView)

            view.imageView.setOnClickListener { listener.invoke(image.file) }
            view.filePath.text = image.file.path
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_gallery_image, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageFiles[position])
    }

    override fun getItemCount(): Int {
        return imageFiles.size
    }

    fun setImages(images: List<GalleryImage>) {
        imageFiles = images
        notifyDataSetChanged()
    }
}