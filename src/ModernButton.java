import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModernButton extends JButton {
    private Color backgroundColor = new Color(52, 152, 219); // Albastru modern
    private Color hoverColor = new Color(41, 128, 185);      // Albastru închis pentru hover
    private Color textColor = Color.WHITE;
    private boolean isMouseOver = false;

    public ModernButton(String text) {
        super(text);
        setupButton();
    }

    // Adăugăm metodele pentru setarea culorilor
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        setBackground(backgroundColor);
    }

    public void setHoverColor(Color color) {
        this.hoverColor = color;
    }

    @Override
    public void setBackground(Color color) {
        this.backgroundColor = color;
        super.setBackground(color);
    }

    private void setupButton() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(backgroundColor);
        setForeground(textColor);
        setFont(new Font("Arial", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isMouseOver = true;
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isMouseOver = false;
                setBackground(backgroundColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(hoverColor.darker());
        } else if (isMouseOver) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(backgroundColor);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        FontMetrics metrics = g2.getFontMetrics(getFont());
        Rectangle stringBounds = metrics.getStringBounds(getText(), g2).getBounds();

        int textX = (getWidth() - stringBounds.width) / 2;
        int textY = (getHeight() - stringBounds.height) / 2 + metrics.getAscent();

        g2.setColor(textColor);
        g2.setFont(getFont());
        g2.drawString(getText(), textX, textY);
        g2.dispose();
    }
}