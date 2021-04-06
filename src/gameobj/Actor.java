package gameobj;

import controllers.ImageController;
import utils.Delay;
import utils.Global;

import java.awt.*;

public class Actor extends GameObject{
    private int ID;
    private int num; //要哪一隻角色
    private ActorAnimal actorAnimal;
    private Global.Direction dir;
    public Actor(int x, int y,int num) {
        super(x+Global.UNIT_X/2, y+Global.UNIT_Y/2, Global.UNIT_X, Global.UNIT_Y);
        this.num=num;
        actorAnimal=new ActorAnimal();
        dir= Global.Direction.NO_DIR;

    }
    public void setId(int id){
        this.ID=id;
    }
    public int getId(){
        return this.ID;
    }
    public int getNum(){
        return this.num;
    }
    public void walk(Global.Direction dir){
        this.dir=dir;
    }

    @Override
    public void paintComponent(Graphics g) {
        actorAnimal.paintComponent(g,num,painter().left(), painter().top(),painter().right(),painter().bottom(),dir);
        dir= Global.Direction.NO_DIR;
    }

    @Override
    public void update() {

    }
    private static class ActorAnimal{
        private int count;
        private Delay delay;
        private Image img;
        private static final int[] ACTOR_WALK={0,1,2,1};
        private Global.Direction dir;

        private ActorAnimal(){
            img= ImageController.getInstance().tryGet("/Actor1.png");
            delay=new Delay(5);
            delay.loop();
            dir= Global.Direction.NO_DIR;
        }
        public void paintComponent(Graphics g,int num,int left,int top,int right,int bottom,Global.Direction dir) {
            if (dir== Global.Direction.NO_DIR){
                count=1;
            }else {
                this.dir=dir;
                if (delay.count()){
                    count=++count%4;
                }
            }
            int tx=num%4*Global.UNIT_X*3;
            int ty=num/4*Global.UNIT_Y*4+this.dir.getValue()*Global.UNIT_Y;

            g.drawImage(img,left,top,right,bottom, tx+Global.UNIT_X*ACTOR_WALK[count],
                    ty, tx+Global.UNIT_X*ACTOR_WALK[count]+Global.UNIT_X, ty+Global.UNIT_Y,null);
        }

    }
}
