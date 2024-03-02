package com.example.ayosapp.chat

import com.google.firebase.Timestamp

class User {
    var inbox_id: String? = null
    var worker_id: String? = null
    var name: String? = null
    var last_message: String? = null
    var time: Timestamp? = null

    constructor(){

    }

    constructor(inbox_id:String?, worker_id:String?, name: String?, lastmessage:String?, time:Timestamp?){
        this.inbox_id = inbox_id
        this.worker_id = worker_id
        this.name = name
        this.last_message = lastmessage
        this.time = time

    }
}