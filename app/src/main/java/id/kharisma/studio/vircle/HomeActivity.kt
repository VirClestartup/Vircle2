package id.kharisma.studio.vircle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.kharisma.studio.vircle.databinding.ActivityhomepageBinding
import id.kharisma.studio.vircle.fragment.HomeFragment

class HomeActivity : AppCompatActivity() {
    val fragmentHome : HomeFragment = HomeFragment()
    lateinit var binding : ActivityhomepageBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityhomepage)
    }
}

class ActivityhomepageBindingBinding {

}
