package id.kharisma.studio.vircle

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.databinding.ActivityAccountsettingBinding
import kotlinx.android.synthetic.main.activity_accountsetting.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.File
import java.util.jar.Manifest


class AccountSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityAccountsettingBinding
    lateinit var auth : FirebaseAuth
    private var checker = ""
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var imageView: ImageView
    private lateinit var file : File
    private lateinit var uri : Uri
    private lateinit var camIntent:Intent
    private lateinit var galIntent:Intent
    private lateinit var cropIntent:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAccountsettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imageView = findViewById(R.id.Profile_imageview)
        enableRuntimePermission()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        auth = FirebaseAuth.getInstance()
        binding.btnSignOutProfile.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        imageView.setOnClickListener{
            openDialog()
        }

        binding.btnSaveProfile.setOnClickListener {
            if(checker == "clicked"){

            }else{
                updateUserInfoOnly()
            }
        }
        userInfo()
    }


    private fun openDialog() {
        val openDialog = AlertDialog.Builder(this@AccountSettingActivity)
        openDialog.setIcon(R.drawable.profile)
        openDialog.setTitle("Choose your Image in...!!")
        openDialog.setPositiveButton("Camera"){
                dialog,_->
            openCamera()
            dialog.dismiss()

        }
        openDialog.setNegativeButton("Gallery"){
                dialog,_->
            openGallery()
            dialog.dismiss()
        }
        openDialog.setNeutralButton("Cancel"){
                dialog,_->
            dialog.dismiss()
        }
        openDialog.create()
        openDialog.show()

    }

    private fun openGallery() {
        galIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(Intent.createChooser(galIntent,
            "Select Image From Gallery "),2)
    }

    private fun openCamera() {
        camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = File(Environment.getExternalStorageDirectory(),
            "file"+System.currentTimeMillis().toString()+".jpg"
        )
        uri = Uri.fromFile(file)
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
        camIntent.putExtra("return-data",true)
        startActivityForResult(camIntent,0)
    }

    private fun enableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@AccountSettingActivity,android.Manifest.permission.CAMERA
            )){
            Toast.makeText(this@AccountSettingActivity,
                "Camera Permission allows us to Camera App",
                Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(this@AccountSettingActivity,
                arrayOf(android.Manifest.permission.CAMERA),RequestPermissionCode)
        }
    }

    private fun cropImages(){
        /**set crop image*/
        try {
            cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(uri,"image/*")
            cropIntent.putExtra("crop",true)
            cropIntent.putExtra("outputX",180)
            cropIntent.putExtra("outputY",180)
            cropIntent.putExtra("aspectX",3)
            cropIntent.putExtra("aspectY",4)
            cropIntent.putExtra("scaleUpIfNeeded",true)
            cropIntent.putExtra("return-data",true)
            startActivityForResult(cropIntent,1)

        }catch (e:ActivityNotFoundException){
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK){
            cropImages()
        } else if (requestCode == 2){
            if (data != null){
                uri = data.data!!
                cropImages()
            }
        }
        else if (requestCode == 1){
            if (data != null){
                val bundle = data.extras
                val bitmap = bundle!!.getParcelable<Bitmap>("data")
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RequestPermissionCode-> if (grantResults.size>0
                && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this@AccountSettingActivity,
                    "Permission Granted , Now your application can access Camera",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this@AccountSettingActivity,
                    "Permission Granted , Now your application can not  access Camera",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object{
        const val RequestPermissionCode = 111
    }

    private fun updateUserInfoOnly() {
        when {
            TextUtils.isEmpty(Fullname_editprofile.text.toString()) -> {
                Toast.makeText(this,"Please write Fullname first", Toast.LENGTH_SHORT).show()
            }
            Username_editprofile.text.toString() == "" -> {
                Toast.makeText(this,"Please write Username first", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val usersRef = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/")
                    .reference.child("Users")
                val userMap = HashMap<String, Any>()
                userMap["Username"] = Fullname_editprofile.text.toString().toLowerCase()
                userMap["Fullname"] = Username_editprofile.text.toString().toLowerCase()

                usersRef.child(firebaseUser.uid).updateChildren(userMap)
                Toast.makeText(this,"Account has been updated",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AccountSettingActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun userInfo(){
        val usersRef = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/")
            .getReference().child("Users").child(firebaseUser.uid)
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
}