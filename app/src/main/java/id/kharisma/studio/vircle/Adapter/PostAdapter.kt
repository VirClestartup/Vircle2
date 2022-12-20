package id.kharisma.studio.vircle.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.kharisma.studio.vircle.Model.Post
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.R
import kotlinx.android.synthetic.main.activity_accountsetting.*

class PostAdapter
    (private  var mContext: Context,
     private var mPost: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>()
{
    private var firebaseUser: FirebaseUser? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mPost.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       firebaseUser = FirebaseAuth.getInstance().currentUser
        val post = mPost[position]
        holder.description.text = post.getDescription()
        Picasso.get().load(post.getPostImage()).into(holder.postImage)
        publisherInfo(holder.profileImage, holder.userName, holder.publisher, post.getPublisher())
        holder.bindItems(post)
    }




    inner class ViewHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView){
        var profileImage: CircleImageView
        var postImage: ImageView
        lateinit var likebutton: ImageView
        lateinit var doubleTapmiddleLike:ImageView
        var commentbutton: ImageView
        var userName: TextView
        lateinit var likes: TextView
        var publisher: TextView
        var description: TextView
        var comments: TextView
        private var doubleTap:Boolean = false
        private lateinit var animDoubleTap: Animation
        init {
            profileImage = itemView.findViewById(R.id.user_profile_image_home)
            postImage = itemView.findViewById(R.id.post_image_home)
            commentbutton = itemView.findViewById(R.id.post_image_comment_btn)
            userName = itemView.findViewById(R.id.user_name_home)
            publisher = itemView.findViewById(R.id.publisher)
            description = itemView.findViewById(R.id.description)
            comments = itemView.findViewById(R.id.comments)
        }
        @SuppressLint("SuspiciousIndentation")
        fun bindItems(post: Post){
            doubleTapmiddleLike = itemView.findViewById(R.id.double_tap_like_icon_middle)
            likebutton = itemView.findViewById(R.id.post_image_like_btn)
            likes = itemView.findViewById(R.id.likes)
            isLikes(post.getPostid())
            mLikes(likes,post.getPostid())
            likebutton.setOnClickListener {
                if (likebutton.tag == "Like"){
                    giveLike(post.getPostid(),true)
                    likebutton.setImageResource(R.drawable.ic_liked)
                }else{
                    giveLike(post.getPostid(),false)
                }
            }
            val handler = Handler(Looper.myLooper()!!)
            val runnable = Runnable {
                doubleTap = false
            }
            postImage.setOnClickListener {
                animDoubleTap = AnimationUtils
                    .loadAnimation(itemView.context,R.anim.anim_double_tap_like)

                if (doubleTap){
                    doubleTap = !doubleTap
                    doubleTapmiddleLike.startAnimation(animDoubleTap)
                    doubleTapmiddleLike.visibility = View.VISIBLE
                    Handler(Looper.myLooper()!!).postDelayed({
                        doubleTapmiddleLike.visibility = View.GONE
                    },600)
                    likebutton.startAnimation(animDoubleTap)
                    giveLike(post.getPostid(),true)
                    likebutton.setImageResource(R.drawable.ic_liked)
                    handler.removeCallbacks(runnable)
                }else{
                    doubleTap = !doubleTap
                    handler.postDelayed(runnable,500)
                }
            }
        }

        private fun giveLike(postid: String, isLiked: Boolean) {
            if (isLiked){
                FirebaseDatabase.getInstance().getReference("likes")
                    .child(postid)
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(isLiked)
            }else{
                FirebaseDatabase.getInstance().getReference("likes")
                    .child(postid)
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .removeValue()
            }
        }

        private fun mLikes(likes: TextView, postid: String) {
            val ref = FirebaseDatabase.getInstance().getReference("likes").child(postid)
            ref.addValueEventListener(object : ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount > 0){
                        likes.text = snapshot.childrenCount.toString() + " Likes"
                        likes.visibility = View.VISIBLE
                    }else{
                        likes.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

        private fun isLikes(postid: String) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val ref = FirebaseDatabase.getInstance().getReference("likes").child(postid)
            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(firebaseUser!!.uid).exists()){
                        likebutton.setImageResource(R.drawable.ic_liked)
                        likebutton.tag = "Liked"
                    }else{
                        likebutton.setImageResource(R.drawable.ic_like)
                        likebutton.tag = "Like"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    private fun publisherInfo(profileImage: CircleImageView, userName: TextView, publisher: TextView, publisherID: String) {
        val userRef = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/")
            .reference.child("Users").child(publisherID)
        userRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(Datasnapshot: DataSnapshot) {
                if(Datasnapshot.exists()){
                    val user = Datasnapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    userName.text = user!!.getUsername()
                    publisher.text = user!!.getFullname()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
