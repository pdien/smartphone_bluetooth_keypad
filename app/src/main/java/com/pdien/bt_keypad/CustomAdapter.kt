package com.pdien.bt_keypad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

class CustomAdapter(private val itemsList: List<BtItem>,
                    private val listener: OnItemClickListener) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemsList[position]
        holder.textView1.text = currentItem.name
        holder.textView2.text = currentItem.mac
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val textView1: TextView = itemView.itemTextView
        val textView2: TextView = itemView.itemTextView2

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position: Int = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}