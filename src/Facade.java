import java.util.Observable;
import java.util.Observer;

public class Facade implements Observer, Runnable {
    public SysTick systick;
    public GuiSysTick gui;
    public IPulseSource generator;
    public PulseGenerator generator1;
    
    public Thread generatorThread;
    public Facade() {
        systick = new SysTick(this);
        generator = new PulseGenerator(this);
        gui = new GuiSysTick(this);
        generator1=new PulseGenerator(this);
        
        updateVariables();
        gui.refreshGui();
        generatorThread=new Thread(generator1);
        generator.trigger();
        generatorThread.run();
        
    }
    public void turnGeneratorOn()
    {
        
    }
    @Override
    public void update(Observable observable, Object o) {
        refreshGui();
    }

    public void refreshGui()
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
