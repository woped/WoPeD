/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl.wfnet;

/**
 *
 * @author Chris
 */
public class Rectangle {

    public int x, y, width, height;

    public Rectangle() {
    }

    public Rectangle(int x, int y, int width, int height) {
        set(x, y, width, height);
    }

    public void set(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void offsetBy(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public int right() {
        return x + width;
    }

    public int bottom() {
        return y + height;
    }

    @Override
    public Rectangle clone() {

        return new Rectangle(x, y, width, height);

    }
}
