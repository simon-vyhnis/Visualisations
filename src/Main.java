import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Main {
    private static Canvas canvas;
    private static Spreading spreading;
    private static Cycle cycle;

    public static void main(String[] args){
        JFrame frame = new JFrame(Values.TITLE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0,0, screenSize.width, screenSize.height);
        System.out.println(frame.getBounds());
        canvas = new Canvas();
        frame.add(canvas);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        cycle=new Cycle();
        Thread cycleThread = new Thread(cycle);
        cycleThread.start();
        spreading = new Spreading();

    }

    public static void update(int tick){
        spreading.update(tick);
    }

    public static void draw(){
        BufferStrategy strategy = canvas.getBufferStrategy();
        if(strategy==null){
            canvas.createBufferStrategy(2);
            return;
        }
        Graphics g = strategy.getDrawGraphics();
        canvas.paint(g);
        spreading.draw(g);
        g.dispose();
        strategy.show();
    }

    public static void secondUpdate(){
        spreading.secondUpdate();
    }
    public static void stop(){
        cycle.stop();
    }
}
