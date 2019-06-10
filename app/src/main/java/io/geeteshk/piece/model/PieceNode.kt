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

package io.geeteshk.piece.model

import android.view.View
import com.google.ar.core.AugmentedImage
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import timber.log.Timber
import java.util.concurrent.CompletableFuture

class PieceNode(view: View) : AnchorNode() {

    lateinit var image: AugmentedImage
        private set

    init {
        realImage = ViewRenderable.builder()
            .setView(view.context, view)
            .build()
    }

    private fun setImage(image: AugmentedImage) {
        this.image = image

        if (!realImage.isDone) {
            CompletableFuture.allOf(realImage)
                .thenAccept { setImage(image) }
                .exceptionally {
                    Timber.e(it)
                    null
                }
        }

        anchor = image.createAnchor(image.centerPose)

        val localPosition = Vector3(image.extentX, 0f, image.extentZ)
        val node = Node()
        node.setParent(this)
        node.localPosition = localPosition
        node.renderable = realImage.getNow(null)
    }

    companion object {
        private lateinit var realImage: CompletableFuture<ViewRenderable>
    }
}