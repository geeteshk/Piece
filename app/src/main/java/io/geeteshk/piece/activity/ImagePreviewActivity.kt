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

import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import io.geeteshk.piece.R
import io.geeteshk.piece.extension.listen
import kotlinx.android.synthetic.main.activity_image_preview.*
import java.io.File

class ImagePreviewActivity : AppCompatActivity() {

    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        file = intent.getSerializableExtra(EXTRA_PREVIEW_FILE) as File

        setupUi()
        setPreviewImage()
    }

    private fun setupUi() {
        augmentImage.shrink(false)
        supportActionBar?.hide()
        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                augmentImage.extend()
            }

            override fun onTransitionCancel(transition: Transition?) {
                augmentImage.extend()
            }

            override fun onTransitionResume(transition: Transition?) {}
            override fun onTransitionPause(transition: Transition?) {}
            override fun onTransitionStart(transition: Transition?) {}
        })

        augmentImage.setOnClickListener {
            val augmentIntent = Intent(this, AugmentImageActivity::class.java)
            augmentIntent.putExtra(EXTRA_PREVIEW_FILE, file)
            startActivity(augmentIntent)
            finish()
        }

        imagePreview.setOnClickListener { supportFinishAfterTransition() }
    }

    private fun setPreviewImage() {
        supportPostponeEnterTransition()
        Glide.with(this)
            .load(file)
            .dontTransform()
            .dontAnimate()
            .listen({
                supportStartPostponedEnterTransition()
                false
            }, {
                supportStartPostponedEnterTransition()
                false
            })
            .placeholder(R.drawable.image_placeholder)
            .into(imagePreview)
    }

    companion object {
        const val EXTRA_PREVIEW_FILE = "io.geeteshk.piece.EXTRA_PREVIEW_FILE"
    }
}
