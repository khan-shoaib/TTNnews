package com.example.ttnnews.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.Constants.Constant
import com.example.ttnnews.R

class CategoryFrag : Fragment() {

    lateinit var rcView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        rcView = view.findViewById(R.id.rccatergory)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = CategoryAdapter(Constant.getCatergoryData())
        rcView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcView.adapter = adapter


    }
}