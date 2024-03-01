package com.example.chat_io


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class messageAdapter(val context: Context, private val messagelist:ArrayList<messages>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val item_receive = 1
    val item_sent = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType ==1){
            //inflate receive
            val view:View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return receiveviewholder(view)
        }else{
            //inflate sent
            val view:View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return sendviewholder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentmessage = messagelist[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentmessage.senderid)){
            return item_sent
        }else{
            return item_receive
        }
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentmessage = messagelist[position]
        if(holder.javaClass == sendviewholder::class.java){
            val viewHolder = holder as sendviewholder
            holder.sentmessage.text = currentmessage.message
        }else{
                val viewHolder = holder as receiveviewholder
                holder.receivemessage.text = currentmessage.message
        }
    }
    class sendviewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentmessage = itemView.findViewById<TextView>(R.id.sending)
    }
    class receiveviewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receivemessage = itemView.findViewById<TextView>(R.id.receiving)
    }
}