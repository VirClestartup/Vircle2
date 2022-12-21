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
import id.kharisma.studio.vircle.Adapter.AdapterCommunity
import id.kharisma.studio.vircle.Model.Community
import id.kharisma.studio.vircle.R
import kotlinx.android.synthetic.main.fragment_community.view.*


class CommunityFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var Adapter: AdapterCommunity? = null
    private var mStress: MutableList<Community>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)
        recyclerView = view.findViewById(R.id.recycleCommunity)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mStress = ArrayList()
        Adapter = context?.let {
            AdapterCommunity(it, mStress as ArrayList<Community>)
        }
        recyclerView?.adapter = Adapter

        view.searchcommunity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.searchcommunity.text.toString() == ""){

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
            .child("Community").orderByChild("Name")
            .startAt(input).endAt(input + "\uf0ff")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mStress?.clear()
                for (snapehot in dataSnapshot.children){
                    val community = snapehot.getValue(Community::class.java)
                    if (community != null){
                        mStress?.add(community)
                    }
                }
                Adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun retrieveUsers() {
        val usersRef = FirebaseDatabase.getInstance().getReference("Community")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (view?.searchcommunity?.text.toString() == ""){
                    mStress?.clear()
                    for (snapshot in dataSnapshot.children){
                        val community = snapshot.getValue(Community::class.java)
                        if (community != null){
                            mStress?.add(community)
                        }
                    }
                    Adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


}