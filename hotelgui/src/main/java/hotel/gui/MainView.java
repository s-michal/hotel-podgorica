package hotel.gui;

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

        ReservationList reservationList = new ReservationList(context.getHotelManager());
        panel.addTab(translate("tabs.Reservations"), null, reservationList.getPanel());

        RoomsList roomsList = new RoomsList(context.getRoomManager(), context.getHotelManager());
        panel.addTab(translate("tabs.Rooms"), null, roomsList.getPanel());

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
