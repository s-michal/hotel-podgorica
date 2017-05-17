package hotel.gui.rooms;

import hotel.gui.BaseView;
import hotel.model.Room;
import hotel.model.RoomManager;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.DuplicateRoomNumberException;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class RoomForm extends BaseView
{

    private RoomManager roomManager;
    private JTextField numberField;
    private JComboBox floorCombobox;
    private JPanel panel;
    private JTextField capacityField;
    private JTextField priceField;
    private JLabel numberLabel;
    private JLabel floorLabel;
    private JLabel capacityLabel;
    private JLabel priceLabel;
    private JButton button1;
    private JLabel errorsContainer;

    private Runnable onSuccess;

    RoomForm(RoomManager roomManager, Room room)
    {
        Objects.requireNonNull(roomManager);
        this.roomManager = roomManager;
        numberLabel.setText(translate("roomNumber"));
        floorLabel.setText(translate("roomFloor"));
        capacityLabel.setText(translate("roomCapacity"));
        priceLabel.setText(translate("roomPrice"));
        button1.setText(translate("createButton"));

        if(room != null) {
            numberField.setText(""+room.getNumber());
            floorCombobox.setSelectedIndex(room.getFloor() - 1);
            capacityField.setText(""+room.getCapacity());
            priceField.setText(room.getPricePerDay().toString());
        }
        button1.addActionListener(e -> createOrUpdate(room));
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

    private void createOrUpdate(Room room)
    {
        ArrayList<String> errors = new ArrayList<>();

        long number = 0;

        try {
            number = Long.parseLong(numberField.getText());
            if(number < 1) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            errors.add(translate("errors.room.invalidNumber"));
        }

        int capacity = 0;
        try {
            capacity = Integer.parseInt(capacityField.getText());
            if(capacity <= 0) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            errors.add(translate("erorrs.room.invalidCapacity"));
        }

        int floor = floorCombobox.getSelectedIndex() + 1;

        BigDecimal price = null;
        try {
            double tempPrice = Double.parseDouble(priceField.getText());
            if(tempPrice < 0) {
                throw new NumberFormatException();
            }
            price = BigDecimal.valueOf(tempPrice);
        } catch(NumberFormatException e) {
            errors.add(translate("erorrs.room.invalidPrice"));
        }

        if(errors.isEmpty()) {
            try {
                if(room != null) {
                    room.update(number, capacity, floor, price);
                    roomManager.update(room);
                } else {
                    roomManager.create(new Room(number, capacity, floor, price));
                }
            } catch(DuplicateRoomNumberException e) {
                errors.add(translate("erorrs.room.duplicateNumber"));
            } catch(ApplicationException e) {
                errors.add(translate("erorrs.general.unknown"));
            }
        }

        errorsContainer.setText(String.join("\n", errors));

        if(errors.isEmpty()) {
            if(onSuccess != null) {
                onSuccess.run();
            }
        }
    }

}
