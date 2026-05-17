package com.hallisanthe.hallisanthe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var adapter: ProductAdapter
    private var productList = mutableListOf<Product>()
    private var filteredList = mutableListOf<Product>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupCategories)
        val emptyState = view.findViewById<LinearLayout>(R.id.emptyStateLayout)

        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductAdapter(filteredList)
        rv.adapter = adapter

        fetchProducts(emptyState)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterProducts("All", query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts("All", newText ?: "")
                return true
            }
        })

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = group.findViewById<Chip>(checkedIds.firstOrNull() ?: -1)
            val category = chip?.text?.toString() ?: "All"
            filterProducts(category, searchView.query.toString())
        }

        return view
    }

    private fun fetchProducts(emptyView: View) {
        db.collection("products").addSnapshotListener { value, _ ->
            productList.clear()
            value?.documents?.forEach { doc ->
                val p = doc.toObject(Product::class.java)
                if (p != null) productList.add(p.copy(id = doc.id))
            }
            
            addMockData()
            
            emptyView.visibility = if (productList.isEmpty()) View.VISIBLE else View.GONE
            filterProducts("All", "")
        }
    }

    private fun addMockData() {
        if (productList.any { it.id.startsWith("p") }) return // Don't add if already there
        
        // Pottery
        productList.add(Product("p1", "Terracotta Planter", 250, "https://images.unsplash.com/photo-1611080541599-8c6dbde6ed28", "Pottery", "Ravi", "Channapatna", "Hand-molded clay planter"))
        productList.add(Product("p2", "Hand-painted Vase", 1200, "https://images.unsplash.com/photo-1578500494198-246f612d3b3d", "Pottery", "Anil", "Bidar", "Intricate Bidriware style vase"))
        productList.add(Product("p3", "Clay Water Pot", 150, "https://images.unsplash.com/photo-1590640346030-975926ec0f8a", "Pottery", "Suresh", "Gundlupet", "Traditional cool water storage"))
        productList.add(Product("p4", "Terracotta Lamps", 80, "https://images.unsplash.com/photo-1543058869-70362f79029a", "Pottery", "Lakshmi", "Mysore", "Beautifully crafted oil lamps"))
        productList.add(Product("p5", "Ceramic Serving Bowl", 450, "https://images.unsplash.com/photo-1574101150499-52382c42c94d", "Pottery", "Arun", "Ramanagara", "Modern touch on traditional clay"))
        productList.add(Product("p6", "Clay Tea Cups (Set)", 300, "https://images.unsplash.com/photo-1576092768241-dec231879fc3", "Pottery", "Balu", "Molakalmuru", "Set of 6 traditional kulhads"))

        // Textiles
        productList.add(Product("t1", "Silk Handloom Saree", 4500, "https://images.unsplash.com/photo-1610030469983-98e550d6193c", "Textiles", "Meera", "Ilkal", "Traditional Ilkal silk saree"))
        productList.add(Product("t2", "Cotton Hand-woven Towel", 200, "https://images.unsplash.com/photo-1620916566398-39f1143ab7be", "Textiles", "Kavita", "Gadag", "Pure soft handloom cotton"))
        productList.add(Product("t3", "Embroidered Cushion", 650, "https://images.unsplash.com/photo-1584100936595-c0654b55a2e6", "Textiles", "Sita", "Hubli", "Hand-stitched folk patterns"))
        productList.add(Product("t4", "Khadi Shirt", 950, "https://images.unsplash.com/photo-1598033129183-c4f50c7176c8", "Textiles", "Manju", "Dharwad", "Breathable organic khadi cotton"))
        productList.add(Product("t5", "Woolen Shawl", 1500, "https://images.unsplash.com/photo-1606760227091-3dd870d97f1d", "Textiles", "Deepa", "Kodagu", "Warm hand-knitted hill wool"))
        productList.add(Product("t6", "Block Print Dupatta", 1200, "https://images.unsplash.com/photo-1617627143750-d86bc21e42bb", "Textiles", "Radha", "Bagalkot", "Natural dye block prints"))

        // Woodwork
        productList.add(Product("w1", "Carved Wood Bowl", 850, "https://images.unsplash.com/photo-1610701596007-11502861dcfa", "Woodwork", "Kiran", "Sagar", "Rosewood hand-carved bowl"))
        productList.add(Product("w2", "Bamboo Storage Basket", 180, "https://images.unsplash.com/photo-1591034351368-223d6118d533", "Woodwork", "Lata", "Sirsi", "Hand-woven durable bamboo"))
        productList.add(Product("w3", "Wooden Toy Set", 400, "https://images.unsplash.com/photo-1539627831859-a911cf04d3cd", "Woodwork", "Basava", "Channapatna", "Lacquered organic wooden toys"))
        productList.add(Product("w4", "Sandalwood Incense Stand", 320, "https://images.unsplash.com/photo-1602928292864-180a03001cae", "Woodwork", "Naveen", "Mysore", "Fragrant carved sandalwood"))
        productList.add(Product("w5", "Teak Wood Spatula", 120, "https://images.unsplash.com/photo-1594385208934-2c356f91f53b", "Woodwork", "Shiv", "Shimoga", "Durable seasoned teak wood"))
        productList.add(Product("w6", "Rosewood Jewelry Box", 2500, "https://images.unsplash.com/photo-1582139329536-e7284fece509", "Woodwork", "Raghu", "Mysore", "Inlaid with white wood patterns"))

        // Organic
        productList.add(Product("o1", "Organic Forest Honey", 350, "https://images.unsplash.com/photo-1587049352846-4a222e784d38", "Organic", "Sidda", "Western Ghats", "Pure wild honey"))
        productList.add(Product("o2", "Handmade Herbal Soap", 95, "https://images.unsplash.com/photo-1600857062241-99e5da7f21e2", "Organic", "Gauri", "Udupi", "Neem and Tulsi organic soap"))
        productList.add(Product("o3", "Cold Pressed Coconut Oil", 280, "https://images.unsplash.com/photo-1590779033100-9f60705a2f3b", "Organic", "Prakash", "Mangalore", "Traditional stone-pressed oil"))
        productList.add(Product("o4", "Spice Blend (Masala)", 150, "https://images.unsplash.com/photo-1596040033229-a9821ebd058d", "Organic", "Savita", "Sirsi", "Grandma's secret recipe spices"))
        productList.add(Product("o5", "Dried Banana Chips", 85, "https://images.unsplash.com/photo-1599490659223-e153c073f867", "Organic", "Anant", "Kumta", "Salted crispy local banana chips"))
        productList.add(Product("o6", "Natural Turmeric Powder", 180, "https://images.unsplash.com/photo-1615485240384-54bc9b4d8d9b", "Organic", "Rupa", "Thirthahalli", "Pure high-curcumin turmeric"))
    }

    private fun filterProducts(category: String, query: String) {
        filteredList.clear()
        val temp = if (category == "All" || category.contains("Trending")) {
            productList
        } else {
            val cleanCategory = if (category.contains(" ")) category.split(" ").last() else category
            productList.filter { it.category.contains(cleanCategory, ignoreCase = true) }
        }

        val final = if (query.isEmpty()) {
            temp
        } else {
            temp.filter { it.name.contains(query, ignoreCase = true) }
        }

        filteredList.addAll(final)
        adapter.notifyDataSetChanged()
    }
}