package hotel.gui.reservations;

import hotel.gui.BaseView;
import hotel.gui.renderers.ButtonMouseListener;
import hotel.gui.renderers.ButtonRenderer;
import hotel.model.CustomerManager;
import hotel.model.HotelManager;
import hotel.model.Reservation;
import hotel.model.RoomManager;
import hotel.model.exceptions.ReservationCannotBeCanceledNow;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.Objects;

public class ReservationList extends BaseView
{

    private ReservationsModel model;
    private RoomManager roomManager;
    private CustomerManager customerManager;

    private JPanel panel;
    private JTable table;
    private JButton createButton;

    public ReservationList(HotelManager hotelManager, RoomManager roomManager, CustomerManager customerManager)
    {
        Objects.requireNonNull(hotelManager);
        Objects.requireNonNull(roomManager);
        Objects.requireNonNull(customerManager);

        this.roomManager = roomManager;
        this.customerManager = customerManager;

        model = new ReservationsModel(hotelManager);

        model.addButton(this::createCancelButton);

        table.setModel(model);
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.addMouseListener(new ButtonMouseListener(table));
        createButton.setText(translate("reservation.button.place"));
        createButton.addActionListener(e -> openFormFrame());
    }

    private JButton createCancelButton(Reservation reservation)
    {
        JButton button = new JButton(translate("button.cancel"));

        if(reservation.isCanceled() || LocalDate.now().isAfter(reservation.getSince())) {
            button.setEnabled(false);
            return button;
        }

        button.addActionListener(e -> {
            try {
                model.cancelReservation(reservation);
            } catch (ReservationCannotBeCanceledNow ex) {
                showMessageDialog(null, "error.reservation.passed");
            }
        });
        return button;
    }

    private void openFormFrame()
    {
        JFrame frame = new JFrame(translate("titles.newReservation"));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        ReservationForm view = new ReservationForm(model, roomManager, customerManager);

        view.onSuccess(() -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        frame.setContentPane(view.getPanel());

        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getPanel()
    {
        return panel;
    }

}
