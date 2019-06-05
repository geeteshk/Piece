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

package io.geeteshk.piece.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.geeteshk.piece.R
import kotlinx.android.synthetic.main.activity_image_preview.*
import java.io.File

class ImagePreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        setupUi()
        setPreviewImage()
    }

    private fun setupUi() {
        supportActionBar?.hide()
    }

    private fun setPreviewImage() {
        Glide.with(this)
            .load(intent.getSerializableExtra(EXTRA_PREVIEW_FILE) as File)
            .fitCenter()
            .placeholder(R.drawable.image_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imagePreview)
    }

    companion object {
        const val EXTRA_PREVIEW_FILE = "io.geeteshk.piece.EXTRA_PREVIEW_FILE"
    }
}
