package id.kharisma.studio.vircle

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitylogin)
        val btnMasuk = findViewById<AppCompatButton>(R.id.btnMasuk)
        btnMasuk.setOnClickListener {
            val inputEmail = findViewById<EditText>(R.id.inputEmail)
            val inputPassword = findViewById<EditText>(R.id.inputPassword)
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            if (email.isEmpty()|| password.isEmpty()) {
                Toast.makeText(this, "Please Insert Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(email == "admin01@gmail.com" || password == "admin01"){

                val intent = Intent (this,Dashboard::class.java)
                startActivity(intent)
                finish()
            }
        }

    }}

