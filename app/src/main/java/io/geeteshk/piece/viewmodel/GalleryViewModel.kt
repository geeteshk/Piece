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

package io.geeteshk.piece.viewmodel

import android.graphics.BitmapFactory
import android.os.Environment
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import io.geeteshk.piece.adapter.GalleryAdapter
import io.geeteshk.piece.model.GalleryImage
import io.geeteshk.piece.util.getActivityContext
import java.io.File

class GalleryViewModel : ViewModel() {

    private val images: MutableLiveData<List<GalleryImage>> by lazy {
        MutableLiveData<List<GalleryImage>>().also {
            loadImages()
        }
    }

    fun getImageFiles(): LiveData<List<GalleryImage>> {
        return images
    }

    /**
     * Asynchronously fetches images in storage
     */
    private fun loadImages() {
        val storageRoot = Environment.getExternalStorageDirectory()
        val imageFiles = ArrayList<GalleryImage>()

        // Do a recursive search for images
        findImages(storageRoot, imageFiles)

        // Sort image files by last modified property
        imageFiles.sortWith(compareByDescending {
            it.file.lastModified()
        })

        // Post our images to the LiveData to be displayed
        images.postValue(imageFiles)
    }

    /**
     * Recursively finds images given a starting directory
     */
    private fun findImages(currentDir: File, imageFiles: ArrayList<GalleryImage>) {
        val contents = currentDir.listFiles()
        contents.forEach {
            if (it.isDirectory) {
                findImages(it, imageFiles)
            } else if (it.isImage()) {
                imageFiles.add(
                    GalleryImage(it)
                )
            }
        }
    }

    /**
     * Checks if file is image with BitmapFactory
     */
    private fun File.isImage(): Boolean {
        // Setup options to only decode image metadata
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(path, options)

        // Dimensions will be -1 if file is not an image
        return options.outWidth != -1 && options.outHeight != -1
    }

    @BindingAdapter("images")
    fun setImageFiles(view: RecyclerView, galleryImages: LiveData<List<GalleryImage>>) {
        galleryImages.observe(view.getActivityContext()!!, Observer {
            if (view.adapter is GalleryAdapter) {
                (view.adapter as GalleryAdapter).setImages(it)
            } else {
                view.adapter = GalleryAdapter(it)
            }
        })
    }
}