package scene;

import camera.Camera;
import gameobj.*;
import internet.server.ClientClass;
import internet.server.Server;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapScene extends Scene {
    private Camera cam;
    private Actor actor;
    private Map map;

    private ArrayList<GameObject> gameObjectArr ;

    @Override
    public void sceneBegin() {
        map=new Map();
        gameObjectArr = new ArrayList();
        actor=new Actor(200, 200,5);
        cam= new Camera.Builder(1000,1000).setChaseObj(actor,1,1)
                .setCameraStartLocation(actor.painter().left(),actor.painter().top()).gen();

        try {
            MapLoader mapLoader = new MapLoader("/genMap.bmp", "/genMap.txt");
            ArrayList<MapInfo> test = mapLoader.combineInfo();
            this.gameObjectArr = mapLoader.creatObjectArray("grass", 128, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new TestObject1(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
            );
            this.gameObjectArr.addAll(mapLoader.creatObjectArray("sand", 128, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new TestObject2(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            /*this.gameObjectArr.addAll(mapLoader.creatObjectArray("P3", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new TestObject3(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
                    )
            );
            this.gameObjectArr.addAll(mapLoader.creatObjectArray("P3", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new TestObject3(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
                    )
            );*/
//            for (int i = 0; i < test.size(); i++) {    //  這邊可以看array內容  {String name ,int x, int y, int xSize, int ySize}
//                System.out.println(test.get(i).getName());
//                System.out.println(test.get(i).getX());
//                System.out.println(test.get(i).getY());
//                System.out.println(test.get(i).getSizeX());
//                System.out.println(test.get(i).getSizeY());
//            }
            this.gameObjectArr.forEach(a -> System.out.println(a.getClass().getSimpleName()));
        } catch (IOException ex) {
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }
//        Server.instance().create(12345); //建立連接埠
//        Server.instance().start();
//        System.out.println(Server.instance().getLocalAddress()[0]); //印出host IP
//        try{
////            //連接("host IP:127.0.0.1", port)
////            ClientClass.getInstance().connect("127.0.0.1", 12345);
//        }catch(IOException ex){
//            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
    @Override
    public void sceneEnd() {

    }
    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener(){

            @Override
            public void keyTyped(char c, long trigTime) {

            }

            @Override
            public void keyPressed(int commandCode, long trigTime) {
                Global.Direction dir=Global.Direction.getDirection(commandCode);
                    actor.walk(dir);
                switch (dir){
                    case DOWN:
                        actor.translateY(1);
                        break;
                    case UP:
                        actor.translateY(-1);
                        break;
                    case LEFT:
                        actor.translateX(-1);
                        break;
                    case RIGHT:
                        actor.translateX(1);
                        break;
                }

            }
            @Override
            public void keyReleased(int commandCode, long trigTime) {
            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        cam.start(g);
        for (int i=0;i<gameObjectArr.size();i++){
            gameObjectArr.get(i).paint(g);
        }
        this.actor.paint(g);
        cam.end(g);
    }

    @Override
    public void update() {
        cam.update();
        for (int i=0;i<gameObjectArr.size();i++){
            if (cam.isCollision(gameObjectArr.get(i))){
                gameObjectArr.get(i).update();
            }
        }
        this.actor.update();

    }
}
