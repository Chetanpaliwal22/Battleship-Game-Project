package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

//import controller.Mouse;
import controller.Mouse;
import main.Game;
import model.AI;
import model.Board;
import model.Ship;
import tools.Coordinate;
import constants.Constants;

public class MainWindow extends JFrame implements ActionListener {

    AI myAI = new AI();

    Board humanBoard = new Board();

    Board AIBoard = new Board();

    private static Mouse mouse;

    public static boolean startedGame = false;

    private JLabel gameStateComponent;

    // A list contains all the five ships
    public static List<Ship> shipList = new ArrayList<Ship>();

    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

    // Button array
    static JButton[][] buttonArray = new JButton[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

    /**
     * default constructor
     */
    public MainWindow() {

        super("BattleShip");

        AIBoard.placeShipsRandomly();

        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - 1280 / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - 720 / 2);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setBackground(Color.green);

        // create top panel

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setMaximumSize(new Dimension(Constants.WINDOW_WIDTH, 100));
        topPanel.setBackground(Color.YELLOW);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // create and add game state component top panel//

        gameStateComponent = new JLabel();
        gameStateComponent.setText("Place your ships and start the game");
        gameStateComponent.setSize(10, 5);
        gameStateComponent.setHorizontalAlignment(JLabel.CENTER);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        topPanel.add(gameStateComponent, gridBagConstraints);

        //  create and add start game button to top panel //

        JButton startGameButton = new JButton("Start game");

        startGameButton.addActionListener((ActionEvent e) -> {

            AIBoard.setGameHasStarted();
            humanBoard.setGameHasStarted();

            boolean positionsAreCorrect = true;
            for (int i = 0; i < shipList.size(); i++) {
                if (!shipList.get(i).validity) {
                    positionsAreCorrect = false;
                    break;
                }
            }
            if (positionsAreCorrect) {
                gameStateComponent.setText("Game has started");
                startedGame = true;
//            gameStateComponent.setText("Game started!!!");
//            gameStateComponent.setText("\nOne emeny ship has been sunk");
                startGameButton.setVisible(false);
//            JOptionPane.showMessageDialog(this, "Player wins!!!!!!");
            } else {
                gameStateComponent.setText("Positions of your ships are illegal");
            }
        });


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;

        topPanel.add(startGameButton, gridBagConstraints);

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

        // create board to display
        JPanel boardPanel = new JPanel(new GridLayout(Constants.BOARD_SIZE.x, Constants.BOARD_SIZE.y, 3, 3));
        //jPanel2.setMaximumSize(new Dimension(Constants.BOARD_PIXEL_SIZE.x / 2, Constants.BOARD_PIXEL_SIZE.x / 2 - 2 * Renderer.holeImageSize));
        boardPanel.setMaximumSize(new Dimension(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_WIDTH / 2 - 2 * Renderer.holeImageSize));

        // Generate the grid to display

        for (int i = Constants.BOARD_SIZE.x-1; i >= 0; i--) {
            for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {

                buttonArray[i][j] = new JButton(alphabet[j] + "" + (i + 1));
                buttonArray[i][j].setName((j+1) + "," + (i+1));
                buttonArray[i][j].addActionListener(this);

                boardPanel.add(buttonArray[i][j]);
            }
        }

        boardPanel.setBackground(Color.red);
        gameBoardPanel.add(boardPanel);

        // finally, add all components to main window
        add(gameBoardPanel);

        shipList.add(new Ship(2, 2, 6, 1));
        shipList.add(new Ship(3, 3, 2, 3));
        shipList.add(new Ship(3, 1, 8, 4));
        shipList.add(new Ship(4, 3, 2, 7));
        shipList.add(new Ship(5, 3, 4, 5));
    }

    // // Button click method
    public void actionPerformed(ActionEvent e) {

        if (startedGame) {
            try {
                JButton temporaryButton = (JButton) e.getSource();

                System.out.println( "Human click on " + temporaryButton.getText() + ", coordinate " + temporaryButton.getName());

                temporaryButton.setEnabled(false);


                // Human player turn to play //

                Coordinate target = new Coordinate(
                        (Integer.parseInt(temporaryButton.getName().split(",")[0]) -1),
                        (Integer.parseInt(temporaryButton.getName().split(",")[1]) - 1));

                int result = AIBoard.fireAtTarget( target );

                if( result == 0 ){
                    temporaryButton.setText("Human => Miss");
                    System.out.println("Human => missed\n");
                } else if( result == 1 ){
                    temporaryButton.setText("Human => hit !");
                    System.out.println("Human => hit !\n");
                } else if( result == 2 ){
                    temporaryButton.setText("Human => Sunk !!!");
                    System.out.println("Human => Sunk !!!\n");
                }


                // AI turn to play //

                target = myAI.getNextMove();

                System.out.println("AI click on xx, coordinate " + target.x + "," + target.y);

                result = humanBoard.fireAtTarget( target );

                myAI.receiveResult(result);

                if( result == 0 ){
                    temporaryButton.setText("AI => Miss");
                    System.out.println("AI => missed.\n");
                } else if( result == 1 ){
                    temporaryButton.setText("AI => hit !");
                    System.out.println("AI => hit !\n");
                } else if( result == 2 ){
                    temporaryButton.setText("AI => Sunk !!!");
                    System.out.println("AI => Sunk !!!\n");
                }

                myAI.printGrid();

                System.out.println("\n///////////////////////////\n");


            } catch (Exception exception) {

                exception.printStackTrace();
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