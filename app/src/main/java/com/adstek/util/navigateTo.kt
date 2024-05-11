package com.adstek.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.navigateTo(directions: NavDirections) = findNavController().navigate(directions)
