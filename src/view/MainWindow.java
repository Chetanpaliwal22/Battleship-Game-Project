package view;

import constants.Constants;
import controller.Mouse;
import exception.CustomException;
import main.Game;
import model.*;
import tools.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class is main window of the game containing all UI components
 */
public class MainWindow extends JFrame {

    public static AI myAI = new AI();

    public static Board humanBoard = new Board();

    public static Board AIBoard = new Board();

    private static Mouse mouse;

    private static Score score = new Score();

    private static DataManager dataManager = new DataManager();

    public static boolean startedGame = false;

    private static JLabel gameStateComponent;

    private static JRadioButton basicGameplayRadioButton, advancedGameplayRadioButton;

    public static JLabel timerLabel;

    // A list contains all the five ships
    public static List<FrontEndShip> shipList = new ArrayList<FrontEndShip>();

    public static boolean submarineNamed = false;

    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

    public static int numberOfAISunkShips = 0;

    public static boolean gameOver = false;

    public static boolean playerWins = false;

    public static String gameMode = "advanced";

    public static Coordinate playerFireTarget = new Coordinate(-1, -1), AIFireTarget = new Coordinate(-1, -1);

    public static boolean playerGaveAllShots = false;

    public static int numberOfPlayerShots = 0, numberOfPlayerMaxShots = 5;

    public static ArrayList<Coordinate> playerFireTargetList = new ArrayList<Coordinate>();

    public static boolean enableDynamicOcean = false, enableSpecialEffect = true, enableSoundEffect = true;


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
        basicGameplayRadioButton.setOpaque(false);
        basicGameplayRadioButton.setSelected(false);

        // button click method of the radio button
        basicGameplayRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMode = "normal";
                basicGameplayRadioButton.setSelected(true);
                advancedGameplayRadioButton.setSelected(false);
            }
        });

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(3, 10, 3, 5);
        gridBagConstraints.anchor = GridBagConstraints.WEST;

        topPanel.add(basicGameplayRadioButton, gridBagConstraints);


        // create and add radio button to top panel//
        advancedGameplayRadioButton = new JRadioButton("Advanced Gameplay - Salva Variation");
        advancedGameplayRadioButton.setOpaque(false);
        advancedGameplayRadioButton.setSelected(true);

        // button click method of the radio button
        advancedGameplayRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMode = "advanced";
                basicGameplayRadioButton.setSelected(false);
                advancedGameplayRadioButton.setSelected(true);
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
                GameTimer.resetTimer();

                // Start the timer
                GameTimer.startTimer();

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
        timerLabel.setSize(10, 10);
        timerLabel.setHorizontalAlignment(JLabel.CENTER);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;

        topPanel.add(timerLabel, gridBagConstraints);

        // create and add save button to top panel

        JButton saveButton = new JButton("Save");

        saveButton.setPreferredSize(new Dimension(40, 15));
        saveButton.setMaximumSize(new Dimension(40, 15));

        saveButton.addActionListener((ActionEvent e) -> {
            dataManager.save();
        });

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;

        topPanel.add(saveButton, gridBagConstraints);


        // create and add load button to top panel

        JButton loadButton = new JButton("Load");

        loadButton.setPreferredSize(new Dimension(40, 15));
        loadButton.setMaximumSize(new Dimension(40, 15));

        loadButton.addActionListener((ActionEvent e) -> {
            myAI = new AI();

            humanBoard = new Board();

            AIBoard = new Board();

            submarineNamed = false;

            // load the data from xml file
            dataManager.load();

            AIBoard.setGameHasStarted();
            humanBoard.setGameHasStarted();
            gameStateComponent.setText("Game has started");
            startedGame = true;
            startGameButton.setVisible(false);

            basicGameplayRadioButton.setVisible(false);
            advancedGameplayRadioButton.setVisible(false);

            // Start the timer
            GameTimer.startTimer();
        });

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;

        topPanel.add(loadButton, gridBagConstraints);


        add(topPanel); // finally add top panel to window


        // create top panel

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        settingsPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 30));
        settingsPanel.setMaximumSize(new Dimension(Constants.WINDOW_WIDTH, 30));
        settingsPanel.setBackground(Color.YELLOW);
        gridBagConstraints = new GridBagConstraints();


        // create and add graphics setting to top panel//

        JLabel settingsJLabel = new JLabel();
        settingsJLabel.setText("Settings:");
        settingsJLabel.setSize(10, 2);
        settingsJLabel.setHorizontalAlignment(JLabel.CENTER);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;

        settingsPanel.add(settingsJLabel, gridBagConstraints);


        // create and add dynamic background checkbox to top panel

        JCheckBox dynamicBackgroundCheckBox = new JCheckBox("Dynamic Background");
        dynamicBackgroundCheckBox.setPreferredSize(new Dimension(40, 15));
        dynamicBackgroundCheckBox.setMaximumSize(new Dimension(40, 15));
        dynamicBackgroundCheckBox.setOpaque(false);

        dynamicBackgroundCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    enableDynamicOcean = true;
                else
                    enableDynamicOcean = false;
            }
        });

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;

        settingsPanel.add(dynamicBackgroundCheckBox, gridBagConstraints);


        // create and add special effect checkbox to top panel

        JCheckBox specialEffectCheckBox = new JCheckBox("Special Effect");
        specialEffectCheckBox.setSelected(true);
        specialEffectCheckBox.setPreferredSize(new Dimension(40, 15));
        specialEffectCheckBox.setMaximumSize(new Dimension(40, 15));
        specialEffectCheckBox.setOpaque(false);

        specialEffectCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    enableSpecialEffect = true;
                else
                    enableSpecialEffect = false;
            }
        });

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;

        settingsPanel.add(specialEffectCheckBox, gridBagConstraints);


        // create and add sound effect checkbox to top panel

        JCheckBox soundEffectCheckBox = new JCheckBox("Sound Effect");
        soundEffectCheckBox.setSelected(true);
        soundEffectCheckBox.setPreferredSize(new Dimension(40, 15));
        soundEffectCheckBox.setMaximumSize(new Dimension(40, 15));
        soundEffectCheckBox.setOpaque(false);

        soundEffectCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    enableSoundEffect = true;
                else
                    enableSoundEffect = false;
            }
        });

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;

        settingsPanel.add(soundEffectCheckBox, gridBagConstraints);


        add(settingsPanel); // finally add graphics setting panel to window


        // create and add main game panel that contains the two game boards //

        JPanel gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new BoxLayout(gameBoardPanel, BoxLayout.X_AXIS));

        // add renderer component to main game window
        Renderer renderer = new Renderer();
        gameBoardPanel.add(renderer);

        // Register the mouse event
        mouse = new Mouse();
        addMouseListener(mouse);


        // finally, add all components to main window
        add(gameBoardPanel);

        ImageIcon icon = new ImageIcon(Constants.ICON);
        JOptionPane.showMessageDialog(Game.mainWindow, "Welcome to the advanced version of Battleship Game.\n\n Rule:\n\n1. Each time you will get 10 seconds to make a move.\n2. If you take more than 10 seconds, you will lose 5 points.\n\nScore:\n\nWinning : 10 Points\n\n5 Star : Score 100.\n4 Star : 80 <= Score < 100\n3 Star : 70 <= Score < 80\n2 Star : 60 <= Score < 70\n1 Star : 30 <= Score < 60\n0 Star : Score < 30\n\nPress Ok and Enjoy the Game.", "Rule", JOptionPane.INFORMATION_MESSAGE, icon);

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

                            int result;

                            if (gameMode.equalsIgnoreCase("normal")) {
                                System.out.println("Human click on " + alphabet[Renderer.fireTargetX] + "" + (Renderer.fireTargetY + 1) + ", coordinate " + (Renderer.fireTargetX + 1) + ", " + (Renderer.fireTargetY + 1));

                                playerFireTarget = new Coordinate(Renderer.fireTargetX, Renderer.fireTargetY);

                                result = AIBoard.fireAtTarget(playerFireTarget);

                                checkAIBoardResult(result);
                            } else if (gameMode.equalsIgnoreCase("advanced")) {
                                System.out.println("Human click on " + alphabet[Renderer.fireTargetX] + "" + (Renderer.fireTargetY + 1) + ", coordinate " + (Renderer.fireTargetX + 1) + ", " + (Renderer.fireTargetY + 1));

                                if (!playerGaveAllShots) {
                                    playerFireTargetList.add(new Coordinate(Renderer.fireTargetX, Renderer.fireTargetY));

                                    // temporarily mark the fire position as missed
                                    AIBoard.setBoardState(Renderer.fireTargetX, Renderer.fireTargetY);

                                    numberOfPlayerShots += 1;

                                    if (numberOfPlayerShots >= numberOfPlayerMaxShots)
                                        playerGaveAllShots = true;
                                }

                                if (playerGaveAllShots) {

                                    // Loop through the target list to fire at all positions
                                    for (int index = 0; index < numberOfPlayerMaxShots; index++) {
                                        if (!gameOver) {

                                            playerFireTarget = playerFireTargetList.get(index);

                                            result = AIBoard.fireAtTarget(playerFireTarget);

                                            checkAIBoardResult(result);
                                        }
                                    }
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

                                if (gameMode.equalsIgnoreCase("normal")) {

                                    // Pause the timer
                                    GameTimer.pauseTimer();

                                    AIFireTarget = myAI.getNextMove();

                                    System.out.println("AI click on " + alphabet[AIFireTarget.x] + (AIFireTarget.y + 1) + ", coordinate " + (AIFireTarget.x + 1) + "," + (AIFireTarget.y + 1));

                                    result = humanBoard.fireAtTarget(AIFireTarget);

                                    myAI.receiveResult(result);

                                    checkHumanBoardResult(result);
                                } else if (gameMode.equalsIgnoreCase("advanced") & playerGaveAllShots) {

                                    ArrayList<Coordinate> coordinateList = myAI.getNextMoveSalvation(humanBoard.sunkNumber);

                                    ArrayList<Integer> resultList = new ArrayList<Integer>();

                                    for (int i = 0; i < coordinateList.size(); i++) {
                                        if (!gameOver) {
                                            AIFireTarget = coordinateList.get(i);

                                            System.out.println("AI click on " + alphabet[AIFireTarget.x] + (AIFireTarget.y + 1) + ", coordinate " + (AIFireTarget.x + 1) + "," + (AIFireTarget.y + 1));

                                            result = humanBoard.fireAtTarget(AIFireTarget);

                                            resultList.add(result);

                                            checkHumanBoardResult(result);
                                        } else {
                                            break;
                                        }
                                    }

                                    // receive a list containing all results of each shot
                                    myAI.receiveResultSalvation(resultList);


                                    // reset player shot attributes
                                    playerGaveAllShots = false;

                                    numberOfPlayerShots = 0;

                                    playerFireTargetList = new ArrayList<Coordinate>();
                                }

                                if (gameOver) {
                                    gameStateComponent.setText("AI wins !!!");
                                }


                                // Reset the timer
                                GameTimer.resetTimer();

                                // Start the timer
                                GameTimer.startTimer();

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

    /**
     * Check the fire target result on the AI board
     */
    private static void checkAIBoardResult(int result) {
        if (result == 0) {
            Renderer.playWaterSplashAnimation(2, playerFireTarget.x, playerFireTarget.y);

            System.out.println("Human => miss\n");
        } else if (result == 1) {
            Renderer.playExplosionAnimation(2, playerFireTarget.x, playerFireTarget.y);

            System.out.println("Human => hit !\n");
        } else if (result == 2) {
            Renderer.playExplosionAnimation(2, playerFireTarget.x, playerFireTarget.y);

            System.out.println("Human => Sunk !!!\n");

            numberOfAISunkShips += 1;

            checkPLayerWins();
        }
    }

    /**
     * Check the fire target result on the human board
     */
    private static void checkHumanBoardResult(int result) {
        if (result == 0) {
            Renderer.playWaterSplashAnimation(1, AIFireTarget.x, AIFireTarget.y);

            gameStateComponent.setText("AI => Miss");
            System.out.println("AI => miss\n");

        } else if (result == 1) {

            checkAIWins();

            Renderer.playExplosionAnimation(1, AIFireTarget.x, AIFireTarget.y);

            gameStateComponent.setText("AI => Hit !");

            System.out.println("AI => hit !\n");
        } else if (result == 2) {

            Renderer.playExplosionAnimation(1, AIFireTarget.x, AIFireTarget.y);

            // Reduce the number of shots by 1 when the player sank an AI ship
            numberOfPlayerMaxShots -= 1;

            checkAIWins();

            gameStateComponent.setText("AI => Sunk !!!");

            System.out.println("AI => Sunk !!!\n");
        }
    }

    /**
     * Check if player wins by looping through all AI ship states
     */
    private static void checkPLayerWins() {
        if (numberOfAISunkShips == 5) {

            // Pause the timer
            GameTimer.pauseTimer();

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

    /**
     * Check if AI wins by looping through all human ship states
     */
    private static void checkAIWins() {
        humanBoard.checkSunk();

        if (humanBoard.checkPlayerSunkShips()) {

            // Pause the timer
            GameTimer.pauseTimer();

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
    }

    /**
     * Return the window position x value
     */
    public static int getWindowLocationX() {
        return Game.mainWindow.getLocationOnScreen().x;
    }

    /**
     * Return the window position y value
     */
    public static int getWindowLocationY() {
        return Game.mainWindow.getLocationOnScreen().y;
    }

}