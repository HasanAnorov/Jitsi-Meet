package com.example.jitsi_meet.presenter

import com.example.jitsi_meet.MainActivity
import com.example.jitsi_meet.model.RoomModel
import com.example.jitsi_meet.utils.MySharedPreference
import com.google.gson.Gson
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions

class MainActivityPresenter(private val view: MainActivity) {

    fun enterRoom(model: RoomModel){
        val option = JitsiMeetConferenceOptions.Builder()
            .setRoom(model.room_name)
            .build()
        JitsiMeetActivity.launch(view.applicationContext,option)
    }

    fun saveUser(model: RoomModel,data:ArrayList<RoomModel>){
        val gson = Gson()
        val roomNames = ArrayList<RoomModel>()

        if (data.isEmpty()) {
            roomNames.add(model)
        } else {
            roomNames.addAll(data)
            roomNames.add(model)
        }
        val userData = gson.toJson(roomNames)
        MySharedPreference.roomNames = userData
    }

    fun submitListToRecycler(data:ArrayList<RoomModel>){
        view.setData(data)
    }
}

