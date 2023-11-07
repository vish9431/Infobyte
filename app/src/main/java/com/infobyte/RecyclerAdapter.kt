package com.infobyte

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val list : ArrayList<DataModelItem>, val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val symbol:TextView = itemView.findViewById(R.id.symbol)
        val company:TextView = itemView.findViewById(R.id.comname)
        val price:TextView = itemView.findViewById(R.id.currentprice)
        val increment:TextView = itemView.findViewById(R.id.rate)
        val percentage:TextView = itemView.findViewById(R.id.perc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.design_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentItem = list[position]

        holder.apply {
            symbol.text = currentItem.SYMBOLE.toString()
            company.text = currentItem.NAME.toString()
            price.text = currentItem.LTP.toString()
            val changeInPrice = currentItem.ChangeInPRICE
            if (changeInPrice >= 0) {
                increment.text = "+${changeInPrice}"
            } else {
                increment.text = changeInPrice.toString() // Negative numbers will have the "-" sign
            }
            val percChange = currentItem.Perc_change

            if (percChange >= 0) {
                percentage.text = "+${percChange}%"
            } else {
                percentage.text = "${percChange}%"
            }

        }

    }
}