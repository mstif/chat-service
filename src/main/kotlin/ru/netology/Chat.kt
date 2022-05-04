package ru.netology

data class Chat(
    val userFirst: User = User(),
    val userSecond:User = User(),
    var messages: MutableList<Message> = mutableListOf(),
    override val id:Int = 0,
    override val isDeleted:Boolean = false): Uniqueness()
 {
}