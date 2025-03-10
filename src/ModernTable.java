import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ModernTable extends JTable {
    private Color alternateColor = new Color(240, 240, 240);
    private Color selectedColor = new Color(52, 152, 219);

    public ModernTable(TableModel model) {
        super(model);
        setupTable();
    }

    private void setupTable() {
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setRowHeight(35);
        setFillsViewportHeight(true);

        // Header styling
        JTableHeader header = getTableHeader();
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Selection colors
        setSelectionBackground(selectedColor);
        setSelectionForeground(Color.WHITE);

        // Alternating row colors
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : alternateColor);
                }

                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                c.setFont(new Font("Arial", Font.PLAIN, 13));
                return c;
            }
        });
    }
}