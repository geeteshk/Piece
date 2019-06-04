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

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.geeteshk.piece.R
import io.geeteshk.piece.adapter.GalleryAdapter
import io.geeteshk.piece.databinding.ActivityGalleryBinding
import io.geeteshk.piece.model.RecyclerGridOptions
import io.geeteshk.piece.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity(), PermissionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()
        setupBinding()
    }

    private fun setupBinding() {
        val binding: ActivityGalleryBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_gallery)

        val viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        viewModel.getImageFiles().observe(this, Observer {
            if (imageList.adapter is GalleryAdapter) {
                (imageList.adapter as GalleryAdapter).setImages(it)
            } else {
                imageList.adapter = GalleryAdapter(it)
            }
        })

        binding.options = RecyclerGridOptions(3, 4, true)
    }

    private fun requestPermissions() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(this)
            .check()
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        setupBinding()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        requestPermissions()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        token?.continuePermissionRequest()
    }
}