package com.adstek.home.ui.games.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adstek.R
import com.adstek.data.remote.models.GamesListModel
import com.adstek.data.remote.response.WalletResponse
import com.adstek.databinding.GamesItemListBinding
import com.adstek.databinding.MobileMoneyItemListBinding
import com.bumptech.glide.Glide

class GamesAdapter(
    private val context: Context,
    private val onItemClick: ((item: GamesListModel) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val diffCallback = object : DiffUtil.ItemCallback<GamesListModel>() {

        override fun areItemsTheSame(oldItem: GamesListModel, newItem: GamesListModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GamesListModel, newItem: GamesListModel): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return GamesViewHolder(
            context,
            GamesItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick

        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GamesViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<GamesListModel>) {
        differ.submitList(list)
    }

    inner class GamesViewHolder(
        private val context: Context,
        private val binding: GamesItemListBinding,
        private val onItemClick: ((item: GamesListModel) -> Unit?),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: GamesListModel) = with(binding) {
            root.setOnClickListener {
                onItemClick.invoke(game)
            }
            gamesDesc.text =  game.description
            binding.gamesName.text = game.name
            Glide.with(context).load(game.icon).into(binding.icon)
        }
    }
}
