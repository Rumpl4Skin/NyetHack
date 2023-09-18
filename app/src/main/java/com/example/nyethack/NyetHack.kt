package com.example.nyethack
val player = Player()
fun main(){
    narrate("${player.name} is ${player.title}")
    player.changeName("Aurelia")
    //changeNarratorMood()
    narrate("${player.name}, ${player.title}, heads to the town square")
    visitTavern()

    player.castFireball()
}

private fun promptHeroName(): String {
    narrate("A hero enters the town of Kronstadt. What is their name?") { message ->
        // Выводит message желтым цветом
        "\u001b[33;1m$message\u001b[0m"
    }
    /*val input = readLine()
    require(input != null && input.isNotEmpty()) {
    "The hero must have a name."
    }
    return input*/
    println("Madrigal")
    return "Madrigal"
}
