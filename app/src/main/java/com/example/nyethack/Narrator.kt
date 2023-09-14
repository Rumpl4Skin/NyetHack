package com.example.nyethack

import kotlin.random.Random
import kotlin.random.nextInt

var narrationModifier: (String) -> String = { it }
  inline fun narrate(
        message: String,
        modifier: (String) -> String = {narrationModifier(it)}
    ) {
        println(narrationModifier(message))
    }

fun changeNarratorMood(){
    val mood: String
    val modifier: (String) -> String
    when(Random.nextInt(1..6)){
        1->{
            mood = "loud"
            modifier = {message ->
                val numExclamationPoint = 3
                message.uppercase()+ "!".repeat(numExclamationPoint)
            }
        }
        2 -> {
            mood = "tired"
            modifier = { message ->
                message.lowercase().replace(" ", "... ")
            }
        }
        3 -> {
            mood = "unsure"
            modifier = {message ->
                "$message?"
            }
        }
        4 -> {
            var narrationsGiven = 0
            mood = "like sending an itemized bill"
            modifier = { message ->
                narrationsGiven++
                "$message.\n(I have narrated $narrationsGiven things)"
            }
        }
        5-> {
            mood ="mysterious"
            modifier= { message ->
                message.replace("l" ,"1")
                message.replace("e","3")
                message.replace("t","7")
                message.replace("L" ,"1")
                message.replace("E","3")
                message.replace("T","7")
            }
        }
        else -> {
            mood = "professional"
            modifier = {message ->
                "$message."
            }
        }
    }
    narrationModifier = modifier
    narrate("The narrator begins to feel $mood")
}
