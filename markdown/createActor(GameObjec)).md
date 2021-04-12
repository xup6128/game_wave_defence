

##  創建腳色

 ###  腳色繼承抽象類別GameObject（實踐方法如下）

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

### 基本腳色碰撞AABB

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

### 物件是否超出鏡頭

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

### Debug模式時

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



