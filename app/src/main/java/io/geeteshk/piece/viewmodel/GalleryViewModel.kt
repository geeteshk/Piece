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

import android.app.Application
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.geeteshk.piece.model.GalleryImage
import java.io.File

class GalleryViewModel(private val app: Application) : AndroidViewModel(app) {

    private val images = MutableLiveData<List<GalleryImage>>()

    init {
        loadImages()
    }

    fun getImages(): LiveData<List<GalleryImage>> {
        return images
    }

    /**
     * Asynchronously fetches images in storage
     */
    private fun loadImages() {
        Thread {
            val imageFiles = ArrayList<GalleryImage>()
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor = app.contentResolver.query(contentUri, projection, null, null, null)

            // Probe for images using a ContentResolver query
            cursor?.let {
                // Start from the end and go backwards to show correct order
                it.moveToLast()

                do {
                    val file = File(it.getString(0))
                    if (file.isImage()) {
                        imageFiles.add(GalleryImage(file))
                    }
                } while (it.moveToPrevious())

                cursor.close()
            }

            images.postValue(imageFiles)
        }.start()
    }

    /**
     * Checks if file is image with BitmapFactory
     */
    private fun File.isImage(): Boolean {
        if (!exists() || isDirectory) return false

        // Setup options to only decode image metadata
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(path, options)

        // Dimensions will be -1 if file is not an image
        return options.outWidth != -1 && options.outHeight != -1
    }
}