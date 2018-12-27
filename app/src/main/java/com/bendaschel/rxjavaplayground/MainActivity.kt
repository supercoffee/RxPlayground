package com.bendaschel.rxjavaplayground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.bendaschel.rxjavaplayground.network.Listing
import com.bendaschel.rxjavaplayground.network.ListingWrapper
import com.bendaschel.rxjavaplayground.network.RedditApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.util.HalfSerializer.onNext
import io.reactivex.schedulers.Schedulers
import kotterknife.bindView
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val mainContent by bindView<RecyclerView>(R.id.main_content_recycler)

    private val adapter by lazy {
        ListingAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainContent.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://reddit.com")
                .build()

        val redditApi = retrofit.create(RedditApi::class.java)

        redditApi.getSubredditPosts(subreddit = "aww", sort = RedditApi.SubredditSort.HOT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onNext, ::onError)

    }

    private fun onNext(listing: ListingWrapper) {
        Log.d(logTag, "success")
        adapter.submitList(listing.data.children.map { it.data })
    }

    private fun onError(error: Throwable) {
        Log.e(logTag, "error", error)
    }
}