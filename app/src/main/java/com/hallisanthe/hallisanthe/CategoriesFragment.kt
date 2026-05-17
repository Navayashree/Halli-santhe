package com.hallisanthe.hallisanthe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rvCategories)
        rv.layoutManager = GridLayoutManager(requireContext(), 2)

        val categories = listOf(
            Category("Pottery", "🏺"),
            Category("Textiles", "🧵"),
            Category("Woodwork", "🪵"),
            Category("Organic", "🌿"),
            Category("Metalwork", "⚒️"),
            Category("Jewelry", "📿"),
            Category("Paintings", "🎨"),
            Category("Basketry", "🧺")
        )

        rv.adapter = CategoryAdapter(categories) { category ->
            // Handle category click - maybe go back to home with filter
            val bundle = Bundle().apply {
                putString("selected_category", category.name)
            }
            val homeFragment = HomeFragment().apply {
                arguments = bundle
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit()
        }

        return view
    }
}