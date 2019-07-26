package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import controller.Mouse;
import main.Game;
import model.AI;
import model.Board;
import model.GameTimer;
import tools.Coordinate;
import constants.Constants;


/**
 * main window of the game
 */
public class MainWindow extends JFrame {

    private static AI myAI = new AI();

    public static Board humanBoard = new Board();

    public static Board AIBoard = new Board();

    private static Mouse mouse;

    public static boolean startedGame = false;

    private static JLabel gameStateComponent;

    public static JLabel timerLabel;

    // A list contains all the five ships
    public static List<FrontEndShip> shipList = new ArrayList<FrontEndShip>();

    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

    // Button array
//    static JButton[][] buttonArray = new JButton[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

    private static int numberOfAISunkShips = 0;

    private static boolean gameOver = false;

    private static String gameMode = "normal";

    //   private String gameMode = "advanced";


    /**
     * default constructor
     */
    public MainWindow() {

        super("BattleShip");

        AIBoard.placeShipsRandomly();

        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - Constants.WINDOW_WIDTH / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - Constants.WINDOW_HEIGHT / 2);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);

        // Initialize the timer class
        java.util.Timer timer = new Timer();
        TimerTask task = new GameTimer();

        timer.schedule(task, 0, 1000);

        // create top panel

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 70));
        topPanel.setMaximumSize(new Dimension(Constants.WINDOW_WIDTH, 70));
        topPanel.setBackground(Color.YELLOW);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();


//        // Create and add game rules label top panel//
//
//        JLabel gameRulesLabel = new JLabel();
//        gameRulesLabel.setText(String.format("<html><div WIDTH=%d>%s</div></html>", Constants.WINDOW_WIDTH / 4, "Rules: Each time timer will count down from 10 seconds. If players plays a move withing these 10 seconds."));
//        gameRulesLabel.setHorizontalAlignment(JLabel.LEFT);
//
//        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
//        gridBagConstraints.weightx = 0.5;
//        gridBagConstraints.weighty = 1;
//        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
//        gridBagConstraints.gridwidth = 1;
//        gridBagConstraints.gridheight = 3;
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 0;
//
//        topPanel.add(gameRulesLabel, gridBagConstraints);

        // create and add game state component top panel//

        gameStateComponent = new JLabel();
        gameStateComponent.setText("Place your ships and start the game");
        gameStateComponent.setSize(10, 5);
        gameStateComponent.setHorizontalAlignment(JLabel.CENTER);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;

        topPanel.add(gameStateComponent, gridBagConstraints);


        //  create and add start game button to top panel //

        JButton startGameButton = new JButton("Start game");

        startGameButton.addActionListener((ActionEvent e) -> {

            boolean positionsAreCorrect = true;

            for (int i = 0; i < shipList.size(); i++) {

                if (!shipList.get(i).validity) {
                    positionsAreCorrect = false;
                    break;
                }
            }

            if (positionsAreCorrect) {
                // Start the timer
                GameTimer.start();

                for (int i = 0; i < shipList.size(); i++) {

                    Coordinate[] CoordinateArray = new Coordinate[shipList.get(i).size];

                    for (int j = 0; j < shipList.get(i).occupiedGridX.size(); j++) {
                        CoordinateArray[j] = new Coordinate(shipList.get(i).occupiedGridX.get(j), shipList.get(i).occupiedGridY.get(j));
                    }

                    try {

                        humanBoard.placeShip(CoordinateArray);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                AIBoard.setGameHasStarted();
                humanBoard.setGameHasStarted();
                gameStateComponent.setText("Game has started");
                startedGame = true;
                startGameButton.setVisible(false);

            } else {
                gameStateComponent.setText("Positions of your ships are illegal");
            }
        });

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;

        topPanel.add(startGameButton, gridBagConstraints);

        // Create a timer text to the top panel
        timerLabel = new JLabel();
        timerLabel.setText("Time 00:00");
        timerLabel.setSize(10, 5);
        timerLabel.setHorizontalAlignment(JLabel.CENTER);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;

        topPanel.add(timerLabel, gridBagConstraints);

        add(topPanel); // finally add top panel to window

        // create and add main game panel that contains the two game boards //

        JPanel gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new BoxLayout(gameBoardPanel, BoxLayout.X_AXIS));

        // add renderer component to main game window
        Renderer renderer = new Renderer();
        gameBoardPanel.add(renderer);

        // Register the mouse event
        mouse = new Mouse();
        addMouseListener(mouse);

//        // create board to display
//        JPanel boardPanel = new JPanel(new GridLayout(Constants.BOARD_SIZE.x, Constants.BOARD_SIZE.y, 3, 3));
//        //jPanel2.setMaximumSize(new Dimension(Constants.BOARD_PIXEL_SIZE.x / 2, Constants.BOARD_PIXEL_SIZE.x / 2 - 2 * Renderer.holeImageSize));
//        boardPanel.setMaximumSize(new Dimension(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_WIDTH / 2 + 2 * Renderer.holeImageSize));
//
//        // Generate the grid to display
//
//        for (int i = Constants.BOARD_SIZE.x - 1; i >= 0; i--) {
//            for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {
//
//                buttonArray[i][j] = new JButton(alphabet[j] + "" + (i + 1));
//                buttonArray[i][j].setName((j + 1) + "," + (i + 1));
//                buttonArray[i][j].addActionListener(this);
//
//                boardPanel.add(buttonArray[i][j]);
//            }
//        }
//
//        boardPanel.setBackground(Color.red);
//        gameBoardPanel.add(boardPanel);

        // finally, add all components to main window
        add(gameBoardPanel);
        JOptionPane.showMessageDialog(Game.mainWindow, String.format("<html><div WIDTH=%d>%s</div></html>", Constants.WINDOW_WIDTH / 4, "Rules: Each time timer will count down from 10 seconds. If players plays a move withing these 10 seconds."));
        shipList.add(new FrontEndShip(2, 2, 6, 1));
        shipList.add(new FrontEndShip(3, 3, 2, 3));
        shipList.add(new FrontEndShip(3, 1, 7, 4));
        shipList.add(new FrontEndShip(4, 3, 2, 6));
        shipList.add(new FrontEndShip(5, 3, 4, 9));
    }

    // // Button click method
    public static void buttonClick() {

        if (startedGame & !gameOver) {
            if (Renderer.fireTargetX != -1 & Renderer.fireTargetY != -1) {
                if (MainWindow.AIBoard.getBoardState()[Renderer.fireTargetY][Renderer.fireTargetX] == 0) {
                    try {
                        // Human player turn to play //

                        System.out.println("Human click on " + alphabet[Renderer.fireTargetX] + "" + (Renderer.fireTargetY + 1) + ", coordinate " + (Renderer.fireTargetX + 1) + ", " + (Renderer.fireTargetY + 1));

                        Coordinate target = new Coordinate(Renderer.fireTargetX, Renderer.fireTargetY);

                        int result = AIBoard.fireAtTarget(target);

                        if (result == 0) {

                            Renderer.playWaterSplashAnimation(2, Renderer.fireTargetX, Renderer.fireTargetY);

                            System.out.println("Human => miss\n");

                        } else if (result == 1) {

                            Renderer.playExplosionAnimation(2, Renderer.fireTargetX, Renderer.fireTargetY);

                            System.out.println("Human => hit !\n");

                        } else if (result == 2) {

                            Renderer.playExplosionAnimation(2, Renderer.fireTargetX, Renderer.fireTargetY);

                            System.out.println("Human => Sunk !!!\n");

                            numberOfAISunkShips += 1;

                            if (numberOfAISunkShips == 5) {

                                JOptionPane.showMessageDialog(Game.mainWindow, "Player wins !!!");
                                gameStateComponent.setText("Player wins !!!");
                                gameOver = true;
                            }
                        }

                        if (gameOver) {
                            gameStateComponent.setText("Player wins !!!");
                        }

                        // for debugging
                        //humanBoard.printStateGrid();
                        //humanBoard.printShipGrid();
                        //System.out.println("");

                        if (!gameOver) {

                            // AI turn to play //

                            // Pause the timer
                            GameTimer.pause();

                            if (gameMode.equalsIgnoreCase("normal")) {
                                target = myAI.getNextMoveNormal();
                            } else if (gameMode.equalsIgnoreCase("advanced")) {
                                myAI.getNextMoveAdvanced();
                            }

                            System.out.println("AI click on " + alphabet[target.x] + (target.y + 1) + ", coordinate " + (target.x + 1) + "," + (target.y + 1));

                            result = humanBoard.fireAtTarget(target);

                            myAI.receiveResult(result);

                            if (result == 0) {

                                Renderer.playWaterSplashAnimation(1, target.x, target.y);

                                gameStateComponent.setText("AI => Miss");
                                System.out.println("AI => miss\n");

                            } else if (result == 1) {

                                humanBoard.checkSunk();

                                if (humanBoard.checkPlayerSunkShips()) {

                                    JOptionPane.showMessageDialog(Game.mainWindow, "AI wins !!!");
                                    gameOver = true;
                                }

                                Renderer.playExplosionAnimation(1, target.x, target.y);

                                gameStateComponent.setText("AI => Hit !");
                                System.out.println("AI => hit !\n");

                            } else if (result == 2) {

                                humanBoard.checkSunk();

                                if (humanBoard.checkPlayerSunkShips()) {

                                    JOptionPane.showMessageDialog(Game.mainWindow, "AI wins !!!");
                                    gameOver = true;
                                }

                                Renderer.playExplosionAnimation(1, target.x, target.y);

                                gameStateComponent.setText("AI => Sunk !!!");
                                System.out.println("AI => Sunk !!!\n");
                            }

                            if (gameOver) {
                                gameStateComponent.setText("AI wins !!!");
                            }

                            // for debugging
//                            AIBoard.printStateGrid();
//                            AIBoard.printShipGrid();
                            myAI.printCountGrid();
                            //System.out.println("");

                            // Continue the timer
                            GameTimer.start();

                            System.out.println("///////////////////////////\n");
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }

    public static int getWindowLocationX() {
        return Game.mainWindow.getLocationOnScreen().x;
    }

    public static int getWindowLocationY() {
        return Game.mainWindow.getLocationOnScreen().y;
    }

}