package ru.netology

class ElementNotFoundException(id:Int):RuntimeException( "Элемент с $id не найден")
class ElementDeletedException(id: Int):RuntimeException( "Элемент уже удален")
