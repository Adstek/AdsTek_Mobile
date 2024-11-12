package com.adstek.util

import androidx.media3.ui.PlayerView

class VideoTransitionAnimator {
    fun animateTransition(oldView: PlayerView?, newView: PlayerView, onAnimationEnd: () -> Unit) {
        // Make sure new view starts invisible
        newView.alpha = 0f

        // If there's an old view, fade it out first
        if (oldView != null) {
            oldView.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction {
                    // Start fading in the new view after old view fades out
                    fadeInNewView(newView, onAnimationEnd)
                }
        } else {
            // If no old view, just fade in the new view
            fadeInNewView(newView, onAnimationEnd)
        }
    }

    private fun fadeInNewView(newView: PlayerView, onAnimationEnd: () -> Unit) {
        newView.animate()
            .alpha(1f)
            .setDuration(500)
            .withEndAction {
                onAnimationEnd()
            }
    }
}
