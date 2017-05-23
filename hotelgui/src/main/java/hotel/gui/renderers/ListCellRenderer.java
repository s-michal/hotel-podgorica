package hotel.gui.renderers;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Function;

public class ListCellRenderer<T> extends DefaultListCellRenderer
{
    private Function<T, String> formatter;
    private Class<T> type;

    public ListCellRenderer(Function<T, String> formatter, Class<T> type)
    {
        Objects.requireNonNull(formatter);
        Objects.requireNonNull(type);
        this.formatter = formatter;
        this.type = type;
    }

    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        return super.getListCellRendererComponent(
                list,
                type.isInstance(value) ? formatter.apply(type.cast(value)): "unknown",
                index,
                isSelected,
                cellHasFocus
        );
    }

}
