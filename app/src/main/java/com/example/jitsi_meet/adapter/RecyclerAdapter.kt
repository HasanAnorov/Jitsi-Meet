package com.example.jitsi_meet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jitsi_meet.databinding.ItemViewBinding
import com.example.jitsi_meet.model.RoomModel


class RecyclerAdapter(val itemClick: (roomName: RoomModel) -> Unit): RecyclerView.Adapter<RecyclerAdapter.ViewHolderHomeFragment>() {

    private val itemCallback = object : DiffUtil.ItemCallback<RoomModel>(){
        override fun areItemsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
            return oldItem.room_name == newItem.room_name
        }

    }

    var differ = AsyncListDiffer(this, itemCallback)

    inner class ViewHolderHomeFragment(private var binding: ItemViewBinding): RecyclerView.ViewHolder(binding.root){

        fun onBind(model: RoomModel){
            binding.roomCount.text = "${model.room_count.toString()} ."
            binding.roomName.text = model.room_name
            binding.roomPurpose.text = model.room_purpose

            binding.root.setOnClickListener {
                itemClick.invoke(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHomeFragment {
        return  ViewHolderHomeFragment(
            ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolderHomeFragment, position: Int) {
        holder.onBind(differ.currentList[position])
    }

}