package id.kharisma.studio.vircle

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import id.kharisma.studio.vircle.databinding.ActivityNewCommunityBinding

class NewCommunityActivity : AppCompatActivity() {
    lateinit var binding : ActivityNewCommunityBinding
    private var myUri = ""
    private var storageCommunityPicRef: StorageReference? = null
    private lateinit var selectedImg : Uri
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNewCommunityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        storageCommunityPicRef = FirebaseStorage.getInstance().reference.child("Community Picture")
        imageView = binding.ImageCommunity
        binding.btnDaftar.setOnClickListener{
            uploadImage()
        }
        binding.ImageCommunity.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            if(data.data != null){
                selectedImg = data.data!!
                binding.ImageCommunity.setImageURI(selectedImg)
            }
        }
        if (requestCode == AccountSettingActivity.IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            imageView.setImageURI(data?.data)

        }
    }
    private fun uploadImage() {
        when{
            selectedImg == null -> {
                Toast.makeText(this,"Please select your image", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(binding.namakomunitas.text.toString()) -> {
                Toast.makeText(this,"Write the name of the community", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(binding.diskripsi.text.toString()) -> {
                Toast.makeText(this,"Write the description", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(binding.diskripsi.text.toString()) -> {
                Toast.makeText(this,"Write the description", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(binding.alasan.text.toString()) -> {
                Toast.makeText(this,"Tulis Ki alasan na", Toast.LENGTH_SHORT).show()
            }
            else ->{
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding New Community")
                progressDialog.setMessage("Please wait, we are Regist your Community....")
                progressDialog.show()

                val fileref = storageCommunityPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                val uploadTask : StorageTask<*>
                uploadTask = fileref.putFile(selectedImg!!)
                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
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
                            .child("Community")
                        val communityId = ref.push().key
                        val communityMap = HashMap<String, Any>()
                        communityMap["Communityid"] = communityId!!
                        communityMap["Name"] = binding.namakomunitas.text.toString()
                        communityMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        communityMap["Communityimage"] = myUri
                        communityMap["Deskripsi"] = binding.diskripsi.text.toString().toLowerCase()
                        communityMap["Alasan"] = binding.alasan.text.toString().toLowerCase()
                        ref.child(communityId).updateChildren(communityMap)
                        Toast.makeText(this,"Community Registered", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@NewCommunityActivity, HomeActivity::class.java)
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