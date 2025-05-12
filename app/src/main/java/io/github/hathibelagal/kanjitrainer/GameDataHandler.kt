package io.github.hathibelagal.kanjitrainer

import android.content.SharedPreferences

class GameDataHandler(val prefs: SharedPreferences) {

    fun getNumberOfKanjiLearned(): Int {
        return prefs.getInt("kanji_learned", 0)
    }

    fun incrementNumberOfKanjiLearned() {
        prefs.edit().putInt("kanji_learned", getNumberOfKanjiLearned() + 1).apply()
    }

}