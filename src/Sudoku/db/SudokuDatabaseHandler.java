package Sudoku.db;

import Sudoku.Difficulty;
import Sudoku.SudokuField;
import Utilities.DatabaseUtil;

import java.sql.*;

public class SudokuDatabaseHandler {
    private static SudokuDatabaseHandler INSTANCE;
    // db parameters
    private static String url = "jdbc:sqlite:C:/Users/rafae/Desktop/Stuff/Java/Java Math Tools/src/Sudoku/db/" + "Sudoku.db";

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
        DatabaseUtil.execute(sb.toString(), url);
    }

    public SudokuField getRandomSudokuField(Difficulty difficulty){
        SudokuField field = new SudokuField();
        field.setDifficulty(difficulty);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM sudokus WHERE difficulty = \"" + difficulty.toString() + "\" ORDER BY RANDOM() LIMIT 1;");
            for(int x = 0; x < 9; x++){
                for (int y = 0; y < 9; y++) {
                    int i = 0;
                    try{ i = rs.getInt("X" + x + "Y" + y); } catch (SQLException f){ /* do nothing */}
                    if(i > 0){
                        field.setNumber(i+"", x, y);
                        field.getSquare(x,y).setInitial(true);
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

    public static SudokuDatabaseHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SudokuDatabaseHandler();
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
        DatabaseUtil.execute(sb.toString(), url);
    }
}