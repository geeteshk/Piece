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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.geeteshk.piece.R
import io.geeteshk.piece.databinding.ActivityGalleryBinding
import io.geeteshk.piece.model.RecyclerGridOptions
import io.geeteshk.piece.viewmodel.GalleryViewModel

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityGalleryBinding = DataBindingUtil.setContentView<ActivityGalleryBinding>(
            this, R.layout.activity_gallery)
        val viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        binding.viewModel = viewModel
        binding.options = RecyclerGridOptions(3, 4, true)
    }
}