package id.kharisma.studio.vircle


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import id.kharisma.studio.vircle.databinding.ActivityloginBinding

class Login_Activity : AppCompatActivity() {

    lateinit var binding : ActivityloginBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityloginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.txtDaftar.setOnClickListener{
            val intent = Intent(this@Login_Activity, RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.btnMasuk.setOnClickListener{
            val email = binding.Email.text.toString()
            val password = binding.Password.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.Email.error = "Email tidak valid"
                binding.Email.requestFocus()
                return@setOnClickListener
            }

            //Validasi Email
            if (email.isEmpty()){
                binding.Email.error = "Email Harus Diisi"
                binding.Email.requestFocus()
                return@setOnClickListener
            }
            //Validasi Password
            if (password.isEmpty()){
                binding.Password.error = "Password Harus Diisi"
                binding.Password.requestFocus()
                return@setOnClickListener
            }
            LoginFirebase(email,password)
        }
        binding.frameStackgoogle.setOnClickListener{
        }
    }

    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.getResult()!=null) {
                    Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    reload()
                } else {
                    Toast.makeText(this,"Login Gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun reload() {
        val intent = Intent(this@Login_Activity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
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