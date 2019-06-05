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
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.geeteshk.piece.R
import io.geeteshk.piece.adapter.GalleryAdapter
import io.geeteshk.piece.ui.GridSpacingItemDecoration
import io.geeteshk.piece.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity(), PermissionListener {

    private lateinit var viewModel: GalleryViewModel

    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)

        requestPermissions()
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        setupUi()
        setupImageList()
    }

    private fun setupUi() {
        val numColumns = 3
        val gridPadding = 4
        val includeEdge = true

        galleryAdapter = GalleryAdapter {
            val previewIntent = Intent(this, ImagePreviewActivity::class.java)
            previewIntent.putExtra(ImagePreviewActivity.EXTRA_PREVIEW_FILE, it)
            startActivity(previewIntent)
        }

        with (imageList) {
            layoutManager = GridLayoutManager(context, numColumns)
            addItemDecoration(GridSpacingItemDecoration(numColumns, gridPadding, includeEdge))
            imageList.itemAnimator = DefaultItemAnimator()
            adapter = galleryAdapter
        }
    }

    private fun setupImageList() {
        viewModel.getImages().observe(this, Observer {
            galleryAdapter.setImages(it)
        })
    }

    private fun requestPermissions() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(this)
            .check()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        requestPermissions()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        token?.continuePermissionRequest()
    }
}