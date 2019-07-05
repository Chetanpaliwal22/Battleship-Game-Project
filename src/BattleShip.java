import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleShip extends JFrame implements ActionListener {
    static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

    static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};

    // Button array
    static JButton[] buttonArray = new JButton[100];

    public BattleShip() {
        super("BattleShip");
        setSize(WIDTH, HEIGHT);
        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WIDTH / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - HEIGHT / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        Label label1 = new Label();
        label1.setText("Select a grid to fire");
        label1.setSize(10, 5);
        label1.setAlignment(Label.CENTER);
        add(label1);

        JPanel jPanel2 = new JPanel(new GridLayout(9, 11, 3, 3));

        // Generating all grids
        for (int i = 9; i >= 1; i--) {
            for (int j = 0; j < 11; j++) {
                int buttonId = i * 10 - j + 1;

                buttonArray[buttonId] = new JButton(alphabet[j] + " " + i);
                buttonArray[buttonId].addActionListener(this);

                jPanel2.add(buttonArray[buttonId]);
            }
        }

        add(jPanel2);
    }

    // Button click method
    public void actionPerformed(ActionEvent e) {
        JButton temporaryButton = (JButton) e.getSource();


        System.out.println(temporaryButton.getText() + " clicked");

        temporaryButton.setEnabled(false);

        temporaryButton.setText("Hit");
    }

    public static void main(String[] args) {
        BattleShip gui = new BattleShip();
        gui.setVisible(true);
    }

}
