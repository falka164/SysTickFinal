import java.util.Observable;
import java.util.Observer;

public class Facade implements Observer, Runnable {
    public SysTick systick;
    public GuiSysTick gui;
    public IPulseSource generator;

    public Facade() {
        systick = new SysTick(this);
        generator = new PulseGenerator();
        gui = new GuiSysTick(this);

        updateVariables();
        gui.refreshGui();

    }

    @Override
    public void update(Observable observable, Object o) {
        refreshGui();
    }

    private void refreshGui()
    {
        updateVariables();
        gui.refreshGui();
    }

    public void makeTick(int x) {
        for (int i = 0; i <x; i++) {
            systick.tick();
        }
    }

    public static void main(String[] args) {
        Facade facade = new Facade();
    }

    @Override
    public void run(){}

    public void updateVariables()
    {
        gui.rvrStateField.setValue(systick.getRvr());
        gui.cvrStateField.setValue(systick.getCvr());
        gui.countflagStateField.setValue(systick.isCountFlag());
        gui.enableflagStateField.setValue(systick.isEnableFlag());
        gui.interruptflagStateField.setValue(systick.getInterrupt());
    }

    public void resetSysTick()
    {
        systick.setCvr(10);
        systick.setRvr(10);
        systick.setInterrupt(false);
        refreshGui();

    }

    public void setTickInt() {

        systick.setTickint(gui.tickintInit.isSelected());
        refreshGui();
    }
}
