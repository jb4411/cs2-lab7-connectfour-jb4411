package connectfour.gui;

import connectfour.model.ConnectFourBoard;
import connectfour.model.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * A JavaFX GUI for the networked Connect Four game.
 *
 * @author RIT CS
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class ConnectFourGUI extends Application implements Observer<ConnectFourBoard> {
    /** empty space image */
    private Image empty = new Image(getClass().getResourceAsStream("empty.png"));
    /** player 1 space image */
    private Image p1 = new Image(getClass().getResourceAsStream("p1black.png"));
    /** player 2 space image */
    private Image p2 = new Image(getClass().getResourceAsStream("p2red.png"));

    private HBox statusBar;
    private Label movesMade;
    private Label currentPlayer;
    private Label gameStatus;
    private ConnectButton[][] board;


    private class ConnectButton extends Button {
        private int row;
        private int col;
        private ConnectFourBoard.Player owner;

        public ConnectButton(int row, int col) {
            this.row = row;
            this.col = col;
            this.owner = ConnectFourBoard.Player.NONE;
            this.setGraphic(new ImageView(empty));
        }
    }

    private GridPane makeBoard() {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < ConnectFourBoard.ROWS; row++) {
            for (int col = 0; col < ConnectFourBoard.COLS; col++) {
                ConnectButton button = new ConnectButton(row,col);

                //button.setOnAction(event ->);
                gridPane.add(button,col,row);
                this.board[row][col] = button;
            }
        }
        return gridPane;
    }

    @Override
    public void init() {
        // TODO
    }

    /**
     * Construct the layout for the game.
     *
     * @param stage container (window) in which to render the GUI
     * @throws Exception if there is a problem
     */
    public void start( Stage stage ) throws Exception {
        //create the border pane that holds the board and status info
        BorderPane borderPane = new BorderPane();
        this.board = new ConnectButton[ConnectFourBoard.ROWS][ConnectFourBoard.COLS];

        this.statusBar = new HBox();
        this.movesMade = new Label("0 moves made ");
        this.currentPlayer = new Label("Current player: ");
        this.gameStatus = new Label("Status: " + ConnectFourBoard.Status.NOT_OVER);

        this.statusBar.getChildren().add(this.movesMade);
        this.statusBar.getChildren().add(this.currentPlayer);
        this.statusBar.getChildren().add(this.gameStatus);
        this.statusBar.setAlignment(Pos.CENTER);
        this.statusBar.setSpacing(100);

        borderPane.setBottom(this.statusBar);
        //BorderPane.setAlignment(this.statusBar, Pos.CENTER);

        // get the grid pane from the helper method
        GridPane gridPane = makeBoard();
        borderPane.setCenter(gridPane);

        //store and display board
        Scene scene = new Scene(borderPane);
        stage.setTitle("Connect Four GUI");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Called by the model, model.ConnectFourBoard, whenever there is a state change
     * that needs to be updated by the GUI.
     *
     * @param connectFourBoard the board
     */
    @Override
    public void update(ConnectFourBoard connectFourBoard) {
        // TODO
    }

    /**
     * The main method expects the host and port.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
