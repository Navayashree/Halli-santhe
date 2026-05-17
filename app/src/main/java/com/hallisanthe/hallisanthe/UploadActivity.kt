package com.hallisanthe.hallisanthe

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UploadActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val cardPick = findViewById<MaterialCardView>(R.id.cardPickImage)
        val btnPost = findViewById<Button>(R.id.btnUpload)
        val etName = findViewById<EditText>(R.id.etName)
        val etArtisan = findViewById<EditText>(R.id.etArtisan)
        val etVillage = findViewById<EditText>(R.id.etVillage)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val autocompleteCategory = findViewById<AutoCompleteTextView>(R.id.spinnerCategory)
        val imgPreview = findViewById<ImageView>(R.id.imgPreview)
        val layoutPlaceholder = findViewById<View>(R.id.layoutPlaceholder)
        val progressIndicator = findViewById<LinearProgressIndicator>(R.id.progressIndicator)

        // Setup Category Dropdown
        val categories = resources.getStringArray(R.array.categories_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        autocompleteCategory.setAdapter(adapter)

        // Photo Picker Logic
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                imgPreview.setImageURI(uri)
                imgPreview.visibility = View.VISIBLE
                layoutPlaceholder.visibility = View.GONE
            }
        }

        cardPick.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnPost.setOnClickListener {
            val name = etName.text.toString().trim()
            val artisan = etArtisan.text.toString().trim()
            val village = etVillage.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val category = autocompleteCategory.text.toString()
            val description = etDescription.text.toString().trim()

            if (name.isNotEmpty() && priceStr.isNotEmpty() && imageUri != null && artisan.isNotEmpty()) {
                val price = priceStr.toLongOrNull() ?: 0L
                uploadToFirebase(name, artisan, village, price, category, description, progressIndicator)
            } else {
                Toast.makeText(this, "Please fill all fields and select a photo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadToFirebase(
        name: String, 
        artisan: String, 
        village: String, 
        price: Long, 
        category: String, 
        description: String,
        progress: LinearProgressIndicator
    ) {
        val fileName = UUID.randomUUID().toString()
        val imageRef = storage.child("product_images/$fileName")

        progress.visibility = View.VISIBLE
        progress.isIndeterminate = true

        imageUri?.let { uri ->
            imageRef.putFile(uri).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    val product = Product(
                        name = name,
                        artisanName = artisan,
                        villageName = village,
                        price = price,
                        imageUrl = downloadUrl.toString(),
                        category = category,
                        description = description
                    )

                    db.collection("products")
                        .add(product)
                        .addOnSuccessListener {
                            progress.visibility = View.GONE
                            Toast.makeText(this, "Listed in Marketplace!", Toast.LENGTH_LONG).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            progress.visibility = View.GONE
                            Toast.makeText(this, "Database Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                progress.visibility = View.GONE
                Toast.makeText(this, "Upload Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}