import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class GuiSysTick {

    //region VARIABLES
    private Facade facade;
    private JFrame frame;

    public JRadioButton onGenBtn, offGenBtn;
    public JButton generatorModeBtn, oneStepBtn, manyStepBtn, resetSystickBtn, setRegistersBtn,startGraphBtn,pauseGraphBtn,stopGraphBtn ;
    private ButtonGroup radioBtnGroup;
    public JCheckBox enableInit, tickintInit;
    private KeyListener onlyDigit;
    private JPanel northPane;
    private JPanel southPane;
    private JPanel eastPane;
    private JPanel westPane, buttonPanel, mainPane, centerPane;

    public JLabel ticksLabel, interruptLabel, rvrLabel, cvrLabel, rvrStateLabel,
            cvrStateLabel, enableflagStateLabel, countflagStateLabel, tickintFlagStateLabel,
            interruptflagStateLabel;
    public JFormattedTextField ticksField, interruptField, rvrField, cvrField,
            rvrStateField, cvrStateField, enableflagStateField, countflagStateField,
            tickintflagStateField, interruptflagStateField, delayField, burstField, manyStepField;

    private Border spaceBorder;
    public GraphPanel graph;
    public JLabel generatorLed;
    private JLabel modeStateLabel;
    public JFormattedTextField modeStateField, timeField;
    private JLabel delayInfo;
    private JLabel burstInfo;
    private JPanel onOffGenPanel;
    public JButton resetGeneratorBtn;
    private JPanel generatorSettingPane;
    private JPanel generatorButtonPane;
    private JPanel generatorDelayPane, genertorBurstPane;
    private JPanel graphHandlingPanel;

    //endregion

    public GuiSysTick(Facade facade) {
        this.facade = facade;

        frame = new JFrame("Systick Simulator ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        borderManager();
        createMenuBar();
        createMainPane();
        createWestPanel();
        createCenterPanel();
        onlyDigit = (new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c = keyEvent.getKeyChar();
                int charId = (int) c;
                if (!(charId >= 48 && charId <= 57)) {
                    keyEvent.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        makeListeners();
        refreshGui();
    }

    public void refreshGui() {
        frame.repaint();
        frame.revalidate();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem aboutItem = new JMenuItem("About");

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
        spaceBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);

    }

    private void createNorthPanel() {
        northPane.setBorder(spaceBorder);
        createInfoPanel();
        createFlagsPanel();
        createRegisterPanel();

    }

    private void createInfoPanel() {
        JPanel infoPane = new JPanel(new GridLayout(2, 2));

        String ticksString = "Ticks: ";
        String interruptString = "Interrupts: ";

        ticksLabel = new JLabel(ticksString);
        interruptLabel = new JLabel(interruptString);

        ticksField = createTextFiled("0", Color.green, false, 10);
        interruptField = createTextFiled("0", Color.green, false, 10);

        ticksLabel.setLabelFor(ticksField);
        interruptLabel.setLabelFor(interruptField);

        infoPane.setBorder(BorderFactory.createTitledBorder(" Current Stats Counter"));

        infoPane.add(ticksLabel);
        infoPane.add(ticksField);
        infoPane.add(interruptLabel);
        infoPane.add(interruptField);

        northPane.add(infoPane);
    }

    private void createFlagsPanel() {
        JPanel flagsPane = new JPanel();
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
        JPanel registerPane = new JPanel(new GridLayout(3, 2));

        rvrLabel = new JLabel(" RVR: ");
        cvrLabel = new JLabel(" CVR: ");
        setRegistersBtn = new JButton("Set");

        rvrField = createTextFiled("10", Color.blue, true, 10);
        cvrField = createTextFiled("5", Color.blue, true, 10);

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
        JPanel allDataStates = new JPanel(new GridLayout(2, 2));

        rvrStateLabel = new JLabel(" RVR: ");
        cvrStateLabel = new JLabel(" CVR: ");
        countflagStateLabel = new JLabel(" COUNTFLAG: ");
        enableflagStateLabel = new JLabel(" ENABLE: ");
        tickintFlagStateLabel = new JLabel(" TICKINT: ");
        interruptflagStateLabel = new JLabel(" INTERRUPT: ");
        modeStateLabel = new JLabel(" GENERATOR MODE: ");

        rvrStateField = createTextFiled("", Color.magenta, false, 6);
        cvrStateField = createTextFiled("", Color.magenta, false, 6);
        countflagStateField = createTextFiled("", Color.red, false, 6);
        enableflagStateField = createTextFiled("", Color.blue, false, 6);
        tickintflagStateField = createTextFiled("", Color.blue, false, 6);
        interruptflagStateField = createTextFiled("", Color.blue, false, 6);
        modeStateField = createTextFiled("continuous", Color.blue, false, 6);

        rvrStateLabel.setLabelFor(rvrStateField);
        cvrStateLabel.setLabelFor(cvrStateField);
        countflagStateLabel.setLabelFor(countflagStateField);
        enableflagStateLabel.setLabelFor(enableflagStateField);
        tickintFlagStateLabel.setLabelFor(tickintflagStateField);
        interruptflagStateLabel.setLabelFor(interruptflagStateField);
        modeStateLabel.setLabelFor(modeStateField);

        allDataStates.setBorder(BorderFactory.createTitledBorder("Current State "));
        allDataStates.setLayout(new BoxLayout(allDataStates, BoxLayout.Y_AXIS));

        allDataStates.add(Box.createRigidArea(new Dimension(0, 10)));
        allDataStates.add(rvrStateLabel);
        allDataStates.add(rvrStateField);
        allDataStates.add(Box.createRigidArea(new Dimension(0, 5)));

        allDataStates.add(cvrStateLabel);
        allDataStates.add(cvrStateField);
        allDataStates.add(Box.createRigidArea(new Dimension(0, 5)));

        allDataStates.add(countflagStateLabel);
        allDataStates.add(countflagStateField);
        allDataStates.add(Box.createRigidArea(new Dimension(0, 5)));

        allDataStates.add(enableflagStateLabel);
        allDataStates.add(enableflagStateField);
        allDataStates.add(Box.createRigidArea(new Dimension(0, 5)));

        allDataStates.add(tickintFlagStateLabel);
        allDataStates.add(tickintflagStateField);
        allDataStates.add(Box.createRigidArea(new Dimension(0, 5)));

        allDataStates.add(interruptflagStateLabel);
        allDataStates.add(interruptflagStateField);
        allDataStates.add(Box.createRigidArea(new Dimension(0, 20)));

        allDataStates.add(modeStateLabel);
        allDataStates.add(modeStateField);
        allDataStates.add(Box.createRigidArea(new Dimension(0, 5)));

        eastPane.add(allDataStates);

    }


    private void createSouthPanel() {
        southPane.setBorder(BorderFactory.createTitledBorder(" Ticks Generator "));

        southPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        generatorOnOffPanel();
        createLedPanel();
        createGeneratorSettingsPanel();
        createGeneratorButtonPanel();


    }

    private void generatorOnOffPanel() {
        onOffGenPanel = new JPanel();
        radioBtnGroup = new ButtonGroup();

        onGenBtn = new JRadioButton(" ON ");
        offGenBtn = new JRadioButton("OFF");

        onOffGenPanel.setLayout(new BoxLayout(onOffGenPanel, BoxLayout.Y_AXIS));

        offGenBtn.setSelected(true);

        radioBtnGroup.add(onGenBtn);
        radioBtnGroup.add(offGenBtn);

        onOffGenPanel.add(onGenBtn);
        onOffGenPanel.add(offGenBtn);

        southPane.add(onOffGenPanel);
    }

    private void createLedPanel() {
        generatorLed = new JLabel("•");
        generatorLed.setForeground(Color.gray);
        generatorLed.setFont(new Font(Font.SERIF, Font.PLAIN, 100));

        southPane.add(generatorLed);

    }

    private void createGeneratorSettingsPanel() {
        generatorSettingPane = new JPanel();
        generatorDelayPane = new JPanel();
        genertorBurstPane = new JPanel();

        delayInfo = new JLabel("Delay [ms]");
        burstInfo = new JLabel("Burst value");

        burstField = createTextFiled("0", Color.green, false, 10);
        delayField = createTextFiled("1000", Color.green, false, 10);

        delayInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        burstInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        delayField.setAlignmentX(Component.CENTER_ALIGNMENT);
        burstField.setAlignmentX(Component.CENTER_ALIGNMENT);

        generatorDelayPane.setLayout(new BoxLayout(generatorDelayPane, BoxLayout.Y_AXIS));
        genertorBurstPane.setLayout(new BoxLayout(genertorBurstPane, BoxLayout.Y_AXIS));

        generatorDelayPane.add(delayInfo);
        generatorDelayPane.add(Box.createRigidArea(new Dimension(0, 5)));
        generatorDelayPane.add(delayField);

        genertorBurstPane.add(burstInfo);
        genertorBurstPane.add(Box.createRigidArea(new Dimension(0, 5)));
        genertorBurstPane.add(burstField);

        generatorSettingPane.add(generatorDelayPane);
        generatorSettingPane.add(genertorBurstPane);

        southPane.add(generatorSettingPane);

    }

    private void createGeneratorButtonPanel() {
        generatorButtonPane = new JPanel();

        generatorModeBtn = new JButton("Set to Burst");
        resetGeneratorBtn = new JButton("RESET GENERATOR");

        generatorModeBtn.setSize(new Dimension(100, 100));
        generatorModeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        generatorModeBtn.setVisible(false);
        resetGeneratorBtn.setVisible(false);

        resetGeneratorBtn.setSize(new Dimension(100, 100));
        resetGeneratorBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        generatorButtonPane.setLayout(new BoxLayout(generatorButtonPane, BoxLayout.Y_AXIS));


        generatorButtonPane.add(generatorModeBtn);
        generatorButtonPane.add(Box.createRigidArea(new Dimension(0, 5)));
        generatorButtonPane.add(resetGeneratorBtn);

        southPane.add(generatorButtonPane);

    }

    private void createWestPanel() {

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        oneStepBtn = new JButton("ONE IMPULS");
        manyStepBtn = new JButton("MANY IMPULS");
        resetSystickBtn = new JButton("RESET SYSTICK");


        oneStepBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        manyStepBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetSystickBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        manyStepField = createTextFiled("", Color.green, true, 2);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        buttonPanel.add(oneStepBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonPanel.add(manyStepBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        buttonPanel.add(manyStepField);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonPanel.add(resetSystickBtn);

        westPane.add(buttonPanel, BorderLayout.WEST);
        westPane.setBorder(BorderFactory.createTitledBorder(" Manual Ticks "));

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
        centerPane.setBorder(BorderFactory.createTitledBorder(" Graph F(x) = ticks(t) "));
        grapfHandlingPanel();
        createAndShowGraph();
    }

    private void grapfHandlingPanel() {
        graphHandlingPanel = new JPanel();

        timeField = createTextFiled("time", Color.darkGray, false, 7);

        startGraphBtn = new JButton("Start");
        pauseGraphBtn = new JButton("Pause");
        stopGraphBtn = new JButton("Stop");

        graphHandlingPanel.add(startGraphBtn);
        graphHandlingPanel.add(pauseGraphBtn);
        graphHandlingPanel.add(stopGraphBtn);

        graphHandlingPanel.add(timeField);

        centerPane.add(graphHandlingPanel);

    }

    private void createAndShowGraph() {
        List<Double> scores = new ArrayList<>();

        graph = new GraphPanel(scores);
       // Dimension centerPaneDim = centerPane.getSize();
        graph.setPreferredSize(new Dimension(450, 330));

        centerPane.add(graph);

    }

    private void makeListeners() {
        oneStepBtn.addActionListener((event) -> facade.makeTick(1));

        manyStepBtn.addActionListener((event) -> facade.makeTick(parseInt(this.manyStepField.getValue().toString())));

        resetSystickBtn.addActionListener((event) -> facade.reset());

        tickintInit.addActionListener((event) -> facade.setTickInt());

        enableInit.addActionListener((event) -> facade.setEnable());

        setRegistersBtn.addActionListener((event) -> facade.setRegisters());

        onGenBtn.addActionListener((event) -> facade.enableGenerator());

        offGenBtn.addActionListener((event) -> facade.disableGenerator());

        generatorModeBtn.addActionListener((event) -> facade.changeGenMode());

        interruptflagStateField.addPropertyChangeListener((event) -> facade.howManyInterrupt());

        resetSystickBtn.addActionListener((event) -> facade.reset());

        resetGeneratorBtn.addActionListener((event) -> facade.resetGenerator());

        startGraphBtn.addActionListener((event) -> facade.startGraph());

        pauseGraphBtn.addActionListener((event) -> facade.pauseGraph());

        stopGraphBtn.addActionListener((event) -> facade.stopGraph());

        delayField.addKeyListener(onlyDigit);
        burstField.addKeyListener(onlyDigit);
        rvrField.addKeyListener(onlyDigit);
        cvrField.addKeyListener(onlyDigit);
        manyStepField.addKeyListener(onlyDigit);

        int x = parseInt(this.delayField.getValue().toString());
        delayField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                facade.setDelay(parseInt(delayField.getValue().toString()));
            }
        });

    }

}


