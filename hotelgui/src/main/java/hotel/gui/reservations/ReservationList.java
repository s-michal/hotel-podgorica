package hotel.gui.reservations;

import hotel.gui.BaseView;
import hotel.model.HotelManager;

import javax.swing.*;
import java.util.Objects;

public class ReservationList extends BaseView
{
    private JPanel panel;
    private JTable table;
    private JButton createButton;

    public ReservationList(HotelManager manager)
    {
        Objects.requireNonNull(manager);
        ReservationsModel model = new ReservationsModel(manager);

        table.setModel(model);
        createButton.setText(translate("reservation.button.place"));
    }

    public JPanel getPanel()
    {
        return panel;
    }

}
