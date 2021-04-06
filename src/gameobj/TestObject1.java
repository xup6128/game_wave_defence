package gameobj;

import controllers.ImageController;

import java.awt.*;

public  class TestObject1 extends GameObject {
    private Image img;


    public TestObject1(int x, int y) {
        super(x+128/2, y+128/2, 128, 128);
        this.img = ImageController.getInstance().tryGet("/grass.png");

    }




    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }
}
