package id.kharisma.studio.vircle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.kharisma.studio.vircle.databinding.ActivityCommunitypopularBinding

class ActivityPopularcommun : AppCompatActivity() {
    lateinit var binding : ActivityCommunitypopularBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCommunitypopularBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.txtKomunitassayapop.setOnClickListener {

        }
    }
}