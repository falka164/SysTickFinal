import javax.swing.*;
import java.awt.*;

public class GuiSysTick {
    private JFrame frame;

    private JMenuBar menuBar;
    private JMenu fileMenu, viewMenu, helpMenu;
    private JMenuItem exitItem, aboutItem;

    private JButton button1;
    private JCheckBox enableInit, tickintInit;

    private JPanel contentPane, mainPane, northPane, southPane, eastPane, westPane, centerPane, infoPane, flagsPane, registerPane;

    private JLabel ticksLabel, interruptLabel, rvrLabel, cvrLabel;
    private JFormattedTextField ticksField, interruptField, rvrField, cvrField;
    private static String ticksString, interruptString, rvrString, cvrString;
    private int howTicks, howInterrupt, rvrValue, cvrValue;



    public GuiSysTick() {
        frame = new JFrame("Systick Simulator ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        createMenuBar();
        //createContentPane();
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

    private void createContentPane() {
        button1 = new JButton("clisk");
        JButton x1 = new JButton("center");
        JButton x2 = new JButton("west");
        JButton x3 = new JButton("north");
        JButton x4 = new JButton("south");
        JButton x5 = new JButton("east");
        contentPane = new JPanel(new BorderLayout());
        //   contentPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 40, 80));
        contentPane.add(x1, BorderLayout.CENTER);
        contentPane.add(x2, BorderLayout.WEST);
        contentPane.add(x3, BorderLayout.NORTH);
        contentPane.add(x4, BorderLayout.SOUTH);
        contentPane.add(x5, BorderLayout.EAST);
        this.frame.add(contentPane);
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
    }

    private void createNorthPanel() {
        northPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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

        infoPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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

        flagsPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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

        registerPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        registerPane.add(rvrLabel);
        registerPane.add(rvrField);
        registerPane.add(cvrLabel);
        registerPane.add(cvrField);

        northPane.add(registerPane);
    }

    private void createEastPanel() {

    }

    private void createSouthPanel() {

    }

    private void createWestPanel() {

    }

    private void createCenterPanel() {

    }
}


