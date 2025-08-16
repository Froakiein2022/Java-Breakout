//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create a JFrame object
        JFrame frame = new JFrame("Java Breakout");

        // Creates a Gameplay object for the gameplay of the game.
        Gameplay gameplay = new Gameplay();

        // Set the size of the frame
        frame.setSize(700, 600);

        frame.setLocationRelativeTo(null);

        // Specify the close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(gameplay);

        // Make the frame visible
        frame.setVisible(true);

        // Make the window unresizable.
        frame.setResizable(false);
    }
}