package Queenproblem;

import java.util.ArrayList;

/**
 * Used in the Queenproblem.QueenProblem as the variable Chessboard
 */
public class ChessBoard {

    private final int LENGTH;
    private boolean[][] board; //fields are false as long as not locked
    private ArrayList<int[]> queens = new ArrayList<>();

    public ChessBoard(int LENGTH){
        this.LENGTH = LENGTH;
        this.board = new boolean[LENGTH][LENGTH];
    }

    private ChessBoard(int LENGTH, ArrayList<int[]> queens){
        this.LENGTH = LENGTH;
        this.board = new boolean[LENGTH][LENGTH];
        setQueens(queens);
    }

    /**
     * Method used to set a new Queen on the Chessboard and setting the adequate fields to true.
     * Precondition: the field has to be false
     * @param xCor x Coordinate to be set
     * @param yCor y Coordinate to be set
     */
    public void setQueen(int xCor, int yCor){
        queens.add(new int[]{xCor, yCor});
        initializeBoard();
    }

    private void initializeBoard(){
        board = new boolean[LENGTH][LENGTH];
        for(int[] queen: queens){
            int xCor = queen[0];
            int yCor = queen[1];
            board[xCor][yCor] = true;
            for(int dx = -1; dx < 2; dx++) {
                for(int dy = -1; dy < 2; dy++) {
                    int x = xCor + dx;
                    int y = yCor + dy;
                    if (dx != 0 || dy != 0) {
                        while (x < LENGTH && y < LENGTH && x >= 0 && y >= 0) {
                            board[x][y] = true;
                            x += dx;
                            y += dy;
                        }
                    }
                }
            }
        }
    }

    public void removeLastQueen(){
        if(queens.size() > 0){
            queens.remove(queens.size()-1);
            initializeBoard();
        }
    }

    public void removeQueenAt(int x, int y){
        for(int[] test: queens){
            if(test[0] == x && test[1] == y){
                queens.remove(test);
                initializeBoard();
                return;
            }
        }
    }

    public boolean getField(int xCor, int yCor){ return board[xCor][yCor]; }

    /**
     * Gets all Empty fields of the board
     * @return Array of all empty fields
     */
    public int[][] getEmptyFields(){
        ArrayList<int[]> fields = new ArrayList<>();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(!board[i][j]) fields.add(new int[]{i, j});
            }
        }
        int[][] result = new int[fields.size()][2];
        int x = 0;
        for(int[] field: fields){
            result[x][0] = field[0];
            result[x][1] = field[1];
            x++;
        }
        return result;
    }

    public int getLength(){return LENGTH;}

    public boolean isFull(){
        for(boolean[] row: board){
            for(boolean field: row){
                if(!field) return false;
            }
        }
        return true;
    }

    public boolean[][] getBoard(){
        boolean[][] copy = new boolean[LENGTH][LENGTH];
        for(int x = 0; x < LENGTH; x++){
            for (int y = 0; y < LENGTH; y++){
                copy[x][y] = getField(x, y);
            }
        }
        return copy;
    }

    public ChessBoard clone(){
        ChessBoard clone = new ChessBoard(LENGTH);
        clone.setBoard(getBoard());
        clone.setQueens(getQueens());
        return clone;
    }

    public void setQueens(ArrayList<int[]> queens){
        for(int[] queen: queens){
            this.queens.add(queen.clone());
        }
    }

    public ArrayList<int[]> getQueens(){
        sortQueens();
        return queens;
    }

    public void sortQueens(){
        queens.sort((q1, q2) -> {
            if(q1[0] > q2[0] || (q1[0] == q2[0] && q1[1] > q2[1])) return 1;
            if(q1[0] == q2[0] && q1[1] == q2[1]) return 0;
            return -1;
        });
    }

    public void setBoard(boolean[][] board){this.board = board;}

    public boolean isQueen(int x, int y) {
        for(int[] test: queens){
            if(test[0] == x && test[1] == y) return true;
        }
        return false;
    }
}
