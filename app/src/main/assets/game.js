var nKanji = kanji.items.length;

var gameState;
var currentCorrectAnswer = "";
var currentKanji = "";

function updateKanjiSeen() {
    document.getElementById("kanji-count").innerText = nKanji;
    document.getElementById("seen-count").innerText = gameState.kanjiInfo.length;
}

function loadGameState() {
    gameState = localStorage.getItem("gameState");
    if (gameState === null) {
        gameState = {
            kanjiInfo: [],
            streak: 0
        };
    } else {
        gameState = JSON.parse(gameState);
        if(gameState.streak === undefined) {
            gameState.streak = 0;
        }
    }
}

function saveGameState() {
    localStorage.setItem("gameState", JSON.stringify(gameState));
}

function getThreeRandomAnswerIndices(correctAnswerIndex) {
    var indices = [];
    for(var i = 0; i < 3; i++) {
        var randomIndex = Math.floor(Math.random() * kanji.items.length);
        while(randomIndex === correctAnswerIndex || indices.includes(randomIndex)) {
            randomIndex = Math.floor(Math.random() * kanji.items.length);
        }
        indices.push(randomIndex);
    }
    return indices;
}

function showRandomKanji() {
    var randomIndex = Math.floor(Math.random() * kanji.items.length);
    var randomKanji = kanji.items[randomIndex];
    currentCorrectAnswer = randomKanji.en;
    currentKanji = randomKanji.jp;
    document.querySelector(".kanji-text").innerText = randomKanji.jp;
    var answerIndices = getThreeRandomAnswerIndices(randomIndex);
    answerIndices.push(randomIndex);
    answerIndices.sort(() => Math.random() - 0.5);
    document.querySelectorAll(".choice-button").forEach((button, index) => {
        button.innerText = kanji.items[answerIndices[index]].en;
    });
}

function getKanjiInfo(kanji) {
    for(var i = 0; i < gameState.kanjiInfo.length; i++) {
        if(gameState.kanjiInfo[i].kanji === kanji) {
            return gameState.kanjiInfo[i];
        }
    }
}

function showCorrectDialog() {

}

function showIncorrectDialog() {

}

function start() {
    loadGameState();
    updateKanjiSeen();
    showRandomKanji();

    document.querySelectorAll(".choice-button").forEach(button => {
        button.addEventListener("click", () => {
            if(button.innerText === currentCorrectAnswer) {
                var kanjiInfo = getKanjiInfo(currentKanji);
                if(kanjiInfo === undefined) {
                    gameState.kanjiInfo.push({
                        "kanji": currentKanji,
                        "nSeen": 0
                    });
                } else {
                    kanjiInfo.nSeen++;
                }
                gameState.streak += 1;
                saveGameState();
                updateKanjiSeen();
                showCorrectDialog();
            } else {
                showIncorrectDialog();
            }
        });
    });
}

start();