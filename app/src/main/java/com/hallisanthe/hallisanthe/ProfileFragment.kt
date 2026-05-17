package com.hallisanthe.hallisanthe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view.findViewById<TextView>(R.id.menuMyListings).setOnClickListener {
            showToast("My Listings clicked")
        }
        view.findViewById<TextView>(R.id.menuWishlist).setOnClickListener {
            showToast("Wishlist clicked")
        }
        view.findViewById<TextView>(R.id.menuMessages).setOnClickListener {
            showToast("Messages clicked")
        }
        view.findViewById<TextView>(R.id.menuSettings).setOnClickListener {
            showToast("Settings clicked")
        }
        view.findViewById<TextView>(R.id.menuLogout).setOnClickListener {
            showToast("Logging out...")
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}