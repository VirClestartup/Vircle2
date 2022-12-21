package id.kharisma.studio.vircle.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.kharisma.studio.vircle.Adapter.UserAdapter
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.R
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var userAdapter: UserAdapter? = null
    private var mUser: MutableList<User>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.recycleSearch)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mUser = ArrayList()
        userAdapter = context?.let {
            UserAdapter(it, mUser as ArrayList<User>)}
        recyclerView?.adapter = userAdapter

        view.Search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.Search.text.toString() == ""){

                }else{
                    //recyclerView?.visibility = View.VISIBLE

                    retrieveUsers()
                    searchUser(s.toString().toLowerCase())
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        retrieveUsers()
        return view
    }

    private fun searchUser(input: String) {
        val query = FirebaseDatabase.getInstance().getReference()
            .child("Users").orderByChild("Fullname")
            .startAt(input).endAt(input + "\uf0ff")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser?.clear()
                for (snapehot in dataSnapshot.children){
                    val user = snapehot.getValue(User::class.java)
                    if (user != null){
                        mUser?.add(user)
                    }
                }
                userAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun retrieveUsers() {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (view?.Search?.text.toString() == ""){
                    mUser?.clear()
                    for (snapehot in dataSnapshot.children){
                        val user = snapehot.getValue(User::class.java)
                        if (user != null){
                            mUser?.add(user)
                        }
                    }
                    userAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

}