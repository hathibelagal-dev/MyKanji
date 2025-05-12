package io.github.hathibelagal.kanjitrainer

import android.content.Context
import org.json.JSONArray

data class KanjiData(val kanji: String, val meaning: String)

class KanjiItems(private var context: Context) {
    private val kanjiItems = mutableListOf<KanjiData>()

    private fun addKanji(kanji: KanjiData) {
        kanjiItems.add(kanji)
    }

    fun getKanji(index: Int): KanjiData {
        return kanjiItems[index]
    }

    fun getNumberOfKanji(): Int {
        return kanjiItems.size
    }

    fun loadKanji() {
        val kanjiList = context.assets.open("kanji.json").bufferedReader().use { it.readText() }
        val kanjiArray = JSONArray(kanjiList)
        for (i in 0 until kanjiArray.length()) {
            val kanjiObject = kanjiArray.getJSONObject(i)
            val kanji = kanjiObject.getString("kanji")
            val meaning = kanjiObject.getString("meaning")
            addKanji(KanjiData(kanji, meaning))
        }
    }
}