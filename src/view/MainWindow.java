package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

//import controller.Mouse;
import model.AI;
import model.Ship;
import tools.Coordinate;
import constants.Constants;

public class MainWindow extends JFrame implements ActionListener {

    AI myAI = new AI();

    //private static Mouse mouse;

    // A list contains all the five ships
    public static List<Ship> shipList = new ArrayList<Ship>();

    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

    // Button array
    static JButton[][] buttonArray = new JButton[9][11];

    /**
     * default constructor
     */
    public MainWindow() {

        super("MainWindow");

        setSize(Constants.BOARD_SIZE.x, Constants.BOARD_SIZE.y);

        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - Constants.BOARD_SIZE.x / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - Constants.BOARD_SIZE.y / 2);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setBackground(Color.green);

        // create top panel

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setMaximumSize(new Dimension(Constants.BOARD_SIZE.x, 100));
        topPanel.setBackground(Color.YELLOW);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

            // create and add game state component top panel//

            JLabel gameStateComponent = new JLabel();
            gameStateComponent.setText("Place your ships and start the game");
            gameStateComponent.setSize(10, 5);
            gameStateComponent.setHorizontalAlignment(JLabel.CENTER);

            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;

            topPanel.add(gameStateComponent, gridBagConstraints);

            //  create and add start game button to top panel //

            JButton startGameButton = new JButton("Start game");
            startGameButton.setBackground(Color.magenta);

            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;

            topPanel.add(startGameButton, gridBagConstraints);

        add(topPanel); // finally add top panel to window


        // create and add main game panel that contains the two game boards //

        JPanel gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new BoxLayout(gameBoardPanel, BoxLayout.X_AXIS));

        // add renderer component to main game window
        //Renderer renderer = new Renderer();
        //gameBoardPanel.add(renderer);

        // Register the mouse event
        //mouse = new Mouse();
        //addMouseListener(mouse);

        // create board to display
        JPanel boardPanel = new JPanel(new GridLayout(9, 11, 3, 3));
        //jPanel2.setMaximumSize(new Dimension(Constants.BOARD_SIZE.x / 2, Constants.BOARD_SIZE.x / 2 - 2 * Renderer.holeImageSize));
        boardPanel.setMaximumSize(new Dimension(Constants.BOARD_SIZE.x / 2, Constants.BOARD_SIZE.x / 2 - 2 * 20));

        // Generate the grid to display

        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {

                buttonArray[i][j] = new JButton(alphabet[j] + " " + (i + 1));
                buttonArray[i][j].setName(i + "," + j);
                buttonArray[i][j].addActionListener(this);

                boardPanel.add(buttonArray[i][j]);
            }
        }

        boardPanel.setBackground(Color.red);
        gameBoardPanel.add(boardPanel);

        // finally, add all components to main window
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

            myAI.recomputeProbabilisticGrid(5, new Coordinate(Constants.BOARD_SIZE.x, Constants.BOARD_SIZE.y), target);

            temporaryButton.setText("Hit");

        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public int getWindowLocationX() {
        return getLocationOnScreen().x;
    }

    public int getWindowLocationY() {
        return getLocationOnScreen().y;
    }

}