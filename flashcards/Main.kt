package flashcards


/**
 * Add card to the deck
 */
fun addCard(cards:MutableMap<String, MutableMap<String, String>>, term:String, definition:String, count:String="0") {
    cards[term] = mutableMapOf("definition" to definition, "count" to count)
}

/**
 * Check if a term already exists in deck
 */
fun checkTermIn(cards:MutableMap<String, MutableMap<String, String>>, term:String): Boolean {
    return cards.containsKey(term)
}

/**
 * Check if a definition already exists in deck
 */
fun checkDefinitionIn(cards:MutableMap<String, MutableMap<String, String>>, definition:String): Boolean {
    for ((k, v) in cards) {
        if (v["definition"] == definition)
            return true
    }
    return false
}

/**
 * Add chosen number of cards to the deck from input
 */
fun addCards(cards: MutableMap<String, MutableMap<String, String>>, count: Int, log: MutableList<String>) {
    for (i in  1..count) {
        println("The card:", log)
        val term = readLine(log)
        if (checkTermIn(cards, term)) {
            println("The card \"$term\" already exists", log)
            return
        }
        println("The definition of the card:", log)
        val definition = readLine(log)
        if (checkDefinitionIn(cards, definition)) {
            println("The definition \"$definition\" already exists.", log)
            return
        }
        addCard(cards, term, definition)
        println("The pair (\"$term\":\"$definition\") has been added", log)
    }

}

/**
 * Get key of chosen value
 */
fun getKey(someMap: MutableMap<String, MutableMap<String, String>>, target: String): String {
    for ((k, v) in someMap) {
        if (v["definition"] == target) return k
    }
    return ""
}

/**
 * Go over chosen number - 'how_many' of random cards
 */
fun playNum(cards:MutableMap<String, MutableMap<String, String>>, how_many: Int, log: MutableList<String>) {
    for (i in 1..how_many) {
        val n = kotlin.random.Random.nextInt(cards.count())
        val (k, v) = cards.toList()[n]
        println("Print the definition of \"$k\":", log)
        val answer = readLine(log)
        if (answer == v["definition"]) {
            println("Correct!", log)
        }
        else {
            if (checkDefinitionIn(cards, answer)){
                val correct = getKey(cards, answer)
                println("Wrong. The right answer is \"${v["definition"]}\", but your definition is correct for \"$correct\".", log)
            }
            else {
                println("Wrong. The right answer is \"${v["definition"]}\".", log)
            }
            v["count"] = ((v["count"]?.toInt() ?: 0) + 1).toString()
        }

    }
}

/**
 * Read from user how many cards to ask
 */
fun ask(cards:MutableMap<String, MutableMap<String, String>>, log: MutableList<String>) {
    println("How many times to ask?", log)
    val number = readLine(log).toInt()
    playNum(cards, number, log)
}

/**
 * Ask user which card to remove and delete from deck
 */
fun remove(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>) {
    println("Which card?", log)
    val toRemove = readLine(log)
    if (cards.containsKey(toRemove)) {
        cards.remove(toRemove)
        println("The card has been removed.", log)
    }
    else {
        println("Can't remove \"$toRemove\": there is no such card.", log)
    }
}

/**
 * Read file name from user and export cards
 */
fun exportCardsUser(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>) {
    println("File name:", log)
    val fileName = readLine(log)
    exportCards(cards, log, fileName)
}

/**
 * Export cards to chosen file
 */
fun exportCards(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>, fileName: String) {
    val myFile = java.io.File(fileName)
    if (myFile.exists()) {
        myFile.writeText("")
    }
    for ((k, v) in cards) {
        myFile.appendText(k+"\n")
        myFile.appendText(v["definition"]+"\n")
        myFile.appendText(v["count"]+"\n")
    }
    println("${cards.count()} cards have been saved.", log)
}

/**
 * Read name of file from user and import cards
 */
fun importCardsUser(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>) {
    println("File name:", log)
    val fileName = readLine(log)
    importCards(cards, log, fileName)
}

/**
 * Import cards from chosen file
 */
fun importCards(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>, fileName: String) {
    val myFile = java.io.File(fileName)
    if (myFile.exists()) {
        val lines = myFile.readLines()
        for (i in lines.indices step 3) {
            addCard(cards, lines[i], lines[i+1], lines[i+2])
        }
        println("${lines.size/3} cards have been loaded.", log)
    } else {
        println("File not found.", log)
    }
}

/**
 * Save the log to file of given by the user name
 */
fun log(log: MutableList<String>) {
    println("File name:", log)
    val fileName = readLine(log)
    val myFile = java.io.File(fileName)
    if (myFile.exists()) {
        myFile.writeText("")
    }
    for (line in log) {
        myFile.appendText(line+"\n")
    }
    println("The log has been saved.", log)
}

/**
 * println with logging
 */
fun println(s: String, log: MutableList<String>) {
    kotlin.io.println(s)
    log.add(s)
}

/**
 * readLine with logging
 */
fun readLine(log: MutableList<String>): String {
    val read = readLine()!!
    log.add(read)
    return read
}

/**
 * Prints hard to learn cards
 */
fun hardCards(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>) {
    val hardest = mutableListOf<String>()
    for ((k, v) in cards) {
        if (hardest.isEmpty()) hardest.add(k)
        else if ((v["count"]?.toInt() ?: 0) > (cards[hardest.first()]?.get("count")?.toInt() ?: 0)) {
            hardest.clear()
            hardest.add(k)
        }
        else if ((v["count"]?.toInt() ?: 0) == (cards[hardest.first()]?.get("count")?.toInt() ?: 0)) {
            hardest.add(k)
        }
    }
    if (hardest.isEmpty()){
        println("There are no cards with errors.", log)
        return
    }
    if ((cards[hardest.first()]?.get("count")?.toInt() ?: 0) == 0) {
        println("There are no cards with errors.", log)
        return
    }
    if (hardest.size == 1) {
        println("The hardest card is \"${hardest[0]}\". You have ${cards[hardest.first()]?.get("count")} errors answering it.", log)
        return
    }
    else {
        var message = "The hardest cards are \"${hardest.first()}\""
        if (hardest.size > 1) {
            for (i in 0 until hardest.size) {
                if (i > 0) {
                    message += ", \"${hardest[i]}\""
                }
            }
        }
        message += ". You have \"${cards[hardest.first()]?.get("count")}\" errors answering them."
        println(message, log)
    }
}

/**
 * Clear the stats of mistakes in deck
 */
fun resetStats(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>) {
    for ((k, v) in cards) {
        v["count"] = 0.toString()
    }
    println("Card statistics have been reset.", log)
}

/**
 * Check run args for import
 */
fun checkImport(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>, args: Array<String>) {
    for (i in args.indices) {
        if (args[i] == "-import") {
            try {
                importCards(cards, log, args[i + 1])
            } catch (e: IndexOutOfBoundsException) {
                print("Import file not given.")
                return
            }

        }
    }
}
/**
 * Check run args for export
 */
fun checkExport(cards: MutableMap<String, MutableMap<String, String>>, log: MutableList<String>, args: Array<String>) {
    for (i in args.indices) {
        if (args[i] == "-export") {
            try {
                exportCards(cards, log, args[i+1])
            } catch (e: IndexOutOfBoundsException) {
                print("Export file not given.")
                return
            }
        }
    }
}

fun main(args: Array<String>) {
    val cards = mutableMapOf<String, MutableMap<String, String>>()
    val log = mutableListOf<String>()
    checkImport(cards, log, args)
    while (true) {
        println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):", log)
        when (readLine(log)) {
            "ask" -> ask(cards, log)
            "add" -> addCards(cards, 1, log)
            "remove" -> remove(cards, log)
            "import" -> importCardsUser(cards, log)
            "export" -> exportCardsUser(cards, log)
            "exit" -> {println("Bye bye!"); break}
            "log" -> log(log)
            "hardest card" -> hardCards(cards, log)
            "reset stats" -> resetStats(cards, log)
        }
    }
    checkExport(cards, log, args)
}
