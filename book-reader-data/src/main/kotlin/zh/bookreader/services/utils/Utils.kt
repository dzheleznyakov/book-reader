package zh.bookreader.services.utils

import java.util.Scanner

fun Scanner.readLines(): Set<String> {
    val lines = mutableSetOf<String>()
    while (hasNext())
        lines.add(nextLine())
    return lines
}