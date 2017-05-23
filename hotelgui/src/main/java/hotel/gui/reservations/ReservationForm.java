package hotel.gui.reservations;

import hotel.gui.BaseView;
import hotel.gui.renderers.ListCellRenderer;
import hotel.utils.DateUtils;
import hotel.workers.CallbackWorker;
import hotel.model.*;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class ReservationForm extends BaseView
{

    private ReservationsModel model;

    private Runnable onSuccess;
    private JComboBox<Customer> customerCombobox;
    private JLabel customerLabel;
    private JLabel roomLabel;
    private JComboBox<Room> roomCombobox;
    private JLabel sinceLabel;
    private JLabel untilLabel;
    private JPanel panel;
    private JButton submitButton;
    private JTextPane errorsContainer;
    private JDatePickerImpl sinceDatePicker;
    private JDatePickerImpl untilDatePicker;

    private static Logger logger = LoggerFactory.getLogger(ReservationForm.class);

    ReservationForm(ReservationsModel model, RoomManager roomManager, CustomerManager customerManager)
    {
        Objects.requireNonNull(model);
        Objects.requireNonNull(roomManager);
        Objects.requireNonNull(customerManager);
        this.model = model;


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

    private Vector<LocalDate> getDates(int daysCount)
    {
        Vector<LocalDate> dates = new Vector<>(daysCount);

        LocalDate today = LocalDate.now();

        for(int i = 0; i < daysCount; i++) {
            dates.add(today.plusDays(i));
        }

        return dates;
    }

    private void create()
    {
        ArrayList<String> errors = new ArrayList<>();

        Room room = Room.class.cast(roomCombobox.getSelectedItem());
        Customer customer = Customer.class.cast(customerCombobox.getSelectedItem());

        LocalDate since = DateUtils.getDatePickerValue(sinceDatePicker);
        LocalDate until = DateUtils.getDatePickerValue(untilDatePicker);

        if(since != null && until != null && until.isAfter(since)) {
            logger.info("Creating reservation...");
            new CallbackWorker(() -> model.placeReservation(new Reservation(room, customer, since, until)), onSuccess).run();
        } else {
            errors.add("errors.reservation.sinceAfterUntil");
            logger.error("Reservation wasn't created");
        }

        errorsContainer.setText(String.join("\n", errors));
    }

    private void createUIComponents()
    {
        sinceDatePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
        untilDatePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()));
    }

}