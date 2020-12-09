import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Square {
    private double x;
    private double y;
    private double xSpeed;
    private double ySpeed;
    private Random random;

    public static final int HEALTHY=0;
    public static final int INFECTED=1;
    public static final int DEATH=2;
    public static final int IMMUNE=3;

    private int status=0;
    private int statusSince=0;
    private int reproductionNumber;

    private Spreading spreading;

    public Square(boolean infected, Spreading spreading){
        if(infected){
            status=INFECTED;
        }
        this.spreading = spreading;
        random = new Random();
        x = random.nextInt(Values.AREA_WIDTH);
        y = random.nextInt(Values.AREA_HEIGHT);
        xSpeed = (random.nextInt(Values.SQUARES_SPEED*2)/10.0)-2;
        ySpeed = (random.nextInt(Values.SQUARES_SPEED*2)/10.0)-2;
    }

    public void draw(Graphics g){
        switch (status){
            case HEALTHY:
                g.setColor(Color.GREEN);
                g.fillRect((int)x,(int)y,Values.SQUARE_SIZE,Values.SQUARE_SIZE);
                //g.drawImage(spreading.getGreenPerson(),(int)x,(int)y,null);
                break;
            case INFECTED:
                g.setColor(Color.RED);
                g.fillRect((int)x,(int)y,Values.SQUARE_SIZE,Values.SQUARE_SIZE);
                //g.drawImage(spreading.getRedPerson(),(int)x,(int)y,null);
                break;
            case DEATH:
                //g.setColor(Color.BLACK);
                //g.fillRect((int)x+(Values.SQUARE_SIZE/3),(int)y,4,Values.SQUARE_SIZE);
                //g.fillRect((int)x,(int)y+Values.SQUARE_SIZE/4, Values.SQUARE_SIZE, Values.SQUARE_SIZE/3);
                g.drawImage(spreading.getGhost(),(int)x,(int)y,null);
                break;
            case IMMUNE:
                g.setColor(Color.ORANGE);
                g.fillRect((int)x,(int)y,Values.SQUARE_SIZE,Values.SQUARE_SIZE);
                //g.drawImage(spreading.getYellowPerson(),(int)x,(int)y,null);
                break;
        }
    }
    public void update(int tick){
        moveUpdate();
        if(status!=DEATH) {
           if(status==INFECTED){
                if(tick-statusSince>=Values.CURE_DAYS*Values.TICKS_PER_SECOND){
                    if(random.nextInt(20)==1){
                        kill(tick);
                    }else{
                        cure(tick);
                    }
                }
           }else if(status==IMMUNE){
                if(tick-statusSince>=Values.IMMUNE_DAYS*Values.TICKS_PER_SECOND){
                    status = HEALTHY;
                    statusSince = tick;
                }
           }
        }
    }
    private void moveUpdate(){
        x += xSpeed;
        y += ySpeed;
        if (x <= 0) {
            xSpeed = random.nextInt(20) / 10.0;
            x += xSpeed;
        } else if (x > Values.AREA_WIDTH - Values.SQUARE_SIZE) {
            xSpeed = -random.nextInt(20) / 10.0;
            x += xSpeed;
        }
        if (y <= 0) {
            ySpeed = random.nextInt(20) / 10.0;
            y += ySpeed;
        } else if (y > Values.AREA_HEIGHT - Values.SQUARE_SIZE) {
            ySpeed = -random.nextInt(20) / 10.0;
            y += ySpeed;
        }
    }

    public void checkForContact(List<Square> healthySquares, int tick){
        for (Square square:healthySquares) {
            if(square.getX()>x-Values.SQUARE_SIZE &&
                    square.getX()<x+Values.SQUARE_SIZE &&
                    square.getY()>y-Values.SQUARE_SIZE &&
                    square.getY()<y+Values.SQUARE_SIZE ){
                square.infect(tick);
                reproductionNumber++;
            }

        }
    }
    public void infect(int tick){
        statusSince = tick;
        status = INFECTED;
        spreading.increaseDailyGrowth();
    }
    public void cure(int tick){
        statusSince=tick;
        reproductionNumber=0;
        status=IMMUNE;
    }
    public void kill(int tick){
        statusSince = tick;
        status=DEATH;
    }
    public int getReproductionNumber(){
        return reproductionNumber;
    }
    public int getStatus(){
        return status;
    }
    public int getY(){
        return (int)y;
    }
    public int getX(){
        return (int)x;
    }


}
