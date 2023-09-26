package com.example.nyethack

import kotlin.random.Random

object Game {
    private val worldMap = listOf(
        listOf(TownSquare(), Tavern(), Room("Back Room")),
        listOf(MonsterRoom("A Long Corridor"), Room("A Generic Room")),
        listOf(MonsterRoom("The Dungeon"))
    )

    private var currentRoom: Room = worldMap[0][0]
    private var currentPosition = Coordinate(0, 0)
    var exit = false

    init {
        narrate("Welcome, adventurer")
        val mortality = if (player.isImmortal) "an immortal" else "a mortal"
        narrate("${player.name}, $mortality, has ${player.healthPoints} health points")
    }

    fun play() {

        while (!exit) {
            narrate(
                "${player.name} of ${player.hometown}, ${player.title}," +
                        "is in ${currentRoom.description()}"
            )
            currentRoom.enterRoom()

            print("> Enter your command: ")
            GameInput(readLine()).processCommand()
        }
    }

    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }

        fun processCommand() = when (command.lowercase()) {
            "move" -> {
                val direction = Direction.values()
                    .firstOrNull { it.name.equals(argument, ignoreCase = true) }
                if (direction != null) {
                    move(direction)
                } else {
                    narrate("I don't know what direction that is")
                }
            }

            "cast" -> {
                if (argument == "fireball")
                    player.castFireball()
                else narrate("I don't know what cast that is")
            }

            "prophesize" -> narrate(player.prophecy)
            "exit" -> {
                narrate("Bye")
                exit = true
            }

            "map" -> viewMap()
            "ring" -> {
                if (currentRoom is TownSquare)
                repeat(argument.toIntOrNull()?: Random.nextInt(1,5)){
                    (currentRoom as TownSquare).ringBell()
                }
                else narrate("You should be in the square")
            }
            else -> narrate("I'm not sure what you're trying to do")
        }
    }

    fun move(direction: Direction) {
        val newPosition = direction.updateCoordinate(currentPosition)
        val newRoom = worldMap.getOrNull(newPosition.y)?.getOrNull(newPosition.x)
        if (newRoom != null) {
            narrate("The hero moves ${direction.name}")
            currentPosition = newPosition
            currentRoom = newRoom
        } else {
            narrate("You cannot move ${direction.name}")
        }
    }

    fun viewMap() {
        var map = ""
        worldMap.forEach {
            it.forEach {
                if (it.name != currentRoom.name)
                    map += "O"
                else map += "X"
                map += " "
            }
            map += "\n"
        }
        narrate(map)
    }
}

open class Weapon(val name: String, val type: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Weapon

        if (name != other.name) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}