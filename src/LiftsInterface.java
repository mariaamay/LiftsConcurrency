import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URI;

public class LiftsInterface extends JPanel {
    final int scale = 15;
    final int maxScreenColumn = 30;
    int maxScreenRow = 52;
    final int screenWidth = scale * maxScreenColumn; // 450
    final int screenHeight = scale * maxScreenRow; // 750

    int MAX_FLOOR;
    private BufferedImage humanImage = null;
    private BufferedImage humanImageFlipped = null;

    public LiftsInterface(int MAX_FLOOR) {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.MAX_FLOOR = MAX_FLOOR;
        this.maxScreenRow = MAX_FLOOR * 5 + 2;
        try {
            this.humanImage = ImageIO.read(new URI("https://raw.githubusercontent.com/mariaamay/LiftsConcurrency/main/images/human.png").toURL());
            this.humanImageFlipped = ImageIO.read(new URI("https://raw.githubusercontent.com/mariaamay/LiftsConcurrency/main/images/humanFlipped.png").toURL());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-8);
        }
    }

    // Get data to update window
    public void repaint(
            int firstLiftFloor,
            int secondLiftFloor,
            int thirdLiftFloor,
            int[] peopleOnFloor,
            int[] peopleInLift,
            int[] peopleOut) {
        this.liftFloors[0] = MAX_FLOOR - firstLiftFloor;
        this.liftFloors[1] = MAX_FLOOR - secondLiftFloor;
        this.liftFloors[2] = MAX_FLOOR - thirdLiftFloor;
        this.peopleOnFloor = peopleOnFloor;
        this.peopleInLift = peopleInLift;
        this.peopleOut = peopleOut;

        super.repaint();
    }

    // Update Window
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw background
        g2.setColor(Color.CYAN);
        g2.fillRect(scale * 20, scale, scale * 10, scale * 50);

        g2.setColor(Color.WHITE);
        g2.fillRect(scale, scale, scale * 17, scale * 50);

        g2.setColor(Color.BLACK);
        g2.fillRect(scale * 6, scale, scale, scale * 50);
        g2.fillRect(scale * 12, scale, scale, scale * 50);

        g2.setColor(Color.WHITE);
        for (int i = 5; i < maxScreenRow; i += 5) {
            g2.fillRect(scale * 20, scale * i, scale * 10, scale);
        }

        // Draw lifts
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(scale, (liftFloors[0] * 5 + 1) * scale, 5 * scale, 5 * scale);
        g2.fillRect(scale * 7, (liftFloors[1] * 5 + 1) * scale, 5 * scale, 5 * scale);
        g2.fillRect(scale * 13, (liftFloors[2] * 5 + 1) * scale, 5 * scale, 5 * scale);
        g2.setColor(Color.WHITE);
        g2.fillRect(scale + scale, (liftFloors[0] * 5 + 1) * scale + scale, 3 * scale, 3 * scale);
        g2.fillRect(scale * 7 + scale, (liftFloors[1] * 5 + 1) * scale + scale, 3 * scale, 3 * scale);
        g2.fillRect(scale * 13 + scale, (liftFloors[2] * 5 + 1) * scale + scale, 3 * scale, 3 * scale);

        // Draw people in lifts
        int liftPositionY = 2 * scale;
        int liftPosititonX = (liftFloors[0] * 5 + 1) * scale + scale;
        if (peopleInLift[0] > 0) {
            g2.drawImage(humanImage, liftPositionY, liftPosititonX, this);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("" + peopleInLift[0], liftPositionY + scale, liftPosititonX);
        }
        if (peopleOut[0] > 0) {
            g2.drawImage(humanImageFlipped, liftPositionY + scale, liftPosititonX, this);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("" + peopleOut[0], liftPositionY + scale * 2, liftPosititonX);
        }

        if (peopleInLift[1] > 0) {
            liftPositionY = scale * 7 + scale;
            liftPosititonX = (liftFloors[1] * 5 + 1) * scale + scale;
            g2.drawImage(humanImage, liftPositionY, liftPosititonX, this);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("" + peopleInLift[1], liftPositionY + scale, liftPosititonX);
        }
        if (peopleOut[1] > 0) {
            g2.drawImage(humanImageFlipped, liftPositionY + scale, liftPosititonX, this);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("" + peopleOut[1], liftPositionY + scale * 2, liftPosititonX);
        }

        if (peopleInLift[2] > 0) {
            liftPositionY = scale * 13 + scale;
            liftPosititonX = (liftFloors[2] * 5 + 1) * scale + scale;
            g2.drawImage(humanImage, liftPositionY, liftPosititonX, this);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("" + peopleInLift[2], liftPositionY + scale, liftPosititonX);
        }
        if (peopleOut[2] > 0) {
            g2.drawImage(humanImageFlipped, liftPositionY + scale, liftPosititonX, this);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("" + peopleOut[2], liftPositionY + scale * 2, liftPosititonX);
        }

        // Draw people on floors
        g2.setColor(new Color(245, 245, 220));
        int peoplePositionY = scale * 20;
        int peoplePositionX = scale * 2;
        for (int floor = MAX_FLOOR; floor >= 1; --floor) {

            for (int i = 0; i < peopleOnFloor[floor]; ++i) {
                g2.drawImage(humanImage, peoplePositionY + scale * i, peoplePositionX, this);
            }

            peoplePositionX += scale * 5;
        }

        g2.dispose();
    }

    private int[] liftFloors = new int[3];
    private int[] peopleOnFloor;
    private int[] peopleInLift;
    private int[] peopleOut;
}