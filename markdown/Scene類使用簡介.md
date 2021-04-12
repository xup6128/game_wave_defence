# Scene使用簡介

為場景抽象類-->
  1.implements兩個GameKernel的介面，並將實作推遲給子類場景實現
  2.還包含兩個抽象方法給子類實現-->**場景初始化**(類似建構子的初始化)和**場景結束**(釋放資源用)

```
public abstract class Scene implements GameKernel.PaintInterface,GameKernel.UpdateInterface{
    public abstract void sceneBegin();
    public abstract void sceneEnd();
    
    //因為滑鼠和鍵盤每個場景依據自己的需求才會用到，所以做成抽象方法給子場景決定是否要監聽滑鼠和鍵盤；
    //假如子類不要，就空方法體即可
    public abstract CommandSolver.MouseListener mouseListener();
    public abstract CommandSolver.KeyListener keyListener();

}
```

## 使用範例:

假設今天有一個開場(Scene的子類OpenScene)，需求如下:

1.有一張背景圖片

2.讓使用者可以按下不同按鍵，做出不同回應，如下:

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/7f10da9e-11de-4e0c-acc7-1f5c0461348e/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T063112Z&X-Amz-Expires=86400&X-Amz-Signature=3efd0169adb649299bef35055f43f1228071035911661be5f6909e8485ddcf41&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)



```java
public class OpenScene extends Scene {
    private Image image; //背景圖片
    @Override
    public void sceneBegin() {  //在場景開始的時候使用ImImageController建立背景圖片
        image= ImageController.getInstance().tryGet("/aa.jpg");
    }

    @Override
    public void sceneEnd() {
		//若無使用到，空方法體即可
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,0,0,null); //畫出背景圖片
    }

    @Override
    public void update() {
		//若無使用到，空方法體即可
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null; //若無滑鼠監聽，return null即可
    }

    @Override
    public CommandSolver.KeyListener keyListener() {  //監聽鍵盤按鍵
        return new CommandSolver.KeyListener() {     //實作CommandSolver.KeyListener()並return
            @Override
            public void keyPressed(int commandCode, long trigTime) { //當按下鍵盤按鍵時
                Scanner sc=new Scanner(System.in);
                    if ( commandCode == 0) {  //commandCode==0時為建立連線
                       //以下自行撰寫實現的邏輯
                    }
                }
            @Override
            public void keyReleased(int commandCode, long trigTime) {
				//若無使用到，空方法體即可
            }
            @Override
            public void keyTyped(char c, long trigTime) {
				//若無使用到，空方法體即可
            }
        };
    }
}
```