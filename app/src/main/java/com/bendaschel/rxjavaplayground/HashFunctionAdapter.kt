package com.bendaschel.rxjavaplayground

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import kotterknife.bindView
import org.apache.commons.codec.digest.DigestUtils

class HashFunctionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val nameText: TextView by bindView(R.id.tv_hash_name)
    private val checkBox: CheckBox by bindView(R.id.cb_hash_enabled)

    fun bind(item: DigestUtils) {
        nameText.text = item.messageDigest.algorithm
        checkBox.isChecked = true
    }

}

class HashFunctionDiffer : DiffUtil.ItemCallback<DigestUtils>() {
    override fun areItemsTheSame(p0: DigestUtils, p1: DigestUtils): Boolean {
        return false
    }

    override fun areContentsTheSame(p0: DigestUtils, p1: DigestUtils): Boolean {
        return false
    }
}

class HashFunctionAdapter : ListAdapter<DigestUtils, HashFunctionViewHolder>(HashFunctionDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashFunctionViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.view_hash_function_control, parent, false)
        return HashFunctionViewHolder(layout)
    }

    override fun onBindViewHolder(vh: HashFunctionViewHolder, position: Int) {
        vh.bind(getItem(position))
    }
}
