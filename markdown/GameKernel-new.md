# 1 GameKernel

GameKernel使用建造者模式來創建

### 1-1 GameKernel的Builder有五個設定初始化的方法:

```java
		//分別為:
		public Builder paint(final PaintInterface paintInterface) { //可以傳入paint介面，設置paint介面
            this.paintInterface = paintInterface;
            return this;
        }

        public Builder update(final UpdateInterface updateInterface) { //可以傳入update介面，設置update介面
            this.updateInterface = updateInterface;
            return this;
        }

        public Builder fps(final int framePerSec) { //可以傳入fps，設定fps
            this.framePerSec = framePerSec;
            return this;
        }

        public Builder ups(final int updatesPerSec) { //可以傳入ups，設定ups
            this.updateInterface = updateInterface;
            return this;
        }

        public Builder input(final CommandSolver.BuildStream buildStream) { //可以傳入CommandSolver
            this.buildStream = buildStream;
            return this;
        }
		//使用方式詳見範例
```

### 1-2 產生GameKernel的方法-gen()

```java
//使用gen方法產生遊戲核心物件
GameKernel gameKernel = new GameKernel.Builder().gen();  //gen方法用在最後產生遊戲核心物件
```

### 1-3 實際使用範例:

範例需求: 
				1.遊戲核心要有input功能(以在遊戲場景中監聽鍵盤和滑鼠)

​				2.需要paint和update介面

```java
//創建遊戲核心    
GameKernel gameKernel = new GameKernel.Builder().     //使用建造者模式對遊戲核心進行初始化
   		input( new CommandSolver.BuildStream().mouseTrack().subscribe(sceneController).  //input傳入CommandSolver的滑鼠和鍵盤監聽
                   keyboardTrack().next().subscribe(sceneController)
            ).paint(sceneController).update(sceneController).gen();  //初始化paint和update介面，最後gen產生遊戲核心物件
```

# 2 CommandSolver

可以用來監聽滑鼠和鍵盤的工具。

## 2-1 使用方式

CommandSolver使用建造者模式創建物件。所以在使用時要使用其內部類**BuildStream**。

## 2-2 監聽滑鼠-mouseTrack()

假如要在某個場景中監聽滑鼠，使用範例如下:

```java
GameKernel gameKernel = new GameKernel.Builder().input(  //在GameKernel的input中
    
				//使用CommandSolver.BuildStream()的mouseTrack()，並subscribe場景
                new CommandSolver.BuildStream().mouseTrack().subscribe(sceneController)
    
        ).paint(sceneController).update(sceneController).gen();
```

## 2-3 監聽鍵盤-keyboardTrack()

注意，keyboardTrack()後要接一個next()後再訂閱場景。

```java
GameKernel gameKernel = new GameKernel.Builder().input(  //在GameKernel的input中
    
				//使用CommandSolver.BuildStream()的keyboardTrack().next()，並subscribe場景
                new CommandSolver.BuildStream().keyboardTrack().next().subscribe(sceneController)
                
        ).paint(sceneController).update(sceneController).gen();
```

#### 2-3-1加入鍵盤按鍵設定

使用keyboardTrack()的**add方法**，就可以設置按鍵對應的指令碼，操作範例如下:

```java
//例如，設置Enter鍵的指令為1
GameKernel gameKernel = new GameKernel.Builder().input(  //在GameKernel的input中
				//使用CommandSolver.BuildStream()的keyboardTrack()
                new CommandSolver.BuildStream().keyboardTrack().add(KeyEvent.VK_ENTER, 0) //設置ENTER按鍵為 -1
                        .next().subscribe(sceneController)                                //add方法參數包含:
																						   //1.從KeyEvent中選擇對應的電腦按鍵
        ).paint(sceneController).update(sceneController).gen();                            //2.自定義對應的commandCode
```

#### 2-3-2按鍵指令碼的使用方式

例如要設置按下Enter後，程式會做出反應:

```java
 @Override
    public CommandSolver.KeyListener keyListener() {  //監聽鍵盤按鍵
        return new CommandSolver.KeyListener() {     //實作CommandSolver.KeyListener()並return
            @Override
            public void keyPressed(int commandCode, long trigTime) { //當按下鍵盤按鍵時
                Scanner sc=new Scanner(System.in);
                    if ( commandCode == 0) {  //我們前面將Enter按鍵的commandCode設置為0，因此按下Enter按鍵後就會傳出指令代碼0
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
```

之後在場景中的keyListener使用設置的指令碼，做出相應的程式邏輯。(**使用範例詳見Scene類使用簡介)。

# 3 GameObject

## 2-1 角色繼承抽象類別GameObject

1. 創建類別並繼承GameObject 。

   ```java
   public class Actor extends GameObject {
   
   }
   
   ```

2. 並繼承父類別的建構子(選擇其中一個繼承即可)。

```java
public class Acotor extends GameObject{
    //角色的(x座標,y座標,寬,高)
   public Actor(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
	//碰撞框,與畫的矩形為同一個時
    public Actor(Rect rect) {
        super(rect);
    }
	//透過傳入x.y,寬,高設定碰撞、畫的矩形
    public Actor(int x, int y, int width, int height, int x2, int y2, int width2, int height2) {
        super(x, y, width, height, x2, y2, width2, height2);
    }
	//rect為碰撞框的矩形,rect2為畫的矩形
    public Actor(Rect rect, Rect rect2) {
        super(rect, rect2);
    }
 
}
```

3.實踐 GameObject 中未實現的抽象方法。

```java
 	@Override
	// 用來畫出物件
    public void paintComponet(Graphics g) {
    }
   

    // GameKernal. UpdateInterface介面中的方法
	// 用來更新繼承之物件邏輯
    @Override     
    public void update() {   

    }
}
```

## 2-2 基本角色碰撞AABB

可透過以下四種方法,去判斷兩物件(都繼承於GameObject)的四個方位是否有碰撞

```java
public boolean topIsCollision(GameObject obj) {
    return collider.top()<=obj.collider.bottom() &&
            obj.collider.left()<collider.right()&&
            obj.collider.right()>collider.left()&&
            obj.collider.top()<collider.bottom();
}
public boolean leftIsCollision(GameObject obj) {
    return collider.left()<=obj.collider.right() &&
            obj.collider.bottom()>collider.top()&&
            obj.collider.top()<collider.bottom()&&
            obj.collider.left()<collider.left();
}
public boolean rightIsCollision(GameObject obj) {
    return collider.right()>=obj.collider.left()&&
            obj.collider.bottom()>collider.top()&&
            obj.collider.top()<collider.bottom()&&
            obj.collider.right()>collider.right();
}
public boolean bottomIsCollision(GameObject obj) {
    return collider.bottom()>=obj.collider.top() &&
            obj.collider.left()<collider.right()&&
            obj.collider.right()>collider.left()&&
            obj.collider.bottom()>collider.top();
}
```

## 2-3 物件是否超出鏡頭

``` java
public boolean outOfScreen() {
        if (painter.bottom() <= 0) {
            return true;
        }
        if (painter.right() <= 0) {
            return true;
        }
        if (painter.left() >= Global.SCREEN_X) {
            return true;
        }
        return painter.top() >= Global.SCREEN_Y;
    }
```

## 2-4 Debug模式

繼承於GameObject的物件(地圖.人物)，會畫出矩形的外框，用於確認物件的碰撞判定

```java
 @Override
 
    public final void paint(Graphics g) {
        paintComponent(g);
        //如果DEBUG開啟時
        if (Global.IS_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect(this.painter.left(), this.painter.top(), this.painter.width(), this.painter.height());
            g.setColor(Color.BLUE);
            g.drawRect(this.collider.left(), this.collider.top(), this.collider.width(), this.collider.height());
            g.setColor(Color.BLACK);
        }
    }
```

# 4 Scene

## 1-1 Scene簡介

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

## 1-2 使用範例

假設今天有一個開場(Scene的子類OpenScene)，需求如下:

1.有一張背景圖片

2.讓使用者可以按下不同按鍵，做出不同回應，如下:

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/7f10da9e-11de-4e0c-acc7-1f5c0461348e/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T063112Z&X-Amz-Expires=86400&X-Amz-Signature=3efd0169adb649299bef35055f43f1228071035911661be5f6909e8485ddcf41&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22)



```java
public class OpenScene extends Scene {
    private Image image; //背景圖片
    @Override
    public void sceneBegin() {  //在場景開始的時候使用ImageController建立背景圖片
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



# 5 Controllers

存在目的:以單例的方式集中管理性質相近且多數的資源

 ## 4-1 建議須被管理的資源

1.圖片(img controller)

2.音源(audio controller)

3.場景(scene controller)

## 4-2 核心技巧-單例模式

何謂單例:將建構子設定為private 外部如果需要使用則需要用get的方法去取用資料，以確保資料來源皆為同一個實體

```java
public class ImageController {
    private static ImageController imageController; //靜態實體
    private ArrayList<KeyPair> keyPairs; //存放所有圖片的陣列
    private ImageController(){//私有化建構子，杜絕外部new
        keyPairs=new ArrayList<>();
    }

    //單例模式靜態方法;取得實體-->且只會產生一個實體
    public static ImageController getInstance(){
        if(imageController==null){
            imageController=new ImageController();
        }
        return imageController;
    }

    //加入圖片的方法，傳入路徑，創建圖片-->私有化
    private Image add(String path){
        Image img=null;
        try {
            img=ImageIO.read(getClass().getResource(path));
            this.keyPairs.add(new KeyPair(path,img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    public BufferedImage addBuff(String path){
        BufferedImage img=null;
        try {
            img=ImageIO.read(getClass().getResource(path));
            this.keyPairs.add(new KeyPair(path,img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    //取得陣列中圖片的方法-->根據路徑找該圖片
    public Image tryGet(String path){
        for(int i=0;i<keyPairs.size();i++){
            if(this.keyPairs.get(i).path.equals(path)){ //假如傳進來的路徑相等
                return this.keyPairs.get(i).image;
            }
        }
        return add(path);
    }

    private static class KeyPair{ //私有的靜態內部類
        private String path;
        private Image image;
        public KeyPair(String path,Image image){
            this.path=path;
            this.image=image;
        }
        //不用寫get，因為是內部類，可以直接取得path和image。
    }

}
```



## 4-3 Scene controller

設定方法與圖片控制器雷同

不同處有以下

1.滑鼠與鍵盤監聽器(詳請見Command Solver)

2. update 與paint(詳請見Game object) 

```java
public class SceneController implements GameKernel.UpdateInterface,GameKernel.PaintInterface, CommandSolver.MouseListener,CommandSolver.KeyListener{
    private Scene currentScene; //內含一個當前場景
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
        if(this.currentScene!=null){
            currentScene.sceneEnd();
        }
        if(scene!=null){
            scene.sceneBegin(); //傳進來的場景初始化
            currentScene=scene; //並換成當前場景
        }
    }
    @Override
    public void keyPressed(int commandCode, long trigTime) {
        if(currentScene!=null && currentScene.keyListener()!=null){
            currentScene.keyListener().keyPressed(commandCode,trigTime);
        }
    }
    @Override
    public void keyReleased(int commandCode, long trigTime) {
        if(currentScene!=null &&currentScene.keyListener()!=null){
            currentScene.keyListener().keyReleased(commandCode,trigTime);
        }
    }
    @Override
    public void keyTyped(char c, long trigTime) {
        if(currentScene!=null && currentScene.keyListener()!=null){
            currentScene.keyListener().keyTyped(c,trigTime);
        }
    }
    @Override
    public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if(currentScene!=null && currentScene.mouseListener()!=null){
            currentScene.mouseListener().mouseTrig(e,state,trigTime);
        }
    }
    @Override
    public void paint(Graphics g) {
        if(currentScene!=null){
            currentScene.paint(g);
        }

    }
    @Override
    public void update() {
        if(currentScene!=null){
            currentScene.update();
        }

    }
}
```

## 4-4 Audio controller



```java
public class AudioResourceController {

    private static AudioResourceController irc;
    private Map<String, ClipThread> soundMap;
    private final ClipThread.FinishHandler finishHandler = (String fileName, Clip clip) -> {
        if (this.soundMap.containsKey(fileName)) {
            if (this.soundMap.get(fileName).framePos == -1) {
                this.soundMap.remove(fileName);
                clip.close();
            }
        } else {
            clip.close();
        }
    };
    private AudioResourceController() {
        this.soundMap = new HashMap<>();
    }

    public static AudioResourceController getInstance() {
        if (irc == null) {
            irc = new AudioResourceController();
        }
        return irc;
    }

    public void play(final String fileName) {
        if (this.soundMap.containsKey(fileName)) {
            final ClipThread ct = this.soundMap.get(fileName);
            if (!ct.isDead()) {
                ct.playSound();
                return;
            }
        }
        final ClipThread ct = new ClipThread(fileName, 1, this.finishHandler);
        this.soundMap.put(fileName, ct);
        ct.start();
    }

    public void shot(final String fileName) {
        new ClipThread(fileName, 1, this.finishHandler).start();
    }

    public void loop(final String fileName, final int count) {
        final ClipThread ct = new ClipThread(fileName, count, this.finishHandler);
        this.soundMap.put(fileName, ct);
        ct.start();
    }

    public void pause(final String fileName) {
        if (this.soundMap.containsKey(fileName)) {
            final ClipThread ct = this.soundMap.get(fileName);
            ct.framePos = ct.clip.getFramePosition();
            ct.clip.stop();
        }
    }

    // 同樣音效連續播放時只能停止最後一次
    public void stop(final String fileName) {
        if (!this.soundMap.containsKey(fileName)) {
            return;
        }
        final ClipThread ct = this.soundMap.get(fileName);
        ct.stopSound();
        this.soundMap.remove(fileName);
    }

    // 單例
    private static class ClipThread extends Thread {

        private final String fileName;
        private final int count;
        private final FinishHandler finishHandler;
        private Clip clip;
        private int framePos;
        public ClipThread(final String fileName, final int count, final FinishHandler finishHandler) {
            this.fileName = fileName;
            this.count = count;
            this.finishHandler = finishHandler;
            this.framePos = -1;
        }

        @Override
        public void run() {
            final AudioInputStream audioInputStream;
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new File(this.getClass().getResource(this.fileName).toURI()));
                this.clip = AudioSystem.getClip();
                this.clip.open(audioInputStream);
                this.clip.setFramePosition(0);
                // values have min/max values, for now don't check for outOfBounds values
                final FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(5f);
                playSound();
                this.clip.addLineListener((LineEvent event) -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        this.finishHandler.whenFinish(this.fileName, this.clip);
                    }
                });
            } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException ex) {
                Logger.getLogger(AudioResourceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void playSound() {
            if (this.framePos != -1) {
                this.clip.setFramePosition(this.framePos);
                this.framePos = -1;
            }
            if (this.count == 1) {
                this.clip.start();
            } else {
                this.clip.loop(this.count);
            }
        }

        public void stopSound() {
            if (this.clip != null && this.clip.isRunning()) {
                this.clip.stop();
                if (isAlive() || !isInterrupted()) {
                    interrupt();
                }
            }
        }

        public boolean isDead() {
            return this.clip == null;
        }

        public interface FinishHandler {
            public void whenFinish(String fileName, Clip clip);
        }
    }
}

```



## 4-5 圖片該如何引入



在src 外層再開一個資料夾(需標記成Sources root)

![](https://github.com/YuWeiLGit/pic/blob/main/rsroot.png?raw=true)





## 4-6 如何在外部使用

1.以img controll為例

物件Space Ship在建構子時使用了此controller 確保所有被創建的space ship的圖片來源都是同一個

```java
 public SpaceShip(int x, int y,int moveStep) {
       super(x, y, 32, 32,null);
       this.img = ImageController.getInstance().tryGet("/spaceship.png");}
```

2.音源則是使用play的方法

3.Scene 則是使用以下方法

```java
SceneController sceneController=SceneController.getInstance(); //取得單例模式的控場實體
        sceneController.changeScene(new MapScene()); //一開始使用開場畫面
```



# 6 Delay

這個類別的目的是當我們希望物件的動作延遲時使用，比如說我們不會希望人物每秒走60步，此時就可以用delay延遲走路的動作。

| function              | 功能                                         |
| --------------------- | -------------------------------------------- |
| Delay(int countLimit) | Delay的建構子，參數放delay的次數             |
| stop()                | 結束delay                                    |
| play()                | delay只進行一次，例如爆炸特效的delay只有一次 |
| loop()                | delay每次結束，會再開始，例如人物走路的delay |
| pause()               | delay暫停，例如遊戲需暫停的時候              |
| isStop()              | 回傳delay是否停止的boolean值                 |
| isPlaying()           | 回傳delay是否進行的boolean值                 |
| isPause()             | 回傳delay是否暫停的boolean值                 |
| count()               | delay計算，並回傳是否delay完成的boolean值    |


```java
public class Delay {
    private int count; //計時器，計算當下經過的幀數
    private int countLimit;// 計數的上限(總共要記幾幀)
    private boolean isPause;//是否暫停計數
    private boolean isLoop;//是否進行週期性的延遲
    //建構子
    public Delay(int countLimit){
        this.countLimit=countLimit;
        count=0;
        isPause=true; //剛開始的時候沒有要計時
        isLoop=false; //剛開始不要週期性延遲，需要時再用
    }

    //方法區
    //停止計時的方法
    public void stop(){
        count=0; //計時器歸0
        this.isPause=true; //暫停計時
    }
    //開始計時
    public void play(){
        this.isPause=false;
    }
    //執行週期延遲
    public void loop(){
        this.isLoop=true; //開始週期延遲
        this.isPause=false; //不要暫停
    }
    //暫停計時
    public void pause(){
        this.isPause=true;
    }
    //是否未開始延遲
    public boolean isStop(){
        return count==0 && isPause; //計數器是0的時候，且是暫停的狀態-->表示還沒開始計時
    }
    //是否正在延遲中
    public boolean isPlaying(){
        return !this.isPause; //非暫停，就是在延遲中
    }
    //是否是暫停的狀態
    public boolean isPause(){
        return isPause;
    }


    // 這個方法需要反覆在更新中被呼叫，並通過被呼叫的次數來進行延遲的判斷
    public boolean count(){  //會回傳是否觸發目標動作的計時器
        if(isPause){ //假如現在是暫停計時狀態
            return false; //即不會計算也不會觸發延遲後要執行的事件
        }
        if(count>=countLimit){ //假如計數計到大於等於計數上限時
            if(this.isLoop){
                // 如果是週期性執行的情況就重新將count歸零繼續計算
                this.count = 0;
            }else {
                this.stop(); //就停止計數
            }
            return true;  //觸發我們要的動作
        }
        count++; //計時
        return false;   //還不要觸發我們要的動作
    }
}
```

## 5-1 加入delay物件並初始化

```java
private Delay delay;//在需要delay的類別加入delay屬性

private ActorAnimal(){
            delay=new Delay(5);//放入要計時幾次
            delay.loop();//如果希望計時會一直重複用loop()
    		delay.play();//希望計時只進行一次用play()
        }
```

## 5-2 在需要delay的方法加入delay.count()

```java
 if (delay.count()){//delay計時完成會回傳一個true，此時進行動作
                    count=++count%4;
                }
```

# 7 Global

## 7-1 介紹

需要共用或是重複使用的東西、資料都可以設為static放在global類別內，減少記憶體使用。

| 種類     | 範例                                                         | 說明                                                         |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 內部類   | public class InternetCommand {}                              | 在使用網路連線封包時會因為連線狀態不同而需要做不同的事情，因此需要定義目前連線狀態以便後續動作執行。 |
| enum     | public enum Direction {}                                     | 若角色(玩家、敵人...等)需要用到方位判斷時，就會需要用到Direction，因此寫在global方便使用。 |
| 靜態常數 | public static final int WINDOW_HEIGHT;<br />public static final boolean IS_DEBUG = true; | 將常數設在global以減少記憶體消耗。<br />藉由設定boolean IS_DEBUG來開啟或關閉debug模式。 |
| 靜態方法 | public static int random(int min, int max) {}                | 將常用統一的方法設在global以減少記憶體消耗。                 |

## 7-1 Code範例

Global中的 InternetCommand:

```java
public class Global {
		public class InternetCommand{
				public static final int CONNECT = 0;
				public static final int MOVE = 1;
				public static final int DISCONNECT = 2;
		}
}
```

```java
ClientClass.getInstance().consume(new CommandReceiver() {
                @Override
                public void receive(int serialNum, int internetcommand, ArrayList<String> strs) {
                    switch(internetcommand){
                        case Global.InternetCommand.CONNECT**:**
                            // do sth
															break;
                        case Global.InternetCommand.MOVE**:**
                            // do sth
															break;
                        case Global.InternetCommand.DISCONNECT**:**
                            // do sth
															break;
                    }
                }
            });
        }
```

Global中的 Direction:

```java
public class Global {
		public enum Direction {
        UP(3),
        DOWN(0),
        LEFT(1),
        RIGHT(2),
        NO_DIR(4);
        private int value;
					
				 //在建構子帶入int參數讓Direction和commandCode做連結
        Direction(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
				 
				 //讓使用者可以用傳的int的方式取得方位。
        public static Direction getDirection(int value){
            for(Direction d : Direction.values()){
                if(d.getValue() == value){
                    return d;
                }
            }
            return Direction.NO_DIR;
        }
    }
}
```

isDebug:

藉由設定IS_DEBUG = true 可以得到各個物件的painter框，以利debug。

```java
public class Global {
		public static final boolean IS_DEBUG = true;
}
```

靜態常數：

```java
//視窗大小
public static final int WINDOW_WIDTH = 960;
public static final int WINDOW_HEIGHT = 640;
// 資料刷新時間
public static final int UPDATE_TIMES_PER_SEC = 60; //每秒更新60次遊戲邏輯
public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC; // 每一次要花費的奈秒數
// 畫面更新時間
public static final int FRAME_LIMIT = 60; //每秒更新60次畫面
public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT; //每一次要花費的奈秒數
```

靜態方法：

```java
//產生亂數
public static int random(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
}
//算機率
public static boolean random(int rate) {
		return random(1, 100) <= rate;
}
```
# 

# 8 MapLoader

可用在2D遊戲物件的位置布置與繪製

所需物件：

1.地圖產生器

2.地圖載入器

##  執行地圖產生器

將MapGenerator.jar,點擊兩次執行，出現以下照片

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/7cd2156a-eb59-485c-9ff4-3367fc2eca16/firstStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053345Z&X-Amz-Expires=86400&X-Amz-Signature=bcc5ba3b5922e854b456a9757c4e4cc69ed7ab07e601d6668bdd4aae62015f4d&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22firstStep.png%22)

### 8-1 設定地圖tile大小

1. Unit:每一個格子的像素大小，圖片中設定為32pixal。

2. map size : 寬 x 高 各有多少格子，圖片中設定為25*25格。
3. zoom : 如果你圖片太大，可以透過它縮放中間紅色格子區域的大小。

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/39c84b79-b341-475e-9107-493c36841e54/SecondStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053442Z&X-Amz-Expires=86400&X-Amz-Signature=457f605462cfdd0bd67d65294b18b625955b867c2f1d1f81466a3e0928576297&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22SecondStep.png%22)

### 8-2 加入圖片&&去除圖片

1.add(紅色框):選擇路徑加入地圖。

2.名稱(黑色框):設定名稱,這邊名稱不能設定相同。

3.物件畫的格數(黃色框):圖中1*1代表只會畫一格在右邊。

4.物件標定的顏色(藍色框):設定標定顏色:這邊顏色不能設定一樣。

5.eraser(棕色框):擦掉畫錯格子,如設定為2*2大小,需點選左上角框框才能清除。

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/a5882654-a9ed-4066-a109-310289178d2c/ThirdStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053726Z&X-Amz-Expires=86400&X-Amz-Signature=0c3033f3f1a8e354003ede70b5781bb1f04f4ac6462d2abf5809385b410a72c8&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22ThirdStep.png%22)

### 8-3 導出bmp圖,txt圖

import:可以導入之前畫好的bmp圖檔接續畫(直接bmp所在資料夾開啟)

export:選取要存取位置並按開啟，導出bmp圖檔&&txt檔(如要在存取相同位置，請將原本位置的檔案先移出，不然會重疊到)

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/b77ebd7d-87fa-4727-821e-1c304c8e0b7c/fourStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053819Z&X-Amz-Expires=86400&X-Amz-Signature=da8c361b15dcad64aaab33a5c2d7ac6b9044dfb98a92def4edff1636de40cf1f&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22fourStep.png%22)

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/022a4796-90f6-41bd-8830-18bb4ff6dd9d/fiveStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053850Z&X-Amz-Expires=86400&X-Amz-Signature=e97ce6c3e3170c5f1f70d913cac139eea63e717456e165b0d8287cef0d36526e&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22fiveStep.png%22)



## 執行地圖載入器

### 8-4 導入maploader package

ReadBmp : 讀取Bmp檔，用來獲取物件座標

ReadFile : 讀取txt檔，用來獲取物件名稱，和尺寸

mapLoader : 整合上面兩個的讀取資料

MapInfo : 儲存單一位置地圖物件的地圖資訊

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/ab6e8a82-207d-4243-a3e8-d984cdc2c112/MapInfo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053909Z&X-Amz-Expires=86400&X-Amz-Signature=c7db3e4fca9725f35cbd10d15a73630c35666f4876a05db998568febdaecfb4b&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22MapInfo.png%22)

### 8-5 將bmp,txt圖檔 丟進 resourse資料夾)

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/8e1a1317-43e9-4df1-af2d-ef278b34ef76/sixstep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T054028Z&X-Amz-Expires=86400&X-Amz-Signature=544477437858b59639953ea0d2ee6a5d5710f159f2c73203ea31dd12155d3cc4&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22sixstep.png%22)

### 8-6 在場景中創建MapLoader物件



1. (藍色框)創建Maploader物件(丟入bmp,txt)。

2. (紫色框)使用mapLoader中的combineInfo()方法產生MapInfo陣列。

3. (綠色框)進行name的比較並產生對應地圖物件 。

4. (綠色框)傳入物件類別名、物件size、MapLoader產出的MapInfo Array、並實現接口實作產生物件之方法。

5. (黃色框)創建出含有相對應圖片的類別實體。

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/9be2cfe0-3c11-4fd4-9300-88c560ad3128/sevenStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T054005Z&X-Amz-Expires=86400&X-Amz-Signature=49a0b41b3bf2897bdc9bc4ed0d06ba04e598e0b2e7de94caa669f570397e93fe&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22sevenStep.png%22)

   5.透過增加addAll()方法，加上要畫的其它物件

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/39162561-205e-4e93-b3b6-317a07624c8f/eightStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T055721Z&X-Amz-Expires=86400&X-Amz-Signature=3f701304ff435be9a61ac20e59611013c93e5f16f0d2e67865412140077b889f&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22eightStep.png%22)

# 9 Camera

### 9-1 在Map類別中設定地圖大小

```java
public class Map extends GameObject{
    private Image img;
		
		**//地圖大小設定**
    public Map() {
        super(640, 640, 1280, 1280); //super(int x, int y, int width, int height)
        img= ImageController.getInstance().tryGet("/a.png");//可在這設定背景圖，無背景圖可省略
        MapInformation.setMapInfo(0,0, 1280,1280); 
    }
			
		**//畫的地方也要記得做調整**
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img,painter().left(), painter().top(),1280,1280,null);
    }

    @Override
    public void update() {

    }
}
```

### 9-2 使用鏡頭的類別

1.導入camera.Camera

2.加入Camera 和 Map 屬性

```java
import camera.Camera;

public class MapScene extends Scene {
		private Camera camera; **//若有需要使用到多鏡頭可以用 ArrayList<Camera> cameraArr;**
		private Map map;

}
```

3.藉由Builder new出Camera  

🔺建議於SceneBegin()中建立，以免重覆創建

| function                                                     | 功能                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| Builder(int width, int height)                               | 設定鏡頭寬 、 高。                                           |
| setChaseObj(GameObject obj)                                  | 使鏡頭追蹤物件。                                             |
| setChaseObj(GameObject  obj, double chaseDivisorX, double chaseDivisorY) | 設定鏡頭追蹤速度。 (後面兩個參數數值需大於1.0 , 1.0) 參數為1.0 , 1.0 時，鏡頭與物件移動速度相等。 參數數值越大，鏡頭追蹤速度越慢。 |
| setCameraMoveSpeed(int num)                                  | 鏡頭沒有追蹤物件時，自由移動的速度。                         |
| setCameraStartLocation(int left, int top)                    | 設定鏡頭在地圖絕對座標的初始位置。 (預設為0,0) 參數代表鏡頭左上角座標 |
| setCameraWindowLocation(int left, int top)                   | 設定鏡頭在視窗中的位置。 (預設為0,0) 參數代表鏡頭左上角座標  |
| setCameraLockDirection(boolean left, boolean up, boolean right, boolean down) | 限制鏡頭移動方向。 (預設四個方向皆可移動)                    |

兩種不同鏡頭的建立

- 有追蹤物件

```java
public void SceneBegin(){
					map = new Map();
					//鏡頭追蹤設定時需要這個參數，所以map一定要new在鏡頭前
					//記得先創建人物以免鏡頭找不到追焦對象
					camera = new Camera.Builder(400,300) //創建大小為400*300的鏡頭
					.setChaseObj(actor, 1, 1) //鏡頭追蹤主角(actor),追焦係數為1,1 
					.setCameraWindowLocation(WINDOW_WIDTH/2,0) //鏡頭只顯示在視窗的右半邊
					.setCameraStartLocation(100,50) 
                        //鏡頭內顯示的地圖範圍：地圖x軸:100~100+400//地圖y軸：50~50+300
					.setCameraLockLocation(true,true,true,false) //限定鏡頭只能往下移動
															
}
//notice!!!
//若有限定鏡頭移動方向時，需做判斷以防角色走出鏡頭外
```

- 沒有追蹤物件

```java
public void SceneBegin(){
					map = new Map();
					camera = new Camera.Builder(400,300) **//創建大小為400*300的鏡頭**
					.setCameraMoveSpeed(5) **//鏡頭移動速度5**
					.setCameraWindowLocation(WINDOW_WIDTH/2,0) **//鏡頭只顯示在視窗的右半邊**
					.setCameraStartLocation(100,50) 
                        **//鏡頭內顯示的地圖範圍：地圖x軸：100~100+400			
                        //地圖y軸：50~50+300**
					.setCameraLockLocation(true,true,true,false) **//限定鏡頭只能往下移動**
															
}
```

4.在Scene的update()中加入camera.update()

```java
public void update(){
			**//單一鏡頭更新**
			camera.update();
			**//多鏡頭更新**
			for(int i = 0; i < cameraArr.size(); i++){
					cameraArr.get(i).update();
			}
}
```

5.在Scene的paint(Graphics g)中加入鏡頭

```java
private GameObject obj;

public void paint(Graphics g){
			
			camera.start(g);

			camera.paint(g); //需要debug時才會用到，平時可略

			**//不畫超出鏡頭外的物件以節省效能**
			if(camera.isCollision(obj)){
					obj.paint(g);
			}

			camera.end(g);
}
```

# 10 Server與ClientClass

## 10-1 Server類簡介

#### 10-1-1啟用Server:

```java
    Server.instance().create(12345);  
    Server.instance().start();
```

#### 10-1-2印出伺服器資訊:

```java
System.out.println("主機IP：" + Server.instance().getLocalAddress()[0] + "\n主機PORT：" + Server.instance().getLocalAddress()[1]);
```

#### 10-1-3關閉Server:

```java
	Server.instance().close();
```

- 備註:1.Server 是單例模式，使用Server.instance()方法便可取得Server實體。

  ​         2.Client連線到Server時，Server便會配一組ID給該Client，ID編號會從100開始上數，Client可以透過這組ID辨認訊息是從哪個Client所傳送。
  ​            Server收到訊息時，會廣播該訊息給所有還在線的Client。

  

#### 10-1-4Server方法列表

| 方法名稱          | 功能說明                                                     |
| :---------------- | :----------------------------------------------------------- |
| create(int port)  | 輸入Port以創建伺服器                                         |
| start()           | 啟動Server並等待連線                                         |
| close()           | 關閉伺服器                                                   |
| getLocalAddress() | 回傳一個長度為2的String[ ]，索引0為Server端IP，索引1為Server端PORT |

## 10-2 Client類簡介

#### 10-2-1連線至Server:

```java
// 連線
    try {
      ClientClass.getInstance().connect("192.168.1.29", 12345); // ("SERVER端IP", "SERVER端PORT")
    } catch (IOException ex) {
        Logger.getLogger(MainScene.class.getName()).log(Level.SEVERE, null, ex);
    }  //ip與port必須設定與開啟server的電腦一致
```

- 備註:啟用Server者，ip可以使用127.0.0.1來連接。

#### 10-2-2傳送指令

```java
// 使用ClientClass中的sent方法。commandCode 為指令編號
 // strs 為指令內容
 sent(int commandCode, ArrayList<String> strs)
```

sent方法會將指令封裝並傳送。

- 封裝指令:

  網路模組**目前設計僅能傳送字串**，因此**需要將資料轉換成字串進行傳送**，所有的指令都會被包裝成Command物件進行傳送。

  **Command物件包含以下屬性：**

  1. ID (Server給的Client ID，儲存與處理皆封裝於ClientClass中，無需自行處理)。

  2. commandCode (執行的指令代碼)

  3. 參數 (指令內容)

     ```java
     //依照遊戲需求設置指令command，參考如下:
     public static class Command{
             // 設定常數或列舉標註遊戲中包含的指令
     	public static final int CONNECT=0;
     	public static final int MOVE=1;
                       
     }
     ```


- 製作訊息封包的內容範例:

  ```java
  //示範將角色的座標當作訊息封包傳送 
  public void mouseTrig(MouseEvent e, MouseState state, long trigTime) {
           if(state == PRESS){		
                  ArrayList<String> strs = new ArrayList<String>;
  		strs.add(x)//角色的X
  		strs.add(y)//角色的Y
  		ClientClass.getInstance().sent(Command.MOVE/*指令編號*/,strs/*參數串成的字串*/)
  	 }
      }
  ```

- 傳送封包:

  ```java
   // commandCode 為指令編號
   // strs 為指令內容
   sent(int commandCode, ArrayList<String> strs)
  ```

#### 10-2-3**接收指令**

在遊戲中的update中使用consume方法接收訊息封包，根據收到的ID,指令編號,參數，去執行相對應的動作(由我們自己撰寫邏輯)。

- 使用範例:

  ```java
  //使用ClientClass中的consume(CommandReceiver cr)方法
  public void update() {
           // 消費指令
           ClientClass.getInstance().consume((int serialNum, int commandCode, ArrayList<String> strs1) -> {
              // 實現CommandReceiver這個介面 把server那邊傳過來的指令做解析並且做出動作
              if (serialNum == ballList.get(0).id()) {//若ID與自己的ID相同則return
                  return;
              }
              switch (commandCode) {//根據指令編號選擇動作
                  case Command.CONNECT: //有
                      boolean isburn = false;
                      for (int i = 0; i < ballList.size(); i++) {
                          if (ballList.get(i).id() == serialNum) {
                              isburn = true;
                              break;
                          }
                      }
                      if (!isburn) {
                          ballList.add(new Ball(serialNum, Integer.parseInt(strs1.get(0)),                         
                                                   Integer.parseInt(strs1.get(1)), 30, Color.CYAN));
                          ArrayList<String> arrs = new ArrayList<>();
                          arrs.add(nowBall.collider().centerX() + "");
                          arrs.add(nowBall.collider().centerY() + "");
                          try {
                               ClientClass.getInstance().sent(0, arrs);
                          } catch (IOException ex) {
                              Logger.getLogger(MainScene.class.getName()).log(Level.SEVERE, null, ex);
                          }
                      }
                      break;
                  case Command.MOVE:
                      if (nowBall.id() == serialNum) {
                          break;
                      }
                      for (int i = 1; i < ballList.size(); i++) {
                          if (ballList.get(i).id() == serialNum) {
                              ballList.get(i).collider().setCenter(Integer.parseInt(strs1.get(0)),                                           
                              Integer.parseInt(strs1.get(1)));
                              ballList.get(i).painter().setCenter(Integer.parseInt(strs1.get(0)), Integer.parseInt(strs1.get(1)));
                              break;
                          }
                      }
                      break;
              }
          });
  ```

- 備註: 1.因為consume的方法執行過後指令將被清空，因此一次update(遊戲數據更新)週期中請僅執行一次為限。

  ​          2.收到的封包內容如下:

  ```java
  //被包成字串集合的訊息封包中擁有:
  //"ID,指令編號,參數..."
    "100,1,x,y"
  ```

#### 10-2-4ClientClass方法列表

| connect(String ip, int port)                  | 連線至指定ip,port之server                                    |
| :-------------------------------------------- | :----------------------------------------------------------- |
| getInstance()                                 | 拿到ClientClass的實體                                        |
| getID()                                       | 拿到client的ID                                               |
| sent(int commandCode, ArrayList<String> strs) | 傳送封包                                                     |
| 方法                                          | 功能                                                         |
| consume(CommandReceiver cr)                   | 接收封包 使用前要實現CommandReceiver                         |
| disconnect()                                  | 中斷連線 server會丟斷線的id 和commandCode = -1 給其他還連著的client |

## 10-3 連線案例示範

案例需求: 1.在首頁選擇建立伺服器(按下Enter)；或連接伺服器(按下Space)

​                  2.多人連線後，可以看見各自的角色行走。

####  10-3-1連線設置及基礎設置

#### 1.連線設置

```java
//在 首頁場景中 的keyListener()實作 建立伺服器
        public void keyPressed(int commandCode, long trigTime) {
            Scanner sc=new Scanner(System.in);
                if ( commandCode == 0) { //建立伺服器
                    Server s=Server.instance(); //取得Server實體
                    s.create(12345); //建立伺服器，並可帶入port號當參數
                    s.start(); //啟動伺服器
                    try {
                        ClientClass.getInstance().connect("127.0.0.1",12345); //連接自己的伺服器
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("主機IP：" + Server.instance().getLocalAddress()[0] +  //印出主伺服器的資訊
                            "\n主機PORT：" + Server.instance().getLocalAddress()[1]);
                }
        }
```



```java
//在 首頁場景中 的keyListener()實作 連線設置
        public void keyPressed(int commandCode, long trigTime) {
            Scanner sc=new Scanner(System.in);
          if(commandCode==5){  //連接伺服器
                    System.out.println("請輸入IP:"); 
                    String str=sc.next();
                    try {
                        ClientClass.getInstance().connect(str,12345); //連接伺服器
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            ArrayList<String> str=new ArrayList<>();
            SceneController.getInstance().changeScene(new MapScene());//觸發換場
    };
}
```

#### 2.設置ID屬性

​    由於Server類會為每位加入的玩家配發一組ID，此範例我們在玩家控制的角色加入ID屬性，接收伺服器配發的ID以**標示玩家**。

```java
public class Actor extends GameObject{
    private int ID;//增加ID屬性，接收連線時伺服器發送的ID
    //其他屬性略...
 
    public Actor(int x, int y,int num) {
       //建構子略
    }
    //增加設置和取得Id的方法
    public void setId(int id){ 
        this.ID=id;
    }
    public int getId(){
        return this.ID;
    }
	//其他方法略
}
```



#### 10-3-2傳送和接收訊息

#### 1.加入Actor的集合

如此才可以在有別人加進來時，將他的角色加入到自己的ArrayList中，並出現在場景。

```java
//在遊戲主場景中加入角色集合，
public class MapScene extends Scene {
  	
    private ArrayList<Actor> actor; //增加角色集合，如此才可以在有別人加進來時，將他人的角色加入到自己的ArrayList中，並出現在場景。
    
    public MapScene(){
    }
    
    @Override
    public void sceneBegin() { //開場時記得給角色設置ID
        actor=new ArrayList<>();
        actor.add(new Actor(Integer.parseInt(str.get(0)),Integer.parseInt(str.get(1)),num));
        ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str); //記得要傳送自己的座標位置等資訊!
        actor.get(0).setId(ClientClass.getInstance().getID());  //為自己的角色設置ID(自己的角色都會是集合中的第一個)。
        //其他略
    }
}
```



#### 2.設置InternetCommand

​	可以在Global中設置InternetCommand，目前需求為連接玩家並看的見移動，然後離開時會斷線。

​	所以設置三個指令CONNECT  MOVE  DISCONNECT。將來有更多邏輯時，要視需求擴充，例如射擊遊戲就會有SHOT指令。

```java
//可以在Global中設置連線指令，如下
public class Global {
    public class InternetCommand{ 
        public static final int CONNECT=0; //連線的指令碼
        public static final int MOVE=1; //移動的指令碼
        public static final int DISCONNECT=2; //斷線的指令碼
    }
}
```



#### 3.指令的傳送和接收

​      主要兩個步驟: 1.發送自己角色的訊息  2.接收並解析別人角色的連接 移動 斷線等訊息

##### - 發送訊息

(1)**在update中發送自己的ID 中心點座標x y 移動方向**

```java
//於主場景的update發送自己的相關訊息
@Override
    public void update() {
        actor.get(0).update();
        ArrayList<String> strr=new ArrayList<>(); //儲存訊息封包的集合
        strr.add(ClientClass.getInstance().getID()+""); //打包ID訊息
        strr.add(actor.get(0).painter().centerX()+"");//打包角色座標x
        strr.add(actor.get(0).painter().centerY()+"");//打包角色座標y
        strr.add(actor.get(0).getDir()+""); //打包角色方向
        ClientClass.getInstance().sent(Global.InternetCommand.MOVE,strr); //sent方法傳送資訊
    }
```



(2)在鍵盤監聽中發送斷線訊息:

```java
//在主場景的keyListener()發送斷線訊息
@Override
public CommandSolver.KeyListener keyListener() {
    return new CommandSolver.KeyListener(){
        @Override
        public void keyTyped(char c, long trigTime) {
			
        }
        public void keyPressed(int commandCode, long trigTime) {
            Global.Direction dir=Global.Direction.getDirection(commandCode);
            if(commandCode==6){  //角色斷線時發送斷線訊息
                ArrayList<String> strs = new ArrayList<String>(); //儲存訊息封包的集合
                strs.add(String.valueOf(ClientClass.getInstance().getID())); //打包ID
                ClientClass.getInstance().sent(Global.InternetCommand.DISCONNECT,strs); //傳送封包
                ClientClass.getInstance().disConnect(); //斷開伺服器連結
                System.exit(0); //結束程式
            }
```

##### - 接收訊息

接收並解析別人角色的連接 移動 斷線等訊息。這部分將會很長，藉由ClinetClass中的consume方法，就可以依據我們在Global中設置的InternetCommand，搭配switchCase來設定三種狀態下要解析的外來訊息。

```java
//在主場的update中處理訊息封包，並做出相應動作
@Override
public void update() {
    //傳送自己訊息的方法見發送訊息段...以上略
	//使用consume方法解析封包，並執行相應動作
    ClientClass.getInstance().consume(new CommandReceiver() {
        @Override
        public void receive(int serialNum, int internetcommand, ArrayList<String> strs) {
            switch(internetcommand){
                case Global.InternetCommand.CONNECT: //(1)完成CONNCENT指令的處理邏輯
                    System.out.println("Connect " + serialNum); //serialNum即客戶的ID
                    boolean isburn = false;
                    for (int i = 0; i < actor.size(); i++) { 
                        if (actor.get(i).getId() == serialNum) {
                            isburn = true; //避免已經加入過的玩家被加入角色陣列
                            break;
                        }
                    }
                    if(!isburn) { //將新進的玩家，加入自己的角色陣列中
                        actor.add(new Actor(Integer.parseInt(strs.get(0)),Integer.parseInt(strs.get(1)), 				
　　　　　　　　　　　　　　　							Integer.parseInt(strs.get(2))));
                        actor.get(actor.size() - 1).setId(serialNum);
                        ArrayList<String> str=new ArrayList<>(); 
                        str.add(actor.get(0).painter().centerX()+"");
                        str.add(actor.get(0).painter().centerY()+"");
                        str.add(actor.get(0).getNum()+"");
                        ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);//並傳送自己的資訊給所有人
                    }
                    break;
                case Global.InternetCommand.MOVE: //(2)完成MOVE指令的處理邏輯
                    for(int i=1;i<actor.size();i++) {
                        if(actor.get(i).getId()==Integer.parseInt(strs.get(0))) {
                           actor.get(i).painter().setCenter(Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                            actor.get(i).collider().setCenter(Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                           actor.get(i).walk(Global.Direction.getDirection(Integer.parseInt(strs.get(3))));
                           break;
                        }
                    }
                    break;
                case Global.InternetCommand.DISCONNECT: //(3)完成DISCONNENT的處理邏輯
                    for(int i=0;i<actor.size();i++){ //接收到有人斷現的訊息時，就從我們的Actor陣列中比對ID，移除該Actor
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
```

#### 4. 最後的主場景繪製

```java
//	記得在paint中把大家的角色都畫出來
 @Override
    public void paint(Graphics g) {
        for(int i=0;i<actor.size();i++){
            actor.get(i).paint(g);
        }
    }
```

# 

# 