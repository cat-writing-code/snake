import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 700;
        int boardHeight = boardWidth;

        // basic frame setup
        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        Game game = new Game(boardWidth, boardHeight);
        frame.add(game);
        frame.pack();
    }
}