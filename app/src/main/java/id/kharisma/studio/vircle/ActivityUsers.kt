package id.kharisma.studio.vircle

//import com.google.firebase.messaging.FirebaseMessaging
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import id.kharisma.studio.vircle.Adapter.UsersAdapter
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.databinding.ActivityUsersBinding
import id.kharisma.studio.vircle.firebase.FirebaseService
import kotlinx.android.synthetic.main.activity_users.*

class ActivityUsers : AppCompatActivity() {
    lateinit var binding : ActivityUsersBinding
    var userList = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUsersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        /*FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
        })*/
        userRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        imgBack.setOnClickListener {
            onBackPressed()
        }
        getUsersList()
    }

    private fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        var userid = firebase.uid
        //FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.getUID().equals(firebase.uid)) {

                        userList.add(user)
                    }
                }

                val userAdapter = UsersAdapter(this@ActivityUsers, userList)

                userRecyclerView.adapter = userAdapter
            }

        })
    }
}