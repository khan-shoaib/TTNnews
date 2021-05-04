package com.example.ttnnews.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.R
import com.example.ttnnews.model.CategoryModel

class CategoryAdapter(
    var myArrayList: ArrayList<CategoryModel>,
    private val clickListener: (String) -> Unit,
) :
    RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(


            LayoutInflater.from(parent.context).inflate(R.layout.row_c, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.tvCatName.text = myArrayList[position].name
        holder.row.setOnClickListener {

            clickListener(myArrayList[position].name)
//            val intent = Intent(holder.itemView.context, SubCategoryActivity::class.java)
//            intent.putExtra(Constant.SOURCENAME, myArrayList[position].name)
//            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return myArrayList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCatName: TextView = itemView.findViewById(R.id.catName)
        val row: CardView = itemView.findViewById(R.id.cardview)
    }
}