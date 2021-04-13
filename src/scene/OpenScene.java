package scene;

import controllers.ImageController;
import controllers.SceneController;
import internet.server.ClientClass;
import internet.server.Server;
import utils.CommandSolver;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class OpenScene extends Scene {
    private Image image;
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,0,0,null);
    }

    @Override
    public void update() {

    }

    @Override
    public void sceneBegin() {
        image= ImageController.getInstance().tryGet("/aa.jpg");
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                Scanner sc=new Scanner(System.in);
                    if ( commandCode == 0) {
                        Server s=Server.instance();
                        s.create(12345);
                        s.start();
                        try {
                            ClientClass.getInstance().connect("127.0.0.1",12345);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("主機IP：" + Server.instance().getLocalAddress()[0] +
                                "\n主機PORT：" + Server.instance().getLocalAddress()[1]);
                    }else if(commandCode==5){
                        System.out.println("請輸入IP:");
                        String str=sc.next();
                        try {
                            ClientClass.getInstance().connect(str,12345);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
               ArrayList<String> str=new ArrayList<>();
                //______________

                //______________
                SceneController.getInstance().changeScene(new TestScene());

                }
            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }
            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }
}
