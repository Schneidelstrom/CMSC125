package cmsc125.project1.views;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecurityRingPanel extends JPanel {
    private final List<SecurityRing> rings = new ArrayList<>();
    private final Random random = new Random();
    private int shakeIntensity = 0;
    private int currentLives = 7;
    private boolean isCompromised = false;

    public SecurityRingPanel() {
        setOpaque(false); // Allows the background of the parent to show through
        setPreferredSize(new Dimension(300, 300)); // Suggests a starting size

        // Initialize Rings with relative size ratios (1.0 = 90% of panel width)
        // Order: Name, Ratio, Color, PulseFreq
        rings.add(new SecurityRing("Kernel", 0.35, new Color(220, 50, 50), 0.30));
        rings.add(new SecurityRing("Drivers", 0.55, new Color(220, 130, 40), 0.15));
        rings.add(new SecurityRing("Libraries", 0.75, new Color(200, 200, 80), 0.08));
        rings.add(new SecurityRing("Applications", 0.95, new Color(80, 200, 80), 0.04));

        Timer globalTimer = new Timer(20, e -> {
            if (shakeIntensity > 0) shakeIntensity--;

            // Find the first ring from the outside that is still visible and not vaporizing
            int activeIndex = -1;
            for (int i = rings.size() - 1; i >= 0; i--) {
                if (rings.get(i).alpha > 0 && !rings.get(i).isVaporizing) {
                    activeIndex = i;
                    break;
                }
            }

            for (int i = 0; i < rings.size(); i++) {
                rings.get(i).update(i == activeIndex);
            }
            repaint();
        });
        globalTimer.start();
    }

    /**
     * Call this from the Controller when lives reach 0
     */
    public void triggerSystemFailure() {
        this.isCompromised = true;
        this.triggerShake(30); // Heavy final shake
        repaint();
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

        if (isCompromised) {
            drawFailureScreen(g2d);
            return; // Skip drawing rings
        }

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

    private void drawFailureScreen(Graphics2D g2d) {
        g2d.setColor(new Color(150, 0, 0));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int cx = getWidth() / 2;
        int cy = getHeight() / 2 - 50;
        int[] tx = {cx, cx - 60, cx + 60};
        int[] ty = {cy - 60, cy + 40, cy + 40};

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawPolygon(tx, ty, 3);

        g2d.setFont(new Font("Monospaced", Font.BOLD, 60));
        g2d.drawString("!", cx - 15, cy + 25);

        g2d.setFont(new Font("Monospaced", Font.BOLD, 32));
        String msg = "SYSTEM COMPROMISED";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(msg)) / 2;

        g2d.setColor(Color.BLACK);
        g2d.drawString(msg, textX + 3, cy + 103);

        g2d.setColor(Color.WHITE);
        g2d.drawString(msg, textX, cy + 100);

        g2d.setColor(Color.YELLOW);
        for (int i = 0; i < getWidth(); i += 40) {
            g2d.fillRect(i, 0, 20, 15);
            g2d.fillRect(i, getHeight() - 15, 20, 15);
        }
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

        void update(boolean isOutermost) {
            if (isVaporizing) {
                animFrame++;
                if (animFrame < 8) alpha = (animFrame % 2 == 0) ? 1.0f : 0.2f;
                else if (animFrame < 25) { alpha -= 0.07f; scale += 0.05f; }
                else alpha = 0;
            } else {
                if (isOutermost) {
                    // Use a slower base time for all rings to prevent jitter
                    double slowTime = System.currentTimeMillis() / 1000.0;

                    // Adjust these values to find your preferred "speed"
                    // Kernel pulses fast, Apps pulse very slow
                    double speedMultiplier = 1.0 + (5.0 * (1.0 - sizeRatio));

                    // Amplitude: Inner rings pulse harder (max 0.05), outer rings pulse softly (0.01)
                    float maxAmplitude = (float)(0.06 * (1.0 - sizeRatio));

                    scale = 1.0f + (float) Math.sin(slowTime * speedMultiplier) * maxAmplitude;
                } else {
                    // Smoothly return to 1.0 when no longer outermost
                    if (scale > 1.01f) scale -= 0.01f;
                    else if (scale < 0.99f) scale += 0.01f;
                    else scale = 1.0f;
                }
            }
        }

        void draw(Graphics2D g2d, int x, int y, int containerSide) {
            if (alpha <= 0) return;
            int size = (int) (containerSide * sizeRatio * scale);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Draw the main circle
            g2d.setColor(color);
            g2d.fillOval(x - size/2, y - size/2, size, size);

            if (damageLevel > 0) {
                Shape oldClip = g2d.getClip(); // Save current clip
                // Set a circular clip for the cracks
                g2d.setClip(new Ellipse2D.Float(x - size/2, y - size/2, size, size));

                drawCracks(g2d, x, y, size);

                g2d.setClip(oldClip);
            }

            // Draw the border on top
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(x - size/2, y - size/2, size, size);

            int fontSize = Math.max(10, (int)(containerSide * 0.04));
            g2d.setFont(new Font("Monospaced", Font.BOLD, fontSize));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(name, x - (fm.stringWidth(name)/2), y + (size/2) - 15);
        }

        private void drawCracks(Graphics2D g2d, int x, int y, int size) {
            g2d.setColor(new Color(10, 10, 10, 210));
            g2d.setStroke(new BasicStroke(0.7f + (damageLevel * 0.3f)));

            Random crackRand = new Random(name.hashCode());

            // Increase density based on damage, but spread them out
            int numFractures = (damageLevel * 3) + 4;

            for (int i = 0; i < numFractures; i++) {
                // Start anywhere on the outer edge of the specific ring
                double startAngle = crackRand.nextDouble() * Math.PI * 2;
                int xStart = x + (int) (Math.cos(startAngle) * (size / 2.0));
                int yStart = y + (int) (Math.sin(startAngle) * (size / 2.0));

                // Target a point across the ring, not just the center
                // Adding Math.PI +/- 1.0 makes it traverse the ring diagonally
                double targetAngle = startAngle + Math.PI + (crackRand.nextDouble() * 2.0 - 1.0);

                // depth 7 and larger segment lengths make it "stretch"
                generateRecursiveCrack(g2d, xStart, yStart, targetAngle, 7, size / 6, crackRand);
            }
        }

        private void generateRecursiveCrack(Graphics2D g2d, int x, int y, double angle, int depth, int length, Random rand) {
            if (depth <= 0 || length < 2) return;

            // "Wander" factor: allows the line to zig-zag more aggressively
            double wander = (rand.nextDouble() * 0.8 - 0.4);
            double currentAngle = angle + wander;

            int xEnd = x + (int) (Math.cos(currentAngle) * length);
            int yEnd = y + (int) (Math.sin(currentAngle) * length);

            g2d.draw(new Line2D.Float(x, y, xEnd, yEnd));

            // Continuation: 85% of length makes it traverse further
            generateRecursiveCrack(g2d, xEnd, yEnd, currentAngle, depth - 1, (int)(length * 0.85), rand);

            // Secondary Branching: lower length but higher spread
            if (rand.nextDouble() < 0.5) {
                double branchAngle = currentAngle + (rand.nextBoolean() ? 0.7 : -0.7);
                generateRecursiveCrack(g2d, xEnd, yEnd, branchAngle, depth - 2, (int)(length * 0.6), rand);
            }
        }
    }
}