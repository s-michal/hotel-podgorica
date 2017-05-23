package hotel.gui;

import hotel.gui.customers.CustomerList;
import hotel.gui.reservations.ReservationList;
import hotel.gui.rooms.RoomsList;

import javax.swing.*;
import java.util.Objects;

public class MainView extends BaseView
{

    private Context context;
    private JTabbedPane panel;

    MainView(Context context)
    {
        Objects.requireNonNull(context);
        this.context = context;

        ReservationList reservationList = new ReservationList(
                context.getHotelManager(),
                context.getRoomManager(),
                context.getCustomerManager()
        );
        panel.addTab(translate("tabs.Reservations"), null, reservationList.getPanel());

        RoomsList roomList = new RoomsList(context.getRoomManager(), context.getHotelManager());
        panel.addTab(translate("tabs.Rooms"), null, roomList.getPanel());

        CustomerList customerList = new CustomerList(
                context.getCustomerManager(),
                context.getHotelManager()
        );
        panel.addTab(translate("tabs.Customers"), null, customerList.getPanel());

        setLookAndFeel();
    }

    @Override
    public JTabbedPane getPanel()
    {
        return panel;
    }

    private void setLookAndFeel()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
