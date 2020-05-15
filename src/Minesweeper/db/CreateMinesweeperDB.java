package Minesweeper.db;

import Utilities.DatabaseUtil;

public class CreateMinesweeperDB {
    private static String url = "jdbc:sqlite:C:/Users/rafae/Desktop/Stuff/Java/Java Math Tools/src/Minesweeper/db/" + "Minesweeper.db";

    public static void main(String[] args) {
        DatabaseUtil.createNewDatabase(url);
        fillDB();
    }

    public static void fillDB() {
        // SQL statement for creating a new table
        String createTable = "CREATE TABLE IF NOT EXISTS Minesweeper (" +
                "winnerTime INTEGER, " +
                "winnerName TEXT, " +
                "difficulty TEXT" +
                ")";

        DatabaseUtil.executeFillDB(createTable, url);
    }
}