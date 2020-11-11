import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private int width;
    private int height;
    private int x;
    private int y;

    private List<Integer> history = new ArrayList<>();
    private int max=0;
    private double pixelPerPoint;
    private double pixelPerRecord;
    private int purgeIndex=1;

    private String title;
    private Color color;

    public Graph(int width, int height, int x, int y, String title, Color color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y+10;
        this.title = title;
        this.color = color;
    }

    public void update(int points){
        history.add(points);

        pixelPerRecord = (double) width / history.size()*purgeIndex;
        while(pixelPerRecord<1){
            purgeIndex++;
            pixelPerRecord = (double) width / history.size()*purgeIndex;
        }

        if(points>max){
            max = points;
            pixelPerPoint = (height -1.0)/(double)max;
        }
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.drawString(title,x,y-10);
        g.drawRect(x,y, width, height);

        g.setColor(color);
        int pos=1;
        for (int i = 0; i< history.size(); i+=purgeIndex){
            int rectHeight = (int)(history.get(i)* pixelPerPoint);
            int rectWidth = (int)pixelPerRecord;
            g.fillRect(x+pos++,y+height-rectHeight, rectWidth, rectHeight);
        }
    }
}
