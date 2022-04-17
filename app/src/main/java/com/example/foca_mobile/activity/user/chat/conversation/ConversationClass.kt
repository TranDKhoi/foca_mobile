package com.example.foca_mobile.activity.user.chat.conversation

class ConversationClass {

    var message: String? = null
    var senderId: String? = null

    constructor() {};

    constructor(mess: String?, id: String?) {
        this.message = mess;
        this.senderId = id;
    }
}