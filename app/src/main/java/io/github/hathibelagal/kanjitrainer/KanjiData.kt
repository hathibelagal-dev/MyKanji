package io.github.hathibelagal.kanjitrainer

import android.content.Context
import org.json.JSONArray

data class KanjiData(val kanji: String, val meaning: String, var nSeen: Int = 0)

class KanjiItems(private var context: Context) {
    private val kanjiList = mutableListOf<KanjiData>()

    fun loadKanjiFromJSON() {
        val prefs = context.getSharedPreferences("kanji_prefs", Context.MODE_PRIVATE)
        val inputStream = context.assets.open("kanji.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val kanji = jsonObject.getString("kanji")
            val meaning = jsonObject.getString("meaning")
            kanjiList.add(KanjiData(kanji, meaning))

            val nSeen = prefs.getInt(kanji, 0)
            kanjiList.last().nSeen = nSeen

            val editor = prefs.edit()
            editor.putInt(kanji, kanjiList.last().nSeen)
            editor.apply()
        }
    }

    fun getKanjiList(): List<KanjiData> {
        return kanjiList
    }

    fun getKanji(index: Int): KanjiData {
        return kanjiList[index]
    }

    fun incrementNSeen(kanji: String) {
        val prefs = context.getSharedPreferences("kanji_prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val nSeen = prefs.getInt(kanji, 0) + 1
        editor.putInt(kanji, nSeen)
        editor.apply()

        for (item in kanjiList) {
            if (item.kanji == kanji) {
                item.nSeen = nSeen
                break
            }
        }
    }

    fun resetNSeen(kanji: String) {
        val prefs = context.getSharedPreferences("kanji_prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(kanji, 0)
        editor.apply()

        for (item in kanjiList) {
            if (item.kanji == kanji) {
                item.nSeen = 0
                break
            }
        }
    }

    fun getRandKanji(): KanjiData {
        return kanjiList.random()
    }

    fun getRandomMeanings(kanji: String, numMeanings: Int): MutableList<String> {
        val meanings = kanjiList.filter { it.kanji != kanji }.map { it.meaning }
        return meanings.shuffled().take(numMeanings).toMutableList()
    }
}