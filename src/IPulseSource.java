import java.awt.event.ActionListener;

public interface IPulseSource {
    final static byte BURST_MODE = 0;
    final static byte CONTINOUS_MODE = 1;

    void addActionListener(ActionListener pl);

    void removeActionListener(ActionListener pl);

    void trigger();

    void setMode(byte mode);

    byte getMode();

    void halt();

    void setPulseDelay(int ms);

    int getPulseDelay();

    void setPulseCount(int burst);
}
