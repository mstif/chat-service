package ru.netology

object ChatService {
    var chatList: MutableList<Chat> = mutableListOf()
    var userList: MutableList<User> = mutableListOf()
    var userService = createUser("MeUser")
    fun clear(){

        chatList = mutableListOf()
        userList = mutableListOf()
        userService = createUser("MeUser")
    }

    private fun <T : Uniqueness> getNextId(mutableList: MutableList<T>): Int {
        if (mutableList.isEmpty()) return 1
        return mutableList.last().id + 1
    }


    fun createUser(name: String): User {
        var user = userList.find { it.name == name }
        return if (user == null) {
            user = User(name, id = getNextId(userList))
            userList.add(user)
            userList.last()
        } else user
    }

    private fun getUser(userId: Int): User? = userList.find { it.id == userId }

    fun createChat(user: User): Chat {
        if(user== userService) throw RuntimeException("Чат с самим собой невозможен!")
        var chat = chatList.find { it.userSecond == user }
        return if (chat == null) {
            chat = Chat(userFirst = userService, userSecond = user, id = getNextId(chatList))
            chatList.add(chat)
            chatList.last()
        } else chat
    }


    fun deleteChat(idChat: Int): Boolean {
        var chat = chatList.find { it.id == idChat } ?: throw ElementNotFoundException(idChat)
        val index = chatList.indexOf(chat)
        val chatCopy = chat.copy(isDeleted = true)
        chatCopy.messages.forEach {
            val copyMessage = it.copy(isDeleted = true)
            chatCopy.messages[chatCopy.messages.indexOf(it)] = copyMessage
        }
        chatList[index] = chatCopy
        return chatList[index] == chatCopy

    }

    fun getChats(): List<Chat> =
        chatList.filter { !it.isDeleted && it.messages.find { message -> !message.isDeleted } != null }

    fun getChatUser(userId: Int): Chat? = chatList.find { it.userSecond.id == userId }

/*
    fun addMessageFromUser(userId: Int, message: String): Boolean {
        val user = getUser(userId) ?: throw ElementNotFoundException(userId)
        val chat = createChat(user)
        return chatList[chatList.indexOf(chat)].messages.add(
            Message(
                message,
                id = getNextId(chat.messages),
                fromUserId = userId,
                toUserId = userService.id
            )
        )
    }

    fun addMessageToUser(userId: Int, message: String): Boolean {
        val user = getUser(userId) ?: throw ElementNotFoundException(userId)
        val chat = createChat(user)
        return chatList[chatList.indexOf(chat)].messages.add(
            Message(
                message,
                id = getNextId(chat.messages),
                fromUserId = userService.id,
                toUserId = userId
            )
        )
    }

*/
    fun deleteMessage(idMessage: Int, idChat: Int): Boolean {
        val chat: Chat = chatList.find { it.id == idChat } ?: throw ElementNotFoundException(idChat)
        val message = chat.messages.find { it.id == idMessage } ?: throw ElementNotFoundException(idChat)
        val index = chat.messages.indexOf(message)
        if (message.isDeleted) throw ElementDeletedException(message.id)
        val messageCopy = message.copy(isDeleted = true)
        val indexChat = chatList.indexOf(chat)
        chatList[indexChat].messages[index] = messageCopy
        if (chatList[indexChat].messages.find { !it.isDeleted } == null) {
            chatList[indexChat] = chat.copy(isDeleted = true)
        }
        return chat.messages[index].isDeleted
    }

    fun editMessage(text: String, idMessage: Int, idChat: Int): Boolean {
        val chat: Chat = chatList.find { it.id == idChat } ?: throw ElementNotFoundException(idChat)
        val message = chat.messages.find { it.id == idMessage } ?: throw ElementNotFoundException(idChat)
        val index = chat.messages.indexOf(message)
        if (message.isDeleted) throw ElementDeletedException(message.id)
        val messageCopy = message.copy(text = text)
        val indexChat = chatList.indexOf(chat)
        chatList[indexChat].messages[index] = messageCopy
        return chatList[indexChat].messages[index].text == text
    }

    fun readMessage(idMessage: Int, idChat: Int): String {
        val chat: Chat = chatList.find { it.id == idChat } ?: throw ElementNotFoundException(idChat)
        val message = chat.messages.find { it.id == idMessage } ?: throw ElementNotFoundException(idChat)
        val index = chat.messages.indexOf(message)
        if (message.isDeleted) throw ElementDeletedException(message.id)
        val messageCopy = message.copy(isRead = true)
        chat.messages[index] = messageCopy
        println(messageCopy.text)
        return messageCopy.text

    }

    fun getUnreadChatsCount(forUserId: Int): Int {
        return chatList.count {
            !it.isDeleted &&
                    it.messages.find { message -> !message.isRead && !message.isDeleted && message.toUserId == forUserId } != null
        }
    }

    fun getMessages(idChat: Int, idMessage: Int, count: Int): List<Message> {
        val chat = chatList.find { it.id == idChat && !it.isDeleted } ?: throw ElementNotFoundException(idChat)
        val listMessages = chatList[chatList.indexOf(chat)].messages

        val messages = chat.messages.takeLastWhile { it.id != idMessage && !it.isDeleted }
            .take(count).map { if (it.toUserId == userService.id) it.apply { isRead = true } else it }

        listMessages.forEach {
            val copyMessage = messages.find { mess -> mess.id == it.id }
            if (copyMessage != null) {
                listMessages[listMessages.indexOf(it)] = copyMessage
            }

        }
        return messages
    }

    fun createMessage(toUserId: Int, fromUserId: Int, message: String): Message {
        var corrUserId = toUserId
        if (toUserId == userService.id) {
            corrUserId = fromUserId
        }
        val user = getUser(corrUserId) ?: throw ElementNotFoundException(corrUserId)
        val chat = createChat(user)
        val result = Message(
            message,
            id = getNextId(chat.messages),
            fromUserId = fromUserId,
            toUserId = toUserId,
            chatId = chat.id
        )
        chatList[chatList.indexOf(chat)].messages.add(result)
        return result
    }

}