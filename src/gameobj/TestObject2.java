package Gameobj;

import controllers.ImgController;

import java.awt.*;

public class TestObject2 extends GameObject {

    private Image img;


    public TestObject2(int x, int y) {
        super(x, y, 44, 48);
        this.img = ImgController.getImgController().tryGet("/p2.png");

    }


    @Override
    public void paint(Graphics g) {
        g.drawImage(this.img, getX(), getY(), null);
    }

    @Override
    public void update() {

    }

}
