package com.petikjombang.instory.ui.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.petikjombang.instory.R
import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.data.local.preferences.UserPreference
import com.petikjombang.instory.data.remote.response.LoginResult
import com.petikjombang.instory.databinding.ActivityMainBinding
import com.petikjombang.instory.ui.ViewModelFactory
import com.petikjombang.instory.ui.adapter.LoadingAdapter
import com.petikjombang.instory.ui.adapter.StoryAdapter
import com.petikjombang.instory.ui.add.AddStoryActivity
import com.petikjombang.instory.ui.detail.DetailActivity
import com.petikjombang.instory.ui.location.LocationActivity
import com.petikjombang.instory.ui.user.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var userPreference: UserPreference
    private lateinit var userData: LoginResult
    private val storyViewModels: StoryViewModels by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        supportActionBar?.elevation = 0f
        userPreference = UserPreference(this)
        userData = userPreference.getUser()

        setAdapterStory()
        setDataStory()
    }

    private fun setDataStory() {
        val adapter = StoryAdapter()
        mainBinding.rvItemStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter {
                adapter.retry()
            }
        )

        storyViewModels.story.observe(this) { adapter.submitData(lifecycle, it) }
        adapter.setOnItemClick { data -> selectUser(data) }
    }

    private fun selectUser(data: StoryEntity) {
        val i = Intent(this, DetailActivity::class.java)
        i.putExtra(DetailActivity.ID_USER, data.id)
        startActivity(i)
    }

    private fun setAdapterStory() {
        val layoutManager = LinearLayoutManager(this)
        mainBinding.rvItemStory.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.menu_location -> {
                startActivity(Intent(this, LocationActivity::class.java))
                true
            }

            R.id.menu_add -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
                true
            }

            R.id.menu_logout -> {
                logoutAction()
                true
            }

            else -> true
        }
    }

    private fun logoutAction() {
        userData.let { user ->
            user.userId = ""
            user.name = ""
            user.token = ""
        }

        userPreference.setUser(userData)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}