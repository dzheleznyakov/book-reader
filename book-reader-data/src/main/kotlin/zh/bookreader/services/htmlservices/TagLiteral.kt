package zh.bookreader.services.htmlservices

data class TagLiteral(
        val name: String,
        val cssClasses: List<String>,
        val dataType: String
) {
    override fun toString(): String {
        var s = name
        if (cssClasses.isNotEmpty())
            s += ".${cssClasses.joinToString(separator = ".")}"
        if (dataType.isNotEmpty())
            s += "[${dataType}]"
        return s
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TagLiteral) return false

        if (name != other.name) return false
        if (cssClasses != other.cssClasses) return false
        if (dataType != other.dataType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + cssClasses.hashCode()
        result = 31 * result + dataType.hashCode()
        return result
    }


}