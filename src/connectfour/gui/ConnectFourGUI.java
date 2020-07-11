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

    /** the HBox that stores the labels for the status bar */
    private HBox statusBar;
    /** the label that stores the number of move made for the status bar */
    private Label movesMade;
    /** the label that stores the current player for the status bar */
    private Label currentPlayer;
    /** the label that stores the current game status for the status bar */
    private Label gameStatus;
    /** the game board */
    private ConnectFourBoard board;
    /** a 2D array of every button in the board */
    private ConnectButton[][] buttonBoard;
    /** the column that a piece was last placed in */
    private int lastPlaced;
    /** whether or not the game has ended */
    private boolean finished;

    private class ConnectButton extends Button {
        /** this button's row */
        private int row;
        /** this button's column */
        private int col;
        /** this button's owner */
        private ConnectFourBoard.Player owner;

        /**
         * A helper function that changes the owner and image of the button
         * passed in through the parameter.
         *
         * @param owner the button to have it's image and owner changed
         */
        public void changeOwner(ConnectFourBoard.Player owner) {
            this.owner = owner;
            switch (owner) {
                case P1 -> this.setGraphic(new ImageView(p1));
                case P2 -> this.setGraphic(new ImageView(p2));
                default -> this.setGraphic(new ImageView(empty));
            }

        }

        /**
         * Create a new ConnectButton with no owner and an image of an empty
         * space.
         *
         * @param row this button's row
         * @param col this button's column
         */
        public ConnectButton(int row, int col) {
            this.row = row;
            this.col = col;
            this.owner = ConnectFourBoard.Player.NONE;
            this.setGraphic(new ImageView(empty));
        }
    }

    /**
     * A helper function that builds a grid of buttons used as the GUI
     * representation of the board.
     *
     * @return the grid pane representing the board
     */
    private GridPane makeBoard() {
        GridPane gridPane = new GridPane();
        //build the grid of buttons
        for (int row = 0; row < ConnectFourBoard.ROWS; row++) {
            for (int col = 0; col < ConnectFourBoard.COLS; col++) {
                ConnectButton button = new ConnectButton(row,col);
                //try to make a move in this button's column
                button.setOnAction(event -> { if (this.board.isValidMove(button.col)) { this.lastPlaced = button.col; this.board.makeMove(button.col);}});
                gridPane.add(button,col,row);
                this.buttonBoard[row][col] = button;
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
    @Override
    public void start( Stage stage ) throws Exception {
        //create the border pane that holds the board and status info
        BorderPane borderPane = new BorderPane();
        //create the board
        this.board = new ConnectFourBoard();
        this.board.addObserver(this);

        //initialize variables
        this.lastPlaced = 0;
        this.finished = false;

        //create the 2D array of buttons
        this.buttonBoard = new ConnectButton[ConnectFourBoard.ROWS][ConnectFourBoard.COLS];

        //create the status bar
        this.statusBar = new HBox();
        this.movesMade = new Label(this.board.getMovesMade() + " moves made ");
        this.currentPlayer = new Label("Current player: " + this.board.getCurrentPlayer());
        this.gameStatus = new Label("Status: " + this.board.getGameStatus());
        this.statusBar.getChildren().add(this.movesMade);
        this.statusBar.getChildren().add(this.currentPlayer);
        this.statusBar.getChildren().add(this.gameStatus);
        this.statusBar.setAlignment(Pos.CENTER);
        this.statusBar.setSpacing(100);
        borderPane.setBottom(this.statusBar);

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
        //if the game has not ended update the view
        if (!this.finished) {
            //update board
            ConnectFourBoard.Player current;
            for (int row = 0; row < ConnectFourBoard.ROWS; row++) {
                current = this.board.getContents(row, this.lastPlaced);
                this.buttonBoard[row][this.lastPlaced].changeOwner(current);
            }
            //update status bar
            this.movesMade.setText(this.board.getMovesMade() + " moves made ");
            this.currentPlayer.setText("Current player: " + this.board.getCurrentPlayer());
            this.gameStatus.setText("Status: " + this.board.getGameStatus());

            //if the game has ended set this.finished to true
            if (this.board.getGameStatus() != ConnectFourBoard.Status.NOT_OVER) {
                this.finished = true;
            }
        }
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
