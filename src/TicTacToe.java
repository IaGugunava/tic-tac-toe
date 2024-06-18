import java.util.Scanner;

public class TicTacToe {

    static final int SIZE = 3;
    static final char EMPTY = ' ';
    static final char X = 'X';
    static final char O = 'O';
    static char[][] board = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printBoard();

        System.out.println("Enter your move (row and column): ");
        int userRow = scanner.nextInt();
        int userCol = scanner.nextInt();
        board[userRow][userCol] = X;

        printBoard();

        char currentPlayer = O; // Computer starts with O after user's move

        while (!isBoardFull() && evaluate(board) == 0) {
            System.out.println("Computer's move:");
            Move bestMove = findBestMove(currentPlayer);
            board[bestMove.row][bestMove.col] = currentPlayer;
            printBoard();

            // Alternate the player
            currentPlayer = (currentPlayer == X) ? O : X;
        }

        int score = evaluate(board);
        if (score == 10) {
            System.out.println("Computer wins!");
        } else if (score == -10) {
            System.out.println("User wins!");
        } else {
            System.out.println("It's a draw!");
        }

        scanner.close();
    }

    static void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    static boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    static int evaluate(char[][] b) {
        // Check rows and columns for a win
        for (int row = 0; row < SIZE; row++) {
            if (b[row][0] == b[row][1] && b[row][1] == b[row][2]) {
                if (b[row][0] == O) return 10;
                else if (b[row][0] == X) return -10;
            }
        }

        for (int col = 0; col < SIZE; col++) {
            if (b[0][col] == b[1][col] && b[1][col] == b[2][col]) {
                if (b[0][col] == O) return 10;
                else if (b[0][col] == X) return -10;
            }
        }

        // Check diagonals for a win
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
            if (b[0][0] == O) return 10;
            else if (b[0][0] == X) return -10;
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
            if (b[0][2] == O) return 10;
            else if (b[0][2] == X) return -10;
        }

        return 0;
    }

    static int minimax(char[][] b, int depth, boolean isMax, char player, char opponent) {
        int score = evaluate(b);

        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (isBoardFull()) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (b[i][j] == EMPTY) {
                        b[i][j] = player;
                        best = Math.max(best, minimax(b, depth + 1, false, player, opponent));
                        b[i][j] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (b[i][j] == EMPTY) {
                        b[i][j] = opponent;
                        best = Math.min(best, minimax(b, depth + 1, true, player, opponent));
                        b[i][j] = EMPTY;
                    }
                }
            }
            return best;
        }
    }

    static class Move {
        int row, col;
        Move(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    static Move findBestMove(char currentPlayer) {
        int bestVal = (currentPlayer == O) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Move bestMove = new Move(-1, -1);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = currentPlayer;
                    int moveVal;
                    if (currentPlayer == O) {
                        moveVal = minimax(board, 0, false, O, X);
                    } else {
                        moveVal = minimax(board, 0, true, X, O);
                    }
                    board[i][j] = EMPTY;

                    if ((currentPlayer == O && moveVal > bestVal) || (currentPlayer == X && moveVal < bestVal)) {
                        bestMove.row = i;
                        bestMove.col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }
}
