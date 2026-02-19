package cmsc125.project1.views;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecurityRingPanel extends JPanel {
    private final List<SecurityRing> rings = new ArrayList<>();
    private final Random random = new Random();
    private int shakeIntensity = 0;
    private int currentLives = 7;

    public SecurityRingPanel() {
        setOpaque(false); // Allows the background of the parent to show through
        setPreferredSize(new Dimension(300, 300)); // Suggests a starting size

        // Initialize Rings with relative size ratios (1.0 = 90% of panel width)
        // Order: Name, Ratio, Color, PulseFreq
        rings.add(new SecurityRing("Kernel", 0.35, new Color(220, 50, 50), 0.12));
        rings.add(new SecurityRing("Drivers", 0.55, new Color(220, 130, 40), 0.08));
        rings.add(new SecurityRing("Libraries", 0.75, new Color(200, 200, 80), 0.05));
        rings.add(new SecurityRing("Applications", 0.95, new Color(80, 200, 80), 0.03));

        Timer globalTimer = new Timer(20, e -> {
            if (shakeIntensity > 0) shakeIntensity--;
            for (SecurityRing ring : rings) ring.update();
            repaint();
        });
        globalTimer.start();
    }

    /**
     * Controller helper: Updates lives and triggers effects.
     * Logic: Apps (7->6), Libs (6->5), Drivers (5,4->3), Kernel (3,2,1->0)
     */
    public void updateStatus(int lives) {
        if (lives < this.currentLives) triggerShake(15);
        this.currentLives = lives;

        // Apps disappears if lives < 7
        rings.get(3).checkState(lives < 7, 0);
        // Libs disappears if lives < 6
        rings.get(2).checkState(lives < 6, 0);
        // Drivers disappears if lives < 4. Cracks if lives == 4.
        rings.get(1).checkState(lives < 4, (lives == 4) ? 2 : 0);
        // Kernel disappears if lives < 1. Cracks increase at 2 and 1.
        rings.get(0).checkState(lives < 1, (lives == 2) ? 2 : (lives == 1) ? 4 : 0);
    }

    public void triggerShake(int intensity) { this.shakeIntensity = intensity; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dynamic scaling: find the smallest dimension to keep rings circular
        int side = Math.min(getWidth(), getHeight());
        int ox = (shakeIntensity > 0) ? random.nextInt(shakeIntensity) - (shakeIntensity / 2) : 0;
        int oy = (shakeIntensity > 0) ? random.nextInt(shakeIntensity) - (shakeIntensity / 2) : 0;

        int cx = (getWidth() / 2) + ox;
        int cy = (getHeight() / 2) + oy;

        // Draw Shield (Blue glow) if health is full
        if (currentLives == 7) drawShield(g2d, cx, cy, (int)(side * 0.98));

        // Draw from Outside (Apps) to Inside (Kernel)
        for (int i = rings.size() - 1; i >= 0; i--) {
            rings.get(i).draw(g2d, cx, cy, side);
        }
    }

    private void drawShield(Graphics2D g2d, int x, int y, int size) {
        g2d.setColor(new Color(100, 200, 255, 100));
        g2d.setStroke(new BasicStroke(5f));
        float pulse = (float) Math.sin(System.currentTimeMillis() / 300.0) * 5;
        g2d.drawOval(x - (size/2) - (int)pulse, y - (size/2) - (int)pulse, size + (int)pulse*2, size + (int)pulse*2);
    }

    private class SecurityRing {
        String name;
        double sizeRatio;
        Color color;
        double pulseFreq;
        float alpha = 1.0f, scale = 1.0f;
        boolean isVaporizing = false;
        int animFrame = 0, damageLevel = 0;

        SecurityRing(String name, double ratio, Color color, double freq) {
            this.name = name; this.sizeRatio = ratio; this.color = color; this.pulseFreq = freq;
        }

        void checkState(boolean shouldDie, int damage) {
            if (shouldDie && !isVaporizing) isVaporizing = true;
            this.damageLevel = damage;
        }

        void update() {
            if (isVaporizing) {
                animFrame++;
                if (animFrame < 8) alpha = (animFrame % 2 == 0) ? 1.0f : 0.2f;
                else if (animFrame < 25) { alpha -= 0.07f; scale += 0.05f; }
                else alpha = 0;
            } else {
                double time = System.currentTimeMillis() * pulseFreq;
                scale = 1.0f + (float) Math.sin(time / 50.0) * 0.02f;
            }
        }

        void draw(Graphics2D g2d, int x, int y, int containerSide) {
            if (alpha <= 0) return;
            int size = (int) (containerSide * sizeRatio * scale);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            g2d.setColor(color);
            g2d.fillOval(x - size/2, y - size/2, size, size);

            if (damageLevel > 0) drawCracks(g2d, x, y, size);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(x - size/2, y - size/2, size, size);

            g2d.setFont(new Font("SansSerif", Font.BOLD, (int)(containerSide * 0.04)));
            g2d.drawString(name, x - (size/5), y + (size/2) - 10);
        }

        private void drawCracks(Graphics2D g2d, int x, int y, int size) {
            g2d.setColor(new Color(0, 0, 0, 180));
            Random crackRand = new Random(name.hashCode());
            for (int i = 0; i < damageLevel * 6; i++) {
                double ang = crackRand.nextDouble() * Math.PI * 2;
                double d = crackRand.nextDouble() * (size / 2.2);
                int x1 = x + (int)(Math.cos(ang) * d);
                int y1 = y + (int)(Math.sin(ang) * d);
                g2d.draw(new Line2D.Float(x1, y1, x1 + crackRand.nextInt(20)-10, y1 + crackRand.nextInt(20)-10));
            }
        }
    }
}