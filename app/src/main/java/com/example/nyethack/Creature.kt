package com.example.nyethack

import kotlin.random.Random

interface Fightable {
    val name: String
    var healthPoints: Int
    val diceCount: Int
    val diceSides: Int
    fun takeDamage(damage: Int)
    fun attack(opponent: Fightable){
        val damageRoll = (0 until diceCount).sumOf {
            Random.nextInt(diceSides+1)
        }
        narrate("$name deals $damageRoll to ${opponent.name}")
        opponent.takeDamage(damageRoll)
    }
}

abstract class Monster(
    override val name: String,
    val description: String,
    override var healthPoints: Int
):Fightable{
    override fun takeDamage(damage: Int) {
        healthPoints-=damage
    }
}
class Goblin(
    name: String = "Goblin",
    description: String = "A nasty-looking goblin",
    healthPoints: Int = 30
): Monster(name, description, healthPoints){
    override val diceCount = 2
    override val diceSides = 8
}

class Wolf(
    name: String = "Wolf",
    description: String = "А big shaggy beast",
    healthPoints: Int = 20
): Monster(name, description, healthPoints){
    override val diceCount: Int = 2
    override val diceSides: Int = 6
}

class Dragon(
    name: String = "Dragon",
    description: String = "Big dragon",
    healthPoints: Int = 100
):Monster(name, description, healthPoints){
    override val diceCount: Int = 3
    override val diceSides: Int = 6
}