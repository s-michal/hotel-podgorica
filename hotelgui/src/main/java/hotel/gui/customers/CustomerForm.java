package hotel.gui.customers;

import hotel.gui.BaseView;
import hotel.model.Customer;
import hotel.model.exceptions.ApplicationException;
import hotel.utils.DateUtils;
import hotel.workers.CallbackWorker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class CustomerForm extends BaseView
{
    private JPanel panel;
    private JTextField nameField;
    private JLabel nameLabel;
    private JLabel birthDateLabel;
    private JLabel addressLabel;
    private JTextField addressField;
    private JButton submitButton;
    private JTextPane errorsContainer;
    private JDatePickerImpl birthDateField;

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
            ((UtilDateModel)birthDateField.getModel()).setValue(DateUtils.asDate(customer.getBirthDate()));
            addressField.setText(customer.getAddress());
        }

        submitButton.setText(translate("button.createCustomer"));
        submitButton.addActionListener(e -> createOrUpdate(customer));
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

    private void createOrUpdate(Customer customer)
    {
        ArrayList<String> errors = new ArrayList<>();

        String name = nameField.getText();
        String address = addressField.getText();
        LocalDate birthDate = DateUtils.getDatePickerValue(birthDateField);

        if(name == null || name.equals("")) {
            errors.add(translate("errors.customer.emptyName"));
        }

        if(address == null || address.equals("")) {
            errors.add(translate("errors.customer.emptyAddress"));
        }

        if(birthDate == null || ! birthDate.isBefore(LocalDate.now())) {
            errors.add(translate("errors.customer.invalidBirthDate"));
        }

        if(errors.isEmpty()) {
            Runnable callback;
            errorsContainer.setVisible(false);
            if(customer != null) {
                customer.update(name, address, birthDate);
                callback = () -> {
                    try {
                        model.updateCustomer(customer);
                    } catch(ApplicationException e) {
                        errors.add(translate("errors.general.unknown"));
                    }
                };

            } else {
                Customer newCustomer = new Customer(name, address, birthDate);
                callback = () -> {
                    try {
                        model.createCustomer(newCustomer);
                    } catch(ApplicationException e) {
                        errors.add(translate("erorrs.general.unknown"));
                    }
                };
            }
            new CallbackWorker(callback, onSuccess).run();
        } else {
            errorsContainer.setText(String.join("\n", errors));
            errorsContainer.setVisible(true);
        }
    }

    private void createUIComponents()
    {
        birthDateField = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
    }


}
