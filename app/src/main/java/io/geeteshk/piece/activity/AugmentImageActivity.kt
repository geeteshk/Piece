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
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.ux.ArFragment
import io.geeteshk.piece.R
import io.geeteshk.piece.extension.listen
import io.geeteshk.piece.model.PieceNode
import kotlinx.android.synthetic.main.activity_augment_image.*
import java.io.File

class AugmentImageActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment

    private val augmentedImageMap = HashMap<AugmentedImage, PieceNode>()

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

    override fun onResume() {
        super.onResume()
        if (augmentedImageMap.isEmpty()) {
            fitToScan.visibility = View.VISIBLE
        }
    }

    private fun setupArFragment(view: ImageView) {
        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            val frame = arFragment.arSceneView.arFrame
            if (frame == null || frame.camera.trackingState != TrackingState.TRACKING) return@addOnUpdateListener

            val updatedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)
            updatedImages.forEach {
                when (it.trackingState) {
                    TrackingState.PAUSED -> {
                        Snackbar.make(findViewById(android.R.id.content),
                            "Detected Image " + it.index,
                            Snackbar.LENGTH_SHORT).show()
                    }

                    TrackingState.TRACKING -> {
                        fitToScan.visibility = View.GONE
                        if (!augmentedImageMap.containsKey(it)) {
                            val node = PieceNode(view)
                            node.setImage(it)
                            augmentedImageMap[it] = node
                            arFragment.arSceneView.scene.addChild(node)
                        }
                    }

                    TrackingState.STOPPED, null -> augmentedImageMap.remove(it)
                }
            }
        }
    }
}
