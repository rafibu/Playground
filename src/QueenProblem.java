import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

public class QueenProblem {

    public static void main(String... args){
        QueenProblem queenProblem = new QueenProblem();
        Stopwatch watch = new Stopwatch();
        int LENGTH = 5000;
        int retries = 1000;
        int max = 0;
        for(int i = 1; i < LENGTH; i++) {
            max = queenProblem.smartBoardFiller(i).getQueens().size();
       /* for(int i = 0; i < retries; i++){
            int test = queenProblem.randomBoardFiller(LENGTH);
            System.out.println(test);
            if(test>max) max = test;
        } */
         //   System.out.println(max);
            if(max != i) System.out.println("Fieldlength: " + i + ", found Queens: " + max);
            System.out.println(watch.getLap());
        }
    }

    public ChessBoard optimalSolution(int size){
        return smartBoardFiller(size);
    }

    /**
     * Finds a number of Queens which can be placed on the Board by placing them randomly
     * @param boardLength Length of Chessboard
     * @return number of Queens
     */
    private int randomBoardFiller(int boardLength){
        int numberOfQueens = 0;
        ChessBoard board = new ChessBoard(boardLength);
        while(!board.isFull()){
            setRandomQueen(board);
            numberOfQueens++;
        }
        return numberOfQueens;
    }

    /**
     * sets a Queen on a random free field
     * @param board Chessboard which is used
     */
    private void setRandomQueen(ChessBoard board){
        assert !board.isFull(): "Chessboard is already full";
        Random random = new Random();
        int xCor;
        int yCor;
        do{
            xCor = random.nextInt(board.getLength());
            yCor = random.nextInt(board.getLength());
        } while(board.getField(xCor, yCor));
        board.setQueen(xCor, yCor);
    }

    /**
     * Fills the board with Queens where the next Queen is set where the board will be filled up the lowest.
     * If several places are equally efficient, they will all be tested.
     */
    private ChessBoard smartBoardFiller(int boardLength){
        ChessBoard board = new ChessBoard(boardLength);
        board = setNextSmartQueen(board);
        board.sortQueens();
        return board;
    }

    /**
     * sets recursively queens on those places where they subdue the least open fields
     * ToDO: change method from recursive to dynamic and save already tested fields, so they aren't tested anymore.
     * @param board board on which to set queen
     */
    private ChessBoard setNextSmartQueen(ChessBoard board){
        if(board.getEmptyFields().length > 0) {
            int[][] emptyFields = board.getEmptyFields();
            ArrayList<ChessBoard> possibleMatches = new ArrayList<>();
            int maxEmptyFields = 0;
            for (int[] cor : emptyFields) {
                ChessBoard test = board.clone();
                test.setQueen(cor[0], cor[1]);
                if (test.getEmptyFields().length > maxEmptyFields) {
                    possibleMatches.clear();
                    possibleMatches.add(test);
                    maxEmptyFields = test.getEmptyFields().length;
                } else if (test.getEmptyFields().length == maxEmptyFields) {
                    possibleMatches.add(test);
                }
            }
            //TODO: Refactor after changing to nonrecursive
            int queenNumbers = 0;
            for (ChessBoard match : possibleMatches) {
                match = setNextSmartQueen(match);
                int matchQueens = match.getQueens().size();
                if (matchQueens > queenNumbers) {
                    board = match;
                    queenNumbers = matchQueens;
                }
                if(queenNumbers == board.getLength()) break;
            }
        }
        return board;
    }

    /**
     * Sets the next Queen by trying all fields recursively
     * @param board Chessboard which it should set on
     * @return board
     */
    private ChessBoard setNextQueen(ChessBoard board){
        if(board.getEmptyFields().length > 0) {
            int[][] emptyFields = board.getEmptyFields();
            ArrayList<ChessBoard> possibleMatches = new ArrayList<>();
            for (int[] cor : emptyFields) {
                ChessBoard test = board.clone();
                test.setQueen(cor[0], cor[1]);
                possibleMatches.add(test);
            }
            int queenNumbers = 0;
            for (ChessBoard match : possibleMatches) {
                match = setNextQueen(match);
                int matchQueens = match.getQueens().size();
                if (matchQueens > queenNumbers) {
                    board = match;
                    queenNumbers = matchQueens;
                }
                if (queenNumbers == board.getLength()) break;
            }
        }
        return board;
    }

    /**
     * Similar to SmartNextQueen but tries to set a Queen in L form to the last one
     */
    private ChessBoard setNextLStyleQueen(ChessBoard board){
        //ToDo: implement
        return board;
    }
}
