package hotel.gui.viewmodel;

import hotel.model.HotelManager;
import hotel.model.Reservation;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CarsTableModel extends AbstractTableModel
{

    private HotelManager manager;

    private List<Reservation> reservations;

    @Override
    public int getRowCount() {
        return cars.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Car car = cars.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return car.getId();
            case 1:
                return car.getRegistrationNumber();
            case 2:
                return car.getType();
            case 3:
                return car.getColor();
            case 4:
                return car.getFuel();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    private List<Reservation> getReservations()
    {
        if(this.reservations == null) {
            this.reservations = manager.findAll();
        }
    }

}