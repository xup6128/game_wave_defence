package Gameobj;

import controllers.ImgController;

import java.awt.*;

public class TestObject1 extends GameObject {
    private Image img;


    public TestObject1(int x, int y) {
        super(x, y, 44, 48);
        this.img = ImgController.getImgController().tryGet("/p1.png");

    }


    @Override
    public void paint(Graphics g) {
        g.drawImage(this.img, getX(), getY(), null);
    }

    @Override
    public void update() {

    }
}