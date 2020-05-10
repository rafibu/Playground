package Sudoku.db;

import Sudoku.Difficulty;
import Sudoku.Sudoku;
import Sudoku.SudokuField;
import Utilities.MathUtilities;

import java.sql.*;
import java.util.Random;

public class DatabaseHandler {
    private static DatabaseHandler INSTANCE;
    // db parameters
    private static String url = "jdbc:sqlite:C:/Users/rafae/Desktop/Stuff/Java/Java Math Tools/src/Sudoku/db/" + "Sudoku.db";

    /**
     * Connects to database and executes a Statement
     */
    private static void execute(String statement) {
       Connection conn = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(statement);

            System.out.println("Statement was successful \uD83E\uDD73");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void addSudokuField(SudokuField field, Difficulty difficulty) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO sudokus ");
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("VALUES (");
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if(field.getNumber(x, y) != null) {
                    columns.append("X").append(x).append("Y").append(y).append(", ");
                    values.append(field.getNumber(x, y).getNumber()).append(", ");
                }
            }
        }
        columns.append("difficulty)");
        values.append("\"").append(difficulty.toString()).append("\");");
        field.setDifficulty(difficulty);

        sb.append(columns).append("\n").append(values);
        execute(sb.toString());
    }

    public SudokuField getRandomSudokuField(){
        SudokuField field = new SudokuField();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM sudokus ORDER BY RANDOM() LIMIT 1;");
            for(int x = 0; x < 9; x++){
                for (int y = 0; y < 9; y++) {
                    int i = 0;
                    try{ i = rs.getInt("X" + x + "Y" + y); } catch (SQLException f){ /* do nothing */}
                    if(i > 0){
                        field.setNumber(i+"", x, y);
                    }
                }
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
        return field;
    }

    public static DatabaseHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DatabaseHandler();
        }
        return INSTANCE;
    }

    public void deleteSudokuField(SudokuField field) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sudokus WHERE difficulty = \"").append(field.getDifficulty()).append("\"");
        for(int x = 0; x < 9; x++){
            for (int y = 0; y < 9; y++) {
                sb.append(" AND ");
                sb.append("X").append(x).append("Y").append(y);
                if(field.getNumber(x, y) != null) {
                    sb.append(" = ").append(field.getNumber(x, y).getNumber());
                } else {
                    sb.append(" ISNULL ");
                }
            }
        }
        execute(sb.toString());
    }
}