package hotel.gui.rooms;

import hotel.gui.BaseView;
import hotel.workers.CallbackWorker;
import hotel.model.Room;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.DuplicateRoomNumberException;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public class RoomForm extends BaseView
{

    private RoomsModel model;
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
    private JTextPane errorsContainer;

    private Runnable onSuccess;

    RoomForm(RoomsModel model, Room room)
    {
        Objects.requireNonNull(model);
        this.model = model;
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
        errorsContainer.setVisible(false);
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
            Supplier<Boolean> callback;
            errorsContainer.setVisible(false);
            if(room != null) {
                room.update(number, capacity, floor, price);
                callback = () -> {
                    try {
                        model.updateRoom(room);
                        return true;
                    } catch(ApplicationException e) {
                        errors.add(translate("errors.general.unknown"));
                        return false;
                    }
                };

            } else {
                Room newRoom = new Room(number, capacity, floor, price);
                callback = () -> {
                    try {
                        model.createRoom(newRoom);
                        return true;
                    } catch(ApplicationException e) {
                        errors.add(translate("erorrs.general.unknown"));
                    } catch(DuplicateRoomNumberException e) {
                        errors.add(translate("erorrs.room.duplicateNumber"));
                    }
                    return false;
                };
            }
            new CallbackWorker(callback, onSuccess).run();
        } else {
            errorsContainer.setText(String.join("\n", errors));
            errorsContainer.setVisible(true);
        }
    }

}
