package Sudoku;

import Sudoku.db.SudokuDatabaseHandler;
import Utilities.UIElements;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Sudoku extends UIElements {
    private SudokuField field = new SudokuField();
    private SudokuGenerator generator = new SudokuGenerator();
    @Override
    protected VBox startBox() {
        VBox box = new VBox();
        box.setPrefSize(500, 500);
        Button newSudoku = new Button("New Sudoku");
        newSudoku.setOnAction(event -> {
            resetField();
            startScreen();
        });
        Button restartField = new Button("Restart Field");
        restartField.setOnAction(event -> {
            field.restartField();
            startScreen();
        });
        Button solve = new Button("Solve");
        solve.setOnAction(event -> {
            field.solveField();
            startScreen();
        });
        Button solveOne = new Button("Solve Next");
        solveOne.setOnAction(event -> {
            field.solveOne();
            startScreen();
        });
        Button generateEasy = new Button("Generate Easy");
        generateEasy.setOnAction(event -> {
            field = generator.generateEasy();
            startScreen();
        });
        Button generateMedium = new Button("Generate Medium");
        generateMedium.setOnAction(event -> {
            field = generator.generateMedium();
            startScreen();
        });
        Button generateHard = new Button("Generate Hard");
        generateHard.setOnAction(event -> {
            field = generator.generateHard();
            startScreen();
        });
        HBox buttonBox = new HBox(newSudoku, restartField, solve, solveOne);
        HBox generateBox = new HBox(generateEasy, generateMedium, generateHard);
        VBox sudokuBox = sudokuBox();
        sudokuBox.setAlignment(Pos.CENTER);
        box.getChildren().addAll(standardMenu(), buttonBox, generateBox, sudokuBox);
        box.setSpacing(10);
        return box;
    }

    private VBox sudokuBox(){
        VBox resultBox = new VBox();
        for(int i = 0; i < 9; i++) {
            HBox row = buildLine(i);
            row.setAlignment(Pos.CENTER);
            resultBox.getChildren().add(row);
            if (i % 3 == 2) {
                Text line = new Text("");
                resultBox.getChildren().add(line);
            }
        }
        return resultBox;
    }

    private HBox buildLine(int i){
        HBox resultBox = new HBox();
        for(int j = 0; j < 9; j++) {
            int fill = field.getNumber(i, j) != null ? field.getNumber(i, j).getNumber(): 0;
            TextField tf = new TextField(fill < 1 ? "" : fill + "");
            tf.setMaxSize(30, 30);
            tf.setMinSize(30, 30);
            tf.setAlignment(Pos.CENTER);
            tf.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            tf.setDisable(field.getSquare(i,j).isInitial());
            final int y = j;
            tf.textProperty().addListener((ov, oldValue, newValue) -> {
                if(field.setNumber(newValue, i, y)) {
                    tf.setText(field.getNumber(i, y)!= null ? field.getNumber(i, y).getNumber() + "": "");
                } else tf.setText("");
            });
            resultBox.getChildren().add(tf);
            if (j % 3 == 2) {
                Text line = new Text("  ");
                resultBox.getChildren().add(line);
            }
        }
        return resultBox;
    }

    private void resetField(){
        field = new SudokuField();
    }
    @Override protected String getTitle() { return "Sudoku"; }

    @Override
    protected MenuBar standardMenu(){
        MenuBar bar = super.standardMenu();
        Menu sudokuMenu = new Menu("Sudoku");
        MenuItem getEasyRandom = new MenuItem("Easy Sudoku");
        getEasyRandom.setOnAction(event -> {
                    field = SudokuDatabaseHandler.getInstance().getRandomSudokuField(Difficulty.EASY);
                    startScreen();
                }
        );
        MenuItem getMediumRandom = new MenuItem("Medium Sudoku");
        getMediumRandom.setOnAction(event -> {
                    field = SudokuDatabaseHandler.getInstance().getRandomSudokuField(Difficulty.MEDIUM);
                    startScreen();
                }
        );
        MenuItem getHardRandom = new MenuItem("Hard Sudoku");
        getHardRandom.setOnAction(event -> {
                    field = SudokuDatabaseHandler.getInstance().getRandomSudokuField(Difficulty.HARD);
                    startScreen();
                }
        );
        MenuItem saveEasy = new MenuItem("Save Current as Easy");
        saveEasy.setOnAction(event -> SudokuDatabaseHandler.getInstance().addSudokuField(field, Difficulty.EASY));
        MenuItem saveMedium = new MenuItem("Save Current as Medium");
        saveMedium.setOnAction(event -> SudokuDatabaseHandler.getInstance().addSudokuField(field, Difficulty.MEDIUM));
        MenuItem saveHard = new MenuItem("Save Current as Hard");
        saveHard.setOnAction(event -> SudokuDatabaseHandler.getInstance().addSudokuField(field, Difficulty.HARD));
        MenuItem delete = new MenuItem("Delete Current");
        delete.setOnAction(event -> SudokuDatabaseHandler.getInstance().deleteSudokuField(field));
        sudokuMenu.getItems().addAll(getEasyRandom, getMediumRandom, getHardRandom, saveEasy, saveMedium, saveHard, delete);
        bar.getMenus().add(sudokuMenu);
        return bar;
    }
}
