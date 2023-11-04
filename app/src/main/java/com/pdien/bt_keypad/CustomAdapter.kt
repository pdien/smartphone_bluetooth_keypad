package com.pdien.bt_keypad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.pdien.bt_keypad.databinding.ActivityMainBinding
import com.pdien.bt_keypad.databinding.ActivityItemsBinding

//import kotlinx.android.synthetic.main.item.view.*

class CustomAdapter(private val itemsList: List<BtItem>,
                    private val listener: OnItemClickListener) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    private lateinit var binding: ActivityItemsBinding

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ActivityItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem: BtItem = itemsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class MyViewHolder(private val itemBinding: ActivityItemsBinding) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener{
        fun bind(currentItem: BtItem) {
            itemBinding.itemTextView.text = currentItem.name
            itemBinding.itemTextView2.text = currentItem.mac
        }

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