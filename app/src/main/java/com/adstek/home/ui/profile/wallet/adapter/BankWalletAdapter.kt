package com.adstek.home.ui.profile.wallet.adapter

import android.graphics.PorterDuff
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adstek.data.remote.response.WalletResponse
import com.adstek.databinding.BankAccountItemListBinding

class BankWalletAdapter(
    private val onItemClick: ((item: WalletResponse) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val diffCallback = object : DiffUtil.ItemCallback<WalletResponse>() {

        override fun areItemsTheSame(oldItem: WalletResponse, newItem: WalletResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WalletResponse, newItem: WalletResponse): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BankWalletViewHolder(
            BankAccountItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick

        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BankWalletViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<WalletResponse>) {
        differ.submitList(list)
    }

    inner class BankWalletViewHolder(
        private val binding: BankAccountItemListBinding,
        private val onItemClick: ((item: WalletResponse) -> Unit?),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(walletResponse: WalletResponse) = with(binding) {
            root.setOnClickListener {
                onItemClick.invoke(walletResponse)
            }
            binding.bankName.text = walletResponse.bankName
            binding.bankAccountNumber.text = walletResponse.accountNumber
            binding.accountName.text = walletResponse.accountName
        }
    }
}
