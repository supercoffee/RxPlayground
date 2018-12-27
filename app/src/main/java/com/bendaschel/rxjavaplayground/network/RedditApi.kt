package com.bendaschel.rxjavaplayground.network
import retrofit2.http.GET
import retrofit2.http.Path
import io.reactivex.Observable

interface RedditApi {

    enum class SubredditSort (val value: String){
        HOT("hot"),
        RISING("rising");

        override fun toString() = value
    }

    @GET("/r/{sub}/{sort}.json")
    fun getSubredditPosts(@Path("sub") subreddit: String,
                          @Path("sort") sort: SubredditSort
                          ): Observable<ListingWrapper>
}

data class ListingWrapper(
        val kind: String,
        val data: Listing
)

data class Listing(
    val before: String,
    val after: String,
    val modhash: String,
    val children: List<LinkWrapper>
)

data class LinkWrapper(
        val kind: String,
        val data: Link
)

data class Link (
        val ups: Int,
        val downs: Int,
        val likes: Boolean?,
        val title: String,
        val url: String
)