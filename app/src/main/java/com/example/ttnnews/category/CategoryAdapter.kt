package com.example.ttnnews.category

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.R
import com.example.ttnnews.model.CategoryModel
import com.example.ttnnews.subcategory.SubCategoryActivity

class CategoryAdapter(var myArrayList: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.tvCatName.text = myArrayList[position].name
        holder.row.setOnClickListener {


            val intent = Intent(holder.itemView.context, SubCategoryActivity::class.java)
            intent.putExtra("sourcename", myArrayList[position].name)

            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return myArrayList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCatName: TextView = itemView.findViewById(R.id.tv)
        val row: TextView = itemView.findViewById(R.id.tv)
    }
}