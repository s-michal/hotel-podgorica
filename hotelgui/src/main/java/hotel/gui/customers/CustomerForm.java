package hotel.gui.customers;

import hotel.gui.BaseView;
import hotel.gui.rooms.RoomsModel;
import hotel.model.Customer;
import hotel.model.Room;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.DuplicateRoomNumberException;
import hotel.workers.CallbackWorker;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class CustomerForm extends BaseView
{
    private JPanel panel;
    private JTextField nameField;
    private JLabel nameLabel;
    private JLabel birthDateLabel;
    private JComboBox birthDateCombobox;
    private JLabel addressLabel;
    private JTextField addressField;
    private JButton submitButton;
    private JTextPane errorsContainer;

    private CustomersModel model;

    private Runnable onSuccess;

    CustomerForm(CustomersModel model, Customer customer)
    {
        Objects.requireNonNull(model);
        this.model = model;
        nameLabel.setText(translate("customerName"));
        birthDateLabel.setText(translate("customerBirthDate"));
        addressLabel.setText(translate("customerAddress"));

        if(customer != null) {
            nameField.setText(customer.getName());
            birthDateCombobox.setSelectedIndex(room.getFloor() - 1);
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
            Runnable callback;
            errorsContainer.setVisible(false);
            if(room != null) {
                room.update(number, capacity, floor, price);
                callback = () -> {
                    try {
                        model.updateRoom(room);
                    } catch(ApplicationException e) {
                        errors.add(translate("errors.general.unknown"));
                    }
                };

            } else {
                Room newRoom = new Room(number, capacity, floor, price);
                callback = () -> {
                    try {
                        model.createRoom(newRoom);
                    } catch(ApplicationException e) {
                        errors.add(translate("erorrs.general.unknown"));
                    } catch(DuplicateRoomNumberException e) {
                        errors.add(translate("erorrs.room.duplicateNumber"));
                    }
                };
            }
            new CallbackWorker(callback, onSuccess).run();
        } else {
            errorsContainer.setText(String.join("\n", errors));
            errorsContainer.setVisible(true);
        }
}
