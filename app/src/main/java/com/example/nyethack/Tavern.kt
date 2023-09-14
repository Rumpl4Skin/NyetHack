package com.example.nyethack

import java.io.File
import java.sql.Struct
import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

private const val TAVERN_MASTER = "Moo"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"
private val menuData =
    File("C:\\Users\\markk\\AndroidStudioProjects\\NyetHack\\app\\sampledata\\tavern-menu-data.txt")
        .readText()
        .split("\n")
        .map{ it.split(",") }

private val menuItems = menuData.map {  (_, name, _) -> name }

private val menuItemPrices = menuData.associate { (_, name, price) ->
    name to price.toDouble()
}

private val menuItemsTypes = menuData.associate { (type, name, _) ->
    name to type
}

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

fun visitTavern() {

    narrate("$heroName enters $TAVERN_NAME")

   // viewMenu(menuData)

    val patrons: MutableSet<String> = firstNames.shuffled()
        .zip(lastNames.shuffled()){ firstName, lastName -> "$firstName $lastName"
        }.toMutableSet()

    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        heroName to 4.50,
        *patrons.map {it to Random.nextDouble(10.0)}.toTypedArray()
    )


    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())

    val favoriteMenuItems = patrons.flatMap{ getFavoriteMenuItems(it) }
    val itemOfDay = favoriteMenuItems.fold(mutableMapOf<String,Int>()){
        list, item ->
         list.apply {
             this[item] = favoriteMenuItems.count { it == item }
         }
    }

   narrate("The item of the day is ${itemOfDay.maxByOrNull { it.value }?.key?:itemOfDay.keys.random()}" )

    //val itemOfDay = patrons.flatMap{ getFavoriteMenuItems(it) }
   // println("The item of the day is $itemOfDay")

    repeat(3) {
        //placeOrder(patrons.random(), menuItems.random(), patronGold)
        massivePlaceOrder(patrons.random(), List(Random.nextInt(1,3)){ menuItems.random()}, patronGold)
    }
    displayPatronBalances(patronGold)

    patrons
        .filter { patron -> patronGold.getOrDefault(patron, 0.0) < 4.0 }
        .also { departingPatrons ->
            patrons -= departingPatrons.toSet()//toSet повышает производительность
            patronGold -= departingPatrons.toSet()
        }
        .forEach { patron ->
            narrate("$heroName sees $patron departing the tavern")
        }

    narrate("There are still some patrons in the tavern")
    narrate(patrons.joinToString())


}

private fun getFavoriteMenuItems(patron: String): List<String>{
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemsTypes[menuItem]?.contains("dessert") == true
        }
        else -> menuItems.shuffled().take(Random.nextInt(1,2))
    }
}

private fun placeOrder(
    patronName: String,
    menuItemName: String,
    patronGold: MutableMap<String, Double>
) {
    val itemPrice = menuItemPrices.getValue(menuItemName)

    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        val action = when (menuItemsTypes[menuItemName]) {
            "shandy", "elixir" -> "pours"
            "meal" -> "serves"
            else -> "hands"
        }
        narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
        narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
        patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
    } else {
        narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
    }
}

private fun massivePlaceOrder(
    patronName: String,
    menuItemName: List<String>,
    patronGold: MutableMap<String, Double>
) {
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    narrate("$patronName wont to place an order: ${menuItemName.joinToString()}")
    val prices = List(menuItemName.size) { menuItemPrices.getValue(menuItemName[it]) }
    val summ = prices.sum()
    if (summ <= patronGold.getOrDefault(patronName, 0.0)) {
        menuItemName.forEach { itemName ->
            val action = when (menuItemsTypes[itemName]) {
                "shandy", "elixir" -> "pours"
                "meal" -> "serves"
                else -> "hands"
            }
            narrate("$TAVERN_MASTER $action $patronName a $itemName")
        }
        narrate("$patronName pays $TAVERN_MASTER $summ gold")
        patronGold[patronName] = patronGold.getValue(patronName) - summ
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + summ
    } else {
        narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
    }

}

/*private fun viewMenu(menuItems: List<String>) {

    val itemName = List(menuData.size) { index ->
        val (_, name, _) = menuData[index].split(",")
        name
    }
    val maxLen = itemName.maxOf { it.length } + 5

    val menuText = "*** Welcome to $TAVERN_NAME ***"


    val menu = List(menuData.size) { index ->
        val (category, name, price) = menuData[index].split(",")
        val cat = "~[${category}]~\n"
        cat.padStart((cat.length + menuText.length) / 2) + name.padEnd(
            menuText.length - price.length + 5,
            '.'
        ) + price

    }
    narrate(menuText)
    menu.forEach { item ->
        narrate(item)
    }
}*/

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("$heroName intuitively knows how much money each patron has")
    patronGold.forEach { patron, balance ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}

private fun flipValues(mas: Map<String, Double>): Map<Double,String>{
    val flippedMap= mas.map { (str, dbl) ->
         dbl to str
    }.toMap()
    return flippedMap
}