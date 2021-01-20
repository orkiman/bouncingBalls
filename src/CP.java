import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CP extends JPanel {

    Ball[] balls;
    double[] sin = new double[360];
    double[] cos = new double[360];
    final int NUMBER_OF_BALLS = 30;
    private JPanel tt;

    public CP() {
        System.out.println(Math.toDegrees(Math.atan2(1, 1)));
        System.out.println(Math.toDegrees(Math.atan2(-1, -1)));
        initBalls();
        runAnimation();
//        fillCoSinArrays();
//        startTimer();


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("yy");
                moveBalls();
            }
        });
    }

    private void fillCoSinArrays() {
        for (int i = 0; i < 360; i++) {
            sin[i] = Math.sin(Math.toRadians(i));
            cos[i] = Math.cos(Math.toRadians(i));
        }
    }

    private void initBalls() {
        balls = new Ball[NUMBER_OF_BALLS];

        for (int i = 0; i < NUMBER_OF_BALLS; i++) {
            Ball newBall;
            boolean ballsCollide;
            do {
                ballsCollide = false;
                newBall = getNewBall();
                for (int j = 0; j < i; j++) {
                    if (doesBallsCollide(newBall, balls[j])) {
                        ballsCollide = true;
                        break;
                    }
                }
            } while (ballsCollide);
            balls[i] = newBall;
        }
    }

    private Ball getNewBall() {
        Random rand = new Random();
        float radius = rand.nextFloat() * 90+10;
        return new Ball(
                rand.nextFloat() * 1500 + radius,
                rand.nextFloat() * 800 + radius,
                radius,
                rand.nextFloat() * 2,
                rand.nextFloat() * 2,
                new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
    }

    private boolean doesBallsCollide(Ball ball1, Ball ball2) {
        double minimumDistanceBetweenBalls = ball1.radius + ball2.radius;
        double distanceBetweenBalls = Math.sqrt((ball1.x - ball2.x) * (ball1.x - ball2.x) + (ball1.y - ball2.y) * (ball1.y - ball2.y));
        return distanceBetweenBalls <= minimumDistanceBetweenBalls;
    }

    private void moveBalls() {

        for (int i = 0; i < NUMBER_OF_BALLS; i++) {
            Ball ball = balls[i];
            moveBall(ball);
            for (int j = i + 1; j < NUMBER_OF_BALLS; j++) {
                Ball otherBall = balls[j];
                if (doesBallsCollide(ball, otherBall)) {
                    handleCollisionWiki2(ball, otherBall);
                } else {
                    ball.inContact = otherBall.inContact = false;
                }
            }
        }
        repaint();
    }

    private void moveBall(Ball ball) {
        ball.x += ball.xSpeed;
        ball.y += ball.ySpeed;
        if (ball.x - ball.radius < 0 || ball.x + ball.radius > getWidth())
            ball.xSpeed *= -1;
        if (ball.y - ball.radius < 0 || ball.y + ball.radius > getHeight())
            ball.ySpeed *= -1;
    }

    private void handleCollisionWiki2(Ball ball, Ball otherBall) {
//        thank to wikipedia :
        double v1 = Math.sqrt(ball.xSpeed * ball.xSpeed + ball.ySpeed * ball.ySpeed);
        double v2 = Math.sqrt(otherBall.xSpeed * otherBall.xSpeed + otherBall.ySpeed * otherBall.ySpeed);
        double m1 = ball.radius;
        double m2 = otherBall.radius;
        double theta1 = Math.atan2(ball.ySpeed, ball.xSpeed);//θ...this function takes y then x...
        double theta2 = Math.atan2(otherBall.ySpeed, otherBall.xSpeed);
        double phi = Math.atan2(otherBall.y - ball.y, otherBall.x - ball.x);
        ball.xSpeed = (v1 * Math.cos(theta1 - phi) * (m1 - m2) + 2 * m2 * v2 * Math.cos(theta2 - phi)) * Math.cos(phi) / (m1 + m2) +
                v1 * Math.sin(theta1 - phi) * Math.cos(phi + Math.PI / 2);
        ball.ySpeed = (v1 * Math.cos(theta1 - phi) * (m1 - m2) + 2 * m2 * v2 * Math.cos(theta2 - phi)) * Math.sin(phi) / (m1 + m2) +
                v1 * Math.sin(theta1 - phi) * Math.sin(phi + Math.PI / 2);
        otherBall.xSpeed = (v2 * Math.cos(theta2 - phi) * (m2 - m1) + 2 * m1 * v1 * Math.cos(theta1 - phi)) * Math.cos(phi) / (m2 + m1) +
                v2 * Math.sin(theta2 - phi) * Math.cos(phi + Math.PI / 2);
        otherBall.ySpeed = (v2 * Math.cos(theta2 - phi) * (m2 - m1) + 2 * m1 * v1 * Math.cos(theta1 - phi)) * Math.sin(phi) / (m2 + m1) +
                v2 * Math.sin(theta2 - phi) * Math.sin(phi + Math.PI / 2);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        paintBalls(balls, g2d);
    }

    private void paintBalls(Ball[] balls, Graphics2D g2d) {
        for (int i = 0; i < NUMBER_OF_BALLS; i++) {
            Ball ball = balls[i];
            g2d.setColor(ball.color);
            g2d.fillOval((int) (ball.x - ball.radius), (int) (ball.y - ball.radius), (int) ball.radius * 2, (int) ball.radius * 2);
            g2d.setColor(Color.black);
            g2d.drawOval((int) (ball.x - ball.radius), (int) (ball.y - ball.radius), (int) ball.radius * 2, (int) ball.radius * 2);
            g2d.drawString(String.valueOf(i), (int) (ball.x), (int) (ball.y));
        }
    }

    private void runAnimation() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    moveBalls();
                    try {
                        Thread.sleep(7);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    private void startTimer() {
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                moveBalls();
            }
        };
        t.scheduleAtFixedRate(tt, 1000, 5);
    }

    class Ball {
        double x, y, radius, direction, speed, xSpeed, ySpeed;
        Color color;
        boolean inContact;

        public Ball(double x, double y, double radius, double xSpeed, double ySpeed, Color color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
            this.color = color;
        }
    }
}
