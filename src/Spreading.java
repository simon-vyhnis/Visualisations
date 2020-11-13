import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Spreading {
    private List<Square> squares;

    private int infected;
    private double reproductionNumber;
    private int day;
    private int deaths;
    private int cured;
    private int healthy;

    private Graph infectedGraph;
    private Graph curedGraph;
    private Graph deathsGraph;

    private int dailyGrowth;
    private int yesterdayCured;
    private int yesterdayDeaths;
    private int yesterdayInfected;
    private int yesterdayHealthy;

    public Spreading(){
        squares=new ArrayList<>();
        for (int i=0;  i<Values.START_HEALTHY; i++){
            squares.add(new Square(false));
        }
        for (int i=0;  i<Values.START_INFECTED; i++){
            squares.add(new Square(true));
        }
        infectedGraph=new Graph(400,200,Values.AREA_WIDTH+10, 180, "Infected in time", Color.RED);
        curedGraph=new Graph(400, 200, Values.AREA_WIDTH+10, 410, "Cured in time", Color.ORANGE);
        deathsGraph=new Graph(400, 200, Values.AREA_WIDTH+10, 640, "Deaths in time", Color.BLACK);
    }

    public void draw(Graphics g){
        g.drawRect(0,0,Values.AREA_WIDTH,Values.AREA_HEIGHT);
        g.setFont(new Font("arial",Font.PLAIN,20));
        g.drawString("STATISTICS",Values.AREA_WIDTH+10,30);
        g.setFont(new Font("arial", Font.PLAIN, 12));
        g.drawString("Day: "+day,Values.AREA_WIDTH+10,50);
        g.drawString("Infected: "+infected,Values.AREA_WIDTH+10,70);
        g.drawString("Death: "+ deaths,Values.AREA_WIDTH+10,90);
        g.drawString("Cured: "+cured,Values.AREA_WIDTH+10, 110);
        g.drawString("Reproduction number: "+reproductionNumber,Values.AREA_WIDTH+10,130);
        g.drawString("Daily growth: "+dailyGrowth,Values.AREA_WIDTH+10,150);
        infectedGraph.draw(g);
        curedGraph.draw(g);
        deathsGraph.draw(g);
        for (Square square:squares){
            square.draw(g);
        }
    }

    public void update(int tick){
        infected=0;
        cured=0;
        deaths=0;
        reproductionNumber=0;
        List<Square> healthySquares = new ArrayList<>();
        List<Square> infectedSquares = new ArrayList<>();
        for(Square square:squares){
            switch (square.getStatus()){
                case Square.HEALTHY:
                    healthySquares.add(square);
                    break;
                case Square.INFECTED:
                    infectedSquares.add(square);
                    break;
                case Square.DEATH:
                    deaths++;
                    break;
                case Square.IMMUNE:
                    cured++;
                    break;
            }
        }
        //contact detecting
        for (Square square:infectedSquares){
            square.checkForContact(healthySquares, tick);
            reproductionNumber+=square.getReproductionNumber();
        }
        infected = infectedSquares.size();
        healthy = healthySquares.size();
        reproductionNumber/=infected;

        //move update
        for (Square square:squares){
            square.update(tick);
        }
        //graphs update
        infectedGraph.update(infected);
        curedGraph.update(cured);
        deathsGraph.update(deaths);
        //end checking
        if(infected<=0) {
            Main.stop();
        }
    }

    public void secondUpdate(){
        day++;
        int deltaCured = cured-yesterdayCured;
        int deltaDeath = deaths-yesterdayDeaths;
        int deltaHealthy = healthy-yesterdayHealthy;
        int deltaInfected = infected-yesterdayInfected;
        int healthyGrowth;
        int curedGrowth;
        dailyGrowth=deltaInfected+deltaDeath;
        yesterdayInfected=infected;
        yesterdayCured=cured;
        yesterdayDeaths=deaths;
        yesterdayHealthy = healthy;
        System.out.println("Infected: "+infected+" Cured: "+cured+" Deaths: "+deaths );
    }
}
