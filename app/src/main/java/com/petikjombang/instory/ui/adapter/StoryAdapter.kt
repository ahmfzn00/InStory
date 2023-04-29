package com.petikjombang.instory.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.databinding.ItemStoryBinding

class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback : OnItemClickCallback

    class ListViewHolder(private val storyBinding: ItemStoryBinding) : RecyclerView.ViewHolder(storyBinding.root) {
        private var name = storyBinding.tvItemStoryName
        private var img = storyBinding.imgItemStory

        fun setData(data: StoryEntity) {
            name.text = data.name
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .skipMemoryCache(true)
                .into(img)
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.setData(data)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val storyBinding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(storyBinding)
    }

    fun setOnItemClick(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(data: StoryEntity)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}