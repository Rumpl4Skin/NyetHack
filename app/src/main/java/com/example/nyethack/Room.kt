package com.example.nyethack

open class Room(val name: String) {
    protected open val status = "Calm"
    fun description() = "$name (Currently: $status)"
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

    fun ringBell() {
        narrate("The bell tower announces the hero's presence: $bellSound")
    }
}