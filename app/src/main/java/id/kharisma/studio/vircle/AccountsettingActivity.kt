package id.kharisma.studio.vircle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.kharisma.studio.vircle.databinding.ActivityRegisterBinding

class AccountsettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountsetting)

    }
}