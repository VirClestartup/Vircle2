package id.kharisma.studio.vircle

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.databinding.ActivityAccountsettingBinding
import kotlinx.android.synthetic.main.activity_accountsetting.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.File
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.HashMap


class AccountSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityAccountsettingBinding
    lateinit var auth : FirebaseAuth
    private var checker = ""
    private var myUri = ""
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var imageView: ImageView
    private lateinit var file : File
    private lateinit var storage : FirebaseStorage
    private var storageProfilePicRef: StorageReference? = null
    private lateinit var selectedImg : Uri
    private lateinit var dialog: AlertDialog.Builder

    companion object{
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAccountsettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this).setMessage("Updating Profile...")
            .setCancelable(false)

        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Picture")

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        auth = FirebaseAuth.getInstance()
        imageView = findViewById(R.id.Profile_imageview)
        binding.btnSignOutProfile.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.ProfileImageview.setOnClickListener{
            checker = "clicked"
            pickImageGallery()
        }
        binding.btnCloseProfile.setOnClickListener{
            val intent = Intent(this@AccountSettingActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSaveProfile.setOnClickListener {
            if(checker == "clicked"){
                uploadImageAndUpdateInfo()
            }else{
                updateUserInfoOnly()
            }
        }
        userInfo()
    }


    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            if(data.data != null){
                selectedImg = data.data!!

                binding.ProfileImageview.setImageURI(selectedImg)
            }
        }
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            imageView.setImageURI(data?.data)

        }
    }


    private fun updateUserInfoOnly() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Account Settings1")
        progressDialog.setMessage("Please wait, we are updating your profile....")
        progressDialog.show()
        when {
            TextUtils.isEmpty(Fullname_editprofile.text.toString()) -> {
                Toast.makeText(this,"Please write Fullname first", Toast.LENGTH_SHORT).show()
            }
            Username_editprofile.text.toString() == "" -> {
                Toast.makeText(this,"Please write Username first", Toast.LENGTH_SHORT).show()
            }
            else -> {

                val ref = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
                    .child("Users")
                val userMap = HashMap<String, Any>()
                userMap["Fullname"] = Fullname_editprofile.text.toString().toLowerCase()
                userMap["Username"] = Username_editprofile.text.toString().toLowerCase()
                ref.child(firebaseUser.uid).updateChildren(userMap)
                Toast.makeText(this,"Account has been updated",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AccountSettingActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
                progressDialog.dismiss()
            }
        }
    }


    private fun userInfo(){
        val usersRef = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/")
            .reference.child("Users").child(firebaseUser.uid)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(Profile_imageview)
                    Fullname_editprofile.setText(user!!.getFullname())
                    Username_editprofile.setText(user!!.getUsername())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun uploadImageAndUpdateInfo() {
        when{
            TextUtils.isEmpty(Fullname_editprofile.text.toString()) -> {
                Toast.makeText(this,"Please write Fullname first", Toast.LENGTH_SHORT).show()
            }
            Username_editprofile.text.toString() == "" -> {
                Toast.makeText(this,"Please write Username first", Toast.LENGTH_SHORT).show()
            }
            selectedImg == null -> {
                Toast.makeText(this,"Please select your image", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Account Settings")
                progressDialog.setMessage("Please wait, we are updating your profile....")
                progressDialog.show()

                val fileref = storageProfilePicRef!!.child(firebaseUser!!.uid + ".jpg")
                var uploadTask : StorageTask<*>
                uploadTask = fileref.putFile(selectedImg!!)
                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot,Task<Uri>>{ task ->
                    if(task.isSuccessful){
                        task.exception?.let{
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation  fileref.downloadUrl
                }).addOnCompleteListener ( OnCompleteListener<Uri>{ task ->
                    if (task.isSuccessful){
                        val downloadUri = task.result
                        myUri = downloadUri.toString()

                        val ref = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
                            .child("Users")
                        val userMap = HashMap<String, Any>()
                        userMap["Fullname"] = Fullname_editprofile.text.toString().toLowerCase()
                        userMap["Username"] = Username_editprofile.text.toString().toLowerCase()
                        userMap["Image"] = myUri
                        ref.child(firebaseUser.uid).updateChildren(userMap)
                        Toast.makeText(this,"Account has been updated",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AccountSettingActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }else{
                        progressDialog.dismiss()
                    }
                } )
            }
        }
    }
}