package view;

import constants.Constants;
import controller.Mouse;
import main.Game;
import model.*;
import network.Client;
import network.Data;
import network.Server;
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
 * This class is main window of the game
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

    private static JButton offlineGameButton, onlineButton;

    // A list contains all the five ships
    public static List<FrontEndShip> shipList = new ArrayList<FrontEndShip>();

    public static boolean submarineNamed = false;

    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

    public static int numberOfAISunkShips = 0;

    public static boolean gameOver = false;

    public static boolean playerWins = false;

    public static String gameMode = "advanced";

    public static Coordinate playerFireTarget, AIFireTarget;

    public static boolean playerGaveAllShots = false;

    public static int numberOfPlayerShots = 0, numberOfPlayerMaxShots = 5;

    public static ArrayList<Coordinate> playerFireTargetList = new ArrayList<Coordinate>();

    public static boolean enableDynamicOcean = false, enableSpecialEffect = true, enableSoundEffect = true;

    public static boolean onlineMode = false;

    public static boolean freezing = false;

    private static Client client;

    public static boolean client1Ready = false, client2Ready = false, bothPlayersReady = false;

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

        // create top panel

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 80));
        topPanel.setMaximumSize(new Dimension(Constants.WINDOW_WIDTH, 80));
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

        // create and add start game button to top panel //

        offlineGameButton = new JButton("Offline");

        offlineGameButton.addActionListener((ActionEvent e) -> {

            if (!onlineMode) {
                // Initialize the timer class
                java.util.Timer timer = new Timer();
                TimerTask task = new GameTimer();

                timer.schedule(task, 0, 1000);
            }

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
                        CoordinateArray[j] = new Coordinate(shipList.get(i).occupiedGridX.get(j),
                                shipList.get(i).occupiedGridY.get(j));
                    }

                    try {

                        humanBoard.placeShip(CoordinateArray);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                AIBoard.setGameHasStarted();
                humanBoard.setGameHasStarted();
                gameStateComponent.setText("Offline game has started");
                startedGame = true;
                offlineGameButton.setVisible(false);

                onlineButton.setVisible(false);
                offlineGameButton.setVisible(false);

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

        topPanel.add(offlineGameButton, gridBagConstraints);

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
            if (!freezing)
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
            if (!freezing) {

                myAI = new AI();

                humanBoard = new Board();

                AIBoard = new Board();

                // load the data from xml file
                dataManager.load();

                AIBoard.setGameHasStarted();
                humanBoard.setGameHasStarted();
                gameStateComponent.setText("Game has started");
                startedGame = true;

                onlineButton.setVisible(false);
                offlineGameButton.setVisible(false);

                basicGameplayRadioButton.setVisible(false);
                advancedGameplayRadioButton.setVisible(false);

                // Start the timer
                GameTimer.startTimer();
            }
        });

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;

        topPanel.add(loadButton, gridBagConstraints);

        // create and add online button to top panel //

        onlineButton = new JButton("Online");
        onlineButton.setPreferredSize(new Dimension(40, 15));
        onlineButton.setMaximumSize(new Dimension(40, 15));

        onlineButton.addActionListener((ActionEvent e) -> {
            boolean positionsAreCorrect = true;

            for (int i = 0; i < shipList.size(); i++) {

                if (!shipList.get(i).validity) {
                    positionsAreCorrect = false;
                    break;
                }
            }

            if (positionsAreCorrect) {

                onlineMode = true;

                saveButton.setVisible(false);
                loadButton.setVisible(false);

                if (gameMode.compareToIgnoreCase("advanced") == 0)
                    MainWindow.timerLabel.setText("<html>Number of shots: " + MainWindow.numberOfPlayerShots + "/" + MainWindow.numberOfPlayerMaxShots + "</html>");
                else
                    MainWindow.timerLabel.setVisible(false);

                client = new Client();

                gameStateComponent.setText("Online game has started");
                startedGame = true;

                onlineButton.setVisible(false);
                offlineGameButton.setVisible(false);

                basicGameplayRadioButton.setVisible(false);
                advancedGameplayRadioButton.setVisible(false);
            } else {
                gameStateComponent.setText("Positions of your ships are illegal");
            }
        });

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;

        topPanel.add(onlineButton, gridBagConstraints);

        add(topPanel); // finally add top panel to window

        // create top panel

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        settingsPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 25));
        settingsPanel.setMaximumSize(new Dimension(Constants.WINDOW_WIDTH, 25));
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
        JOptionPane.showMessageDialog(Game.mainWindow, "Welcome to the advanced version of Battleship Game.\n\n Rule:\n\n1. Each time you will get 10 seconds to make a move.\n2. If you take more than 10 seconds, you will lose 5 points.\n\nScore:\n\nWinning : 10 Points\n\n5 Star : Score 100.\n4 Star : 80 <= Score < 100\n3 Star : 70 <= Score < 80\n2 Star : 60 <= Score < 70\n1 Star : 30 <= Score < 60\n0 Star : Score < 30 \n \n Online:\n" + "\n To start online Game please click on online button and wait \n for other player to join, once he join click on start game and \n your game will be started. \n\nPress Ok and Enjoy the Game.", "Rule", JOptionPane.INFORMATION_MESSAGE, icon);

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
                            if (onlineMode) {
                                if (!freezing) {

                                    int result;

                                    if (gameMode.equalsIgnoreCase("normal")) {
                                        freezing = true;

                                        Data data = client.getDefaultClient();

                                        // System.out.println("Initial: " + data.fireTargetX + ", " + data.fireTargetY +
                                        // " ");

                                        ArrayList<Integer> fireTargetXList = new ArrayList<Integer>();
                                        ArrayList<Integer> fireTargetYList = new ArrayList<Integer>();

                                        playerFireTarget = new Coordinate(Renderer.fireTargetX, Renderer.fireTargetY);

                                        fireTargetXList.add(playerFireTarget.x);
                                        fireTargetYList.add(playerFireTarget.y);

                                        data.freezing = true;

                                        data.receiveResult = false;

                                        data.fireTargetX = playerFireTarget.x;
                                        data.fireTargetY = playerFireTarget.y;

                                        data.fireTargetXList = fireTargetXList;
                                        data.fireTargetYList = fireTargetYList;

                                        data.gameMode = gameMode;

                                        client.sendServer(data);

                                    } else if (gameMode.equalsIgnoreCase("advanced")) {
                                        if (!playerGaveAllShots) {
                                            playerFireTargetList
                                                    .add(new Coordinate(Renderer.fireTargetX, Renderer.fireTargetY));

                                            // temporarily mark the fire position as missed
                                            AIBoard.setBoardState(Renderer.fireTargetX, Renderer.fireTargetY);

                                            numberOfPlayerShots += 1;

                                            if (numberOfPlayerShots >= numberOfPlayerMaxShots)
                                                playerGaveAllShots = true;
                                        }

                                        if (playerGaveAllShots) {
                                            freezing = true;

                                            numberOfPlayerShots = 0;

                                            Data data = client.getDefaultClient();

                                            ArrayList<Integer> fireTargetXList = new ArrayList<Integer>();
                                            ArrayList<Integer> fireTargetYList = new ArrayList<Integer>();

                                            // Loop through the target list to fire at all positions
                                            for (int index = 0; index < numberOfPlayerMaxShots; index++) {
                                                if (!gameOver) {

                                                    playerFireTarget = playerFireTargetList.get(index);

                                                    fireTargetXList.add(playerFireTarget.x);
                                                    fireTargetYList.add(playerFireTarget.y);
                                                }
                                            }

                                            playerFireTarget = new Coordinate(Renderer.fireTargetX,
                                                    Renderer.fireTargetY);

                                            fireTargetXList.add(playerFireTarget.x);
                                            fireTargetYList.add(playerFireTarget.y);

                                            data.freezing = true;

                                            data.receiveResult = false;

                                            data.fireTargetX = playerFireTarget.x;
                                            data.fireTargetY = playerFireTarget.y;

                                            data.fireTargetXList = fireTargetXList;
                                            data.fireTargetYList = fireTargetYList;

                                            data.gameMode = gameMode;

                                            client.sendServer(data);
                                        }

                                        MainWindow.timerLabel.setText("<html>Number of shots: " + numberOfPlayerShots + "/" + numberOfPlayerMaxShots + "</html>");
                                    }
                                }
                            } else {
                                if (!freezing) {

                                    // Human player turn to play //

                                    int result;

                                    if (gameMode.equalsIgnoreCase("normal")) {

                                        freezing = true;

                                        // System.out.println("Human click on " + alphabet[Renderer.fireTargetX] + ""
                                        // + (Renderer.fireTargetY + 1) + ", coordinate " + (Renderer.fireTargetX + 1)
                                        // + ", " + (Renderer.fireTargetY + 1));

                                        playerFireTarget = new Coordinate(Renderer.fireTargetX, Renderer.fireTargetY);

                                        result = AIBoard.fireAtTarget(playerFireTarget);

                                        checkAIBoardResult(result);
                                    } else if (gameMode.equalsIgnoreCase("advanced")) {
                                        System.out.println("Human click on " + alphabet[Renderer.fireTargetX] + ""
                                                + (Renderer.fireTargetY + 1) + ", coordinate "
                                                + (Renderer.fireTargetX + 1) + ", " + (Renderer.fireTargetY + 1));

                                        if (!playerGaveAllShots) {
                                            playerFireTargetList
                                                    .add(new Coordinate(Renderer.fireTargetX, Renderer.fireTargetY));

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

                                }
                            }

                            // for debugging
                            // humanBoard.printStateGrid();
                            // humanBoard.printShipGrid();
                            // System.out.println("");

                            if (!gameOver & !onlineMode) {

                                // AI turn to play //

                                if (gameMode.equalsIgnoreCase("normal")) {

                                    // Pause the timer
                                    GameTimer.pauseTimer();

                                    AIFireTarget = myAI.getNextMove();

                                

                                    int result = humanBoard.fireAtTarget(AIFireTarget);

                                    myAI.receiveResult(result);

                                    checkHumanBoardResult(result);
                                } else if (gameMode.equalsIgnoreCase("advanced") & playerGaveAllShots) {

                                    ArrayList<Coordinate> coordinateList = myAI
                                            .getNextMoveSalvation(humanBoard.sunkNumber);

                                    ArrayList<Integer> resultList = new ArrayList<Integer>();

                                    for (int i = 0; i < coordinateList.size(); i++) {
                                        if (!gameOver) {
                                            AIFireTarget = coordinateList.get(i);

                                         
                                            int result = humanBoard.fireAtTarget(AIFireTarget);

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

                                freezing = false;

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

    // send the result to opponent to update the board
    public static void receiveResultFromOpponent(ArrayList<Integer> fireTargetXList, ArrayList<Integer> fireTargetYList,
                                                 ArrayList<Integer> resultIdList) {

    

        int[][] targetGridState = MainWindow.AIBoard.getBoardState();

        for (int index = 0; index < resultIdList.size(); index++) {
            // Check the result
            if (resultIdList.get(index) == 0) {
                targetGridState[fireTargetYList.get(index)][fireTargetXList.get(index)] = 1;

                MainWindow.AIBoard.setBoardState(targetGridState);

                Renderer.playWaterSplashAnimation(2, fireTargetXList.get(index), fireTargetYList.get(index));

                gameStateComponent.setText("You => Miss");

            } else if (resultIdList.get(index) == 1) {
                targetGridState[fireTargetYList.get(index)][fireTargetXList.get(index)] = 2;

                MainWindow.AIBoard.setBoardState(targetGridState);

                Renderer.playExplosionAnimation(2, fireTargetXList.get(index), fireTargetYList.get(index));

                gameStateComponent.setText("You => Hit !");
            } else if (resultIdList.get(index) == 2) {
                targetGridState[fireTargetYList.get(index)][fireTargetXList.get(index)] = 2;

                MainWindow.AIBoard.setBoardState(targetGridState);

                Renderer.playExplosionAnimation(2, fireTargetXList.get(index), fireTargetYList.get(index));

                gameStateComponent.setText("You => Hit !");
            }
        }

        System.out.println("Game over: " + gameOver + " " + humanBoard.checkPlayerSunkShips());

        if (gameOver) {
            if (!humanBoard.checkPlayerSunkShips()) {

                ImageIcon icon = null;
                icon = new ImageIcon(Constants.FIVE_STARS);

                JOptionPane.showMessageDialog(Game.mainWindow, "Congratulations!! You were able to defeat your opponent.", "Game Over", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }

        // reset player shot attributes
        playerGaveAllShots = false;

        numberOfPlayerShots = 0;

        playerFireTargetList = new ArrayList<Coordinate>();
    }

    // receive the fire target from opponent
    public static void receiveFireTarget(ArrayList<Integer> fireTargetXList, ArrayList<Integer> fireTargetYList) {
        if (fireTargetXList.size() >= 1) {

            int result;

            ArrayList<Integer> resultIdList = new ArrayList<Integer>();

            for (int index = 0; index < fireTargetXList.size(); index++) {

                result = 0;

                for (int i = 0; i < shipList.size(); i++) {
                    for (int gridIndex = 0; gridIndex < MainWindow.shipList.get(i).occupiedGridX.size(); gridIndex++) {
                       
                        int gridX = MainWindow.shipList.get(i).occupiedGridX.get(gridIndex),
                                gridY = MainWindow.shipList.get(i).occupiedGridY.get(gridIndex),
                                fireX = fireTargetXList.get(index), fireY = fireTargetYList.get(index);

                      
                        if ((gridX == fireX) & (gridY == fireY))
                            result = 1;
                    }
                }

                humanBoard.checkSunk();

              

                int[][] targetGridState = MainWindow.humanBoard.getBoardState();

              
                if (result == 0) {
                    targetGridState[fireTargetYList.get(index)][fireTargetXList.get(index)] = 1;

                    MainWindow.humanBoard.setBoardState(targetGridState);

                    Renderer.playWaterSplashAnimation(1, fireTargetXList.get(index), fireTargetYList.get(index));

                    gameStateComponent.setText("Opponent => Miss");

                } else if (result == 1) {
                    targetGridState[fireTargetYList.get(index)][fireTargetXList.get(index)] = 2;

                    MainWindow.humanBoard.setBoardState(targetGridState);

                    checkOpponentWins();

                    Renderer.playExplosionAnimation(1, fireTargetXList.get(index), fireTargetYList.get(index));

                    gameStateComponent.setText("Opponent => Hit !");
                } else if (result == 2) {
                    targetGridState[fireTargetYList.get(index)][fireTargetXList.get(index)] = 2;

                    MainWindow.humanBoard.setBoardState(targetGridState);

                    Renderer.playExplosionAnimation(1, fireTargetXList.get(index), fireTargetYList.get(index));

                    System.out.println("numberOfPlayerMaxShots " + numberOfPlayerMaxShots);

                    checkOpponentWins();

                    gameStateComponent.setText("Opponent => Sunk !!!");
                }

                resultIdList.add(result);
            }

            if (gameOver) {
                if (!humanBoard.checkPlayerSunkShips()) {

                    ImageIcon icon = null;
                    icon = new ImageIcon(Constants.FIVE_STARS);

                    JOptionPane.showMessageDialog(Game.mainWindow, "Congratulations!! You were able to defeat your opponent.", "Game Over", JOptionPane.INFORMATION_MESSAGE, icon);
                }
            }

            Data data = client.getDefaultClient();

            data.fireTargetX = fireTargetXList.get(0);
            data.fireTargetY = fireTargetYList.get(0);

            data.fireTargetXList = fireTargetXList;
            data.fireTargetYList = fireTargetYList;

            data.receiveResult = true;

            data.resultIdList = resultIdList;

            if (gameOver)
                data.gameOver = true;

            data.gameMode = gameMode;

            client.sendServer(data);
        }
    }

    private static void checkAIBoardResult(int result) {
        if (result == 0) {
            Renderer.playWaterSplashAnimation(2, playerFireTarget.x, playerFireTarget.y);

            // System.out.println("Human => miss\n");
        } else if (result == 1) {
            Renderer.playExplosionAnimation(2, playerFireTarget.x, playerFireTarget.y);

            // System.out.println("Human => hit !\n");
        } else if (result == 2) {
            Renderer.playExplosionAnimation(2, playerFireTarget.x, playerFireTarget.y);

            // System.out.println("Human => Sunk !!!\n");

            numberOfAISunkShips += 1;

            checkPLayerWins();
        }
    }

    private static void checkHumanBoardResult(int result) {
        if (result == 0) {
            Renderer.playWaterSplashAnimation(1, AIFireTarget.x, AIFireTarget.y);

            gameStateComponent.setText("AI => Miss");
            // System.out.println("AI => miss\n");

        } else if (result == 1) {

            checkOpponentWins();

            Renderer.playExplosionAnimation(1, AIFireTarget.x, AIFireTarget.y);

            gameStateComponent.setText("AI => Hit !");

            // System.out.println("AI => hit !\n");
        } else if (result == 2) {

            Renderer.playExplosionAnimation(1, AIFireTarget.x, AIFireTarget.y);

            checkOpponentWins();

            gameStateComponent.setText("AI => Sunk !!!");

            // System.out.println("AI => Sunk !!!\n");
        }
    }

    private static void checkPLayerWins() {
        if (!gameOver & numberOfAISunkShips == 5) {

            // Pause the timer
            GameTimer.pauseTimer();

            playerWins = true;

            if (!onlineMode)
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

            if (!onlineMode)
                JOptionPane.showMessageDialog(Game.mainWindow, "Congratulations!! You were able to defeat your opponent.\nYour score is " + score.returnFinalScore() + " points.\n", "Game Over", JOptionPane.INFORMATION_MESSAGE, icon);

            gameStateComponent.setText("Player wins !!!");
            gameOver = true;
        }
    }

    private static void checkOpponentWins() {
        humanBoard.checkSunk();

        if (!gameOver & humanBoard.checkPlayerSunkShips()) {

            // Pause the timer
            GameTimer.pauseTimer();

            if (!onlineMode)
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

            if (!onlineMode) {
                JOptionPane.showMessageDialog(Game.mainWindow, "Boohoo !! You lost !! Keep Trying.\nYou scored " + score.returnFinalScore() + " points.\n", "Game Over", JOptionPane.INFORMATION_MESSAGE, icon);
            } else {
                gameStateComponent.setText("");


                icon = new ImageIcon(Constants.ONE_STAR);
                JOptionPane.showMessageDialog(Game.mainWindow, "Boohoo !! You lost !! Keep Trying.", "Game Over", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            gameOver = true;
        }
    }

    public static int getWindowLocationX() {
        return Game.mainWindow.getLocationOnScreen().x;
    }

    public static int getWindowLocationY() {
        return Game.mainWindow.getLocationOnScreen().y;
    }

}