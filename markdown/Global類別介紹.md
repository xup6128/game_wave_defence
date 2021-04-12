Global類別介紹

需要共用或是重覆使用的東西、資料都可以設為static放在global類別內，減少記憶體使用。

Global中的 InternetCommand:

在使用網路連線封包時會因為連線狀態不同而需要做不同的事情，因此需要定義目前連線狀態以便後續動作執行

```java
public class Global {
		public class InternetCommand{
				public static final int CONNECT = 0;
				public static final int MOVE = 1;
				public static final int DISCONNECT = 2;
		}
}
ClientClass.getInstance().consume(new CommandReceiver() {
                @Override
                public void receive(int serialNum, int internetcommand, ArrayList<String> strs) {
                    switch(**internetcommand**){
                        case **Global.InternetCommand.CONNECT:**
                            System.out.println("Connect " + serialNum);
                            boolean isBorn = false;
                            for (int i = 0; i < aliens.size(); i++) {
                                if (aliens.get(i).getId() == serialNum) {
                                    isBorn = true;
                                    break;
                                }
                            }
                            if(!isBorn) {
                                aliens.add(new Alien(Integer.valueOf(strs.get(0)), Integer.valueOf(strs.get(1)), Integer.valueOf(strs.get(2))));
                                aliens.get(aliens.size() - 1).setId(serialNum);
                                ArrayList<String> str=new ArrayList<>();
                                str.add(aliens.get(0).painter().centerX()+"");
                                str.add(aliens.get(0).painter().centerY()+"");
                                str.add(aliens.get(0).getNum()+"");
                                ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);
                            }
                            break;
                        case **Global.InternetCommand.MOVE:**
                            for(int i=1;i<aliens.size();i++) {
                                if(aliens.get(i).getId()==Integer.valueOf(strs.get(0))) {
                                    aliens.get(i).painter().setCenter(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2)));
                                    if(aliens.get(i).getHorizontalDir() == Global.Direction.LEFT || aliens.get(i).getHorizontalDir() == Global.Direction.RIGHT) {
                                        aliens.get(i).setHorizontalDir(Global.Direction.getDirection(Integer.valueOf(strs.get(3))));
                                    }
                                    if(aliens.get(i).getVerticalDir() == Global.Direction.DOWN || aliens.get(i).getVerticalDir() == Global.Direction.UP){
                                        aliens.get(i).setVerticalDir(Global.Direction.getDirection(Integer.valueOf(strs.get(3))));
                                    }
                                    break;
                                }
                            }
                            break;
                        case **Global.InternetCommand.DISCONNECT:**
                            for(int i=0;i<aliens.size();i++){
                                if(aliens.get(i).getId()==Integer.valueOf(strs.get(0))){
                                    aliens.remove(i);
                                }
                            }
                            break;
                    }
                }
            });
        }
```

Global中的 Direction:

若角色(玩家、敵人...等)需要用到方位判斷時，就會需要用到Direction，因此寫在global方便使用。

並在Direction enum中給予每個實體數值，以便CommandSolver的add(int key, int command)方法使用。

並在Direction enum中寫一個getDirection(int valus)方法，讓使用者可以用傳入的int值取得方位。

```java
public class Global {
		public enum Direction {
        UP(3),
        DOWN(0),
        LEFT(1),
        RIGHT(2),
        NO_DIR(4);
        private int value;

        Direction(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }

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

Global中的 isDebug:

藉由設定IS_DEBUG = true 可以得到各個物件的painter框，以利debug。

```java
public class Global {
		public static final boolean IS_DEBUG = true;
}
```

Global中的靜態常數：

將常數設在global以減少記憶體消耗。

```java
**//視窗大小**
public static final int WINDOW_WIDTH = 960;
public static final int WINDOW_HEIGHT = 640;
**// 資料刷新時間**
public static final int UPDATE_TIMES_PER_SEC = 60; //每秒更新60次遊戲邏輯
public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC; // 每一次要花費的奈秒數
**// 畫面更新時間**
public static final int FRAME_LIMIT = 60; //每秒更新60次畫面
public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT; //每一次要花費的奈秒數
```

Global中的靜態方法：

將常用統一的方法設在global以減少記憶體消耗。

```java
**//產生亂數**
public static int random(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
}
**//算機率**
public static boolean random(int rate) {
		return random(1, 100) <= rate;
}
```