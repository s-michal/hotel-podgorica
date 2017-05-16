package hotel.gui;


import javax.swing.*;

public class TopMenuFactory extends JMenuBar
{

    static JMenuBar create()
    {
        //hlavní úroveň menu
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        final JMenu helpMenu = new JMenu("Help");
        menubar.add(fileMenu);
        menubar.add(Box.createHorizontalGlue());
        menubar.add(helpMenu);
        //menu File
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(e -> System.exit(1));
        //menu Help
        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);

        aboutMenuItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(helpMenu, "Authors (c) František Maša & Michal Šustera", "About", JOptionPane.INFORMATION_MESSAGE);
        });

        return menubar;
    }
}
