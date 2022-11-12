package id.kharisma.studio.vircle.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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
            requireActivity().run{
                startActivity(Intent(this, Login_Activity::class.java))
                finish()
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
            }
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

}