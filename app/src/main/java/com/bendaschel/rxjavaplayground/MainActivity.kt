package com.bendaschel.rxjavaplayground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables.combineLatest
import org.apache.commons.codec.digest.DigestUtils

class MainActivity : AppCompatActivity() {

    private val defaultHashFunctions = listOf<DigestUtils>(
            DigestUtils("MD5"),
            DigestUtils("SHA-1"),
            DigestUtils("SHA-256")
    )

    private val hashControlState = HashControlState(defaultHashFunctions)

    private val adapter: MessageDigestAdapter by lazy {
        MessageDigestAdapter()
    }

    private val hashFunctionControlsAdapter by lazy {
        HashFunctionAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.rv_output).apply{
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }

        findViewById<RecyclerView>(R.id.rv_enabled_hashes).apply {
            adapter = hashFunctionControlsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        val inputField = findViewById<EditText>(R.id.et_plain_text)

        val enabledHashes = hashControlState.map {
            it.mapNotNull {
                if (it.enabled) it.digest else null
            }
        }

        hashControlState.subscribe { hashFunctionControlsAdapter.submitList(it) }

        hashFunctionControlsAdapter.output.subscribe(hashControlState::onStateChanged)

        combineLatest(inputField.observe(), enabledHashes, ::makeHash)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::updateOutput)
    }

    private fun updateOutput(items: List<MessageDigestInfo>) {
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