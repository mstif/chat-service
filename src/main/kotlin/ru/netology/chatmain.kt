package ru.netology

fun main() {
    val chat = ChatService.createChat(ChatService.createUser("user1"))
    ChatService.deleteChat(chat.id)
}