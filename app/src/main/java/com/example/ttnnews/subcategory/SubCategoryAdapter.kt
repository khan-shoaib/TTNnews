package com.example.ttnnews.subcategory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidbootcamp2021.roomdemo.AppRoomDatabase
import com.example.finalapp.model.NewsModel
import com.example.ttnnews.R
import com.example.ttnnews.databse.NewModelRoom
import java.util.concurrent.Executors

class SubCategoryAdapter(
    private val context: Context,
    private var list: List<NewsModel>) : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {
    var onItemClick: ((NewsModel) -> Unit)? = null
    var onTitleClick: ((NewsModel) -> Unit)? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)
        val title: TextView = view.findViewById(R.id.textView1)
        val description: TextView = view.findViewById(R.id.textView2)
        val imgFav: ImageView = view.findViewById(R.id.imgfav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.description.text = list[position].description
        if(list[position].isFav)
            holder.imgFav.setImageResource(R.drawable.ic_action_fav)
            else
            holder.imgFav.setImageResource(R.drawable.ic_action_unfav)

        holder.title.setOnClickListener { onTitleClick!!.invoke(list[position]) }

        holder.image.setOnClickListener {onItemClick!!.invoke(list[position])}
        val img = list[position].image
        Glide.with(context).load(img).placeholder(R.drawable.ic_launcher_foreground).into(holder.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}