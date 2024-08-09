package com.adstek.home.ui.profile.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adstek.data.local.model.WalletTypeModel
import com.adstek.databinding.WalletItemListBinding

class WalletAdapter(
    private val onItemClick: ((walletType: WalletTypeModel) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val diffCallback = object : DiffUtil.ItemCallback<WalletTypeModel>() {

        override fun areItemsTheSame(oldItem: WalletTypeModel, newItem: WalletTypeModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: WalletTypeModel, newItem: WalletTypeModel): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return WalletTypeViewHolder(
            WalletItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WalletTypeViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<WalletTypeModel>) {
        differ.submitList(list)
    }

    inner class WalletTypeViewHolder(
        private val binding: WalletItemListBinding,
        private val onItemClick: ((walletType: WalletTypeModel) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(walletType: WalletTypeModel) = with(binding) {
            root.setOnClickListener {
                onItemClick?.invoke(walletType)
            }
            icon.setImageDrawable(root.context.getDrawable(walletType.image))
            binding.title.text = walletType.title

        }

    }

}
