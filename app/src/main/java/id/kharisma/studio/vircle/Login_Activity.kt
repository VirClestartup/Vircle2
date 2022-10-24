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
        binding.txtDaftar.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnMasuk.setOnClickListener{
            val email = binding.InputEmailLogin.editText.toString()
            val password = binding.InputPasswordLogin.editText.toString()

            //Validasi Email
            if (email.isEmpty()){
                binding.InputEmailLogin.error = "Email Harus Diisi"
                binding.InputEmailLogin.requestFocus()
                return@setOnClickListener
            }
            //Validasi Email Tidak Sesuai
            if ( Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.InputEmailLogin.error = "Email Tidak Valid"
                binding.InputEmailLogin.requestFocus()
                return@setOnClickListener
            }
            //Validasi Password
            if (password.isEmpty()){
                binding.InputPasswordLogin.error = "Password Harus Diisi"
                binding.InputPasswordLogin.requestFocus()
                return@setOnClickListener
            }
            

            LoginFirebase(email,password)
        }
    }

    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                } else {
                    Toast.makeText(this,"Login Gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }

}