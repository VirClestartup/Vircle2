package id.kharisma.studio.vircle.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.kharisma.studio.vircle.AccountSettingActivity
import id.kharisma.studio.vircle.Adapter.PostAdapter
import id.kharisma.studio.vircle.Login_Activity
import id.kharisma.studio.vircle.Model.Post
import id.kharisma.studio.vircle.R
import id.kharisma.studio.vircle.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var user : FirebaseAuth
    private var postAdapter : PostAdapter? = null
    private var postList: MutableList<Post>? = null
    private var followingList: MutableList<Post>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        user = FirebaseAuth.getInstance()
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.chat.setOnClickListener{
            val intent = Intent(context, ActivityChat::class.java)
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        var recyclerView: RecyclerView? = null
        recyclerView = view.findViewById(R.id.recycleHome)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        postAdapter = context?.let {PostAdapter(it, postList as ArrayList<Post>)}
        recyclerView.adapter = postAdapter
        checkFollowings()
        return view
    }

    private fun checkFollowings() {
        followingList = ArrayList()
        val followingRef = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
                .child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Following")
        followingRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    (followingList as ArrayList<String>).clear()
                    for (snapshots in snapshot.children) {
                        snapshots.key?.let { (followingList as ArrayList<String>).add(it)}
                    }
                    retrievePosts()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/").reference
            .child("posts")
        postsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                postList?.clear()

                for(snapshots in snapshot.children){
                    val post = snapshot.getValue(Post::class.java)
                    for (id in (followingList as ArrayList<String>)){
                        if(post!!.getPublisher() == id){
                            postList!!.add(post)
                        }
                        postAdapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}