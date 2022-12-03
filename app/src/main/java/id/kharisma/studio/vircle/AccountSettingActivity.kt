package id.kharisma.studio.vircle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import id.kharisma.studio.vircle.databinding.ActivityAccountsettingBinding


class AccountSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityAccountsettingBinding
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAccountsettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnSignOutProfile.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}