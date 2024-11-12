const emojis = ['🎮', '🎲', '🎯', '🎨', '🎭', '🎪', '🎸', '🎺'];
const gameEmojis = [...emojis, ...emojis];
let flippedCards = [];
let matchedPairs = 0;
let score = 0;
let timeLeft = 60;
let timer;
let canFlip = false;
let isAnimating = false;
let gameStarted = false;

function updateStartButton() {
    const startBtn = document.querySelector('.start-btn');
    startBtn.textContent = gameStarted ? 'New Game' : 'Start Game';
}

function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

function showPopup(isMatch) {
    if (isAnimating) return;
    isAnimating = true;

    const popup = document.getElementById(isMatch ? 'scorePopup' : 'failPopup');
    popup.style.display = 'flex';
    popup.querySelector('.score-popup-content').classList.add('animate');

    setTimeout(() => {
        popup.style.display = 'none';
        popup.querySelector('.score-popup-content').classList.remove('animate');
        isAnimating = false;
    }, 1500);
}

function createCard(emoji, index) {
    const card = document.createElement('div');
    card.className = 'card';
    card.innerHTML = `
        <div class="card-front">${emoji}</div>
        <div class="card-back">?</div>
    `;
    card.dataset.index = index;
    card.dataset.emoji = emoji;
    card.addEventListener('click', flipCard);
    return card;
}

function flipCard() {
    if (!gameStarted || !canFlip || this.classList.contains('flipped') || flippedCards.length >= 2 || isAnimating) return;

    this.classList.add('flipped');
    flippedCards.push(this);

    if (flippedCards.length === 2) {
        canFlip = false;
        setTimeout(checkMatch, 800);
    }
}

function checkMatch() {
    const [card1, card2] = flippedCards;
    const match = card1.dataset.emoji === card2.dataset.emoji;

    if (match) {
        matchedPairs++;
        score += 10;
        document.getElementById('score').textContent = score;

        setTimeout(() => {
            card1.querySelector('.card-front').classList.add('matched');
            card2.querySelector('.card-front').classList.add('matched');
            showPopup(true);
        }, 300);

        flippedCards = [];
        canFlip = true;

        if (matchedPairs === emojis.length) {
            setTimeout(() => endGame(true), 1500);
        }
    } else {
        setTimeout(() => {
            showPopup(false);
            setTimeout(() => {
                card1.classList.remove('flipped');
                card2.classList.remove('flipped');
                flippedCards = [];
                canFlip = true;
            }, 1000);
        }, 600);
    }
}

function updateTimer() {
    if (!gameStarted) return;

    timeLeft--;
    document.getElementById('timer').textContent = timeLeft;

    if (timeLeft <= 0) {
        endGame(false);
    }
}

function getGameOverMessage(won) {
    if (won) {
        if (score >= 70) return "Incredible! You're a memory master! 🏆";
        if (score >= 50) return "Great job! Your memory is impressive! 🌟";
        return "Well done! You completed the game! 👏";
    } else {
        if (matchedPairs >= 6) return "So close! Try again to beat the clock! ⏰";
        if (matchedPairs >= 4) return "Good effort! Keep practicing! 💪";
        return "Don't give up! You can do better! 🎯";
    }
}

function endGame(won = false) {
    clearInterval(timer);
    gameStarted = false;
    canFlip = false;
    updateStartButton();

    setTimeout(() => {
        document.getElementById('finalScore').textContent = score;
        document.getElementById('matchesFound').textContent = `${matchedPairs} / ${emojis.length}`;
        document.getElementById('timeRemaining').textContent = `${timeLeft}s`;
        document.getElementById('resultEmoji').textContent = won ? '🏆' : '⏰';
        document.getElementById('gameOverMessage').textContent = getGameOverMessage(won);

        document.getElementById('gameOver').style.display = 'flex';
    }, 800);
}

function setupInitialGrid() {
    const grid = document.getElementById('grid');
    grid.innerHTML = '';

    const shuffledEmojis = shuffle(gameEmojis);
    shuffledEmojis.forEach((emoji, index) => {
        grid.appendChild(createCard(emoji, index));
    });

    document.getElementById('score').textContent = '0';
    document.getElementById('timer').textContent = '60';
    updateStartButton();
}

function startGame() {
    flippedCards = [];
    matchedPairs = 0;
    score = 0;
    timeLeft = 60;
    canFlip = true;
    isAnimating = false;
    gameStarted = true;

    document.getElementById('gameOver').style.display = 'none';

    setupInitialGrid();
    updateStartButton();

    clearInterval(timer);
    timer = setInterval(updateTimer, 1000);
}

// Initial setup
document.addEventListener('DOMContentLoaded', () => {
    setupInitialGrid();
});