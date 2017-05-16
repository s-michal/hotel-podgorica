package hotel.gui.rooms;

import hotel.gui.BaseView;
import hotel.gui.utils.PriceCellRenderer;
import hotel.model.RoomManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.Objects;

public class RoomsList extends BaseView
{

    private RoomManager roomManager;

    private JPanel topPanel;
    private JTable table;
    private JButton createNewRoomButton;

    public RoomsList(RoomManager roomManager)
    {
        Objects.requireNonNull(roomManager);

        this.roomManager = roomManager;

        table.setModel(new RoomsModel(roomManager));
        table.setDefaultRenderer(BigDecimal.class, new PriceCellRenderer());
        createNewRoomButton.addActionListener(e -> openFormFrame());
    }

    public JPanel getPanel()
    {
        return topPanel;
    }

    private void openFormFrame()
    {
        JFrame frame = new JFrame(translate("titles.newRoom"));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        RoomForm view = new RoomForm(roomManager);

        view.onSuccess(() -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            table.setModel(new RoomsModel(roomManager));
        });

        frame.setContentPane(view.getPanel());

        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }

}
