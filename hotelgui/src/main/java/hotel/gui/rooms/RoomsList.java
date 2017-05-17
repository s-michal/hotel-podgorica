package hotel.gui.rooms;

import hotel.gui.BaseView;
import hotel.gui.cellRenderers.ButtonMouseListener;
import hotel.gui.cellRenderers.ButtonRenderer;
import hotel.gui.cellRenderers.PriceCellRenderer;
import hotel.model.HotelManager;
import hotel.model.Room;
import hotel.model.RoomManager;
import hotel.model.exceptions.RoomHasReservationException;

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

    private RoomsModel model;

    public RoomsList(RoomManager roomManager, HotelManager hotelManager)
    {
        Objects.requireNonNull(roomManager);
        Objects.requireNonNull(hotelManager);

        this.roomManager = roomManager;

        model = new RoomsModel(roomManager, hotelManager);

        model.addButton(this::createUpdateButton);
        model.addButton(this::createDeleteButton);

        table.setModel(model);
        table.setDefaultRenderer(BigDecimal.class, new PriceCellRenderer());
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.addMouseListener(new ButtonMouseListener(table));

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

        RoomForm view = new RoomForm(roomManager, room);

        view.onSuccess(() -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            ((RoomsModel) table.getModel()).invalidate();
        });

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

        button.addActionListener(x -> {
            try {
                model.deleteRoom(room);
            } catch (RoomHasReservationException e) {
            }
        });

        return button;
    }

}
