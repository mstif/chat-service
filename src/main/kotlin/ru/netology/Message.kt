package ru.netology

data class Message(val text: String = "",
                   override val id: Int = 0,
                   override val isDeleted: Boolean = false,
                   val fromUserId:Int=0,
                   val toUserId:Int,
                   var isRead:Boolean=false,
                   val chatId:Int=0) :
    Uniqueness() {
}