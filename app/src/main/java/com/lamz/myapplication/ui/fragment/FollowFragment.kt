package com.lamz.myapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lamz.myapplication.adapter.ListFollowerAdapter
import com.lamz.myapplication.databinding.FragmentFollowBinding
import com.lamz.myapplication.model.DetailViewModel
import com.lamz.myapplication.remote.response.ItemsItem


class FollowFragment : Fragment() {

    private lateinit var binding : FragmentFollowBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvListFollower.layoutManager = layoutManager

        val itemDecor = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvListFollower.addItemDecoration(itemDecor)

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME)

            if (position == 1) {
                detailViewModel.getFollowers("$username")
                detailViewModel.listFollower.observe(viewLifecycleOwner){listFollow ->
                    setUserFollow(listFollow)
                }
            } else {
                detailViewModel.getFollowing("$username")
                detailViewModel.listFollowing.observe(viewLifecycleOwner){listFollow ->
                    setUserFollow(listFollow)
                }

                detailViewModel.followLoading.observe(viewLifecycleOwner) { load ->
                    showLoading(load)
                }

            }
        }
    }

    private fun setUserFollow(listUser: List<ItemsItem>) {
        val adapter = ListFollowerAdapter()
        adapter.submitList(listUser)
        binding.rvListFollower.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }
}