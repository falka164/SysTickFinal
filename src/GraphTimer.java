import java.util.TimerTask;

public class GraphTimer extends TimerTask {
    Facade facade;
    public boolean isPaused;

    public GraphTimer(Facade facade) {
        isPaused = false;
        this.facade = facade;
    }

    @Override
    //co frequency odpalanie run()

    public void run() {

        if (!isPaused) {
            facade.updateGraph();
        }
    }

}
