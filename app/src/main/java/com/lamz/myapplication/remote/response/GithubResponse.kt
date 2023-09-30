package com.lamz.myapplication.remote.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(

	@field:SerializedName("items")
	val items: List<ItemsItem>
)

data class ItemsItem(

	@field:SerializedName("following_url")
	val followingUrl: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("followers_url")
	val followersUrl: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,
)
