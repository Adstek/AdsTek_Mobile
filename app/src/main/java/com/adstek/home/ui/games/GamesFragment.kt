package com.adstek.home.ui.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adstek.R
import com.adstek.data.remote.models.GamesListModel
import com.adstek.databinding.FragmentGamesBinding
import com.adstek.extensions.navigateTo
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.home.ui.games.adapter.GamesAdapter
import com.adstek.home.ui.profile.wallet.MobileMoneyFragmentDirections
import com.adstek.home.ui.profile.wallet.adapter.MomoWalletAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding


    private val gamesAdapter by lazy {
        GamesAdapter(
            this.requireContext(),
            onItemClick = {game->
                when(game.id){
                    0 -> {
                        navigateTo(GamesFragmentDirections.actionGamesFragmentToQuestionsAndAnswersFragment())
                    }
                    1 -> {
                        navigateTo(GamesFragmentDirections.actionGamesFragmentToTiktaeFragment())
                    }

                    2 -> {
                        navigateTo(GamesFragmentDirections.actionGamesFragmentToMemoryCardFragment())
                    }
                    else ->{}
                }

            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGamesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpGamesAdapter()
        with(binding){
            back.btnBack.setOnClickListener {
                popBackStackOrFinish()
            }

        }

    }

    private fun setUpGamesAdapter() = with(binding){
        rvGames.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            adapter = gamesAdapter
        }
        val gamesList = mutableListOf(GamesListModel(0, "Trivia",description =  requireContext().getString(R.string.trivia_desc) ,R.drawable.trivia_game_icon))
        gamesList.add(GamesListModel(1, "Tic-Tac-Toe", description =  requireContext().getString(R.string.tic_desc), R.drawable.tic_tac_toe))
        gamesList.add(GamesListModel(2, "Memory Card", description =  requireContext().getString(R.string.memory_card_desc), R.drawable.memory_game))
        gamesAdapter.submitList(gamesList)
    }



}