package hotel.gui.reservations;

import hotel.gui.BaseView;
import hotel.gui.cellRenderers.ButtonMouseListener;
import hotel.gui.cellRenderers.ButtonRenderer;
import hotel.model.HotelManager;
import hotel.model.exceptions.ReservationCannotBeCanceledNow;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.*;
import java.time.LocalDate;
import java.util.Objects;

public class ReservationList extends BaseView
{
    private JPanel panel;
    private JTable table;
    private JButton createButton;

    public ReservationList(HotelManager manager)
    {
        Objects.requireNonNull(manager);
        ReservationsModel model = new ReservationsModel(manager);

        LocalDate now = LocalDate.now();

        model.addButton(reservation -> {
            JButton button = new JButton(translate("button.cancel"));

            if(reservation.isCanceled() || now.isAfter(reservation.getSince())) {
                button.setEnabled(false);
                return button;
            }

            button.addActionListener(e -> {
                try {
                    model.cancelReservation(reservation);
                } catch (ReservationCannotBeCanceledNow ex) {
                    showMessageDialog(null, "error.reservation.passed");
                }
            });
            return button;
        });
        table.setModel(model);
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.addMouseListener(new ButtonMouseListener(table));
        createButton.setText(translate("reservation.button.place"));
    }

    public JPanel getPanel()
    {
        return panel;
    }

}
