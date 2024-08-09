package com.adstek.home.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adstek.data.local.ProfileScreenModel
import com.adstek.databinding.ProfileItemListBinding

class ProfileAdapter(
    private val onItemClick: ((profile: ProfileScreenModel) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val diffCallback = object : DiffUtil.ItemCallback<ProfileScreenModel>() {

        override fun areItemsTheSame(oldItem: ProfileScreenModel, newItem: ProfileScreenModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ProfileScreenModel, newItem: ProfileScreenModel): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ProfileScreenViewHolder(
            ProfileItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileScreenViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ProfileScreenModel>) {
        differ.submitList(list)
    }

    inner class ProfileScreenViewHolder(
        private val binding: ProfileItemListBinding,
        private val onItemClick: ((profile: ProfileScreenModel) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: ProfileScreenModel) = with(binding) {
            root.setOnClickListener {
                onItemClick?.invoke(profile)
            }
            icon.setImageDrawable(root.context.getDrawable(profile.image))
            binding.title.text = profile.title

        }

    }

}
