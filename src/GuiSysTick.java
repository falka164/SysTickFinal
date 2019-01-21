import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class GuiSysTick {
    private JFrame frame;

    private JMenuBar menuBar;
    private JMenu fileMenu, viewMenu, helpMenu;
    private JMenuItem exitItem, aboutItem;

    private JButton button1;
    private JCheckBox enableInit, tickintInit;

    private JPanel mainPane, northPane, southPane, eastPane, westPane, centerPane, infoPane, flagsPane, registerPane, allDataStates;

    private JLabel ticksLabel, interruptLabel, rvrLabel, cvrLabel, rvrStateLabel,
            cvrStateLabel, enableflagStateLabel, countflagStateLabel, tickintFlagStateLabel,
            interruptflagStateLabel;
    private JFormattedTextField ticksField, interruptField, rvrField, cvrField,
            rvrStateField, cvrStateField, enableflagStateField, countflagStateField,
            tickintflagStateField, interruptflagStateField;
    private static String ticksString, interruptString, rvrString, cvrString;
    private int howTicks, howInterrupt, rvrValue, cvrValue;

    private Border borderLine, spaceBorder;


    public GuiSysTick() {
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
        refreshGui();

    }

    private void refreshGui() {
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

        ticksLabel = new JLabel(ticksString);   //Create the labels.
        interruptLabel = new JLabel(interruptString);

        ticksField = new JFormattedTextField(); //Create the text fields and set them up.
        ticksField.setValue(howTicks);
        ticksField.setEditable(false);
        ticksField.setForeground(Color.green);

        interruptField = new JFormattedTextField();
        interruptField.setValue(howInterrupt);
        interruptField.setEditable(false);
        interruptField.setForeground(Color.green);

        ticksLabel.setLabelFor(ticksField);  //Tell accessibility tools about label/textfield pairs.
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
        registerPane = new JPanel(new GridLayout(2, 2));

        rvrString = "RVR: ";
        cvrString = "CVR: ";
        rvrValue = 5;
        cvrValue = 6;

        rvrLabel = new JLabel(rvrString);   //Create the labels.
        cvrLabel = new JLabel(cvrString);

        rvrField = new JFormattedTextField(); //Create the text fields and set them up.
        rvrField.setValue(rvrValue);
        rvrField.setEditable(true);
        rvrField.setForeground(Color.blue);

        cvrField = new JFormattedTextField();
        cvrField.setValue(cvrValue);
        cvrField.setEditable(true);
        cvrField.setForeground(Color.blue);

        rvrLabel.setLabelFor(rvrField);  //Tell accessibility tools about label/textfield pairs.
        cvrLabel.setLabelFor(cvrField);

        registerPane.setBorder(BorderFactory.createTitledBorder(" Set RVR & CVR value "));

        registerPane.add(rvrLabel);
        registerPane.add(rvrField);
        registerPane.add(cvrLabel);
        registerPane.add(cvrField);

        northPane.add(registerPane);
    }

    private void createEastPanel() {
        allDataStates = new JPanel(new GridLayout(2,2));

        rvrStateLabel = new JLabel(rvrString);
        cvrStateLabel = new JLabel(cvrString);
        countflagStateLabel = new JLabel(" COUNTFLAG: ");
        enableflagStateLabel = new JLabel(" ENABLE: ");
        tickintFlagStateLabel = new JLabel(" TICKINT: ");
        interruptflagStateLabel = new JLabel(" INTERRUPT: ");

        rvrStateField = new JFormattedTextField();
        rvrStateField.setValue(14);
        rvrStateField.setEditable(false);
        rvrStateField.setForeground(Color.pink);

        cvrStateField = new JFormattedTextField();
        cvrStateField.setValue(545);
        cvrStateField.setEditable(false);
        cvrStateField.setForeground(Color.pink);

        countflagStateField = new JFormattedTextField();
        countflagStateField.setValue("true");
        countflagStateField.setEditable(false);
        countflagStateField.setForeground(Color.pink);

        enableflagStateField = new JFormattedTextField();
        enableflagStateField.setValue("true");
        enableflagStateField.setEditable(false);
        enableflagStateField.setForeground(Color.pink);

        tickintflagStateField = new JFormattedTextField();
        tickintflagStateField.setValue("true");
        tickintflagStateField.setEditable(false);
        tickintflagStateField.setForeground(Color.pink);

        interruptflagStateField = new JFormattedTextField();
        interruptflagStateField.setValue("true");
        interruptflagStateField.setEditable(false);
        interruptflagStateField.setForeground(Color.pink);

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

    }

    private void createWestPanel() {

    }

    private void createCenterPanel() {
        //wykressssss
    }
}


