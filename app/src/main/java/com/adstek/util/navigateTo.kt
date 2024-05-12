package com.adstek.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.navigateTo(directions: NavDirections) = findNavController().navigate(directions)

fun Fragment.popBackStackOrFinish(): Boolean {
    return if (findNavController().popBackStack()) {
        true
    } else {
        requireActivity().finish()
        true
    }
}
