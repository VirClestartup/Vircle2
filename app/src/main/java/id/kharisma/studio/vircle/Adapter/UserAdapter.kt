package id.kharisma.studio.vircle.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.R

class UserAdapter (private var mContext: Context,
private var mUser: List<User>,
private var isFragment: Boolean = false) : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false)
        return UserAdapter.ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUser[position]
        holder.userUsernameTextView.text = user.getUsername()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userProfileImage)


    }
    class ViewHolder ( @NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var userUsernameTextView: TextView = itemView.findViewById(R.id.username_search)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.userprofile_item)
        var followButton: Button = itemView.findViewById(R.id.btnFollow)

    }
}