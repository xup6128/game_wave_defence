# GameKernel使用簡介

GameKernel使用建造者模式來創建

### GameKernel的Builder有五個設定初始化的方法:

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

### 產生GameKernel的方法-gen()

```java
//使用gen方法產生遊戲核心物件
GameKernel gameKernel = new GameKernel.Builder().gen();  //gen方法用在最後產生遊戲核心物件
```

### 實際使用範例:

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



# CommandSolver使用簡介

可以用來監聽滑鼠和鍵盤的工具。

## 使用方式

CommandSolver使用建造者模式創建物件。所以在使用時要使用其內部類**BuildStream**。

### 監聽滑鼠-mouseTrack()

假如要在某個場景中監聽滑鼠，使用範例如下:

```java
GameKernel gameKernel = new GameKernel.Builder().input(  //在GameKernel的input中
    
				//使用CoCommandSolver.BuildStream()的mouseTrack()，並subscribe場景
                new CommandSolver.BuildStream().mouseTrack().subscribe(sceneController)
    
        ).paint(sceneController).update(sceneController).gen();
```

## 監聽鍵盤-keyboardTrack()

注意，keyboardTrack()後要接一個next()後再訂閱場景。

```java
GameKernel gameKernel = new GameKernel.Builder().input(  //在GameKernel的input中
    
				//使用CoCommandSolver.BuildStream()的keyboardTrack().next()，並subscribe場景
                new CommandSolver.BuildStream().keyboardTrack().next().subscribe(sceneController)
                
        ).paint(sceneController).update(sceneController).gen();
```

### 1.加入鍵盤按鍵設定

使用keyboardTrack()的**add方法**，就可以設置按鍵對應的指令碼，操作範例如下:

```java
//例如，設置Enter鍵的指令為1
GameKernel gameKernel = new GameKernel.Builder().input(  //在GameKernel的input中
				//使用CoCommandSolver.BuildStream()的keyboardTrack()
                new CommandSolver.BuildStream().keyboardTrack().add(KeyEvent.VK_ENTER, 0) //設置ENTER按鍵為 -1
                        .next().subscribe(sceneController)                                //add方法參數包含:
																						   //1.從KeyEvent中選擇對應的電腦按鍵
        ).paint(sceneController).update(sceneController).gen();                            //2.自定義對應的commandCode
```

### 2.按鍵指令碼的使用方式:

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

