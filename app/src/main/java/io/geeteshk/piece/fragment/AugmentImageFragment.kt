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

package io.geeteshk.piece.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import timber.log.Timber

class AugmentImageFragment : ArFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // Hide plane discovery
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false

        return view
    }

    override fun getSessionConfiguration(session: Session): Config {
        val config = super.getSessionConfiguration(session)
        if (initAugmentedImageDb(config, session)) {
            Snackbar.make(activity?.findViewById(android.R.id.content)!!,
                "Unable to initialize augmented image database.",
                Snackbar.LENGTH_LONG).show()
        }

        return config
    }

    private fun initAugmentedImageDb(config: Config, session: Session): Boolean {
        with (context?.assets?.open(AUGMENTED_IMAGE_DB)) {
            return if (this != null) {
                config.augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, this)
                true
            } else {
                Timber.e("IOException while loading image database.")
                false
            }
        }
    }

    companion object {
        private const val AUGMENTED_IMAGE_NAME = "thrbff.jpg"
        private const val AUGMENTED_IMAGE_DB = "augmented_thrbff.imgdb"
    }
}