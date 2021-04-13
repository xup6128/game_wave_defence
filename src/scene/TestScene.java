package scene;

import camera.Camera;
import gameobj.*;
import internet.server.ClientClass;
import internet.server.CommandReceiver;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestScene extends Scene {
    private Camera cam;
    private ArrayList<Actor> actor;
    private Map map;
    private int num;
    private ArrayList<GameObject> gameObjectArr1 ;
    private ArrayList<GameObject> gameObjectArr ;
    public TestScene(){
    }

    @Override
    public void sceneBegin() {
        map=new Map();
        gameObjectArr = new ArrayList();
        gameObjectArr1 = new ArrayList();
        actor=new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        ArrayList<String> str=new ArrayList<>();
        str.add("200");
        str.add("200");
        System.out.print("輸入0~7決定角色: ");
        int num = sc.nextInt();
        str.add(num+"");
        actor.add(new Actor(Integer.parseInt(str.get(0)),Integer.parseInt(str.get(1)),num));
        ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);
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

            this.gameObjectArr1 = mapLoader.creatObjectArray("grass", 128, test, new MapLoader.CompareClass() {
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
        } catch (IOException ex) {
            Logger.getLogger(TestScene.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Override
    public void sceneEnd() {

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
        cam.end(g);
    }

    @Override
    public void update() {
        actor.get(0).update();
        cam.update();
        for (int i=0;i<gameObjectArr.size();i++){
            if (cam.isCollision(gameObjectArr.get(i))){
                gameObjectArr.get(i).update();
            }
        }
        ArrayList<String> strr=new ArrayList<>();
        strr.add(ClientClass.getInstance().getID()+"");
        strr.add(actor.get(0).painter().centerX()+"");
        strr.add(actor.get(0).painter().centerY()+"");
        strr.add(actor.get(0).getDir()+"");
        ClientClass.getInstance().sent(Global.InternetCommand.MOVE,strr);

        ClientClass.getInstance().consume(new CommandReceiver() {
            @Override
            public void receive(int serialNum, int internetcommand, ArrayList<String> strs) {
                switch(internetcommand){
                    case Global.InternetCommand.CONNECT:
                        System.out.println("Connect " + serialNum);
                        boolean isborn = false;
                        for (int i = 0; i < actor.size(); i++) {
                            if (actor.get(i).getId() == serialNum) {
                                isborn = true;
                                break;
                            }
                        }
                        if(!isborn) {
                            actor.add(new Actor(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2))));
                            System.out.println("!!!!!!!!!!!!!!!!!!!!");
                            actor.get(actor.size() - 1).setId(serialNum);
                            ArrayList<String> str=new ArrayList<>();
                            str.add(actor.get(0).painter().centerX()+"");
                            str.add(actor.get(0).painter().centerY()+"");
                            str.add(actor.get(0).getNum()+"");
                            ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);
                        }
                        break;
                    case Global.InternetCommand.MOVE:
                        for(int i=1;i<actor.size();i++) {
                            if(actor.get(i).getId()==Integer.parseInt(strs.get(0))) {
                               actor.get(i).painter().setCenter(Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                                actor.get(i).collider().setCenter(Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                               actor.get(i).walk(Global.Direction.getDirection(Integer.parseInt(strs.get(3))));
                               break;
                            }
                        }
                        break;
                    case Global.InternetCommand.DISCONNECT:
                        for(int i=0;i<actor.size();i++){
                            if(actor.get(i).getId()==Integer.parseInt(strs.get(0))){
                               actor.remove(i);
                               i--;
                               break;

                            }
                        }
                        break;
                }
            }
        });
    }
    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener(){
            @Override
            public void keyTyped(char c, long trigTime) {

            }
            public void keyPressed(int commandCode, long trigTime) {
                Global.Direction dir=Global.Direction.getDirection(commandCode);
                if(commandCode==6){  //角色斷線時發送斷線訊息
                    ArrayList<String> strs = new ArrayList<String>();
                    strs.add(String.valueOf(ClientClass.getInstance().getID()));
                    ClientClass.getInstance().sent(Global.InternetCommand.DISCONNECT,strs);
                    ClientClass.getInstance().disConnect();
                    System.exit(0);
                }
                actor.get(0).walk(dir);
                switch (dir){
                    case DOWN:
                        for (int i=0;i<gameObjectArr1.size();i++){
                            if (actor.get(0).isCollision(gameObjectArr.get(i))&&
                                    actor.get(0).bottomIsCollision(gameObjectArr.get(i))){
                                actor.get(0).translateY(-1);
                                break;
                            }
                        }
                        for (int i=1;i<actor.size();i++){
                            System.out.println(actor.get(i));
                            if (actor.get(0).isCollision(actor.get(i))&&
                                    actor.get(0).bottomIsCollision(actor.get(i))){
                                System.out.println("DDD");
                                actor.get(0).translateY(-1);
                                break;
                            }
                        }
                        actor.get(0).translateY(1);
                        break;
                    case UP:
                        for (int i=0;i<gameObjectArr1.size();i++){
                            if (actor.get(0).isCollision(gameObjectArr.get(i))&&
                                    actor.get(0).topIsCollision(gameObjectArr.get(i))){
                                actor.get(0).translateY(1);
                                break;
                            }
                        }
                        for (int i=1;i<actor.size();i++){
                            if (actor.get(0).isCollision(actor.get(i))&&
                                    actor.get(0).topIsCollision(actor.get(i))){
                                actor.get(0).translateY(1);
                                break;
                            }
                        }
                        actor.get(0).translateY(-1);
                        break;
                    case LEFT:
                        for (int i=0;i<gameObjectArr1.size();i++){
                            if (actor.get(0).isCollision(gameObjectArr.get(i))
                                    &&actor.get(0).leftIsCollision(gameObjectArr.get(i))){
                                actor.get(0).translateX(1);
                                break;
                            }
                        }
                        for (int i=1;i<actor.size();i++){
                            if (actor.get(0).isCollision(actor.get(i))
                                    &&actor.get(0).leftIsCollision(actor.get(i))){
                                actor.get(0).translateX(1);
                                break;
                            }
                        }
                        actor.get(0).translateX(-1);
                        break;
                    case RIGHT:
                        for (int i=0;i<gameObjectArr1.size();i++){
                            if (actor.get(0).isCollision(gameObjectArr.get(i))&&
                                    actor.get(0).rightIsCollision(gameObjectArr.get(i))){
                                actor.get(0).translateX(-1);
                                break;
                            }
                        }
                        for (int i=1;i<actor.size();i++){
                            if (actor.get(0).isCollision(actor.get(i))&&
                                    actor.get(0).rightIsCollision(actor.get(i))){
                                actor.get(0).translateX(-1);
                                break;
                            }
                        }
                        actor.get(0).translateX(1);
                        break;
                }}

            @Override
            public void keyReleased(int commandCode, long trigTime) {
            }
        };
    }
}
