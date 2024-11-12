// Logging utility
const Logger = {
    debug: (message) => {
        console.debug(`[DEBUG] TicTacToe: ${message}`);
        if (window.Android) {
            window.Android.log(message, 'DEBUG');
        }
    },
    info: (message) => {
        console.info(`[INFO] TicTacToe: ${message}`);
        if (window.Android) {
            window.Android.log(message, 'INFO');
        }
    },
    error: (message) => {
        console.error(`[ERROR] TicTacToe: ${message}`);
        if (window.Android) {
            window.Android.log(message, 'ERROR');
        }
    }
};

// Game elements
const board = document.getElementById('board');
const cells = document.querySelectorAll('[data-cell]');
const status = document.getElementById('status');
const resetButton = document.getElementById('reset');
const difficultyButtons = document.querySelectorAll('.difficulty-btn');
const playerScoreElement = document.querySelector('#player-score span');
const aiScoreElement = document.querySelector('#ai-score span');

// Game state
let gameBoard = Array(9).fill('');
let gameActive = true;
let scores = { player: 0, ai: 0 };
let difficulty = 'easy';
let gameHeaders = {};
let sessionId = null;

const winningCombinations = [
    [0, 1, 2], [3, 4, 5], [6, 7, 8],
    [0, 3, 6], [1, 4, 7], [2, 5, 8],
    [0, 4, 8], [2, 4, 6]
];

// Initialize headers from Android
function initializeHeaders() {
    Logger.debug('Initializing headers from Android interface');
    try {
        if (window.Android) {
            const headersStr = window.Android.getHeaders();
            gameHeaders = JSON.parse(headersStr);
            Logger.info('Headers initialized successfully');
            Logger.debug(`Headers initialized with: ${JSON.stringify(gameHeaders)}`);

            // Verify JWT token is present
            if (gameHeaders.Authorization && gameHeaders.Authorization.startsWith('JWT ')) {
                Logger.info('JWT token successfully loaded');
            } else {
                Logger.warn('JWT token not found in headers');
            }
        } else {
            Logger.warn('Android interface not available, using default headers');
            gameHeaders = {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            };
        }
    } catch (error) {
        Logger.error(`Error initializing headers: ${error.message}`);
        gameHeaders = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };
    }
}

// Handle cell clicks
function handleCellClick(e, index) {
    if (gameBoard[index] || !gameActive) {
        Logger.debug(`Invalid move attempted at index ${index}`);
        return;
    }

    Logger.info(`Player clicked cell ${index}`);
    makeMove(index, 'x');

    if (!checkWin() && !checkDraw()) {
        Logger.debug('Scheduling AI move');
        setTimeout(() => {
            makeAIMove();
        }, 500);
    }
}

// Make a move
function makeMove(index, player) {
    Logger.debug(`Making move: player=${player}, index=${index}`);
    gameBoard[index] = player;
    cells[index].classList.add(player);

    if (checkWin()) {
        gameActive = false;
        const winner = player === 'x' ? 'player' : 'ai';
        scores[winner]++;
        updateScores();
        status.textContent = winner === 'player' ? 'You win!' : 'AI wins!';
        Logger.info(`Game won by ${winner}`);
        highlightWinningCombination();
    } else if (checkDraw()) {
        gameActive = false;
        status.textContent = "It's a draw!";
        Logger.info('Game ended in draw');
    } else {
        status.textContent = player === 'x' ? "AI's turn" : 'Your turn';
        Logger.debug(`Turn changed to ${player === 'x' ? 'AI' : 'player'}`);
    }
}

// AI move logic
async function makeAIMove() {
    if (!gameActive) {
        Logger.debug('Game not active, AI move cancelled');
        return;
    }

    let move;
    switch (difficulty) {
        case 'easy':
            move = getRandomMove();
            Logger.info(`AI made easy move: ${move}`);
            break;
        case 'medium':
            move = Math.random() < 0.5 ? getBestMove() : getRandomMove();
            Logger.info(`AI made medium move: ${move}`);
            break;
        case 'hard':
            move = getBestMove();
            Logger.info(`AI made hard move: ${move}`);
            break;
    }

    makeMove(move, 'o');


    Logger.debug(`AI move details: ${JSON.stringify(moveDetails)}`);
    await callInteractApi(moveDetails);
}

// API interaction
// Update the API call to log headers being used
async function callInteractApi(moveDetails) {
    Logger.debug(`Making API call with headers: ${JSON.stringify(gameHeaders)}`);
    const loader = document.querySelector('.loader');
    loader.classList.add('active');


      const interactDetails = {
            interaction: true,
            started_at: "",
        };

    try {
        // Log the complete request details
        Logger.debug(`API Request:
            URL: /driver/questions/interact/
            Headers: ${JSON.stringify(gameHeaders)}
            Body: ${JSON.stringify({
                message: moveDetails,
                session_id: sessionId
            })}
        `);

        const response = await fetch('/driver/questions/interact/', {
            method: 'POST',
            headers: gameHeaders,
            body: interactDetails
        });

        if (!response.ok) {
            Logger.error(`API call failed with status: ${response.status}`);
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        Logger.info(`API response received: ${JSON.stringify(data)}`);

        if (data.message.is_advideo && data.message.video) {
            Logger.debug(`Showing video: ${data.message.video}`);
            showVideo(data.message.video);
        }

        updateGameStatus(data.message.message);
        return data;
    } catch (error) {
        Logger.error(`API call failed: ${error.message}`);
        showError('Failed to communicate with the server');
        return null;
    } finally {
        loader.classList.remove('active');
    }
}
// Get random move for AI
function getRandomMove() {
    const emptyCells = gameBoard
        .map((cell, index) => cell === '' ? index : null)
        .filter(cell => cell !== null);
    const move = emptyCells[Math.floor(Math.random() * emptyCells.length)];
    Logger.debug(`Random move selected: ${move}`);
    return move;
}

// Get best move using minimax
function getBestMove() {
    Logger.debug('Calculating best move using minimax');
    let bestScore = -Infinity;
    let bestMove;

    for (let i = 0; i < 9; i++) {
        if (gameBoard[i] === '') {
            gameBoard[i] = 'o';
            let score = minimax(gameBoard, 0, false);
            gameBoard[i] = '';
            if (score > bestScore) {
                bestScore = score;
                bestMove = i;
            }
        }
    }

    Logger.debug(`Best move found: ${bestMove} with score: ${bestScore}`);
    return bestMove;
}

// Minimax algorithm
function minimax(board, depth, isMaximizing) {
    if (checkWinForPlayer('o')) return 1;
    if (checkWinForPlayer('x')) return -1;
    if (checkDraw()) return 0;

    if (isMaximizing) {
        let bestScore = -Infinity;
        for (let i = 0; i < 9; i++) {
            if (board[i] === '') {
                board[i] = 'o';
                let score = minimax(board, depth + 1, false);
                board[i] = '';
                bestScore = Math.max(score, bestScore);
            }
        }
        return bestScore;
    } else {
        let bestScore = Infinity;
        for (let i = 0; i < 9; i++) {
            if (board[i] === '') {
                board[i] = 'x';
                let score = minimax(board, depth + 1, true);
                board[i] = '';
                bestScore = Math.min(score, bestScore);
            }
        }
        return bestScore;
    }
}

// Check win condition for player
function checkWinForPlayer(player) {
    return winningCombinations.some(combination => {
        return combination.every(index => gameBoard[index] === player);
    });
}

// Check win condition
function checkWin() {
    const hasWin = checkWinForPlayer('x') || checkWinForPlayer('o');
    if (hasWin) Logger.info(`Win detected for player: ${checkWinForPlayer('x') ? 'x' : 'o'}`);
    return hasWin;
}

// Check draw condition
function checkDraw() {
    const isDraw = gameBoard.every(cell => cell !== '');
    if (isDraw) Logger.info('Draw detected');
    return isDraw;
}

// Highlight winning combination
function highlightWinningCombination() {
    Logger.debug('Highlighting winning combination');
    winningCombinations.forEach(combination => {
        if (combination.every(index => gameBoard[index] === gameBoard[combination[0]] && gameBoard[index])) {
            combination.forEach(index => cells[index].classList.add('winning-cell'));
            Logger.debug(`Highlighted cells: ${combination.join(', ')}`);
        }
    });
}

// Update scores
function updateScores() {
    Logger.debug(`Updating scores - Player: ${scores.player}, AI: ${scores.ai}`);
    playerScoreElement.textContent = scores.player;
    aiScoreElement.textContent = scores.ai;
}

// Reset game
function resetGame() {
    Logger.info('Resetting game');
    gameBoard = Array(9).fill('');
    gameActive = true;
    cells.forEach(cell => {
        cell.className = 'cell';
    });
    status.textContent = 'Your turn';
}

// Set difficulty
function setDifficulty(newDifficulty) {
    Logger.info(`Setting difficulty to: ${newDifficulty}`);
    difficulty = newDifficulty;
    difficultyButtons.forEach(button => {
        button.classList.toggle('active', button.dataset.difficulty === newDifficulty);
    });
    resetGame();
}

// Error display
function showError(message) {
    Logger.error(`Showing error: ${message}`);
    const errorElement = document.querySelector('.error-message');
    errorElement.textContent = message;
    errorElement.classList.add('active');

    setTimeout(() => {
        errorElement.classList.remove('active');
    }, 3000);
}

// Video display
function showVideo(videoUrl) {
    Logger.info(`Showing video: ${videoUrl}`);
    const modal = document.createElement('div');
    modal.className = 'video-modal';
    modal.innerHTML = `
        <div class="video-container">
            <video src="${videoUrl}" controls autoplay></video>
            <button class="close-video">Close</button>
        </div>
    `;

    document.body.appendChild(modal);
    modal.querySelector('.close-video').onclick = () => {
        Logger.debug('Video modal closed');
        modal.remove();
    };
}

// Update game status
function updateGameStatus(message) {
    Logger.debug(`Updating game status: ${message}`);
    status.textContent = message;
}

// Event Listeners
cells.forEach((cell, index) => {
    cell.addEventListener('click', (e) => handleCellClick(e, index));
});

resetButton.addEventListener('click', () => {
    Logger.info('Reset button clicked');
    resetGame();
});

difficultyButtons.forEach(button => {
    button.addEventListener('click', () => {
        Logger.info(`Difficulty button clicked: ${button.dataset.difficulty}`);
        setDifficulty(button.dataset.difficulty);
    });
});

// Error handling
window.onerror = function(msg, url, lineNo, columnNo, error) {
    const errorMessage = `Error: ${msg}\nURL: ${url}\nLine: ${lineNo}\nColumn: ${columnNo}`;
    Logger.error(errorMessage);
    return false;
};

// Initialize game
document.addEventListener('DOMContentLoaded', () => {
    Logger.info('Game initializing');
    initializeHeaders();
    updateScores();
    Logger.info('Game initialized successfully');
});