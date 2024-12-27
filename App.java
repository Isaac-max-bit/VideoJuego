import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        // Crear el JFrame
        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el panel del juego
        Snake snake = new Snake(boardWidth, boardHeight);

        // Añadir el panel Snake al JFrame
        frame.add(snake);

        // Asegurarse de que el panel tiene el foco para recibir teclas
        snake.setFocusable(true);
        snake.requestFocusInWindow();  // Asegura que el panel recibe el foco

        // Ajustar el tamaño del frame para que el contenido se ajuste bien
        frame.pack();
    }
}
