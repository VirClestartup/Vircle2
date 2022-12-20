package id.kharisma.studio.vircle.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.R
import id.kharisma.studio.vircle.fragment.ProfileFragment

class UserAdapter (private var mContext: Context,
private var mUser: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUser[position]
        holder.userUsernameTextView.text = user.getUsername()
        holder.userFullnameTextView.text = user.getFullname()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userProfileImage)

        checkFollowingStatus(user.getUID(), holder.followButton)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit()
            pref.putString("profileId", user.getUID())
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.FragmentContainer, ProfileFragment()).commit()

        })

        holder.followButton.setOnClickListener {
            if(holder.followButton.text.toString() == "Follow"){
                firebaseUser?.uid.let{it1 ->
                    FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(user.getUID())
                        .setValue(true).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                firebaseUser?.uid.let{it1 ->
                                    FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
                                        .child("Follow").child(user.getUID())
                                        .child("Followers").child(it1.toString())
                                        .setValue(true).addOnCompleteListener { task ->
                                            if(task.isSuccessful){

                                            }
                                        }
                                }
                            }
                        }
                }
            }else{
                firebaseUser?.uid.let{it1 ->
                    FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(user.getUID())
                        .removeValue().addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                firebaseUser?.uid.let{it1 ->
                                    FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
                                        .child("Follow").child(user.getUID())
                                        .child("Following").child(it1.toString())
                                        .removeValue().addOnCompleteListener { task ->
                                            if(task.isSuccessful){

                                            }
                                        }
                                }
                            }
                        }
                }
            }
        }
    }

    class ViewHolder ( @NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var userUsernameTextView: TextView = itemView.findViewById(R.id.username_search)
        var userFullnameTextView: TextView = itemView.findViewById(R.id.fullname_search)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.userprofile_item)
        var followButton: Button = itemView.findViewById(R.id.btnFollow)
    }

    private fun checkFollowingStatus(uid: String, followButton: Button) {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.child(uid).exists()){
                    followButton.text = "Following"
                }else{
                    followButton.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}