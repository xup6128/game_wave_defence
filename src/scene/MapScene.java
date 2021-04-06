package scene;

import camera.Camera;
import gameobj.*;
import internet.server.ClientClass;
import internet.server.CommandReceiver;
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
    private ArrayList<Actor> actor;
    private Map map;

    private ArrayList<GameObject> gameObjectArr ;

    @Override
    public void sceneBegin() {
        map=new Map();
        gameObjectArr = new ArrayList();
        actor=new ArrayList<>();
        actor.add(new Actor(100,100,0));
        actor.get(0).setId(ClientClass.getInstance().getID());
        cam= new Camera.Builder(1000,1000).setChaseObj(actor.get(0),1,1)
                .setCameraStartLocation(actor.get(0).painter().left(),actor.get(0).painter().top()).gen();

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
                    actor.get(0).walk(dir);
                switch (dir){
                    case DOWN:
                        actor.get(0).translateY(1);
                        break;
                    case UP:
                        actor.get(0).translateY(-1);
                        break;
                    case LEFT:
                        actor.get(0).translateX(-1);
                        break;
                    case RIGHT:
                        actor.get(0).translateX(1);
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
        for(int i=0;i<actor.size();i++){
            actor.get(i).paint(g);
        }
//        this.actor.get(0).paint(g); //自己決角色
        cam.end(g);
    }

    @Override
    public void update() {
        System.out.println(actor.size());
        cam.update();
        for (int i=0;i<gameObjectArr.size();i++){
            if (cam.isCollision(gameObjectArr.get(i))){
                gameObjectArr.get(i).update();
            }
        }
        ClientClass.getInstance().consume(new CommandReceiver() {
            @Override
            public void receive(int serialNum, int internetcommand, ArrayList<String> strs) {
                switch(internetcommand){
                    case Global.InternetCommand.CONNECT:
                        boolean isburn = false;
                        for (int i = 0; i < actor.size(); i++) {
                            if (actor.get(i).getId() == serialNum) {
                                isburn = true;
                                break;
                            }
                        }
                        if(!isburn) {
                            System.out.println("!!!!!");
                            actor.add(new Actor(100, 100, 0));
                            actor.get(actor.size() - 1).setId(serialNum);
                        }
                        ArrayList<String> str=new ArrayList<>();
                        str.add(actor.get(0).collider().centerX()+""); //傳我們自己的座標出去
                        str.add(actor.get(0).collider().centerY()+"");
                        ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);
                        break;
                    case Global.InternetCommand.MOVE:
                        ArrayList<String> str1=new ArrayList<>();
                        for(int i=0;i<actor.size();i++) {
                            if(actor.get(i).getId()==serialNum) {
                                str1.add(actor.get(0).collider().centerX() + ""); //傳我們自己的座標出去
                                str1.add(actor.get(0).collider().centerY() + "");
                                ClientClass.getInstance().sent(Global.InternetCommand.MOVE, str1);
                            }
                        }
                        break;
                }
            }
        });
        this.actor.get(0).update();
    }
}
