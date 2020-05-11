package Minesweeper;

import org.junit.Before;
import org.junit.Test;

public class MineBoardTest {
    private MineBoard board;

    @Before
    public void setUp(){
        board = new MineBoard(10, 10, 8);
    }

    @Test
    public void initializeBoardTest(){
        board.initalizeBoard(3, 3);
        assert(board.updateMinecounter() == 8);
    }

    @Test
    public void setFlagsTest(){
        board.initalizeBoard(3,3);
        int first = board.updateMinecounter();
        board.setFlag(1, 2);
        board.setFlag(4, 5);
        int second = board.updateMinecounter();
        assert(first - second == 2);
        //Setting a flag on an already uncovered field
        board.setFlag(3, 3);
        assert(board.updateMinecounter() == second);
    }
}
