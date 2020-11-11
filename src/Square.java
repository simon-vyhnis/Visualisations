import java.awt.*;
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
    private int reproductionNumber;
    private int illTicks;
    private int immuneTicks;

    public Square(boolean infected){
        if(infected){
            status=INFECTED;
        }
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
                break;
            case INFECTED:
                g.setColor(Color.RED);
                break;
            case DEATH:
                g.setColor(Color.BLACK);
                break;
            case IMMUNE:
                g.setColor(Color.ORANGE);
                break;
        }
        g.fillRect((int)x,(int)y,Values.SQUARE_SIZE,Values.SQUARE_SIZE);
    }
    public void update(){
        if(status!=DEATH) {
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

            if(status==INFECTED){
                illTicks++;
                if(illTicks>=Values.CURE_DAYS*Values.TICKS_PER_SECOND){
                    if(random.nextInt(20)==1){
                        kill();
                    }else{
                        cure();
                    }
                }
            }else if(status==IMMUNE){
                immuneTicks++;
                if(immuneTicks>=Values.IMMUNE_DAYS*Values.TICKS_PER_SECOND){
                    status=HEALTHY;
                    immuneTicks=0;
                }
            }
        }
    }


    public void checkForContact(List<Square> healthySquares){
        for (Square square:healthySquares) {
            if(square.getX()>x-Values.SQUARE_SIZE &&
                    square.getX()<x+Values.SQUARE_SIZE &&
                    square.getY()>y-Values.SQUARE_SIZE &&
                    square.getY()<y+Values.SQUARE_SIZE ){
                square.infect();
                reproductionNumber++;
            }

        }
    }
    public void infect(){
        if(status==HEALTHY){
            status=INFECTED;
        }
    }
    public void cure(){
        illTicks=0;
        reproductionNumber=0;
        status=IMMUNE;
    }
    public void kill(){
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
