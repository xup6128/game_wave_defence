package gameobj;

import camera.MapInformation;
import controllers.ImageController;

import java.awt.*;

public class Map extends GameObject{
    private Image img;
    public Map() {
        super(640, 640, 1280, 1280);
        img= ImageController.getInstance().tryGet("/genMap.bmp");
        MapInformation.setMapInfo(0,0, 1280,1280); //地圖大小(自行調整)
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img,painter().left(), painter().top(),1280,1280,null);
    }

    @Override
    public void update() {

    }
}
