package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import controller.Mouse;
import model.AI;
import model.Ship;
import tools.Coordinate;
import model.Board;
import model.Player;

public class BattleShip extends JFrame implements ActionListener {

    AI myAI = new AI();

    private static BattleShip battleShip;

    private static Mouse mouse;

    // A list contains all the five ships
    public static List<Ship> shipList = new ArrayList<Ship>();

//    public static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()), HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.5);

    public static final int WIDTH = 1280, HEIGHT = 720;

    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

    // Button array
    static JButton[][] buttonArray = new JButton[9][11];

    public BattleShip() {
        super("BattleShip");
        setSize(WIDTH, HEIGHT);
        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WIDTH / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - HEIGHT / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setBackground(Color.green);

        // Panel that contains regular buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setMaximumSize(new Dimension(WIDTH, 100));
        buttonPanel.setBackground(Color.YELLOW);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        JLabel label1 = new JLabel();
        label1.setText("Place your ships and start the game");
        label1.setSize(10, 5);
        label1.setHorizontalAlignment(JLabel.CENTER);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        buttonPanel.add(label1, gridBagConstraints);


        JButton startGameButton = new JButton("Start game");
        startGameButton.setBackground(Color.magenta);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;

        buttonPanel.add(startGameButton, gridBagConstraints);

        add(buttonPanel);

        // Panel that contains two game boards
        JPanel gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new BoxLayout(gameBoardPanel, BoxLayout.X_AXIS));

        // add renderer component to main game window
        Renderer renderer = new Renderer();
        gameBoardPanel.add(renderer);

        // Register the mouse event
        mouse = new Mouse();
        addMouseListener(mouse);

        // create board to display
        JPanel jPanel2 = new JPanel(new GridLayout(9, 11, 3, 3));
        jPanel2.setMaximumSize(new Dimension(WIDTH / 2, WIDTH / 2 - 2 * Renderer.holeImageSize));

        // Generating all grids
        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                buttonArray[i][j] = new JButton(alphabet[j] + " " + (i + 1));
                buttonArray[i][j].setName(i + "," + j);
                buttonArray[i][j].addActionListener(this);

                jPanel2.add(buttonArray[i][j]);
            }
        }

        jPanel2.setBackground(Color.red);
        gameBoardPanel.add(jPanel2);

        add(gameBoardPanel);


    }

    // // Button click method
    public void actionPerformed(ActionEvent e) {
        try {
            JButton temporaryButton = (JButton) e.getSource();

            System.out.println(temporaryButton.getText() + " clicked, coordinate: " + temporaryButton.getName());

            temporaryButton.setEnabled(false);

            Coordinate target = new Coordinate(Integer.parseInt(temporaryButton.getName().split(",")[0]),
                    Integer.parseInt(temporaryButton.getName().split(",")[1]));

            myAI.recomputeProbabilisticGrid(5, new Coordinate(9, 11), target);

            temporaryButton.setText("Hit");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static int getWindowLocationX() {
        return battleShip.getLocationOnScreen().x;
    }

    public static int getWindowLocationY() {
        return battleShip.getLocationOnScreen().y;
    }

    public static void main(String[] args) {
        battleShip = new BattleShip();
        battleShip.setVisible(true);
        
        boolean allShipSunkFlag = false;

        /*Board humanBoard = new Board(Constants.BOARD_SIZE,
                1, 1, 1, 1, 1,
                Constants.DESTROYER_SIZE, Constants.SUBMARINE_SIZE, Constants.CRUISER_SIZE,
                Constants.BATTLESHIP_SIZE, Constants.CARRIER_SIZE);*/

        /*Board AIBoard = new Board(Constants.BOARD_SIZE,
                1, 1, 1, 1, 1,
                Constants.DESTROYER_SIZE, Constants.SUBMARINE_SIZE, Constants.CRUISER_SIZE,
                Constants.BATTLESHIP_SIZE, Constants.CARRIER_SIZE);*/





        shipList.add(new Ship(2, 2, 6, 1));
        shipList.add(new Ship(3, 3, 2, 3));
        shipList.add(new Ship(3, 1, 8, 4));
        shipList.add(new Ship(4, 3, 2, 7));
        shipList.add(new Ship(5, 3, 4, 5));

        for (int i = 0; i < shipList.get(0).occupiedGridX.size(); i++) {
            System.out.println(
                    i + ": " + shipList.get(0).occupiedGridX.get(i) + ", " + shipList.get(0).occupiedGridY.get(i));
        }

        // for (int i = 0; i < shipList.get(1).occupiedGridX.size(); i++) {
        // System.out.println(i + ": " + shipList.get(1).occupiedGridX.get(i) + ", " +
        // shipList.get(1).occupiedGridY.get(i));
        // }

        // MAIN GAME LOOP OF THE GAME
        
        Player humanPlayer = new Player("human");
        Player AIPlayer = new Player("AI");
        
        while(!allShipSunkFlag) {
        
        //Human plays the first move.
        
       //Play the move from ui.
        
        AIPlayer.getNextMove(9, 11, null);

        
        }

    }

}