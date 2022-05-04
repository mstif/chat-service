package ru.netology

import org.junit.Assert.assertTrue
import org.junit.Test

class ChatServiceTest {

    @Test
    fun createChat_For_User() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val chat = ChatService.createChat(user1)
        assertTrue(ChatService.chatList[0] == chat)
    }
    @Test(expected = RuntimeException::class)
    fun createChat_For_User_Exeption() {
        ChatService.clear()

        val chat = ChatService.createChat(ChatService.userService)

    }

    @Test
    fun deleteChat_For_User() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val chat = ChatService.createChat(user1)
        ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение для ${user1.name}")
        var result = ChatService.chatList[0] == chat
        result = if (!ChatService.deleteChat(chat.id)) false else result
        result = if (!ChatService.chatList[0].isDeleted) false else result
        result = if (!ChatService.chatList[0].messages[0].isDeleted) false else result
        assertTrue(result)

    }
    @Test(expected = ElementNotFoundException::class)
    fun deleteChat_For_User_Exeption() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val chat = ChatService.createChat(user1)
        ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение для ${user1.name}")
        var result = ChatService.chatList[0] == chat
       ChatService.deleteChat(chat.id+1)


    }

    @Test
    fun createMessage_For_New_User() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message =
            ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение для ${user1.name}")
        val resultMessage = ChatService.chatList[0].messages[0]
        assertTrue(resultMessage == message && resultMessage.toUserId == user1.id)
    }

    @Test
    fun createMessage_From_New_User() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )
        val resultMessage = ChatService.chatList[0].messages[0]
        assertTrue(resultMessage == message && resultMessage.fromUserId == user1.id)
    }

    @Test(expected = ElementNotFoundException::class)
    fun createMessage_From_New_User_Exception() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message = ChatService.createMessage(
            ChatService.userService.id,
            user1.id+1,
            "Новое сообщение для ${ChatService.userService.name}"
        )

    }


    @Test
    fun deleteMessages() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )
        val message2 =
            ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение для ${user1.name}")

        ChatService.deleteMessage(message1.id, message1.chatId)

        var result = true
        result = if (!ChatService.chatList[0].messages[0].isDeleted) false else result
        result = if (ChatService.chatList[0].messages[1].isDeleted) false else result

        ChatService.deleteMessage(message2.id, message2.chatId)

        result = if (!ChatService.chatList[0].isDeleted) false else result
        result = if (!ChatService.chatList[0].messages[1].isDeleted) false else result
        assertTrue(result)
    }

    @Test(expected = ElementNotFoundException::class)
    fun deleteMessages_Exeption() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )


        ChatService.deleteMessage(message1.id+1, message1.chatId)

    }

    @Test(expected = ElementNotFoundException::class)
    fun deleteMessages_Exeption_1() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )


        ChatService.deleteMessage(message1.id, message1.chatId+1)

    }




    @Test
    fun editMessages() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )
        val message2 =
            ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение для ${user1.name}")
        val newText = "Исправлено сообщение"
        var result = ChatService.editMessage(newText, message1.id, message1.chatId)

        result = if (ChatService.chatList[0].messages[0].text != newText) false else result
        assertTrue(result)
    }
    @Test(expected = ElementNotFoundException::class)
    fun editMessages_Exeption() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )
        val newText = "Исправлено сообщение"
        var result = ChatService.editMessage(newText, message1.id+1, message1.chatId)


    }
    @Test(expected = ElementNotFoundException::class)
    fun editMessages_Exeption_1() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )
        val newText = "Исправлено сообщение"
        var result = ChatService.editMessage(newText, message1.id, message1.chatId+1)


    }

    @Test
    fun getChat_For_User() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val chat = ChatService.createChat(user1)

        assertTrue(ChatService.getChatUser(user1.id)== chat)
    }
    @Test
    fun getChats() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val chat = ChatService.createChat(user1)
        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )
        val message2 =
            ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение для ${user1.name}")

        ChatService.deleteMessage(message1.id, message1.chatId)
        var chats = ChatService.getChats()
        var result = chats[0].messages[0].isDeleted
        ChatService.deleteMessage(message2.id, message2.chatId)
        chats = ChatService.getChats()
        result=if(chats.isNotEmpty())false else result
        assertTrue(result)
    }

    @Test
    fun getUnReadChats() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")
        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение для ${ChatService.userService.name}"
        )
        val message2 =
            ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение для ${user1.name}")

        var result = ChatService.getUnreadChatsCount(user1.id)==1

        ChatService.readMessage(message1.id,message1.chatId)
        result = if (ChatService.getUnreadChatsCount(user1.id)!=1) false else result

        ChatService.readMessage(message2.id,message2.chatId)
        result = if (ChatService.getUnreadChatsCount(user1.id)!=0) false else result

        assertTrue(result)
    }

    @Test
    fun getMessages() {
        ChatService.clear()
        val user1 = ChatService.createUser("user1")

        val message1 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение 1 для ${ChatService.userService.name}"
        )
        val message2 =
            ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение 2 для ${user1.name}")

        val message3 = ChatService.createMessage(
            ChatService.userService.id,
            user1.id,
            "Новое сообщение 3 для ${ChatService.userService.name}"
        )
        val message4 =
            ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение 4 для ${user1.name}")

        ChatService.deleteMessage(message2.id, message2.chatId)

        val resList = ChatService.getMessages(message2.chatId,message2.id,2)
        val chat = ChatService.getChatUser(user1.id)?:throw ElementNotFoundException(user1.id)
        var result = resList.count()==2
        result = if (resList[0].id!=message3.id ) false else result
        result = if (resList[1].id!=message4.id) false else result
        result = if (!chat.messages[2].isRead) false else result
        result = if (chat.messages[3].isRead) false else result
        assertTrue(result)
    }

    @Test(expected = ElementNotFoundException::class)
    fun getMessages_Exception (){
        ChatService.clear()
        val user1 = ChatService.createUser("user1")

        val message2 =
            ChatService.createMessage(user1.id, ChatService.userService.id, "Новое сообщение 2 для ${user1.name}")

        val resList = ChatService.getMessages(message2.chatId+1,message2.id,2)

    }




}