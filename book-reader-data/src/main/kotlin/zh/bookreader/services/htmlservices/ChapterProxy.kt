package zh.bookreader.services.htmlservices

import zh.bookreader.model.Chapter

class ChapterProxy(name: String) : Chapter() {
    init {
        this.name = name
    }
}