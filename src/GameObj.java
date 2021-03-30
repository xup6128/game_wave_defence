//管理遊戲物件的抽象父類
public abstract class GameObj implements GameKernel.UpdateInterface,GameKernel.PaintInterface{
    //共性屬性
    private int x;//圖片座標
    private int y;
    private int width; //圖片寬
    private int height; //圖片高
    public GameObj(int x,int y,int width,int heigh){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=heigh;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    //取得物件上下左右的方法
    public int getTop(){
        return this.y;
    }
    public int getBottom(){
        return this.y+this.height;
    }
    public int getLeft(){
        return this.x;
    }
    public int getRight(){
        return this.x+this.width;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    //offset方法
    public void offSetX(int x){
        this.x+=x;
    }
    public void offSetY(int y){
        this.y+=y;
    }
    //共性方法
    //偵測碰撞
    public boolean isColission(GameObj obj){
        if(this.getTop()>obj.getBottom()){return false;}
        if(this.getBottom()<obj.getTop()){return false;}
        if(this.getLeft()>obj.getRight()){return false;}
        if(this.getRight()<obj.getLeft()){return false;}
        return true;
    }

}
