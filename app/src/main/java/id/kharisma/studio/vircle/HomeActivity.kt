package id.kharisma.studio.vircle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import id.kharisma.studio.vircle.databinding.ActivityhomepageBinding
import id.kharisma.studio.vircle.fragment.CommunityFragment
import id.kharisma.studio.vircle.fragment.FriendsFragment
import id.kharisma.studio.vircle.fragment.HomeFragment
import id.kharisma.studio.vircle.fragment.NotifikasiFragment


class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityhomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityhomepageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeMenu -> replaceFragment(HomeFragment())
                R.id.CommunityMenu -> replaceFragment(CommunityFragment())
                R.id.FriendsMenu -> replaceFragment(FriendsFragment())
                R.id.NotifikasiMenu -> replaceFragment(NotifikasiFragment())
                else -> {

                }
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.Homepagescreen,fragment)
        fragmentTransaction.commit()
    }

}
