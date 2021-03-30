import java.awt.*;
import java.awt.event.MouseEvent;
//單例模式的控場類
public class SceneController implements GameKernel.UpdateInterface,GameKernel.PaintInterface,CommandSolver.MouseListener,CommandSolver.KeyListener{
    private Scene currenteScene; //內含一個當前場景
    private static SceneController sceneController;//單例模式的控場靜態實體
    //建構子不讓別人用!
    private SceneController(){}
    //外界要取得實體只能透過這個靜態方法
    public static SceneController getInstance(){
        if(sceneController==null){ //假如一開始沒有實體
            sceneController=new SceneController(); //就給他實體
        }
        return sceneController;
    }

    //換場機制
    public void changeScene(Scene scene){
        if(this.currenteScene!=null){
            currenteScene.sceneEnd();
        }
        if(scene!=null){
            scene.sceneBegin(); //傳進來的場景初始化
            currenteScene=scene; //並換成當前場景
        }
    }
    @Override
    public void keyPressed(int commandCode, long trigTime) {
        if(currenteScene!=null && currenteScene.keyListener()!=null){
            currenteScene.keyListener().keyPressed(commandCode,trigTime);
        }
    }
    @Override
    public void keyReleased(int commandCode, long trigTime) {
        if(currenteScene!=null &&currenteScene.keyListener()!=null){
            currenteScene.keyListener().keyReleased(commandCode,trigTime);
        }
    }
    @Override
    public void keyTyped(char c, long trigTime) {
        if(currenteScene!=null && currenteScene.keyListener()!=null){
            currenteScene.keyListener().keyTyped(c,trigTime);
        }
    }
    @Override
    public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if(currenteScene!=null && currenteScene.mouseListener()!=null){
            currenteScene.mouseListener().mouseTrig(e,state,trigTime);
        }
    }
    @Override
    public void paint(Graphics g) {
        if(currenteScene!=null){
            currenteScene.paint(g);
        }

    }
    @Override
    public void update() {
        if(currenteScene!=null){
            currenteScene.update();
        }

    }
}
