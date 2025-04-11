package game;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/* @author Стельмашук Максим */

public class Game extends JFrame {

    private static Game gameInstance;
    private static long lastFrameTime;
    private static Image virtualboxWindowsXP;
    private static Image gta5;
    private static Image endImage; 
    private static float dropLeft = 200;
    private static float dropTop = -100;
    private static float dropVelocity = 200;
    private static int score = 0;

    public static void main(String[] args) throws IOException {
        virtualboxWindowsXP = ImageIO.read(Game.class.getResourceAsStream("virtualbox_windowsxp.png"));
        gta5 = ImageIO.read(Game.class.getResourceAsStream("gta5.png"));
        endImage = ImageIO.read(Game.class.getResourceAsStream("end.png"));

        gameInstance = new Game();
        gameInstance.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameInstance.setLocation(200, 50);
        gameInstance.setSize(1024, 768);
        gameInstance.setResizable(false);
        lastFrameTime = System.nanoTime();

        GameField gameField = new GameField();
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float dropRight = dropLeft + gta5.getWidth(null);
                float dropBottom = dropTop + gta5.getHeight(null);
                boolean isHit = x >= dropLeft && x <= dropRight && y >= dropTop && y <= dropBottom;

                if (isHit) {
                    dropTop = -100; // Сбрасываем позицию
                    dropLeft = (int) (Math.random() * (gameField.getWidth() - gta5.getWidth(null))); // Новая случайная позиция
                    dropVelocity += 10; // Увеличиваем скорость
                    score++;
                    gameInstance.setTitle("Score: " + score); // Обновляем заголовок с текущим счетом
                }
            }
        });

        gameInstance.add(gameField);
        gameInstance.setVisible(true);
    }

    private static void onRepaint(Graphics g) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;

        dropTop += dropVelocity * deltaTime; // Обновление позиции

        g.drawImage(virtualboxWindowsXP, 0, 0, null); // Рисуем фон
        g.drawImage(gta5, (int) dropLeft, (int) dropTop, null); // Рисуем падающий объект

        // Проверка выхода за пределы окна
        if (dropTop > gameInstance.getHeight()) {
            g.drawImage(endImage, 210, 150, null); // Показываем изображение конца игры
        }
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g); // Вызываем метод перерисовки
            repaint(); // Перерисовываем панель
        }
    }
}
