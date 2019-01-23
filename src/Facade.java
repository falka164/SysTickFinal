import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;

import static java.lang.Integer.parseInt;

public class Facade implements Observer, Runnable {
    public SysTick systick;
    public GuiSysTick gui;
    public PulseGenerator generator;
    public int ticksDone;
    public int interruptDone;
    public Thread generatorThread;
    private Timer timer;
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
        timer=new Timer();
        timer.schedule(new GraphTimer(this),1000,100);
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
            if (systick.isEnableFlag() && systick.getRvr()>0) {
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

    public void reset() {
        systick.setCvr(0);
        systick.setRvr(0);
        systick.setInterrupt(false);
        systick.setEnableFlag(false);
        systick.setTickint(false);
        gui.enableInit.setSelected(false);
        gui.tickintInit.setSelected(false);
        gui.ticksField.setValue(0);
        gui.interruptField.setValue(0);
        ticksDone=0;
        interruptDone=0;
        gui.graph.scores=new ArrayList<>();

   //     gui.offGenBtn.setSelected(true);
     //   gui.generatorLed.setForeground(Color.gray);

        refreshGui();

    }

    public void resetGenerator() {

        gui.modeStateField.setValue("continuous");
        generator.setMode((byte)1);
        gui.generatorModeBtn.setText("Set to burst");
        generator.setPulseDelay(1000);
        generator.setPulseCount(0);
        gui.delayField.setValue(1000);
        gui.burstField.setValue(0);
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
        gui.generatorModeBtn.setVisible(true);
        gui.resetGeneratorBtn.setVisible(true);
        gui.generatorLed.setForeground(Color.green);
        gui.delayField.setEditable(true);
        gui.burstField.setEditable(true);

    }

    public void disableGenerator() {
        generator.halt();
        gui.generatorModeBtn.setVisible(false);
        gui.resetGeneratorBtn.setVisible(false);
        gui.generatorLed.setForeground(Color.gray);
        gui.delayField.setEditable(false);
        gui.burstField.setEditable(false);
        refreshGui();
    }

    public void setDelay(int ms) {
        generator.setPulseDelay(ms);

    }

    public void changeGenMode() {
        if (generator.mode == (byte) 1) {
            gui.generatorModeBtn.setText("Set to continuous");
            generator.mode = (byte) 0;

            gui.modeStateField.setValue("burst");

        } else {
            gui.generatorModeBtn.setText("Set to burst ");
            generator.mode = 1;

            gui.modeStateField.setValue("continuous");
        }

    }
    public void updateGraph(){
        gui.graph.scores.add((double)ticksDone);
        if(gui.graph.scores.size()>100)
        gui.graph.scores.remove(0);
        gui.graph.repaint();
    }
}
