import java.awt.*;

//Generates the grid of bricks for the current round.
public class MapGenerator
{
    public boolean[][] map;
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int column)
    {
        map = new boolean[row][column];
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < column; j++)
            {
                map[i][j] = true;
            }
        }
        brickWidth = 540/column;
        brickHeight = 150/row;
    }

    //Sets visibility of the brick on the window
    public void setBrick(boolean value, int r, int c)
    {
        map[r][c] = value;
    }

    //Draws the grid of bricks for the current round except for the bricks that were cleared
    public void draw(Graphics2D g)
    {
        for (int i = map.length - 1; i >= 0; i--)
        {
            for (int j = map[0].length - 1; j >= 0; j--)
            {
                if (map[i][j]) //If the brick is not cleared, display it.
                {
                    if (i >= map.length - 2)//The bottom 2 blocks are yellow.
                    {
                        g.setColor(Color.YELLOW);
                    }
                    else if (i >= map.length - 4)//The blocks on top of the yellow blocks are green
                    {
                        g.setColor(Color.GREEN);
                    }
                    else if (i >= map.length - 6)//The blocks on top of the green blocks are orange
                    {
                        g.setColor(Color.ORANGE);
                    }
                    else//The blocks on top of the orange blocks are red
                    {
                        g.setColor(Color.RED);
                    }


                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setColor(Color.BLACK);
                    g.setStroke(new BasicStroke(3));
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }
}
