package ru.netology


data class User(val name:String="",
                override val id :Int=0): Uniqueness()