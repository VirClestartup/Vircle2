package id.kharisma.studio.vircle.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.kharisma.studio.vircle.Model.Community
import id.kharisma.studio.vircle.Model.User
import id.kharisma.studio.vircle.R

class AdapterCommunity(private var mContext: Context,
                       private var mCommunity: List<Community>) : RecyclerView.Adapter<AdapterCommunity.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_community,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mCommunity.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val community = mCommunity[position]
        holder.Name.text = community.getCommunityName()
        holder.Description.text = community.getDeskripsi()
        Picasso.get().load(community.getcommunityImage()).placeholder(R.drawable.profile).into(holder.Image)
        publisherInfo(holder.publisher, community.getpublisher())
    }

    private fun publisherInfo(publisher: TextView, publisherID: String) {
        val comRef = FirebaseDatabase.getInstance("https://vircle-77b59-default-rtdb.firebaseio.com/")
            .reference.child("Users").child(publisherID)
        comRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user = snapshot.getValue(User::class.java)
                    publisher.text = user!!.getFullname()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var Name : TextView = itemView.findViewById(R.id.communityname)
        var publisher: TextView = itemView.findViewById(R.id.publisher)
        var Description: TextView = itemView.findViewById(R.id.description)
        var Image : CircleImageView = itemView.findViewById(R.id.community_item)
        var btnJoin: Button = itemView.findViewById(R.id.btnJoin)
    }



}