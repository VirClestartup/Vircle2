package id.kharisma.studio.vircle.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.kharisma.studio.vircle.Adapter.PostAdapter
import id.kharisma.studio.vircle.BlankActivity
import id.kharisma.studio.vircle.Model.Post
import id.kharisma.studio.vircle.R
import id.kharisma.studio.vircle.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.view.*


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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        var recyclerView: RecyclerView? = null
        recyclerView = view.findViewById(R.id.recycleHome)
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        //val btnchat = view.findViewById<Button>(R.id.btn_Chat)as Button
        view.btn_Chat.setOnClickListener{
            //Toast.makeText(context, "You clicked me.", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, BlankActivity::class.java)
            startActivity(intent)
        }
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = LinearLayoutManager(context)

        postList = ArrayList()
        postAdapter = context?.let {
            PostAdapter(it, postList as ArrayList<Post>)}
        recyclerView.adapter = postAdapter
        checkFollowings()

        return view
    }

    private fun checkFollowings() {
        followingList = ArrayList()
        val followingRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Following")
        followingRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if(datasnapshot.exists()){
                    (followingList as ArrayList<String>).clear()
                    for (snapshot in datasnapshot.children) {
                        snapshot.key?.let { (followingList as ArrayList<String>).add(it)}
                    }
                    retrievePosts()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun retrievePosts() {
        val postsRef = FirebaseDatabase.getInstance().getReference("Post")
        postsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(Datasnapshot: DataSnapshot) {
                postList?.clear()

                for(snapshot in Datasnapshot.children){
                    val post = snapshot.getValue(Post::class.java)
                    for (id in (followingList as ArrayList<String>)){
                        if(post!!.getPublisher().equals(id) == true){
                            postList?.add(post)
                        }

                    }
                }
                postAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}