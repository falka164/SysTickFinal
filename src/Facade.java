import java.util.Observable;
import java.util.Observer;

public class Facade implements Observer {
    public SysTick systick;
    public GuiSysTick gui;
    public IPulseSource generator;

    public Facade() {
        systick = new SysTick();
        generator = new PulseGenerator();
        gui = new GuiSysTick();

    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
