package com.adstek.home.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adstek.home.ui.games.memorycard.MemoryCardFragment
import com.adstek.home.ui.games.tik.ui.TiktaeFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when (position) {
//            0 -> TiktaeFragment()
               0 -> MemoryCardFragment()
//            1 -> TiktaeFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}