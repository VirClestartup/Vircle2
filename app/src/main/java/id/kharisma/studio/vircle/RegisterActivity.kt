package id.kharisma.studio.vircle

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
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
    lateinit var database : DatabaseReference

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
            val name = binding.usernameRegister.text.toString()
            val email = binding.Email.text.toString()
            val password = binding.Password.text.toString()
            val passwordConf = binding.KonfirmasiPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.Email.error = "Email Tidak Valid"
                binding.Email.requestFocus()
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
            RegisterFirebase(name,email,password,progressDialog)



        }
    }
    private fun RegisterFirebase(name: String, email: String, password: String, progressDialog: ProgressDialog) {
        database = FirebaseDatabase.getInstance().getReference("Users")

        val userId = database.push().key!!
        val user = Users(userId,name,email,password)
        database.child(userId).setValue(user)
            .addOnCompleteListener {task ->
            if (task.isSuccessful && task.getResult()!=null) {
                binding.usernameRegister.text?.clear()
                progressDialog.dismiss()
                Toast.makeText(this, "data tersimpan", Toast.LENGTH_LONG).show()
            }else{
                progressDialog.dismiss()
                Toast.makeText(this,"${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.getResult()!=null) {
                    val users = Firebase.auth.currentUser
                    if (users!=null) {
                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }
                        users!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    reload()
                                }
                            }
                    }else{
                        Toast.makeText(this,"Register Gagal", Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    reload()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this,"${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun reload() {
        val intent = Intent(this, HomeActivity::class.java)
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