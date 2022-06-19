package com.example.giftwith

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.giftwith.databinding.ActivityProfileinfoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception
import java.util.*


class ProfileinfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileinfoBinding
    private lateinit var activityResultLaucher: ActivityResultLauncher<Intent>
    private lateinit var permissionLaucher: ActivityResultLauncher<String>
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private  lateinit var auth: FirebaseAuth
    var selectedPicture: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileinfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLaunchers()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

    }
    fun uploadProfilePhoto(view: View)
    {
        if(ContextCompat.checkSelfPermission(this@ProfileinfoActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Snackbar.make(view,"Permission is needed to put a photo",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",View.OnClickListener {
                    permissionLaucher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }).show()
            }
            else {
                permissionLaucher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        else {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLaucher.launch(intentToGallery)
        }
    }
    fun finishSignUp(view: View) {
/*        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()*/
        val currentUser = auth.currentUser
        val reference = storage.reference
        val userName = binding.textNickname.text.toString()
        val gender = binding.textGender.text.toString()
        val birthday = binding.textBirthday.text.toString()
        val uudi = UUID.randomUUID()
        val imageName = "$uudi.jpg"
        val imageReference = reference.child("image").child(imageName)
        val intent = Intent(this@ProfileinfoActivity, HomeActivity::class.java)

        if(selectedPicture != null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                val uploadedImageReference = storage.reference.child("image").child(imageName)
                uploadedImageReference.downloadUrl.addOnSuccessListener {
                    val downloadedUrl = it.toString()
                    val ProfileMap = hashMapOf<String,Any>()

                    if (currentUser != null && userName.isNotEmpty()){
                        ProfileMap.put("downloadedUrl",downloadedUrl)
                        ProfileMap.put("Username",userName)
                        ProfileMap.put("Birthday",birthday)
                        ProfileMap.put("Gender",gender)
                        ProfileMap.put("UID",currentUser.uid)

                        firestore.collection("Profiles").add(ProfileMap).addOnSuccessListener {
                            startActivity(intent)
                            finish()
                            Toast.makeText(this@ProfileinfoActivity,"Account is created succesfully",Toast.LENGTH_LONG).show()

                        }.addOnFailureListener{
                            Toast.makeText(this@ProfileinfoActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun registerLaunchers()
    {
        activityResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if(result.resultCode == RESULT_OK)
            {
                val intentFromResult = result.data
                if (intentFromResult != null)
                {
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        binding.profilePhoto.setImageURI(it)
                    }
                }
            }
        }
        permissionLaucher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            result ->
            if(result){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLaucher.launch(intentToGallery)
            }
            else {
                Toast.makeText(this,"Permission Needed",Toast.LENGTH_LONG).show()
            }
        }
    }
}