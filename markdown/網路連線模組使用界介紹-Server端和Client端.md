# 網路連線模組使用界介紹

## Server類簡介

### 1.啟用Server:

```java
    Server.instance().create(12345);  
    Server.instance().start();
```
### 2.印出伺服器資訊:

```java
System.out.println("主機IP：" + Server.instance().getLocalAddress()[0] + "\n主機PORT：" + Server.instance().getLocalAddress()[1]);
```

### 3.關閉Server:

```java
	Server.instance().close();
```

- 備註:1.Server 是單例模式，使用Server.instance()方法便可取得Server實體。

  ​         2.Client連線到Server時，Server便會配一組ID給該Client，ID編號會從100開始上數，Client可以透過這組ID辨認訊息是從哪個Client所傳送。
  ​            Server收到訊息時，會廣播該訊息給所有還在線的Client。

  

## Client類簡介

### 1.連線至Server:

```java
// 連線
    try {
      ClientClass.getInstance().connect("192.168.1.29", 12345); // ("SERVER端IP", "SERVER端PORT")
    } catch (IOException ex) {
        Logger.getLogger(MainScene.class.getName()).log(Level.SEVERE, null, ex);
    }  //ip與port必須設定與開啟server的電腦一致
```

- 備註:啟用Server者，ip可以使用127.0.0.1來連接。

### 2.傳送指令**:使用**ClientClass**中的**sent方法**

```java
// commandCode 為指令編號
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

### 3.**接收指令**:透過ClientClass中的**consume**(CommandReceiver cr)方法

在遊戲中的update中使用consume方法接收訊息封包，根據收到的ID,指令編號,參數，去執行相對應的動作(由我們自己撰寫邏輯)。

- 使用範例:

  ```java
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