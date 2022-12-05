package id.kharisma.studio.vircle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


class AccountSettingActivity : AppCompatActivity() {
    lateinit var binding : ActivityAccountsettingBinding
    lateinit var auth : FirebaseAuth
    private lateinit var firebaseUser : FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAccountsettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        auth = FirebaseAuth.getInstance()
        binding.btnSignOutProfile.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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
                    //Fullname_editprofile.text = user!!.getFullname()
                    //Username_editprofile.text = user!!.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}