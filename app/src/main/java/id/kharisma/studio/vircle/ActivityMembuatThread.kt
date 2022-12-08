package id.kharisma.studio.vircle

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_accountsetting.*
import kotlinx.android.synthetic.main.activity_membuat_thread.*

class ActivityMembuatThread : AppCompatActivity() {
    private var myUri = ""
    private var storageThreadPicRef: StorageReference? = null
    private lateinit var selectedImg : Uri
    private lateinit var imageView: ImageView
    companion object{
        val IMAGE_REQUEST_CODE = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membuat_thread)
        storageThreadPicRef = FirebaseStorage.getInstance().reference.child("Thread Picture")
        imageView = findViewById(R.id.image_thread)

        btn_Savethread.setOnClickListener{
        uploadImage()
        }
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

                image_thread.setImageURI(selectedImg)
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
            TextUtils.isEmpty(deskripsithread.text.toString()) -> {
                Toast.makeText(this,"Write the description", Toast.LENGTH_SHORT).show()
            }
            else ->{
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding New Post")
                progressDialog.setMessage("Please wait, we are upload your post....")
                progressDialog.show()

                val fileref = storageThreadPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                var uploadTask : StorageTask<*>
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
                            .child("Post")
                        val postId = ref.push().key
                        val postMap = HashMap<String, Any>()
                        postMap["postid"] = postId!!
                        postMap["description"] = deskripsithread.text.toString().toLowerCase()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        postMap["postimage"] = myUri
                        ref.child(postId).updateChildren(postMap)
                        Toast.makeText(this,"Post uploaded",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ActivityMembuatThread, HomeActivity::class.java)
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