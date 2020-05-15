package Sudoku.db;

import Utilities.DatabaseUtil;

import java.util.ArrayList;

public class CreateSudokuDB {
    private static String url = "jdbc:sqlite:C:/Users/rafae/Desktop/Stuff/Java/Java Math Tools/src/Sudoku/db/" + "Sudoku.db";

    public static void main(String[] args) {
        DatabaseUtil.createNewDatabase(url);
        fillDB();
    }

    public static void fillDB() {

        // SQL statement for creating a new table
        ArrayList<String> addSudokus = new ArrayList<>();
        for(int x = 0; x < 9; x++){
            for(int y = 0; y < 9; y++){
                addSudokus.add("X" + x +  "Y" + y+ " INTEGER");
            }
        }
        StringBuilder squares = new StringBuilder();
        addSudokus.forEach(e -> {
            squares.append(e);
            if(addSudokus.indexOf(e) != addSudokus.size() - 1) {
                squares.append(", ");
            }
        });
        String createTable = "CREATE TABLE IF NOT EXISTS sudokus(" +
                "difficulty TEXT, " +
                squares.toString() +
                ")";


        DatabaseUtil.executeFillDB(createTable, url);
    }

}