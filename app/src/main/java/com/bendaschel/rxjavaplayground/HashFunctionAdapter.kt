package com.bendaschel.rxjavaplayground

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.jakewharton.rxbinding3.widget.checkedChanges
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView
import org.apache.commons.codec.digest.DigestUtils

data class HashFunctionControl(val digest: DigestUtils, val enabled: Boolean)

class HashFunctionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val nameText: TextView by bindView(R.id.tv_hash_name)
    private val checkBox: CheckBox by bindView(R.id.cb_hash_enabled)

    fun bind(item: HashFunctionControl): Observable<HashFunctionControl> {
        nameText.text = item.digest.messageDigest.algorithm
        checkBox.isChecked = item.enabled

        return checkBox.checkedChanges().skipInitialValue().map {checked ->
            item.copy(enabled = checked)
        }
    }
}

class HashFunctionDiffer : DiffUtil.ItemCallback<HashFunctionControl>() {
    override fun areItemsTheSame(p0: HashFunctionControl, p1: HashFunctionControl): Boolean {
        return false
    }

    override fun areContentsTheSame(p0: HashFunctionControl, p1: HashFunctionControl): Boolean {
        return false
    }
}

class HashFunctionAdapter : ListAdapter<HashFunctionControl, HashFunctionViewHolder>(HashFunctionDiffer()) {

    val output = PublishSubject.create<HashFunctionControl>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashFunctionViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.view_hash_function_control, parent, false)
        return HashFunctionViewHolder(layout)
    }

    override fun onBindViewHolder(vh: HashFunctionViewHolder, position: Int) {
        val stream = vh.bind(getItem(position))
        stream.subscribe(output)
    }

    override fun onViewRecycled(holder: HashFunctionViewHolder) {
        Log.d(logTag, "onViewRecycled: holder = $holder")
    }
}
