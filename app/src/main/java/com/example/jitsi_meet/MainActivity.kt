package com.example.jitsi_meet

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jitsi_meet.adapter.RecyclerAdapter
import com.example.jitsi_meet.databinding.ActivityMainBinding
import com.example.jitsi_meet.model.RoomModel
import com.example.jitsi_meet.presenter.MainActivityPresenter
import com.example.jitsi_meet.utils.MySharedPreference
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter : RecyclerAdapter
    private lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        presenter = MainActivityPresenter(this)
        MySharedPreference.init(this)
        val gson = Gson()

        // status bar text color
        window.decorView.systemUiVisibility =(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or  View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)

        //status bar color
        window.statusBarColor = getColor(R.color.white)
        window.navigationBarColor = getColor(R.color.white)


        val data = MySharedPreference.getUserDataToMainActivity()
        var roomNames  = ArrayList<RoomModel>()
        if (data != ""){
            val type = object : TypeToken<ArrayList<RoomModel>>() {}.type
            roomNames = gson.fromJson<ArrayList<RoomModel>>(data,type)
            presenter.submitListToRecycler(roomNames)
        }

        adapter = RecyclerAdapter {
            val option = JitsiMeetConferenceOptions.Builder()
                .setRoom(it.room_name)
                .build()
            JitsiMeetActivity.launch(this,option)
        }

        try {
            val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL(""))
                .setWelcomePageEnabled(false)
                .build()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        binding.btnEnterRoom.setOnClickListener {
            val roomName = binding.textInputEditPassword.text.toString()
            var roomPurpose = binding.textInputEditLogin.text.toString()
            if (roomPurpose.isEmpty()){
               roomPurpose = "Not entered"
            }
            if (roomName.isEmpty()){
                Snackbar.make(it, "Please fill field !", Snackbar.LENGTH_SHORT)
                       .setAction("Action", null).show()
            }else{
                presenter.enterRoom(RoomModel(roomPurpose,roomName,adapter.differ.currentList.size+1))
                presenter.saveUser(RoomModel(roomPurpose,roomName,adapter.differ.currentList.size+1),roomNames)
                roomNames.add(RoomModel(roomPurpose,roomName,adapter.differ.currentList.size+1))
                presenter.submitListToRecycler(roomNames)

                binding.textInputEditPassword.text?.clear()
                binding.textInputEditLogin.text?.clear()
            }
        }
    }

    fun setData(models:ArrayList<RoomModel>){
        adapter = RecyclerAdapter {
            val option = JitsiMeetConferenceOptions.Builder()
                .setRoom(it.room_name)
                .build()
            JitsiMeetActivity.launch(this,option)
        }
        adapter.differ.submitList(models)
        binding.recyclerView.adapter = adapter
    }
}