package Minesweeper;

import Minesweeper.db.MinesweeperDatabaseHandler;
import Sudoku.Difficulty;
import Utilities.SliderBox;
import Utilities.UIElements;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.util.Duration;

import java.util.Arrays;

import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.CENTER_LEFT;

public class Minesweeper extends UIElements {
    private final int MINEHEIGHT = 25;
    private MineBoard board;

    private Image flagImage = new Image(getClass().getResourceAsStream("/Minesweeper/resources/flag.png"));
    private Image mineImage = new Image(getClass().getResourceAsStream("/Minesweeper/resources/mine.png"));
    private Image exlposionImage = new Image(getClass().getResourceAsStream("/Minesweeper/resources/explosion.png"));

    public static void main(String... args){
        launch(args);
    }

    @Override
    protected VBox startBox(){
        VBox box = new VBox();
        box.setPrefSize(600, 600);
        box.setAlignment(Pos.TOP_CENTER);

        Button easy = defaultButton("Einfach", 9, 9, 10);
        Button medium = defaultButton("Mittel", 16, 16, 36);
        Button hard = defaultButton("Schwierig", 30, 16, 99);
        HBox buttonBox = new HBox(new Text(" "), easy, new Text(" "), medium, new Text(" "), hard);

        SliderBox widthSlider = new SliderBox("Breite:", 20, 80, 4);
        SliderBox lengthSlider = new SliderBox("Länge:", 20, 80, 4);
        SliderBox mineSlider = new SliderBox("Anzahl Minen:", 45, 400);

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            final int length = lengthSlider.getIntValue();
            final int width = widthSlider.getIntValue();
            final int mines = mineSlider.getIntValue();
            if(length*width >= mines && (length*width)/mines < 200) {
                board = new MineBoard(length, width, mines);
                openStage.setScene(new Scene(MinesweeperScene(board)));
            } else {
                lengthSlider.setValue(20);
                widthSlider.setValue(20);
                mineSlider.setValue(45);
            }
            openStage.show();
        });
        box.getChildren().addAll(standardMenu(), new Text(""), buttonBox, new Text(""), new Text("Eigenes Feld:"), lengthSlider, widthSlider, mineSlider, startButton, statsBox());
        return box;
    }

    @Override
    protected String getTitle() {
        return "Minesweeper";
    }

    private Button defaultButton(String name, int x, int y, int mines){
        Button button = new Button(name + " " + x + "x" + y);
        button.setOnMouseClicked(e -> openStage.setScene(new Scene(MinesweeperScene(board = new MineBoard(y, x, mines)))));
        return button;
    }

    private PopupWindow endGameBox(String text){
        Popup popup = new Popup();
        Text message = new Text(text);
        message.minHeight(40);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
        popup.setAutoFix(true);
        popup.setHideOnEscape(true);
        Button okButton = new Button("Zurück zum Menü");
        okButton.minWidth(80);
        okButton.setAlignment(Pos.CENTER_LEFT);
        okButton.setOnMouseClicked(event -> {
            startScreen(openStage);
            popup.hide();
        });
        Button restartButton = new Button("neues Spiel");
        restartButton.setAlignment(Pos.CENTER_RIGHT);
        restartButton.minWidth(80);
        restartButton.setOnMouseClicked(event -> {
            openStage.setScene(new Scene(MinesweeperScene(board = new MineBoard(board.getLength(), board.getWidth(), board.getAmountMines()))));
            popup.hide();
        });
        HBox hbox =  new HBox(10, okButton, restartButton);
        VBox vbox = new VBox(10, message, hbox);
        popup.getContent().addAll(vbox);
        popup.setAutoFix(true);
        return popup;
    }
    private PopupWindow loserWindow() {
        return endGameBox("Das war eine Mine, sei etwas vorsichtiger das nächste Mal!");
    }

    private PopupWindow winnerWindow() {
        return endGameBox( "Gratuliere, du hast gewonnen! Deine Zeit: " + board.getTime());
    }
    private TextField time = new TextField();
    private VBox MinesweeperScene(MineBoard board) {
        VBox box = new VBox();
        int boardLength = board.getLength()*MINEHEIGHT;
        int boardWidth = board.getWidth()*MINEHEIGHT;
        box.setPrefSize(boardWidth, boardLength + 50);
        box.getChildren().add(standardMenu());
        HBox topRow = new HBox();
        TextField mineCounter = new TextField();
        mineCounter.setDisable(true);
        mineCounter.setText(board.updateMinecounter() + "");
        time.setText(getTime());
        time.setDisable(true);
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> time.setText(getTime())));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        topRow.getChildren().addAll(mineCounter, time);
        box.getChildren().add(topRow);
        Button[][] boardButtons = mineButtons(board);

        for(Button[] row: boardButtons){
            HBox rowBox = new HBox();
            for (Button button: row){
                rowBox.getChildren().add(button);
            }
            box.getChildren().add(rowBox);
        }
        if(board.hasWon()){
            PopupWindow window = winnerWindow();
            window.setAutoFix(true);
            Difficulty difficulty = evaluateDifficulty(board);
            if(difficulty != null){
                MinesweeperDatabaseHandler.getInstance().addWinnerTime(board.timer.getTime(), difficulty);
            }
            window.show(openStage);
        } else if(board.hasLost()){
            PopupWindow window = loserWindow();
            window.setAutoFix(true);
            window.show(openStage);
        }
        return box;
    }

    private VBox statsBox(){
        VBox box = new VBox(new Text(""), new Text("Statistiken:"), new Text(""));
        Difficulty[] difficulties = new Difficulty[]{Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD};
        for(Difficulty difficulty: difficulties){
            box.getChildren().add(difficultyBox(difficulty));
        }
        box.setAlignment(CENTER);
        return box;
    }

    private VBox difficultyBox(Difficulty difficulty) {
        VBox box = new VBox(new Text(difficulty.toString()));
        long[] times = MinesweeperDatabaseHandler.getInstance().loadTimes(difficulty);
        Arrays.sort(times);
        for(int i = 1; i <= 3; i++){
            if(times.length >= i) {
                box.getChildren().add(new Text(i + ". " + GameTimer.getTime(times[i - 1])));
            }
        }
        if(times.length > 0) {
            long average = 0;
            for (long e : times) average += e;
            average = average / times.length;
            box.getChildren().add(new Text("Gewonnene Spiele: " + times.length));
            box.getChildren().add(new Text("Durchschnitt: " + GameTimer.getTime(average)));
        }
        box.getChildren().add(new Text(""));
        box.setAlignment(CENTER);
        return box;
    }

    private Difficulty evaluateDifficulty(MineBoard board) {
        switch(board.getWidth()){
            case 9: if(board.getLength() == 9 && board.getAmountMines() == 10) return Difficulty.EASY;
            case 16: if(board.getLength() == 16 && board.getAmountMines() == 36) return Difficulty.MEDIUM;
            case 30: if(board.getLength() == 16 && board.getAmountMines() == 99) return Difficulty.HARD;
            default: return null;
        }
    }

    private Button[][] mineButtons(MineBoard board) {
        Button[][] buttons = new Button[board.getLength()][board.getWidth()];
        for (int i = 0; i < board.getLength(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                buttons[i][j] = mineSquare(board, i, j);
            }
        }
        return buttons;
    }


    private Button mineSquare(MineBoard board, int x, int y){

        Button button = new Button();
        button.setMinSize(MINEHEIGHT, MINEHEIGHT);
        button.setMaxSize(MINEHEIGHT, MINEHEIGHT);
        button.setDisable(board.isDisabled(x, y));
        if(board.isFlag(x, y)) {
            ImageView flag = new ImageView(flagImage);
            flag.setFitHeight(25);
            flag.setFitWidth(25);
            button.setGraphic(flag);
        } else if(board.isUncovered(x, y)){
            int number = board.getNumber(x, y);
            button.setText(number != 0 ? number + "": "");
            button.setStyle("-fx-background-color: #888888");
        } else if(board.isUncoveredMine(x, y)){
            ImageView exp = new ImageView(exlposionImage);
            exp.setFitHeight(25);
            exp.setFitWidth(25);
            button.setGraphic(exp);
            button.setStyle("-fx-background-color: #888888");
        }
        if(board.hasLost() && board.isMine(x, y)){
            ImageView exp = new ImageView(mineImage);
            exp.setFitHeight(25);
            exp.setFitWidth(25);
            button.setGraphic(exp);
        }
        button.setOnMousePressed(e -> {
            if(e.isPrimaryButtonDown() && e.isSecondaryButtonDown() || e.isPrimaryButtonDown() && e.getClickCount() > 1){
                board.uncoverAdjacent(x,y);
            } else if(e.isSecondaryButtonDown()){
                board.setFlag(x,y);
            } else if(e.isPrimaryButtonDown()) {
                board.uncover(x,y);
            }
            openStage.setScene(new Scene(MinesweeperScene(board)));
        });
        return button;
    }

    private String getTime(){
        if(board != null && board.getTime() != null) {
            if (board.hasLost() || board.hasWon()) {
                board.timer.stopTime();
            }
            return board.getTime();
        }
        return "00:00";
    }
}
