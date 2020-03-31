package Queenproblem;

import Utilities.Stopwatch;

import java.util.ArrayList;
import java.util.Random;

public class QueenProblem {

    public static void main(String... args){
        QueenProblem queenProblem = new QueenProblem();
        Stopwatch watch = new Stopwatch();
        int LENGTH = 5000;
        int retries = 5000;
        int max;
        for(int i = 1; i < LENGTH; i++) {
            max = queenProblem.randomSmartBoardFiller(i, retries).getQueens().size();
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

    public ChessBoard randomBoardFiller(ChessBoard board){
        while(!board.isFull()){
            setRandomQueen(board);
        }
        return board;
    }

    public ChessBoard randomSmartBoardFiller(int boardLength, int retries){
        ChessBoard board = new ChessBoard(boardLength);
        for(int i = 0; i < retries; i++) {
            ChessBoard test = new ChessBoard(boardLength);
            randomBoardFiller(test);
            if(test.getQueens().size() > board.getQueens().size()) board = test;
            if(board.getQueens().size() == boardLength) return board;
        }
        return board;
    }

    /**
     * sets a Queen on a random free field
     * @param board Chessboard which is used
     */
    public void setRandomQueen(ChessBoard board){
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
     * @param board board on which to set queen
     */
    private ChessBoard setNextSmartQueen(ChessBoard board){
        if(board.getEmptyFields().length > 0) {
            ArrayList<ChessBoard> possibleMatches = getPossibleMatches(board);
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

    private ArrayList<ChessBoard> getPossibleMatches(ChessBoard board){
        assert board.getEmptyFields().length > 0;
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
        return possibleMatches;
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
}
