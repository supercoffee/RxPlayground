package com.bendaschel.rxjavaplayground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import org.apache.commons.codec.digest.DigestUtils
import kotlin.experimental.and

class MainActivity : AppCompatActivity() {

    private val hashFunctions = Observable.just(listOf<DigestUtils>(
            DigestUtils("MD5"),
            DigestUtils("SHA-1"),
            DigestUtils("SHA-256")
    ))


    private val adapter: MessageDigestAdapter by lazy {
        MessageDigestAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputField = findViewById<EditText>(R.id.et_plain_text)
        val outputRecyclerView = findViewById<RecyclerView>(R.id.rv_output)
        outputRecyclerView.adapter = adapter
        outputRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        Observable.combineLatest(inputField.observe(), hashFunctions, BiFunction { t1: CharSequence, t2: List<DigestUtils> ->
            makeHash(t1, t2)
        }).subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateAdapter)
    }

    private fun updateAdapter(items: List<MessageDigestInfo>) {
        adapter.hashFunctions = items
    }

    private fun makeHash(input: CharSequence, hashFunctions: List<DigestUtils>) =
            hashFunctions.map {
                MessageDigestInfo(
                        name = it.messageDigest.algorithm,
                        output = it.digest(input.toString()).bytesToHex()
                )
            }

}

fun ByteArray.bytesToHex(): String {
    val sb = StringBuilder(2 * this.size)
    for (b in this) {
        sb.append("0123456789ABCDEF"[(b.toInt() and 0xF0) shr 4])
        sb.append("0123456789ABCDEF"[(b and 0x0F).toInt()])
    }
    return sb.toString()
}