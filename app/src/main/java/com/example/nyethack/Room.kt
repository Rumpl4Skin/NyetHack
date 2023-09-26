package com.example.nyethack

open class Room(val name: String) {
    protected open val status = "Calm"
    open fun description() = "$name (Currently: $status)"
    open fun enterRoom() {
        narrate("There is nothing to do here")
    }

}
class TownSquare : Room("The Town Square"){
    override val status = "Bustling"
    private var bellSound = "GWONG"
   final override fun enterRoom(){
        narrate("The villagers rally and cheer as the hero enters")
        ringBell()
    }

    public fun ringBell() {
        narrate("The bell tower announces the hero's presence: $bellSound")
    }
}

class MonsterRoom(
    name: String,
    var monster: Monster? = Goblin()
):Room(name){
    override fun description()=super.description()+ " (Creature: ${monster?.description ?: "None"})"

    override fun enterRoom() {
        if (monster == null) {
            super.enterRoom()
        } else {
            narrate("Danger is lurking in this room")
        }
    }
}