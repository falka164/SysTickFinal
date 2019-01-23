import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PulseGenerator extends Thread implements IPulseSource {
    byte mode;

    int delay;
    boolean isUp;
    int burstValue;
    SysTick systick;
    Facade facade;

    public PulseGenerator(Facade facade) {
        this.systick = facade.systick;
        this.facade = facade;
        this.delay = 1000;
        this.burstValue = 100;
        mode = CONTINOUS_MODE;
        this.isUp = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (isUp) {
             //   System.out.println("Generator up " + isUp);
                this.tick();
            }
        }

    }

    @Override
    public void addActionListener(ActionListener pl) {

    }

    @Override
    public void removeActionListener(ActionListener pl) {

    }

    @Override
    public void trigger() {
        isUp = true;
    }

    public void tick() {

        switch (mode) {
            case CONTINOUS_MODE: {
                this.facade.makeTick(1);
                try {
                    sleep(delay);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PulseGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }


                break;
            }
            case BURST_MODE: {
                for (int i = 0; i < burstValue; i++)
                    this.facade.makeTick(1);
                try {
                    sleep(delay);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PulseGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }


                break;
            }
        }


    }


    @Override
    public void setMode(byte mode) {
        this.mode = mode;
    }

    @Override
    public byte getMode() {
        return mode;
    }

    @Override
    public void halt() {
        isUp = false;
    }

    @Override
    public void setPulseDelay(int ms) {
        System.out.println("Generator delay set to" + ms);
        delay = ms;
    }

    @Override
    public int getPulseDelay() {
        return delay;
    }

    @Override
    public void setPulseCount(int burst) {
        burstValue = burst;
    }
}
