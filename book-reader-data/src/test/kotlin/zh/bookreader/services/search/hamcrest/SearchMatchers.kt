package zh.bookreader.services.search.hamcrest

import org.hamcrest.TypeSafeMatcher

fun matchesIndexMap(mapString: String): TypeSafeMatcher<String> = MatchesIndex(mapString)