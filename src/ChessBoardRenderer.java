import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.NumberFormat;

public class ChessBoardRenderer extends Application {
    private Stage openStage;

    public static void main(String... args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        startScreen(primaryStage);
    }

    private void startScreen(Stage primaryStage){
        openStage = primaryStage;
        primaryStage.setScene(new Scene(startBox()));
        primaryStage.setTitle("Schachbrett");
        primaryStage.show();
    }

    private MenuBar standardMenu(ChessBoard board){
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Datei");

        MenuItem itemNew = new MenuItem("Neu");
        itemNew.setOnAction(event -> startScreen(openStage));

        menuFile.getItems().addAll(itemNew);

        Menu menuBearbeiten = new Menu("Bearbeiten");

        MenuItem itemBack = new MenuItem("Zurück");
        itemBack.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
        itemBack.setOnAction(event -> removeLastQueen(board));
        itemBack.setDisable(board == null || board.getQueens().size() < 1);

        menuBearbeiten.getItems().addAll(itemBack);


        menuBar.getMenus().addAll(menuFile, menuBearbeiten);
        return menuBar;
    }

    private VBox startBox(){
        VBox box = new VBox();
        box.setPrefSize(400, 400);
        box.setAlignment(Pos.CENTER);

        Text boardSize = new Text("Brett Grösse");
        Slider verticalSlider = new Slider();
        verticalSlider.setOrientation(Orientation.VERTICAL);
        verticalSlider.setMin(1);
        verticalSlider.setMax(100);
        verticalSlider.setValue(8);
        verticalSlider.setPrefHeight(250);
        verticalSlider.valueProperty().addListener((obs, oldval, newVal) ->
                verticalSlider.setValue(newVal.intValue()));
        verticalSlider.setShowTickLabels(true);
        verticalSlider.setShowTickMarks(true);
        TextField field = new TextField();
        field.setEditable(false);
        field.setAlignment(Pos.CENTER);
        field.textProperty().bindBidirectional(verticalSlider.valueProperty(), NumberFormat.getNumberInstance());
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            ChessBoard board = new ChessBoard((int)verticalSlider.getValue());
            openStage.setScene(new Scene(ChessboardScene(board)));
            openStage.show();
        });
        Button solveButton = quickScopeButton(verticalSlider);
        box.getChildren().addAll(standardMenu(null), boardSize, verticalSlider, field, startButton);
        return box;
    }

    private VBox ChessboardScene(ChessBoard board) {
        VBox box = new VBox();
        box.setPrefSize(600, 650);
        box.getChildren().add(standardMenu(board));
        HBox topRow = new HBox();
        topRow.getChildren().add(optimalSolutionButton(board));
        topRow.getChildren().add(new Text("Queens set: " + board.getQueens().size()));
        box.getChildren().add(topRow);
        Button[][] boardButtons = chessBoardButtons(board, 600);
        for(Button[] row: boardButtons){
            HBox rowBox = new HBox();
            for (Button button: row){
                rowBox.getChildren().add(button);
            }
            box.getChildren().add(rowBox);
        }
        return box;
    }

    private Button[][] chessBoardButtons(ChessBoard board, int windowSize) {
        Button[][] buttons = new Button[board.getLength()][board.getLength()];
        int buttonSize = windowSize / board.getLength();
        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getLength(); j++) {
                buttons[i][j] = chessSquare(board, i, j, buttonSize);
            }
        }
        return buttons;
    }

    private Button chessSquare(ChessBoard board, int x, int y, int size){
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);
        button.setDisable(board.getField(x,y) && !board.isQueen(x,y));
        if(board.isQueen(x, y)){
            button.setStyle("-fx-background-color: #ff0000");
            button.setText("Q");
        }
        if(button.isDisabled()) {
            button.setStyle("-fx-background-color: #000000");
        }
        button.setOnAction(event -> {
            if(!board.isQueen(x,y)) {
                board.setQueen(x, y);
            } else {
                board.removeQueenAt(x,y);
            }
            openStage.setScene(new Scene(ChessboardScene(board)));
        });
        return button;
    }

    private Button optimalSolutionButton(ChessBoard oldBoard){
        Button button = new Button("Show Solution");
        button.setOnAction(event -> {
            QueenProblem solver = new QueenProblem();
            ChessBoard board = solver.optimalSolution(oldBoard.getLength());
            openStage.setScene(new Scene(ChessboardScene(board)));
        });
        return button;
    }

    private void removeLastQueen(ChessBoard oldBoard){
        oldBoard.removeLastQueen();
        openStage.setScene(new Scene(ChessboardScene(oldBoard)));
    }

    //FixMe: Doesn't work
    private final Object lock = new Object();
    private Button quickScopeButton(Slider slider){
        Button button = new Button("Show all solutions");
        button.setOnAction(event -> {
            QueenProblem solver = new QueenProblem();
            try {
                for (int i = 1; i <= slider.getValue(); i++) {
                    Stage stage = new Stage();
                    synchronized (lock) {
                        stage.setScene(new Scene(ChessboardScene(solver.optimalSolution(i))));
                        stage.show();
                        stage.wait(2000);
                        stage.close();
                    }
                }
            } catch (Exception e){
            }
        });
        return button;
    }
}
