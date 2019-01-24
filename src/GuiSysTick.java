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

import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.lang.Integer.parseInt;

public class GuiSysTick {

    //region VARIABLES
    private Facade facade;
    private JFrame frame;

    public JRadioButton onGenBtn, offGenBtn;
    private Border spaceBorder;

    public JButton generatorModeBtn, oneStepBtn, manyStepBtn, resetSystickBtn,
            setRegistersBtn, startGraphBtn, pauseGraphBtn, stopGraphBtn, resetGeneratorBtn;

    private ButtonGroup radioBtnGroup;

    public JCheckBox enableInit, tickintInit;
    private KeyListener onlyDigit;
    private JPanel northPane, southPane, eastPane, westPane, buttonPanel, mainPane, centerPane, onOffGenPanel,
            generatorSettingPane, generatorButtonPane, generatorDelayPane, genertorBurstPane, graphHandlingPanel;

    public JLabel ticksLabel, interruptLabel, rvrLabel, cvrLabel, rvrStateLabel,
            cvrStateLabel, enableflagStateLabel, countflagStateLabel, tickintFlagStateLabel,
            interruptflagStateLabel, delayInfo, burstInfo, modeStateLabel, generatorLed;

    public JFormattedTextField ticksField, interruptField, rvrField, cvrField,
            rvrStateField, cvrStateField, enableflagStateField, countflagStateField,
            tickintflagStateField, interruptflagStateField, delayField, burstField, manyStepField, modeStateField, timeField;

    public GraphPanel graph;

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

        onlyDigit = (new KeyListener() {        //zabezpiecznie przed wpisaniem zbyt dużej wartości do inputów. Maxymalnie 23bity-1.
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c = keyEvent.getKeyChar();

                JFormattedTextField input = (JFormattedTextField) keyEvent.getComponent();

                int lenght = input.getText().length();
                char key = keyEvent.getKeyChar();
                int charId = (int) c;
                if ((!(charId >= 48 && charId <= 57) || (lenght >= 7)) && !((int) key == VK_BACK_SPACE)) {
                    if (lenght >= 7) {
                        int val = parseInt(input.getText() + c);
                        if (val > 16777215) val = 16777215;
                        String value = Integer.toString(val);
                        input.setText(value);
                        input.setValue(value);
                    }
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
        JMenu helpMenu = new JMenu("Help");

        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem aboutSystickItem = new JMenuItem("Systick");
        JMenuItem aboutFlagsItem = new JMenuItem("Flags");
        JMenuItem aboutGeneratorItem = new JMenuItem("Generator");
        JMenuItem aboutGraphItem = new JMenuItem("Diagram");


        exitItem.setToolTipText("exit aplication");
        exitItem.addActionListener((event) -> System.exit(0));

        aboutSystickItem.setToolTipText("Short note about Systick");
        aboutSystickItem.addActionListener((event) -> {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Systick jest to 24-bitowy licznik, " +
                    "zliczający w dół od zadanej wartości. \n" +
                    "Może być używany do przechowywania czasu, pomiaru czasu lub jako źródło przerwań dla zadań, " +
                    "które muszą być wykonywane regularnie.", "About SysTick", JOptionPane.INFORMATION_MESSAGE);
        });

        aboutFlagsItem.addActionListener((event) -> {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "ENABLE - (CSR) określa dostępność licznika. \n" +
                    "TICKINT - (CSR) określa dostępnośc przerwań. \n" +
                    "COUNTFLAG - (CSR) wskazuje przejście licznika przez wartość 0. \n" +
                    "INTERRUPTFLAG - (dodatkowa) wskazuje wystąpienie przerwania przy przejściu z 1 -> 0. \n" +
                    "", "About Flags", JOptionPane.INFORMATION_MESSAGE);
        });

        aboutGeneratorItem.addActionListener((event) -> {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Ustawia generowanie impulsów co określony odstęp w czasie (delay). \n" +
                    "Posiada 2 tryby pracy: \n" +
                    "   CONTINUOUS - generowanie impulsów jest ciągłe,\n" +
                    "   BURST - generowanie pakietów impulsów.\n\n" +
                    "Pole GENERATOR MODE wyświetla, który tryb jest aktualnie wybrany.\n" +
                    "Domyślnymi wartościami są opóźnenie 1ms oraz paczka 0 impulsów.\n" +
                    "Po włączeniu, generator działa cały czas w tle do momentu wyłączenia.", " About Generator", JOptionPane.INFORMATION_MESSAGE);
        });

        aboutGraphItem.addActionListener((event) -> {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Wykres reprezentuje ilość wygenerowanych impulsów w czasie. \n\n" +
                    "Na pojawienie się lub zmianę charakterystyki wpływa: \n" +
                    "   - Włączenie generatora i zmiana trybu bądź wartości. \n" +
                    "   - Ręczne generowanie impulsów w polu \"Set Tick Manually\" (przy wcześniejszym ustawieniu ENABLE=true, CVR, RVR)., \n\n" +
                    "\"Start\" - rysowanie wykresu\n" +
                    "\"Pause\" - zapauzowanie rysowania w danym momencie, ponowne kliknięcie \"Start\" wznawia rysowanie,,\n" +
                    "\"Stop\" - zatrzymanie rysowania \n" +
                    "Mini zegarek pokazuje czas, w którym trwają pomiary [s]. Jest aktualizowany co pomiar próbki.\n\n" +
                    "Domyślna częstotliwość jest ustawiona na 1000. \n\n" +
                    "Legenda:\nośX - czas\n" +
                    "ośY - ilość ticków", "About Diagram", JOptionPane.INFORMATION_MESSAGE);
        });


        helpMenu.add(aboutSystickItem);
        helpMenu.add(aboutFlagsItem);
        helpMenu.add(aboutGeneratorItem);
        helpMenu.add(aboutGraphItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

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
        ticksField.setToolTipText("How many ticks was generated.");
        interruptField = createTextFiled("0", Color.green, false, 10);
        interruptField.setToolTipText("How many interrupts was generated.");

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
        rvrField.setToolTipText("Max value is 23bits -1.");
        cvrField = createTextFiled("5", Color.blue, true, 10);
        cvrField.setToolTipText("Max value is 23bits -1.");

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
        burstField.setToolTipText("Click Enter to accept value.");

        delayField = createTextFiled("1000", Color.green, false, 10);
        delayField.setToolTipText("Click Enter to accept value.");

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
        manyStepField.setToolTipText("Max value is 100. Automatically set to 100 after out of range.");

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        buttonPanel.add(oneStepBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonPanel.add(manyStepBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        buttonPanel.add(manyStepField);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonPanel.add(resetSystickBtn);

        westPane.add(buttonPanel, BorderLayout.WEST);
        westPane.setBorder(BorderFactory.createTitledBorder("Set Tick Manually"));
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
        centerPane.setBorder(BorderFactory.createTitledBorder(" Graph F(t) = ticks(t) "));

        grapfHandlingPanel();
        createAndShowGraph();
    }

    private void grapfHandlingPanel() {
        graphHandlingPanel = new JPanel();

        timeField = createTextFiled("time", Color.darkGray, false, 7);
        timeField.setToolTipText("Time is updated after measuring.");

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

        manyStepBtn.addActionListener((event) -> facade.makeTick(countManyTicks()));

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
        burstField.addActionListener((event) -> facade.setBurst());

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

    private int countManyTicks() {
        int value;
        try {
            value = parseInt(manyStepField.getText().toString());
        } catch (NumberFormatException e) {
            value = 100;

        }
        if (value > 100) {
            value = 100;
            manyStepField.setText(Integer.toString(value));
            manyStepField.setValue(value);
        }

        return value;
    }

}


