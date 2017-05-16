package hotel.gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.function.Function;

public abstract class BaseModel<T> extends AbstractTableModel
{
    private ArrayList<Function<T, JButton>> buttonFactories = new ArrayList<>();


    private String[] columnNames;
    private Class<?>[] columnTypes;

    public BaseModel(String columnNames[], Class<?> columnTypes[])
    {
        Objects.requireNonNull(columnNames);
        Objects.requireNonNull(columnTypes);
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnIndex < columnNames.length ? columnNames[columnIndex] : "";
    }

    @Override
    public int getColumnCount()
    {
        return columnTypes.length + buttonFactories.size();
    }


    public void addButton(Function<T, JButton> factory)
    {
        this.buttonFactories.add(factory);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (columnIndex >= columnTypes.length && columnIndex < getColumnCount()) {
            return buttonFactories.get(columnIndex - columnTypes.length).apply(getRow(rowIndex));
        }

        return getColumnValue(rowIndex, columnIndex);
    }

    protected abstract Object getColumnValue(int rowIndex, int columnIndex);

    protected abstract T getRow(int rowIndex);

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if (columnIndex >= 0) {
            if (columnIndex < columnTypes.length) {
                return columnTypes[columnIndex];
            }
            if (columnIndex < getColumnCount()) {
                return JButton.class;
            }
        }
        throw new IllegalArgumentException("columnIndex");
    }

}
