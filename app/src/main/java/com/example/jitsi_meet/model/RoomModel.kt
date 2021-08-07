package com.example.jitsi_meet.model

class RoomModel {
    var room_purpose:String = ""
    var room_name: String = ""
    var room_count:Int = 0

    constructor()

    constructor(room_purpose: String, room_name: String, room_count: Int) {
        this.room_purpose = room_purpose
        this.room_name = room_name
        this.room_count = room_count
    }

}