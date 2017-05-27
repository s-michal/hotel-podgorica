package hotel.gui.customers;

import hotel.gui.BaseView;
import hotel.gui.renderers.ButtonMouseListener;
import hotel.gui.renderers.ButtonRenderer;
import hotel.gui.rooms.RoomForm;
import hotel.model.*;
import hotel.model.exceptions.CustomerHasReservationsException;
import hotel.workers.CallbackWorker;
import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class CustomerList extends BaseView
{

    private JPanel topPanel;
    private JTable table;
    private JButton createNewCustomerButton;

    private CustomersModel model;

    public CustomerList(CustomerManager manager, HotelManager hotelManager)
    {
        Objects.requireNonNull(manager);
        Objects.requireNonNull(hotelManager);
        model = new CustomersModel(manager, hotelManager);

        model.addButton(this::createUpdateButton);
        model.addButton(this::createDeleteButton);

        table.setModel(model);
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.addMouseListener(new ButtonMouseListener(table));

        createNewCustomerButton.setText(translate("button.createNewCustomer"));
        createNewCustomerButton.addActionListener(e -> openFormFrame(null));
    }

    public JPanel getPanel()
    {
        return topPanel;
    }

    private void openFormFrame(Customer customer)
    {
        JFrame frame = new JFrame(translate("titles.newCustomer"));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        CustomerForm view = new CustomerForm(model, customer);

        view.onSuccess(() -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        frame.setContentPane(view.getPanel());

        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setVisible(true);
    }

    private JButton createUpdateButton(Customer customer)
    {
        JButton button = new JButton(translate("button.update"));
        button.addActionListener(e -> openFormFrame(customer));
        return button;
    }

    private JButton createDeleteButton(Customer customer)
    {
        JButton button = new JButton(translate("button.delete"));
        if (model.hasReservations(customer)) {
            button.setEnabled(false);
            return button;
        }

        button.addActionListener(x ->
                new CallbackWorker(() -> {
                    try {
                        model.deleteCustomer(customer);
                        return true;
                    } catch(CustomerHasReservationsException e) {
                        showMessageDialog(null, translate("errors.customer.hasReservations"));
                        return false;
                    }
                }, null).run()

        );

        return button;
    }

}
