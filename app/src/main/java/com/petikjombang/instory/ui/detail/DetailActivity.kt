package com.petikjombang.instory.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.petikjombang.instory.R
import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.databinding.ActivityDetailBinding
import com.petikjombang.instory.ui.ViewModelFactory
import com.petikjombang.instory.ui.list.StoryViewModels

class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private val storyViewModels: StoryViewModels by viewModels {
        ViewModelFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        id = intent.getStringExtra(ID_USER).toString()

        supportActionBar?.title = "Detail Story"
        supportActionBar?.elevation = 0f
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        storyViewModels.getDetailStory(id).observe(this){ story ->
            if (story != null) {
                when(story) {
                    is com.petikjombang.instory.data.Result.Loading -> {
                        detailBinding.detailProgressBar.visibility = View.VISIBLE
                    }

                    is com.petikjombang.instory.data.Result.Success -> {
                        detailBinding.detailProgressBar.visibility = View.GONE
                        val data = story.data
                        setDetailStory(data)
                    }

                    is com.petikjombang.instory.data.Result.Error -> {
                        detailBinding.detailProgressBar.visibility = View.GONE
                        Toast.makeText(this, "Gagal Mendapatkan Data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setDetailStory(data: StoryEntity) {
        detailBinding.tvTitleDetailStory.text = data.name
        detailBinding.tvDescDetailStory.text = data.description
        Glide.with(this)
            .load(data.photoUrl)
            .skipMemoryCache(true)
            .into(detailBinding.imgDetailStory)
        
    }


    companion object {
        const val ID_USER = ""
        var id = String()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            else -> true
        }
    }
}