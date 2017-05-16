package hotel.gui.reservations;

import hotel.gui.Context;
import hotel.model.HotelManager;

import javax.swing.*;
import java.util.Objects;

public class ReservationList
{
    private JPanel topPanel;
    private JTable table1;

    private Context context;

    public ReservationList(HotelManager manager)
    {
        Objects.requireNonNull(manager);
        this.table1.setModel(new ReservationsModel(manager));
    }

    public JPanel getPanel()
    {
        return topPanel;
    }

}
