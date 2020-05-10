package Sudoku.db;

import java.sql.*;
import java.util.ArrayList;

public class Create {
    private static String url = "jdbc:sqlite:C:/Users/rafae/Desktop/Stuff/Java/Java Math Tools/src/Sudoku/db/" + "Sudoku.db";

    public static void createNewDatabase() {
        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        createNewDatabase();
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


        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(createTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}