package id.kharisma.studio.vircle.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import id.kharisma.studio.vircle.ActivityChat
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.R

class UsersAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.txtUserName.text = user.getUsername()
        Glide.with(context).load(user.getImage()).placeholder(R.drawable.profile).into(holder.imgUser)

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context,ActivityChat::class.java)
            intent.putExtra("userId",user.getUID())
            intent.putExtra("userName",user.getUsername())
            intent.putExtra("image",user.getImage())
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtUserName:TextView = view.findViewById(R.id.userName)
        val txtTemp:TextView = view.findViewById(R.id.temp)
        val imgUser:CircleImageView = view.findViewById(R.id.userImage)
        val layoutUser:LinearLayout = view.findViewById(R.id.layoutUser)
    }
}