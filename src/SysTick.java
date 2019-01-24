import java.util.Observable;

public class SysTick extends Observable {
    private int rvr, cvr;
    private boolean enableFlag, tickintFlag, countFlag, interrupt;

    public SysTick(Facade facade) {

        tickintFlag = false;
        enableFlag = false;
        countFlag = false;
        interrupt = false;
        this.addObserver(facade);
    }

    public void setRvr(int rvrSet) {
        if (rvr >= 0 && rvr < (1 << 24))
            rvr = rvrSet;

    }

    public int getRvr() {
        return this.rvr;
    }

    public void setCvr(int cvrSet) {
        countFlag = false;
        cvr = cvrSet;

    }

    public int getCvr() {
        return this.cvr;
    }

    public void setTickint(boolean tickint) {
        tickintFlag = tickint;

    }

    public boolean isTickintFlag() {
        return tickintFlag;
    }

    public void setEnableFlag(boolean enable) {
        enableFlag = enable;

    }

    public boolean isEnableFlag() {

        return enableFlag;
    }

public void setCountFlag() {
        countFlag = false;
}


    public boolean isCountFlag() {
        return countFlag;
    }

    public boolean checkInterrupt() {
        return cvr == 0 && (tickintFlag);
    }

    public String toString() {
        return ("\nenableFlag = " + enableFlag +
                " countFlag = " + countFlag +
                " tickintFlag = " + tickintFlag +
                "\nCVR = " + cvr +
                "\nRVR = " + rvr);

    }

    private void doATick() {

        if (!enableFlag) return;
        if (cvr == 0) {

            interrupt = checkInterrupt();
            cvr = rvr;
           // countFlag = false;
            return;
        }
        cvr--;
     //   countFlag = false;
        if (cvr == 0) countFlag = true;
        interrupt = checkInterrupt();

    }

    public void tick() {
        doATick();
        setChanged();
        notifyObservers();
    }


    public boolean getInterrupt() {
        return interrupt;
    }

    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }
}
