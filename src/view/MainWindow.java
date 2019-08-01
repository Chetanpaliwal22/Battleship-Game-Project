package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import controller.Mouse;
import main.Game;
import model.AI;
import model.Board;
import model.GameTimer;
import model.Score;
import tools.Coordinate;
import constants.Constants;


/**
 * This class is main window of the game
 */
public class MainWindow extends JFrame {

    static AI myAI = new AI();

    public static Board humanBoard = new Board();

    public static Board AIBoard = new Board();

    private static Mouse mouse;

    private static Score score = new Score();

    public static boolean startedGame = false;

    private static JLabel gameStateComponent;

    private static JRadioButton basicGameplayRadioButton, advancedGameplayRadioButton;

    public static JLabel timerLabel;

    // A list contains all the five ships
    public static List<FrontEndShip> shipList = new ArrayList<FrontEndShip>();

    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

//    // Button array
//    static JButton[][] buttonArray = new JButton[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

    private static int numberOfAISunkShips = 0;

    public static boolean gameOver = false;

    public static boolean playerWins = false;

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

        setBackground(Color.green);
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


        // create and add radio button to top panel//
        basicGameplayRadioButton = new JRadioButton("Basic Gameplay");
        basicGameplayRadioButton.setSelected(false);

        // button click method of the radio button
        basicGameplayRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (basicGameplayRadioButton.isSelected()) {
                    gameMode = "normal";
                    advancedGameplayRadioButton.setSelected(false);
                }
            }
        });

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(3, 10, 3, 5);
        gridBagConstraints.anchor = GridBagConstraints.WEST;

        topPanel.add(basicGameplayRadioButton, gridBagConstraints);


        // create and add radio button to top panel//
        advancedGameplayRadioButton = new JRadioButton("Advanced Gameplay - Salva Variation");
        advancedGameplayRadioButton.setSelected(true);

        // button click method of the radio button
        advancedGameplayRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (advancedGameplayRadioButton.isSelected()) {
                    gameMode = "advanced";
                    basicGameplayRadioButton.setSelected(false);
                }
            }
        });

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(3, 10, 3, 5);
        gridBagConstraints.anchor = GridBagConstraints.WEST;

        topPanel.add(advancedGameplayRadioButton, gridBagConstraints);


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

                basicGameplayRadioButton.setVisible(false);
                advancedGameplayRadioButton.setVisible(false);

                // Reset the timer
                GameTimer.reset();

                // Start the timer
                GameTimer.start();

            } else {
                gameStateComponent.setText("Positions of your ships are illegal");
            }
        });

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

        ImageIcon icon = new ImageIcon(Constants.ICON);
        JOptionPane.showMessageDialog(Game.mainWindow, "Welcome to the advanced version of Battleship Game.\n\n Rule:\n\n1. Each time you will get 10 seconds to make a move.\n2. If you take more than 10 seconds, you will lose 5 points.\n\nScore:\n\nWinning : 10 Points\n\n5 Star : Score 100.\n4 Star : 80 <= Score <= 90\n3 Star : 70 <= Score < 80\n2 Star : 60 <= Score < 70\n1 Star : 30 <= Score < 60\n0 Star : Score < 30\n\nPress Ok and Enjoy the Game.", "Rule", JOptionPane.INFORMATION_MESSAGE, icon);

        shipList.add(new FrontEndShip(2, 2, 6, 1));
        shipList.add(new FrontEndShip(3, 3, 2, 3));
        shipList.add(new FrontEndShip(3, 1, 7, 4));
        shipList.add(new FrontEndShip(4, 3, 2, 6));
        shipList.add(new FrontEndShip(5, 3, 4, 9));
    }

    // button click method
    public static void buttonClick() {
        if (startedGame & !gameOver) {
            if (Renderer.fireTargetX != -1 & Renderer.fireTargetY != -1) {
                if (MainWindow.AIBoard.getBoardState()[Renderer.fireTargetY][Renderer.fireTargetX] == 0) {
                    if (startedGame & !gameOver) {
                        try {
                            // Human player turn to play //

                            // Reset the timer
                            GameTimer.reset();

                            // Start the timer
                            GameTimer.start();

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

                                    // Pause the timer
                                    GameTimer.pause();

                                    playerWins = true;

                                    timerLabel.setText("Final Score: " + score.calculateFinalScore());

                                    ImageIcon icon = null;

                                    int numberOfStars = score.calculateStars();

                                    if (numberOfStars == 1)
                                        icon = new ImageIcon(Constants.ONE_STAR);
                                    else if (numberOfStars == 2)
                                        icon = new ImageIcon(Constants.TWO_STARS);
                                    else if (numberOfStars == 3)
                                        icon = new ImageIcon(Constants.THREE_STARS);
                                    else if (numberOfStars == 4)
                                        icon = new ImageIcon(Constants.FOUR_STARS);
                                    else if (numberOfStars == 5)
                                        icon = new ImageIcon(Constants.FIVE_STARS);

                                    JOptionPane.showMessageDialog(Game.mainWindow, "Congratulations!! You were able to defeat AI.\nYour score is " + score.returnFinalScore() + " points.\n", "Game Over", JOptionPane.INFORMATION_MESSAGE, icon);

                                    gameStateComponent.setText("Player wins !!!");
                                    gameOver = true;
                                }
                            }

                            if (gameOver) {
                                gameStateComponent.setText("Player wins !!!");
                            }

                            // for debugging
                            humanBoard.printStateGrid();
                            humanBoard.printShipGrid();
                            System.out.println("");

                            if (!gameOver) {

                                // AI turn to play //

                                // Pause the timer
                                GameTimer.pause();

                                if (gameMode.equalsIgnoreCase("normal")) {
                                    target = myAI.getNextMove();
                                }

                                //else if(gameMode.equalsIgnoreCase("advanced")) { myAI.getFiveNextMove(); }

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

                                        // Pause the timer
                                        GameTimer.pause();

                                        timerLabel.setText("Final Score: " + score.calculateFinalScore());

                                        ImageIcon icon = null;

                                        int numberOfStars = score.calculateStars();

                                        if (numberOfStars == 1)
                                            icon = new ImageIcon(Constants.ONE_STAR);
                                        else if (numberOfStars == 2)
                                            icon = new ImageIcon(Constants.TWO_STARS);
                                        else if (numberOfStars == 3)
                                            icon = new ImageIcon(Constants.THREE_STARS);
                                        else if (numberOfStars == 4)
                                            icon = new ImageIcon(Constants.FOUR_STARS);
                                        else if (numberOfStars == 5)
                                            icon = new ImageIcon(Constants.FIVE_STARS);

                                        JOptionPane.showMessageDialog(Game.mainWindow, "Boohoo !! AI Won !! Keep Trying.\nYour score is " + score.returnFinalScore() + " points.\n", "Game Over", JOptionPane.INFORMATION_MESSAGE, icon);
                                        gameOver = true;
                                    }

                                    Renderer.playExplosionAnimation(1, target.x, target.y);

                                    gameStateComponent.setText("AI => Hit !");
                                    System.out.println("AI => hit !\n");

                                } else if (result == 2) {

                                    Renderer.playExplosionAnimation(1, target.x, target.y);

                                    humanBoard.checkSunk();

                                    if (humanBoard.checkPlayerSunkShips()) {

                                        // Pause the timer
                                        GameTimer.pause();

                                        timerLabel.setText("Final Score: " + score.calculateFinalScore());

                                        ImageIcon icon = null;

                                        int numberOfStars = score.calculateStars();

                                        if (numberOfStars == 1)
                                            icon = new ImageIcon(Constants.ONE_STAR);
                                        else if (numberOfStars == 2)
                                            icon = new ImageIcon(Constants.TWO_STARS);
                                        else if (numberOfStars == 3)
                                            icon = new ImageIcon(Constants.THREE_STARS);
                                        else if (numberOfStars == 4)
                                            icon = new ImageIcon(Constants.FOUR_STARS);
                                        else if (numberOfStars == 5)
                                            icon = new ImageIcon(Constants.FIVE_STARS);

                                        JOptionPane.showMessageDialog(Game.mainWindow, "Boohoo !! AI Won !! Keep Trying.\nYou scored " + score.returnFinalScore() + " points.\n", "Game Over", JOptionPane.INFORMATION_MESSAGE, icon);
                                        gameOver = true;
                                    }

                                    gameStateComponent.setText("AI => Sunk !!!");
                                    System.out.println("AI => Sunk !!!\n");
                                }

                                if (gameOver) {
                                    gameStateComponent.setText("AI wins !!!");
                                }

                                // for debugging
                                AIBoard.printStateGrid();
                                AIBoard.printShipGrid();
                                myAI.printCountGrid();
                                System.out.println("");

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
    }

    public static int getWindowLocationX() {
        return Game.mainWindow.getLocationOnScreen().x;
    }

    public static int getWindowLocationY() {
        return Game.mainWindow.getLocationOnScreen().y;
    }

}