import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Integer.parseInt;

public class Facade implements Observer, Runnable {
    public SysTick systick;
    public GuiSysTick gui;
    public PulseGenerator generator;
    public int ticksDone;
    public int interruptDone;
    public Thread generatorThread;

    public Facade() {
        systick = new SysTick(this);
        gui = new GuiSysTick(this);
        generator = new PulseGenerator(this);
        ticksDone = 0;
        interruptDone = 0;
        updateVariables();
        gui.refreshGui();
        generatorThread = new Thread(generator);
        generatorThread.start();

    }

    @Override
    public void update(Observable observable, Object o) {
        refreshGui();
    }

    public void refreshGui() {
        updateVariables();
        gui.refreshGui();
    }

    public void makeTick(int x) {
        for (int i = 0; i < x; i++) {
            if (systick.isEnableFlag()) {
                ticksDone++;
            }
            systick.tick();
        }
    }

    public void howManyInterrupt() {
       String  val = gui.interruptflagStateField.getValue().toString();
        if (val.equals("true")) {
            interruptDone++;
        }
        gui.interruptField.setValue(interruptDone);
    }

    public static void main(String[] args) {
        Facade facade = new Facade();
    }

    @Override
    public void run() {
    }

    public void updateVariables() {
        gui.rvrStateField.setValue(systick.getRvr());
        gui.cvrStateField.setValue(systick.getCvr());
        gui.countflagStateField.setValue(systick.isCountFlag());
        gui.enableflagStateField.setValue(systick.isEnableFlag());
        gui.tickintflagStateField.setValue(systick.isTickintFlag());
        gui.interruptflagStateField.setValue(systick.getInterrupt());
        gui.ticksField.setValue(ticksDone);
        gui.interruptField.setValue(interruptDone);
    }

    public void resetSysTick() {
        systick.setCvr(10);
        systick.setRvr(10);
        systick.setInterrupt(false);
        refreshGui();

    }

    public void setTickInt() {

        systick.setTickint(gui.tickintInit.isSelected());
        refreshGui();
    }

    public void setEnable() {
        systick.setEnableFlag(gui.enableInit.isSelected());
        refreshGui();
    }

    public void rvrRegset() {
        int val = parseInt(gui.rvrField.getValue().toString());

        gui.rvrStateField.setValue(val);
        systick.setRvr(val);

    }

    public void cvrRegset() {
        int val = parseInt(gui.cvrField.getValue().toString());

        gui.cvrStateField.setValue(val);
        systick.setCvr(val);
    }

    public void setRegisters() {
        rvrRegset();
        cvrRegset();
    }

    public void enableGenerator() {
        generator.trigger();
        gui.generatorLed.setForeground(Color.green);

    }

    public void disableGenerator() {
        generator.halt();
        gui.generatorLed.setForeground(Color.gray);
    }

    public void setDelay(int ms) {
        generator.setPulseDelay(ms);
    }

    public void changeGenMode() {
        if (generator.mode == (byte) 1) {
            gui.generatorModeBtn.setText("Set to Burst");
            generator.mode = (byte) 0;
        } else {
            gui.generatorModeBtn.setText("Set to Continous");
            generator.mode = 1;
        }
    }
}
