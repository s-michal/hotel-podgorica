package hotel.gui.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by fmasa on 16.5.17.
 */
public class PriceCellRenderer extends DefaultTableCellRenderer
{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setText(value + " " + Currency.getInstance("CZK").getSymbol());
        return this;
    }

}
