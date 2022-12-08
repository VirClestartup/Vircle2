package id.kharisma.studio.vircle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import id.kharisma.studio.vircle.databinding.ActivityhomepageBinding
import id.kharisma.studio.vircle.fragment.*


class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityhomepageBinding
    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var user : FirebaseAuth


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.homeMenu -> {
                movetoFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.CommunityMenu -> {
                movetoFragment(CommunityFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.SearchMenu -> {
                movetoFragment(SearchFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.ThreadMenu -> {
                it.isChecked
                startActivity(Intent(this@HomeActivity, ActivityMembuatThread::class.java))
                return@OnNavigationItemSelectedListener true
            }
            R.id.ProfileMenu -> {
                movetoFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityhomepageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityhomepage)

        val nav_View : BottomNavigationView = findViewById(R.id.bottomNavigation)

        nav_View.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        user = FirebaseAuth.getInstance()
        movetoFragment(HomeFragment())
    }
    private fun movetoFragment(fragment: Fragment){
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.FragmentContainer, fragment)
        fragmentTrans.commit()
    }


}

