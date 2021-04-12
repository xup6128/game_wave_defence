# 地圖產生

可用在2D遊戲物件的位置布置與繪製

 ## 所需物件
1.地圖產生器

2.地圖載入器

## 1. 執行地圖產生器

將MapGenerator.jar,點擊兩次執行，出現已下照片

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/7cd2156a-eb59-485c-9ff4-3367fc2eca16/firstStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053345Z&X-Amz-Expires=86400&X-Amz-Signature=bcc5ba3b5922e854b456a9757c4e4cc69ed7ab07e601d6668bdd4aae62015f4d&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22firstStep.png%22)

### 設定地圖tile大小

1. Unit:每一個格子的像素大小，圖片中設定為32pixal。

2. map size : 寬 x 高 各有多少格子，圖片中設定為25*25格。
3. zoom : 如果你圖片太大，可以透過它縮放中間紅色格子區域的大小。

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/39c84b79-b341-475e-9107-493c36841e54/SecondStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053442Z&X-Amz-Expires=86400&X-Amz-Signature=457f605462cfdd0bd67d65294b18b625955b867c2f1d1f81466a3e0928576297&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22SecondStep.png%22)

### 加入圖片&&去除圖片

1.add(紅色框):選擇路徑加入地圖。

2.名稱(黑色框):設定名稱,這邊名稱不能設定相同。

3.物件畫的格數(黃色框):圖中1*1代表只會畫一格在右邊。

4.物件標定的顏色(藍色框):設定標定顏色:這邊顏色不能設定一樣。

5.eraser(宗色框):擦掉畫錯格子,如設定為2*2大小,需點選左上角框框才能清除。

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/a5882654-a9ed-4066-a109-310289178d2c/ThirdStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053726Z&X-Amz-Expires=86400&X-Amz-Signature=0c3033f3f1a8e354003ede70b5781bb1f04f4ac6462d2abf5809385b410a72c8&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22ThirdStep.png%22)

### 導出bmp圖,txt圖

import:可以導入之前畫好的bmp圖檔接續畫(直接bmp所在資料夾開啟)

export:選取要存取位置並按開啟，導出bmp圖檔&&txt檔(如要在存取相同位置，請將原本位置的檔案先移出，不然會重疊到)

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/b77ebd7d-87fa-4727-821e-1c304c8e0b7c/fourStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053819Z&X-Amz-Expires=86400&X-Amz-Signature=da8c361b15dcad64aaab33a5c2d7ac6b9044dfb98a92def4edff1636de40cf1f&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22fourStep.png%22)

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/022a4796-90f6-41bd-8830-18bb4ff6dd9d/fiveStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053850Z&X-Amz-Expires=86400&X-Amz-Signature=e97ce6c3e3170c5f1f70d913cac139eea63e717456e165b0d8287cef0d36526e&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22fiveStep.png%22)



## 2.執行地圖載入器

### 導入maploader package

ReadBmp : 讀取Bmp檔，用來獲取物件座標

ReadFile : 讀取txt檔，用來獲取物件名稱，和尺寸

mapLoader : 整合上面兩個的讀取資料

MapInfo : 儲存單一位置地圖物件的地圖資訊

!![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/ab6e8a82-207d-4243-a3e8-d984cdc2c112/MapInfo.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T053909Z&X-Amz-Expires=86400&X-Amz-Signature=c7db3e4fca9725f35cbd10d15a73630c35666f4876a05db998568febdaecfb4b&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22MapInfo.png%22)

### 將bmp,txt圖檔 丟進 resourse資料夾)

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/8e1a1317-43e9-4df1-af2d-ef278b34ef76/sixstep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T054028Z&X-Amz-Expires=86400&X-Amz-Signature=544477437858b59639953ea0d2ee6a5d5710f159f2c73203ea31dd12155d3cc4&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22sixstep.png%22)

## 在場景中創建MapLoader物件

1. (藍色框)創建Maploader物件(丟入bmp,txt)。

2. (紫色框)使用mapLoader中的combineInfo()方法產生MapInfo陣列。(test)  紫色框

3. (綠色框)進行name的比較並產生對應地圖物件   

4. (綠色框)傳入物件類別名、物件size、MapLoader產出的MapInfo Array、並實現接口實作產生物件之方法  5

5. (黃色框)創建出含有相對應圖片的類別實體    

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/9be2cfe0-3c11-4fd4-9300-88c560ad3128/sevenStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T054005Z&X-Amz-Expires=86400&X-Amz-Signature=49a0b41b3bf2897bdc9bc4ed0d06ba04e598e0b2e7de94caa669f570397e93fe&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22sevenStep.png%22)

   5.透過增加addAll()方法，加上要畫物件

![img](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/39162561-205e-4e93-b3b6-317a07624c8f/eightStep.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210412T055721Z&X-Amz-Expires=86400&X-Amz-Signature=3f701304ff435be9a61ac20e59611013c93e5f16f0d2e67865412140077b889f&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22eightStep.png%22)

