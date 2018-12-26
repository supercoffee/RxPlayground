package com.bendaschel.rxjavaplayground.network
import retrofit2.http.GET
import retrofit2.http.Path
import io.reactivex.Observable

interface RedditApi {

    enum class SubredditSort (value: String){
        HOT("hot"),
        RISING("rising")
    }

    @GET("/r/{sub}/{sort}")
    fun getSubredditPosts(@Path("sub") subreddit: String,
                          @Path("sort") sort: SubredditSort
                          ): Observable<Listing>
}

data class Listing(
    val before: String,
    val after: String,
    val modhash: String,
    val children: List<Link>
)

data class Link (
        val ups: Int,
        val downs: Int,
        val likes: Boolean?,
        val title: String,
        val url: String
)