package com.hallisanthe.hallisanthe

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Long = 0,
    val imageUrl: String = "",
    val category: String = "General",
    val artisanName: String = "Unknown Artisan",
    val villageName: String = "Unknown Village",
    val description: String = ""
)