public class Cycle implements Runnable {
    private boolean isRunning;
    private Thread t;

    public Cycle() {
        isRunning=true;
    }

    @Override
    public void run() {
        //initialization
        long startTime;
        int FPS=0;
        int ticks=0;
        double nanoPerTick = 1000000000/Values.TICKS_PER_SECOND;
        System.out.println(nanoPerTick);
        double unprocessedTicks=0;
        double lastNano=System.nanoTime();
        long lastMilis=System.currentTimeMillis();
        //Main loop
        while(isRunning){
            double nowNano=System.nanoTime();
            unprocessedTicks+=(nowNano-lastNano)/nanoPerTick;
            lastNano=nowNano;
            //Ticks processing
            while(unprocessedTicks >1){
                Main.update();
                ticks++;
                unprocessedTicks--;
            }

            //Drawing
            Main.draw();
            FPS++;
            //Second processing
            if(System.currentTimeMillis()-lastMilis>1000){
                System.out.println("FPS: "+FPS+" Ticks: "+ticks);
                FPS=0;
                ticks=0;
                lastMilis+=1000;
                Main.secondUpdate();
            }
        }

    }
    public void start(){
        isRunning=true;
        if(t==null){
            t=new Thread(this, "CycleThread");
            t.start();
        }

    }

    public void stop(){
        isRunning=false;
    }
}
