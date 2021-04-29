package com.example.ttnnews.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.R
import com.example.ttnnews.model.CategoryModel

class CategoryFrag : Fragment() {

    var rcView: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        rcView = view.findViewById(R.id.rccatergory);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myArrayList = ArrayList<CategoryModel>()
        myArrayList.add(CategoryModel("business"))
        myArrayList.add(CategoryModel("general"))
        myArrayList.add(CategoryModel("entertainment"))
        myArrayList.add(CategoryModel("health"))
        myArrayList.add(CategoryModel("science"))
        myArrayList.add(CategoryModel("sports"))
        myArrayList.add(CategoryModel("technology"))

        val adapter = MyCategoryAdapter(myArrayList)
        rcView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcView!!.adapter = adapter


    }
}