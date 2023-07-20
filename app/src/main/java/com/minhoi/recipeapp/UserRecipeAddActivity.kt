package com.minhoi.recipeapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.databinding.DataBindingUtil
import com.google.firebase.storage.FirebaseStorage
import com.kakao.sdk.user.UserApiClient
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.databinding.ActivityUserRecipeAddBinding
import java.io.ByteArrayOutputStream

class UserRecipeAddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserRecipeAddBinding
    private val storage = FirebaseStorage.getInstance()
    private lateinit var imageUri : Uri
    private var key : String? = null
    private var userId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        UserApiClient.instance.me { user, error ->
            if (user != null) {
                userId = user.id.toString()
                key = Ref.userRecipeDataRef.child(userId!!).push().key.toString()
            }
        }

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_recipe_add)

        binding.inputUserRecipeImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }

        binding.btnUserRecipeAdd.setOnClickListener {
            val title = binding.inputUserRecipeTitle.text.toString()
            val ingredient = binding.innputUserRecipeIngredient.text.toString()
            val way = binding.inputUserRecipeWay.text.toString()

            imageUpload()

            userId?.let { id -> key?.let { key -> Ref.userRecipeDataRef.child(id).child(key).setValue(UserRecipeData(title,ingredient, way)) } }
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.inputUserRecipeImage.setImageURI(data?.data)
            imageUri = data?.data!!
        }
    }

    private fun imageUpload() {
        // Get the data from an ImageView as bytes
      
        val userRecipeRef = key?.let { storage.reference.child(it) }
        val imageView = binding.inputUserRecipeImage
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = userRecipeRef?.putBytes(data)

        uploadTask?.addOnFailureListener {
            // Handle unsuccessful uploads
        }?.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

}