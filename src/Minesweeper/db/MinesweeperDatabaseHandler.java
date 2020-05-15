package Minesweeper.db;

import Sudoku.Difficulty;
import Utilities.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;

public class MinesweeperDatabaseHandler {
    private static MinesweeperDatabaseHandler INSTANCE;
    // db parameters
    private static String url = "jdbc:sqlite:C:/Users/rafae/Desktop/Stuff/Java/Java Math Tools/src/Minesweeper/db/" + "Minesweeper.db";

    public void addWinnerTime(long time, Difficulty difficulty){
        addWinnerTime(time, difficulty, null);
    }

    public static void addWinnerTime(long time, Difficulty difficulty, String player) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Minesweeper ");
        sb.append("(winnerTime, ");
        if(player!= null) sb.append("winnerName, ");
        sb.append("difficulty) ");
        sb.append("VALUES (");
        sb.append(time).append(", ");
        sb.append("\"").append(difficulty).append("\"");
        if(player != null)sb.append(", ").append("\"").append(player).append("\"");
        sb.append(")");
        DatabaseUtil.execute(sb.toString(), url);
    }

    public long[] loadTimes(Difficulty difficulty){
        Connection conn = null;
        ArrayList<Long> list = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Minesweeper WHERE difficulty = \"" + difficulty.toString() + "\";");
            while(rs.next()) {
                try {
                    list.add(rs.getLong("winnerTime"));
                } catch (SQLException f) { /* do nothing */}
            }
        } catch(SQLException e){
            System.out.print(e.getSQLState());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        long[] res = new long[list.size()];
        int i = 0;
        for (Long e : list) {
            res[i++] = e;
        }
        return res;
    }

    public static MinesweeperDatabaseHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MinesweeperDatabaseHandler();
        }
        return INSTANCE;
    }
}