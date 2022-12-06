package id.kharisma.studio.vircle

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import id.kharisma.studio.vircle.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.txtMasuk.setOnClickListener{
            val intent = Intent(this, Login_Activity::class.java)
            startActivity(intent)
        }

        binding.btnDaftar.setOnClickListener{
            val progressDialog = ProgressDialog(this@RegisterActivity)
            progressDialog.setTitle("SignUp")
            progressDialog.setMessage("Please wait")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            val fullname = binding.fullnameRegister.text.toString()
            val name = binding.usernameRegister.text.toString()
            val email = binding.Email.text.toString()
            val password = binding.Password.text.toString()
            val passwordConf = binding.KonfirmasiPassword.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.Email.error = "Email Tidak Valid"
                binding.Email.requestFocus()
                return@setOnClickListener
            }
            if (fullname.isEmpty()){
                binding.fullnameRegister.error = "nama Harus Diisi"
                binding.fullnameRegister.requestFocus()
                return@setOnClickListener
            }
            if (name.isEmpty()){
                binding.usernameRegister.error = "Username Harus Diisi"
                binding.usernameRegister.requestFocus()
                return@setOnClickListener
            }
            //Validasi Email
            if (email.isEmpty()){
                binding.Email.error = "Email Harus Diisi"
                binding.Email.requestFocus()
                return@setOnClickListener
            }
            //Validasi Email Tidak Sesuai

            //Validasi Password
            if (password.isEmpty()){
                binding.Password.error = "Password Harus Diisi"
                binding.Password.requestFocus()
                return@setOnClickListener
            }
            //Validasi Panjang Password
            if (password.length < 6){
                binding.Password.error = "Password Minimal 6 Karakter"
                binding.Password.requestFocus()
                return@setOnClickListener
            }
            if (password != passwordConf){
                binding.KonfirmasiPassword.error = "Password tidak sama"
                binding.KonfirmasiPassword.requestFocus()
                return@setOnClickListener
            }
            RegisterFirebase(name,fullname,email,password,progressDialog)



        }
    }
    private fun RegisterFirebase(name: String,fullname: String, email: String, password: String,progressDialog: ProgressDialog) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.getResult()!=null) {
                    val users = Firebase.auth.currentUser
                    if (users!=null) {
                        progressDialog.dismiss()
                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }
                        users!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    progressDialog.dismiss()
                                    reload()
                                }
                            }
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this,"Register Gagal", Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    saveUserInfo(name,fullname,email,password)

                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this,"${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun saveUserInfo(name: String,fullname: String, email: String, password: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference.child("Users")
        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["Username"] = name.toLowerCase()
        userMap["Fullname"] = fullname.toLowerCase()
        userMap["Email"] = email
        userMap["Password"] = password
        userMap["Image"] = "https://firebasestorage.googleapis.com/v0/b/vircle-77b59.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=980d517d-d1f8-4013-9602-fc3387d8c2f5"
        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Account has been saved",Toast.LENGTH_SHORT).show()
                }else{
                    val message = task.exception!!.toString()
                    Toast.makeText(this,"Error: $message", Toast.LENGTH_SHORT)
                    auth.signOut()
                }
            }
    }


    fun reload() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        super.startActivity(intent)
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

}