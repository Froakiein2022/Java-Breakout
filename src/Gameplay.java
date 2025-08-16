import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Gameplay extends JPanel implements KeyListener,ActionListener
{
    private int ballX = 120, ballY = 350; // Ball's position
    private int ballDX = 2, ballDY = 2; // Ball's speed
    private int paddleX = 350; //Paddle's x position
    private boolean gameStarted = false; //Boolean for if the game has started
    private MapGenerator map; //Map of bricks in each round.
    private int score = 0; //Score for the game
    private int round = 1; //Current game round
    private int lives = 5; //Number of lives left
    private int bricks; //Number of bricks left in each round

    public Gameplay()
    {
        setBackground(Color.BLACK);//Sets the background to black
        Timer timer = new Timer(5, this);//Sets timer required for animation in the game
        timer.start();//Starts animation
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!gameStarted)
        {
            if (lives > 0 && round <= 1)//Start screen:
            {
                g.setColor(Color.GREEN);
                g.setFont(new Font("serif", Font.BOLD, 50));
                g.drawString("Java Breakout", 175, 300);
                g.setFont(new Font("serif", Font.BOLD, 25));
                g.drawString("Press Enter To Play", 240, 350);
            }
            else//End screen
            {
                g.setColor(Color.GREEN);
                g.setFont(new Font("serif", Font.BOLD, 50));
                if (lives == 0)
                {
                    g.drawString("Game Over!", 210, 300);
                }
                else
                {
                    g.drawString("Congratulations!", 155, 300);
                }
                g.setFont(new Font("serif", Font.BOLD, 25));
                g.drawString("Press Enter To Restart", 225, 350);
                g.setFont(new Font("serif",Font.BOLD , 20));
                g.drawString("Lives: " + lives, 0, 30);
                g.drawString("Round: " + round, 300, 30);
                g.drawString("Score: " + score, 550, 30);
            }
        }
        else//Main game: Draw the ball, paddle, and bricks
        {
            //Ball:
            g.setColor(Color.WHITE);
            g.fillOval(ballX, ballY, 30, 30);

            //Paddle:
            g.setColor(Color.BLUE);
            g.fillRect(paddleX, 550, 100, 8);

            //Bricks:
            map.draw((Graphics2D) g);

            //Layout of the game:
            g.setColor(Color.GREEN);
            g.setFont(new Font("serif",Font.BOLD , 20));
            g.drawString("Lives: " + lives, 0, 30);
            g.drawString("Round: " + round, 300, 30);
            g.drawString("Score: " + score, 550, 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameStarted) {
            Rectangle ballRect = new Rectangle(ballX, ballY, 30, 30);
            Rectangle paddleRect = new Rectangle(paddleX, 550, 100, 8);

            if (ballX <= 0 || ballX >= 670)//Keeps the ball within boundaries of the window
            {
                ballDX = -ballDX;
            }
            //Bounces the ball if it either reaches the top boundary of the window or hits the paddle
            if (ballY <= 0 || ballRect.intersects(paddleRect))
            {
                ballDY = -ballDY;
                if (ballRect.intersects(paddleRect))
                {
                    if (ballX < paddleX + 25)
                    {
                        ballDX = -2;
                    }
                    else if (ballX < paddleX + 48)
                    {
                        ballDX = -1;
                    }
                    else if (ballX < paddleX + 50)
                    {
                        ballDX = 0;
                    }
                    else if (ballX < paddleX + 75)
                    {
                        ballDX = 1;
                    }
                    else
                    {
                        ballDX = 2;
                    }
                }
            }
            //If the ball hits the bottom of the screen, lose a life and reset the ball and paddle
            if (ballY + ballDY > getHeight() - 30)
            {

                lives--;
                if (lives > 0)
                {
                    resetPaddleAndBall();
                }
                else
                {
                    gameStarted = false;
                }
            }

            A:for (int i = 0; i < map.map.length; i++)
            {
                for (int j = 0; j < map.map[0].length; j++)
                {
                    int brickWidth = map.brickWidth;
                    int brickHeight = map.brickHeight;
                    int brickX = j*brickWidth+80;
                    int brickY = i*brickHeight+50;
                    Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

                    //If a brick is hit by the ball, remove the brick:
                    if (ballRect.intersects(brickRect) && map.map[i][j])
                    {
                        map.setBrick(false, i, j);
                        bricks--;
                        score++;

                        if (bricks > 0)//If not all bricks are cleared yet, rebound the ball
                        {
                            if ((ballX + 22 <= brickX && ballDX > 0) || (ballX + 7 > brickX + brickWidth && ballDX < 0))
                            {
                                ballDX = -ballDX;
                            }
                            else
                            {
                                ballDY = -ballDY;
                            }
                        }
                        else//If all bricks in the current round are cleared:
                        {
                            if (round + 1 <= 5)//If less than 5 rounds are cleared, start the next round
                            {
                                round++;
                                newRound();
                            }
                            else//Else, end the game.
                            {
                                gameStarted = false;
                            }
                        }
                        break A;
                    }
                }
            }
            //Moves the ball.
            ballX += ballDX;
            ballY += ballDY;
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Moves the paddle when one of the arrow keys is pressed during the game:
        if(e.getKeyCode() == KeyEvent.VK_LEFT && gameStarted)
        {
            paddleX = Math.max(paddleX - 20, 0);
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && gameStarted)
        {
            paddleX = Math.min(paddleX + 20, 600);
        }
        //Starts the game if the Enter key is pressed.
        else if(e.getKeyCode() == KeyEvent.VK_ENTER && !gameStarted)
        {
            gameStarted = true;

            score = 0;
            round = 1;
            lives = 5;
            newRound();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    private void newRound()//Starts a new round of the game given the current round number
    {
        resetPaddleAndBall();

        map = new MapGenerator(round + 3,7);
        bricks = (round + 3) * 7;
    }
    private void resetPaddleAndBall()//Resets the ball and paddle.
    {
        ballX = 120;
        ballY = 350;
        ballDX = 2;
        ballDY = 2;
        paddleX = 350;
    }
}
