# Controllers

存在目的:以單例的方式集中管理性質相近且多數的資源

 ## 建議須被管理的資源
1.圖片(img controller)

2.音源(audio controller)

3.場景(scene controller)

## 1. 核心技巧-單例(已圖片為例)

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



### Scene controller

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

### Audio controller

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

    // 同樣音效連續撥放時只能停止最後一次
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



### 圖片該如何引入

在src 外層再開一個資料夾(需標記成Sources root)

![](https://github.com/YuWeiLGit/pic/blob/main/rsroot.png?raw=true)





### 在外部該如何使用

1.已img controll為例

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

