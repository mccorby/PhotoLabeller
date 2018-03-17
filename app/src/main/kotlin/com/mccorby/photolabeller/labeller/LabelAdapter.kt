package com.mccorby.photolabeller.labeller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mccorby.photolabeller.R.layout.label_item
import kotlinx.android.synthetic.main.label_item.view.*
import java.text.DecimalFormat

interface OnLabelSelectedListener {
    fun onLabelSelected(label: String, score: Double)
}

class LabelAdapter(private val labels: Array<String>, private val onLabelSelectedListener: OnLabelSelectedListener) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private val df = DecimalFormat("#.00")
    }

    private var labelScores: List<Double>? = null

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(label_item, parent, false))
    }

    override fun getItemCount() = labels.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.label.text = labels[position]
        holder.score.text = labelScores?.let { df.format(it[position]) } ?: "0.00"
        holder.itemView.setOnClickListener({
            onLabelSelectedListener.onLabelSelected(labels[position], labelScores?.get(position) ?: 0.0)
        })
    }

    fun setScores(scores: List<Double>) {
        labelScores = scores
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var label: TextView = view.label_name
    var score: TextView = view.label_score
}
