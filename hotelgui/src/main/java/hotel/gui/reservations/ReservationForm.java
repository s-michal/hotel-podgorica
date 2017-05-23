package hotel.gui.reservations;

import hotel.gui.BaseView;
import hotel.gui.renderers.*;
import hotel.gui.renderers.ListCellRenderer;
import hotel.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class ReservationForm extends BaseView
{

    private HotelManager hotelManager;
    private RoomManager roomManager;
    private CustomerManager customerManager;

    private Runnable onSuccess;
    private JPanel errorsContainer;
    private JComboBox<Customer> customerCombobox;
    private JLabel customerLabel;
    private JLabel roomLabel;
    private JComboBox<Room> roomCombobox;
    private JComboBox sinceCombobox;
    private JComboBox untilCombobox;
    private JLabel sinceLabel;
    private JLabel untilLabel;
    private JPanel panel;
    private JButton submitButton;

    ReservationForm(HotelManager hotelManager, RoomManager roomManager, CustomerManager customerManager)
    {
        Objects.requireNonNull(hotelManager);
        Objects.requireNonNull(roomManager);
        Objects.requireNonNull(customerManager);
        this.hotelManager = hotelManager;
        this.roomManager = roomManager;
        this.customerManager = customerManager;


        customerLabel.setText(translate("customer"));
        roomLabel.setText(translate("room"));
        sinceLabel.setText(translate("since"));
        untilLabel.setText(translate("until"));
        submitButton.setText(translate("button.submit.place"));

        customerCombobox.setModel(new DefaultComboBoxModel<>(new Vector<>(customerManager.findAll())));
        customerCombobox.setRenderer(
                new ListCellRenderer<>(c -> c.getName() + " " + c.getAddress(), Customer.class)
        );

        roomCombobox.setModel(new DefaultComboBoxModel<>(new Vector<>(roomManager.findAll())));
        roomCombobox.setRenderer(new ListCellRenderer<>(r -> ""+r.getNumber(), Room.class));
        submitButton.addActionListener(e -> create());
    }

    @Override
    public JPanel getPanel()
    {
        return panel;
    }

    public void onSuccess(Runnable onSuccess)
    {
        this.onSuccess = onSuccess;
    }

    private void create()
    {
        ArrayList<String> errors = new ArrayList<>();
        //
//        long number = 0;
//
//        try {
//            number = Long.parseLong(numberField.getText());
//            if(number < 1) {
//                throw new NumberFormatException();
//            }
//        } catch(NumberFormatException e) {
//            errors.add(translate("errors.room.invalidNumber"));
//        }
//
//        int capacity = 0;
//        try {
//            capacity = Integer.parseInt(capacityField.getText());
//            if(capacity < 0) {
//                throw new NumberFormatException();
//            }
//        } catch(NumberFormatException e) {
//            errors.add(translate("erorrs.room.invalidCapacity"));
//        }
//
//        int floor = floorCombobox.getSelectedIndex() + 1;
//
//        BigDecimal price = null;
//        try {
//            double tempPrice = Double.parseDouble(priceField.getText());
//            if(tempPrice < 0) {
//                throw new NumberFormatException();
//            }
//            price = BigDecimal.valueOf(tempPrice);
//        } catch(NumberFormatException e) {
//            errors.add(translate("erorrs.room.invalidPrice"));
//        }
//
//        if(errors.isEmpty()) {
//            try {
//                if(room != null) {
//                    room.update(number, capacity, floor, price);
//                    roomManager.update(room);
//                } else {
//                    roomManager.create(new Room(number, capacity, floor, price));
//                }
//            } catch(DuplicateRoomNumberException e) {
//                errors.add(translate("erorrs.room.duplicateNumber"));
//            } catch(ApplicationException e) {
//                errors.add(translate("erorrs.general.unknown"));
//            }
//        }
//
//        errorsContainer.setText(String.join("\n", errors));
//
//        if(errors.isEmpty()) {
//            if(onSuccess != null) {
//                onSuccess.run();
//            }
//        }
    }

}