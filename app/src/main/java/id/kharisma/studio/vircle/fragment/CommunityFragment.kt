package id.kharisma.studio.vircle.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.kharisma.studio.vircle.AccountSettingActivity
import id.kharisma.studio.vircle.ActivityPopularcommun
import id.kharisma.studio.vircle.R
import kotlinx.android.synthetic.main.fragment_community.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class CommunityFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)
        view.txt_Kominitaspopular.setOnClickListener {
            val intent = Intent(context, ActivityPopularcommun::class.java)
            startActivity(intent)
        }
        return view
    }


}