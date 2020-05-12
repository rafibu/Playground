package Minesweeper;

import java.util.ArrayList;
import java.util.Random;

public class MineBoard {
    private final int LENGTH;
    private final int WIDTH;
    private final int NUMBER_OF_MINES;
    private Minefield[][] board;
    private boolean[][] flags;
    private boolean lost = false;
    private boolean initalized = false;
    GameTimer timer;

    public MineBoard(int length, int width, int numberOfMines){
        this.LENGTH = length;
        this.WIDTH  = width;
        this.NUMBER_OF_MINES = numberOfMines;
        board = new Minefield[length][width];
        flags = new boolean[length][width];
    }

    public void initalizeBoard(int xCor, int yCor){
        assert xCor < LENGTH && yCor < WIDTH;
        setMines(xCor, yCor);
        timer = new GameTimer();
    }

    private void setMines(int xCor, int yCor){
        Random random = new Random();
        int mines = 0;
        while(mines < NUMBER_OF_MINES){
            int x = random.nextInt(LENGTH);
            int y = random.nextInt(WIDTH);
            if(x != xCor && y != yCor && board[x][y] != Minefield.MINE_COVERED){
                board[x][y] = Minefield.MINE_COVERED;
                mines++;
            }
        }
        for(int i = 0; i < LENGTH; i++){
            for(int j = 0; j < WIDTH; j++) {
                if(board[i][j] != Minefield.MINE_COVERED){
                    board[i][j] = Minefield.NONE_COVERED;
                }
            }
        }
    }

    public int updateMinecounter(){
        int minesCovered = 0;
        for(Minefield[] row: board){
            for(Minefield field: row){
                if(field == Minefield.MINE_COVERED)
                    minesCovered++;
            }
        }
        for(boolean[] row: flags){
            for(boolean flag: row){
                if(flag) minesCovered--;
            }
        }
        return minesCovered;
    }

    public void uncover(int xCor, int yCor){
        if (!initalized) {
            initalizeBoard(xCor, yCor);
            initalized = true;
        }
        if(!hasLost() && !isFlag(xCor, yCor)) {
            switch (getField(xCor, yCor)) {
                case NONE_COVERED:
                    setField(xCor, yCor, Minefield.NONE_UNCOVERED);
                    if(getNumber(xCor, yCor) == 0){
                        for(int[] adjacent: getAdjacentFields(xCor, yCor)){
                            uncover(adjacent[0], adjacent[1]);
                            if(hasWon()) break;
                        }
                    }
                    break;
                case MINE_COVERED:
                    setField(xCor, yCor, Minefield.MINE_UNCOVERED);
                    timer.stopTime();
                    lost = true;
                    break;
            }
        }
    }

    public void uncoverAdjacent(int xCor, int yCor){
        int adjFlags = 0;
        for(int[] adjacent: getAdjacentFields(xCor, yCor)){
            if(isFlag(adjacent[0], adjacent[1])) adjFlags++;
        }
        if(adjFlags == getNumber(xCor, yCor)){
            for(int[] adjacent: getAdjacentFields(xCor, yCor)){
                uncover(adjacent[0], adjacent[1]);
            }
        }
    }

    public boolean hasLost(){
        return lost;
    }
    public boolean hasWon(){
        for(Minefield[] row: board){
            for(Minefield field: row){
                if(field == null || field == Minefield.NONE_COVERED || field == Minefield.MINE_UNCOVERED) return false;
            }
        }
        return true;
    }

    private Minefield getField(int xCor, int yCor){
        assert xCor < LENGTH && yCor < WIDTH: xCor + ", " + yCor;
        return board[xCor][yCor];
    }
    private void setField(int xCor, int yCor, Minefield state){
        assert xCor < LENGTH && yCor < WIDTH;
        board[xCor][yCor] = state;
    }
    private ArrayList<int[]> getAdjacentFields(int xCor, int yCor){
        ArrayList<int[]> adjacent = new ArrayList<>();
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if((i != 0 || j != 0) && xCor + i < LENGTH && xCor + i >= 0 && yCor + j < WIDTH && yCor + j >= 0){
                    int[] field = new int[]{xCor + i, yCor + j};
                    adjacent.add(field);
                }
            }
        }
        return adjacent;
    }

    public int getNumber(int xCor, int yCor){
        int mines = 0;
        for(int[] coordinates: getAdjacentFields(xCor, yCor)){
            Minefield adjacent = getField(coordinates[0], coordinates[1]);
            if(adjacent == Minefield.MINE_COVERED || adjacent == Minefield.MINE_UNCOVERED){
                mines++;
            }
        }
        return mines;
    }
    public void setFlag(int xCor, int yCor){
        Minefield field = getField(xCor, yCor);
        if(field != Minefield.MINE_UNCOVERED && field != Minefield.NONE_UNCOVERED){
            flags[xCor][yCor] = !flags[xCor][yCor];
        }
    }
    public int getWidth(){ return WIDTH; }
    public int getLength(){ return LENGTH; }
    public int getAmountMines(){ return NUMBER_OF_MINES;}

    public boolean isDisabled(int x, int y) {
        return hasWon() || hasLost();
    }

    public boolean isUncovered(int x, int y) {
        return getField(x, y) == Minefield.NONE_UNCOVERED;
    }
    public boolean isFlag(int x, int y){
        return flags[x][y];
    }
    public boolean isUncoveredMine(int x, int y){
        return getField(x, y) == Minefield.MINE_UNCOVERED;
    }
    public boolean isMine(int x, int y){
        assert hasLost();
        return getField(x, y) == Minefield.MINE_COVERED;
    }
    public String getTime(){
        return timer != null ? timer.showTime(): null;
    }
}
