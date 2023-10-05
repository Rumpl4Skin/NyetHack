package com.example.nyethack

import kotlin.random.Random

open class Room(val name: String) {
    protected open val status = "Calm"
    open val lootBox = LootBox.random()
    open fun description() = "$name (Currently: $status)"
    open fun enterRoom() {
        narrate("There is nothing to do here")
    }

}

class TownSquare : Room("The Town Square") {
    override val status = "Bustling"
    private var bellSound = "GWONG"
    val hatDropOffBox = DropOffBox<Hat>()
    val gemDropOffBox = DropOffBox<Gemstones>()
    final override fun enterRoom() {
        narrate("The villagers rally and cheer as the hero enters")
        ringBell()
    }

    public fun ringBell() {
        narrate("The bell tower announces the hero's presence: $bellSound")
    }

    fun <T> sellLoot(
        loot: T
    ): Int where T : Loot, T : Sellable {
        return when (loot) {
            is Hat -> hatDropOffBox.sellLoot(loot)
            is Gemstones -> gemDropOffBox.sellLoot(loot)
            else -> 0
        }
    }
}

class MonsterRoom(
    name: String,
    var monster: Monster? = null
) : Room(name) {
    init {
        when (Random.nextInt(0, 4)) {
            0 -> monster = null
            1 -> monster = Goblin()
            2 -> monster = Wolf()
            3 -> monster = Dragon()
        }
    }

    override fun description() =
        super.description() + " (Creature: ${monster?.description ?: "None"})"

    override fun enterRoom() {
        if (monster == null) {
            super.enterRoom()
        } else {
            narrate("Danger is lurking in this room")
        }
    }
}