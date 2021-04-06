package gameobj;

import controllers.ImageController;

import java.awt.*;

public class TestObject2 extends GameObject {

    private Image img;


    public TestObject2(int x, int y) {
        super(x+128/2, y+128/2, 128, 128);
        this.img = ImageController.getInstance().tryGet("/sand.png");
    }




    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);

    }
}
