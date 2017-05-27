package hotel.gui.rooms;

import hotel.gui.BaseView;
import hotel.gui.renderers.ButtonMouseListener;
import hotel.gui.renderers.ButtonRenderer;
import hotel.gui.renderers.PriceCellRenderer;
import hotel.model.HotelManager;
import hotel.model.Room;
import hotel.model.RoomManager;
import hotel.model.exceptions.RoomHasReservationException;
import hotel.workers.CallbackWorker;
import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.Objects;

public class RoomsList extends BaseView
{
    private JPanel topPanel;
    private JTable table;
    private JButton createNewRoomButton;

    private RoomsModel model;

    public RoomsList(RoomManager roomManager, HotelManager hotelManager)
    {
        Objects.requireNonNull(roomManager);
        Objects.requireNonNull(hotelManager);

        model = new RoomsModel(roomManager, hotelManager);

        model.addButton(this::createUpdateButton);
        model.addButton(this::createDeleteButton);

        table.setModel(model);
        table.setDefaultRenderer(BigDecimal.class, new PriceCellRenderer());
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.addMouseListener(new ButtonMouseListener(table));

        createNewRoomButton.setText(translate("button.createNewRoom"));
        createNewRoomButton.addActionListener(e -> openFormFrame(null));
    }

    public JPanel getPanel()
    {
        return topPanel;
    }

    private void openFormFrame(Room room)
    {
        JFrame frame = new JFrame(translate("titles.newRoom"));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        RoomForm view = new RoomForm(model, room);

        view.onSuccess(() -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        frame.setContentPane(view.getPanel());

        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setVisible(true);
    }

    private JButton createUpdateButton(Room room)
    {
        JButton button = new JButton(translate("button.update"));
        button.addActionListener(e -> openFormFrame(room));
        return button;
    }

    private JButton createDeleteButton(Room room)
    {
        JButton button = new JButton(translate("button.delete"));
        if (model.hasReservations(room)) {
            button.setEnabled(false);
            return button;
        }

        button.addActionListener(x ->
            new CallbackWorker(() -> {
                try {
                    model.deleteRoom(room);
                    return true;
                } catch(RoomHasReservationException e) {
                    showMessageDialog(null, translate("errors.rooms.hasReservations"));
                    return false;
                }
            }, null).run()

        );

        return button;
    }

}
