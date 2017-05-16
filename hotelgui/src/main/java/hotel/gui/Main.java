package hotel.gui;

import javax.swing.*;
import java.awt.*;

public class Main
{


    public static void main(String... args)
    {
        EventQueue.invokeLater(() -> {
            setSystemFeel();
            JFrame frame = new JFrame("Reservations");

            frame.setJMenuBar(TopMenuFactory.create());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            MainView view = new MainView(new Context());

            frame.setContentPane(view.getPanel());

            frame.setPreferredSize(new Dimension(800, 600));
            frame.pack();
            frame.setVisible(true);
        });
    }

    private static void setSystemFeel()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
