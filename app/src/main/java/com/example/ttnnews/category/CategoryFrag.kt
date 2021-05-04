package com.example.ttnnews.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.constants.Constant
import com.example.ttnnews.R
import com.example.ttnnews.fav.FragFav
import com.example.ttnnews.subcategory.SubCategoryFrag

class CategoryFrag : Fragment() {

    lateinit var rcView: RecyclerView
    lateinit var btSave: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        rcView = view.findViewById(R.id.rccatergory)
        btSave = view.findViewById(R.id.btsave)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CategoryAdapter(Constant.getCatergoryData()){ data ->
            val frag = SubCategoryFrag.newInstance()
            val bundle = Bundle()
            bundle.putString(Constant.SOURCENAME,data)
            frag.arguments = bundle
             activity!!.supportFragmentManager.beginTransaction().replace(R.id.container,frag).addToBackStack(null).commit()
        }
        rcView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcView.adapter = adapter

        btSave.setOnClickListener {

            val frag = FragFav.newInstance()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.container,frag).addToBackStack(null).commit()

        }

    }
}