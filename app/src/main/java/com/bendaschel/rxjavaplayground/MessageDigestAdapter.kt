package com.bendaschel.rxjavaplayground

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

data class MessageDigestInfo(val name: String, val output: String)

class MessageDigestAdapter : RecyclerView.Adapter<MessageDigestVh>() {

    var hashFunctions = emptyList<MessageDigestInfo>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MessageDigestVh {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.include_hash_output, parent, false)
        return MessageDigestVh(view)
    }

    override fun getItemCount() = hashFunctions.size

    override fun onBindViewHolder(vh: MessageDigestVh, position: Int) {
        vh.bind(hashFunctions[position])
    }

}

class MessageDigestVh(view: View): RecyclerView.ViewHolder(view) {

    private val nameText = view.findViewById<TextView>(R.id.tv_algo_name)!!
    private val outputText = view.findViewById<TextView>(R.id.tv_hash_output)!!

    fun bind(item: MessageDigestInfo) {
        nameText.text = item.name
        outputText.text =  item.output
    }
}
