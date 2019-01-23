import java.util.TimerTask;

public class GraphTimer extends TimerTask {
    Facade facade;
    public GraphTimer(Facade facade) {
        this.facade=facade;
    }

    @Override
    public void run() {
        facade.updateGraph();

    }
}
