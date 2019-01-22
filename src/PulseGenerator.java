import java.awt.event.ActionListener;

public class PulseGenerator implements IPulseSource, Runnable {
        byte mode;
        boolean isUp;
        int delay;
        int burstValue;
        SysTick= systick;
        public PulseGenerator(SysTick systick)
        {
            this.systick=systick;
        }
    @Override
    public void run(){
        trigger();
    }
        
    @Override
    public void addActionListener(ActionListener pl) {
        
    }

    @Override
    public void removeActionListener(ActionListener pl) {

    }

    @Override
    public void trigger() {
        switch(mode)
        {
            case 1:{
                systick.tick();
                wait(ms);
                break;}
            case 2:{
                for(int i=;i<burstValue;i++)
                    systick.tick();
                wait(ms);
                break;}
        }
    }

    @Override
    public void setMode(byte mode) {
        this.mode=mode;
    }

    @Override
    public byte getMode() {
        return mode;
    }

    @Override
    public void halt() {
        isUp=false;
    }

    @Override
    public void setPulseDelay(int ms) {
        delay=ms;
    }

    @Override
    public int getPulseDelay() {
        return delay;
    }

    @Override
    public void setPulseCount(int burst) {
        burstValue=burst;
    }
}
