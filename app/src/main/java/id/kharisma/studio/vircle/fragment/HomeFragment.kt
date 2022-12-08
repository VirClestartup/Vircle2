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
import id.kharisma.studio.vircle.AccountSettingActivity
import id.kharisma.studio.vircle.Login_Activity
import id.kharisma.studio.vircle.R
import id.kharisma.studio.vircle.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var user : FirebaseAuth
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

        return view
    }

}