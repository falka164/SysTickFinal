import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

import static java.lang.Integer.parseInt;

public class GuiSysTick {

    private Facade facade;
    private JFrame frame;

    private JMenuBar menuBar;
    private JMenu fileMenu, viewMenu, helpMenu;
    private JMenuItem exitItem, aboutItem;

    private JRadioButton onGenBtn, offGenBtn;
    private ButtonGroup radioBtnGroup;
    private JButton generatorModeBtn, oneStepBtn, manyStepBtn, resetSystickBtn, setRegistersBtn;
    public JCheckBox enableInit, tickintInit;

    private JPanel mainPane, northPane, southPane, eastPane, westPane, centerPane, infoPane,
            flagsPane, registerPane, allDataStates, onOffGenPanel, buttonPanel;

    private JLabel ticksLabel, interruptLabel, rvrLabel, cvrLabel, rvrStateLabel,
            cvrStateLabel, enableflagStateLabel, countflagStateLabel, tickintFlagStateLabel,
            interruptflagStateLabel;
    public JFormattedTextField ticksField, interruptField, rvrField, cvrField,
            rvrStateField, cvrStateField, enableflagStateField, countflagStateField,
            tickintflagStateField, interruptflagStateField, delayField, burstField, manyStepField;
    private static String ticksString, interruptString, rvrString, cvrString;
    private int howTicks, howInterrupt, rvrValue, cvrValue;

    private Border borderLine, spaceBorder;


    public GuiSysTick(Facade facade) {
        this.facade=facade;
        frame = new JFrame("Systick Simulator ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        borderManager();
        createMenuBar();
        createMainPane();
        createWestPanel();
        makeListeners();
        refreshGui();

    }

    public void refreshGui() {
        frame.repaint();
        frame.revalidate();
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");
        exitItem = new JMenuItem("Exit");
        aboutItem = new JMenuItem("About");

        exitItem.setToolTipText("exit aplication");
        exitItem.addActionListener((event) -> System.exit(0));

        aboutItem.setToolTipText("Short note about Systick");
        aboutItem.addActionListener((event) -> {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Systick jest to 24-bitowy licznik, " +
                    "zliczający w dół od zadanej wartości do 0.\n" +
                    "Działa w dwóch trybach. Domyślny tryb, tzw. polling mode, gdzie...  ", "SysTick", JOptionPane.INFORMATION_MESSAGE);

        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        helpMenu.add(aboutItem);
        this.frame.setJMenuBar(menuBar);
    }

    private void createMainPane() {
        mainPane = new JPanel(new BorderLayout());
        northPane = new JPanel(new GridLayout(1, 2));
        westPane = new JPanel();
        southPane = new JPanel();
        eastPane = new JPanel();
        centerPane = new JPanel();

        mainPane.add(northPane, BorderLayout.NORTH);
        mainPane.add(southPane, BorderLayout.SOUTH);
        mainPane.add(westPane, BorderLayout.WEST);
        mainPane.add(eastPane, BorderLayout.EAST);
        mainPane.add(centerPane, BorderLayout.CENTER);
        this.frame.add(mainPane);
        createNorthPanel();
        createEastPanel();
        createSouthPanel();
    }

    private void borderManager() {
        borderLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        spaceBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);

    }

    private void createNorthPanel() {
        northPane.setBorder(spaceBorder);
        createInfoPanel();
        createFlagsPanel();
        createRegisterPanel();

    }

    private void createInfoPanel() {
        infoPane = new JPanel(new GridLayout(2, 2));

        ticksString = "Ilość ticków: ";
        interruptString = "Ilość przerwań: ";
        howTicks = 0;
        howInterrupt = 5;

        ticksLabel = new JLabel(ticksString);
        interruptLabel = new JLabel(interruptString);

        ticksField = createTextFiled("ilosc impulsow", Color.green, false,10);
        interruptField = createTextFiled("ilosc przerwan", Color.green, false,10);

        ticksLabel.setLabelFor(ticksField);
        interruptLabel.setLabelFor(interruptField);

        infoPane.setBorder(BorderFactory.createTitledBorder(" Actual Stats "));

        infoPane.add(ticksLabel);
        infoPane.add(ticksField);
        infoPane.add(interruptLabel);
        infoPane.add(interruptField);

        northPane.add(infoPane);
    }

    private void createFlagsPanel() {
        flagsPane = new JPanel();
        flagsPane.setLayout(new BoxLayout(flagsPane, BoxLayout.Y_AXIS));


        enableInit = new JCheckBox("ENABLE");
        tickintInit = new JCheckBox("TICKINT");

        enableInit.setAlignmentX(Component.CENTER_ALIGNMENT);
        tickintInit.setAlignmentX(Component.CENTER_ALIGNMENT);

        flagsPane.setBorder(BorderFactory.createTitledBorder("On/Off CSR Flags"));

        flagsPane.add(enableInit);
        flagsPane.add(tickintInit);

        northPane.add(flagsPane);

    }

    public void createRegisterPanel() {
        registerPane = new JPanel(new GridLayout(3, 2));

        rvrString = " RVR: ";
        cvrString = " CVR: ";
        rvrValue = 5;
        cvrValue = 6;

        rvrLabel = new JLabel(rvrString);   //Create the labels.
        cvrLabel = new JLabel(cvrString);
        setRegistersBtn = new JButton("Set");

        rvrField = createTextFiled("", Color.blue, true,10);
        cvrField = createTextFiled("", Color.blue, true,10);

        rvrLabel.setLabelFor(rvrField);
        cvrLabel.setLabelFor(cvrField);

        registerPane.setBorder(BorderFactory.createTitledBorder(" Set RVR & CVR value "));

        registerPane.add(rvrLabel);
        registerPane.add(rvrField);
        registerPane.add(cvrLabel);
        registerPane.add(cvrField);
        registerPane.add(setRegistersBtn);

        northPane.add(registerPane);
    }

    private void createEastPanel() {
        allDataStates = new JPanel(new GridLayout(2, 2));

        rvrStateLabel = new JLabel(rvrString);
        cvrStateLabel = new JLabel(cvrString);
        countflagStateLabel = new JLabel(" COUNTFLAG: ");
        enableflagStateLabel = new JLabel(" ENABLE: ");
        tickintFlagStateLabel = new JLabel(" TICKINT: ");
        interruptflagStateLabel = new JLabel(" INTERRUPT: ");

        rvrStateField = createTextFiled("", Color.orange, false,6);
        cvrStateField = createTextFiled("", Color.orange, false,6);
        countflagStateField = createTextFiled("", Color.red, false,6);
        enableflagStateField = createTextFiled("", Color.blue, false,6);
        tickintflagStateField = createTextFiled("", Color.blue, false,6);
        interruptflagStateField = createTextFiled("", Color.blue, false, 6);

        rvrStateLabel.setLabelFor(rvrStateField);
        cvrStateLabel.setLabelFor(cvrStateField);
        countflagStateLabel.setLabelFor(countflagStateField);
        enableflagStateLabel.setLabelFor(enableflagStateField);
        tickintFlagStateLabel.setLabelFor(tickintflagStateField);
        interruptflagStateLabel.setLabelFor(interruptflagStateField);

        allDataStates.setBorder(BorderFactory.createTitledBorder("SysTick State Info"));
        allDataStates.setLayout(new BoxLayout(allDataStates, BoxLayout.Y_AXIS));

        allDataStates.add(rvrStateLabel);
        allDataStates.add(rvrStateField);
        allDataStates.add(cvrStateLabel);
        allDataStates.add(cvrStateField);
        allDataStates.add(countflagStateLabel);
        allDataStates.add(countflagStateField);
        allDataStates.add(enableflagStateLabel);
        allDataStates.add(enableflagStateField);
        allDataStates.add(interruptflagStateLabel);
        allDataStates.add(interruptflagStateField);

        eastPane.add(allDataStates);

    }


    private void createSouthPanel() {
        southPane.setBorder(BorderFactory.createTitledBorder(" Generator "));

        generatorOnOffPanel();

    }

    private void generatorOnOffPanel() {
        onOffGenPanel = new JPanel();
        onOffGenPanel.setLayout(new BoxLayout(onOffGenPanel, BoxLayout.Y_AXIS));
        radioBtnGroup = new ButtonGroup();

        onGenBtn = new JRadioButton(" ON ");
        offGenBtn = new JRadioButton("OFF");

        radioBtnGroup.add(onGenBtn);
        radioBtnGroup.add(offGenBtn);
        offGenBtn.setSelected(true);
        onOffGenPanel.add(onGenBtn);
        onOffGenPanel.add(offGenBtn);


        southPane.setLayout(new FlowLayout());
        southPane.add(onOffGenPanel);

        JLabel generatorLed = new JLabel("•");
        generatorLed.setForeground(Color.red);
        generatorLed.setFont(new Font(Font.SERIF, Font.PLAIN, 100));

        southPane.add(generatorLed);
        generatorModeBtn = new JButton("Set to Burst");
        generatorModeBtn.setSize(new Dimension(100, 100));

        delayField = createTextFiled("", Color.green, true, 10);

        JLabel delayInfo = new JLabel("Delay [ms]");
        JLabel burstInfo = new JLabel("Burst value");

        burstField = createTextFiled("", Color.green, true, 10);


        southPane.add(generatorModeBtn);
        southPane.add(delayInfo);

        southPane.add(delayField);
        southPane.add(burstInfo);
        southPane.add(burstField);

    }

    private void createWestPanel() {

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        oneStepBtn = new JButton("ONE IMPULS");
        manyStepBtn = new JButton("MANY IMPULS");
        resetSystickBtn = new JButton("RESET SYSTICK");

        manyStepField = createTextFiled("", Color.green, true, 2);

        buttonPanel.add(oneStepBtn);
        buttonPanel.add(manyStepBtn);
        buttonPanel.add(manyStepField);
        buttonPanel.add(resetSystickBtn);

        westPane.add(buttonPanel, BorderLayout.WEST);

    }

    private JFormattedTextField createTextFiled(String value, Color color, boolean editable, int columns) {
        JFormattedTextField field = new JFormattedTextField();
        field.setValue(value);
        field.setForeground(color);
        field.setEditable(editable);
        field.setColumns(columns);
        return field;
    }

    private void createCenterPanel() {
        //wykressssss
    }

    private void makeListeners() {
        oneStepBtn.addActionListener((event)-> facade.makeTick(1));

        manyStepBtn.addActionListener((event)->facade.makeTick(parseInt(this.manyStepField.getValue().toString())));

        resetSystickBtn.addActionListener((event)->facade.resetSysTick());

        tickintInit.addActionListener((event)->facade.setTickInt());
    }
}


