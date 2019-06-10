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
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import io.geeteshk.piece.R
import io.geeteshk.piece.extension.listen
import kotlinx.android.synthetic.main.activity_augment_image.*
import timber.log.Timber
import java.io.File

class AugmentImageActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private lateinit var imageRenderable: ViewRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_augment_image)

        supportActionBar?.hide()
        arFragment = augmentFragment as ArFragment

        val view = View.inflate(this, R.layout.renderable_image_view, null) as ImageView
        Glide.with(this)
            .load(intent.getSerializableExtra(ImagePreviewActivity.EXTRA_PREVIEW_FILE) as File)
            .dontTransform()
            .dontAnimate()
            .listen({
                runOnUiThread {
                    view.setImageDrawable(it)
                    setupArFragment(view)
                }

                true
            }, { true })
            .submit()
    }

    private fun setupArFragment(view: ImageView) {
        ViewRenderable.builder()
            .setView(this, view)
            .build()
            .thenAccept { imageRenderable = it }
            .exceptionally {
                Timber.d(it)
                // TODO: Report the failure
                null
            }

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (!::imageRenderable.isInitialized) {
                return@setOnTapArPlaneListener
            }



            // Create AR anchor
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            // Create the transformable node and add it to the anchor
            val piece = TransformableNode(arFragment.transformationSystem)
            piece.setParent(anchorNode)
            piece.renderable = imageRenderable
            piece.select()
        }
    }
}
