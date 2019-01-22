import java.util.Observable;

public class SysTick extends Observable {
    private int rvr, cvr;
    private boolean enableFlag, tickintFlag, countFlag, interrupt;

    public SysTick(Facade facade) {
        //cvr = 10;
        //rvr = 5;
        tickintFlag = false;
        enableFlag = false;
        countFlag = false;
        interrupt = false;
        this.addObserver(facade);
    }

    public void setRvr(int rvr) {
        if (rvr >= 0 && rvr < (1 << 24))
            this.rvr = rvr;

    }

    public int getRvr() {
        return this.rvr;
    }

    public void setCvr(int cvr) {
        countFlag = false;
        this.cvr = cvr;

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

    public boolean isCountFlag() {
        return countFlag;
    }

    public boolean checkInterrupt() {
        if ((countFlag == true) && (tickintFlag == true)) {
            countFlag = false;//count flag reset
            return true;
        } else return false;
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
            cvr = rvr;
            countFlag = true;
            interrupt = checkInterrupt();


            return;
        }
        cvr--;

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
