package id.kharisma.studio.vircle.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.kharisma.studio.vircle.AccountsettingActivity
import id.kharisma.studio.vircle.Login_Activity
import id.kharisma.studio.vircle.R
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.Dispatchers.Main


class ProfileFragment : Fragment() {
    lateinit var user : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.btn_editProfile.setOnClickListener {
            startActivity(Intent(context, AccountsettingActivity::class.java))
        }
        if (user.currentUser != null){
            user.currentUser?.let{
                view.profile_username.text = it.email
            }
        }
        view.btnsignout.setOnClickListener {
            user.signOut()
            startActivity(Intent(context,Login_Activity::class.java))
        }
        return view
    }

}