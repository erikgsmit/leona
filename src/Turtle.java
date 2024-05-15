// Author: Erik Smit & Hugo Larsson Wilhelmsson

import java.util.ArrayList;
import java.util.List;

/*
 * The turtle class is responsible for keeping track of the turtle's position
 * and
 * the lines it draws
 */
public class Turtle {

    // Initiate startvalues
    private boolean isPenDown = false;
    private double x = 0;
    private double y = 0;
    private double degree = 0;
    private String color = "#0000FF";

    private List<String> lines;

    // Constructor
    public Turtle() {
        this.lines = new ArrayList<>();
    }

    /*
     * Move the turtle forward by a certain distance, back is negative
     * 
     * @param dist
     */
    public void forward(int dist) {
        double newX = x + dist * Math.cos(Math.toRadians(degree));
        double newY = y + dist * Math.sin(Math.toRadians(degree));

        // X and Y sometimes assume negative zero, handle this special case
        newX = isNegativeZero(newX);
        newY = isNegativeZero(newY);

        // Print the old and new coordinates if pen is down
        if (isPenDown) {
            lines.add(String.format("%s %.4f %.4f %.4f %.4f", color, x, y, newX, newY));
        }

        x = newX;
        y = newY;
    }

    // Move if negative
    public void backward(int dist) {
        forward(-dist);
    }

    // Rotate right
    public void right(int degrees) {
        degree = (degree - degrees + 360) % 360;
    }

    // Rotate left
    public void left(int degrees) {
        degree = (degree + degrees) % 360;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void penUp() {
        isPenDown = false;
    }

    public void penDown() {
        isPenDown = true;
    }

    public void update() {
        // Print all lines stored in the lines list
        for (String l : lines) {
            System.out.println(l);
        }
    }

    // Return 0 if the distance from zero (abs(coord)) is less than 1e-10 (very
    // small), otherwise return coord
    public double isNegativeZero(double coord) {
        if (Math.abs(coord) < 1e-10)
            return 0.0;
        else
            return coord;
    }

}
