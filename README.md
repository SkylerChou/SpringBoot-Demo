# 參考教學文章
[2023 iThome 鐵人賽 - Spring Boot 零基礎入門](https://ithelp.ithome.com.tw/users/20151036/ironman/6130)

## 注入 Bean 的方法 - `@Autowired`
一般的情況下，我們是可以直接使用 `@Autowired` 去注入 Bean 的
### 注意
1. 首先必須要確保 「自己也是一個 Bean」（即是有在 class 上面加上 `@Component`）
2. 並且 `@Autowired` 是透過 「變數的類型」 來注入 Bean 的

## 指定注入的 Bean 的名字：`@Qualifier`
是去指定要注入的 Bean 的「名字」是什麼，進而解決同時有兩個同樣類型的 Bean 存在的問題

> 名字自首要填小寫 `@Qualifier("hpPrinter")`

## 初始化 Bean 的方法：`@PostConstruct`
為這個 Bean 去進行初始化

### 注意
### 1. 使用 @PostConstruct 的注意事項之一：方法有特定格式
我們是可以在 class 中新增一個方法，然後在該方法上加上 `@PostConstruct`，這樣就可以在「該方法裡面」，去寫上初始化 Bean 的程式

不過這個「被加上 `@PostConstruct` 的方法」，他在宣告上也是有一些格式需要遵守的：

1. 這個方法必須是 public
2. 這個方法的返回值必須是 void
3. 這個方法「不能」有參數
4. 這個方法的名字可以隨意取，不影響 Spring Boot 運作

所以綜合以上 4 點的話，基本上這個「初始化 Bean 的方法」，通常就會長得像是下面這個樣子
```java
public void XXX();
```

### 2. 使用 `@PostConstruct` 的注意事項之二：一個 class 建議只有一個方法加上 `@PostConstruct`
在使用 `@PostConstruct` 去初始化 Bean 的時候，在同一個 class 中，建議一次只讓一個方法加上 `@PostConstruct`，不要同時在多個方法上，都加上 `@PostConstruct`

如果在同一個 class 中，同時有多個方法上面都加上 `@PostConstruct`，雖然 Spring Boot 程式仍舊是可以正常運行起來，但是我們無法知道 Spring Boot 會先執行哪一個方法去初始化 Bean，因此可能會造成程式邏輯的錯誤，並且後續也很難統一管理初始化的設定

因此就建議大家，在同一個 class 內，一次只使用一個 `@PostConstruct`，統一的去管理初始化的設定，這樣子不管是在維護上還是運作上，都是比較好的做法

## 讀取 Spring Boot 設定檔（application.properties）中的值：`@Value`

### 注意
### 1. 使用 `@Value` 的注意事項之一：需要遵守固定格式寫法
在使用 @Value 去讀取 Spring Boot 設定檔 (application.properties 檔案) 中的值的時候，一定要在 @Value 後面的括號中，寫上如下的格式：

```java
@Value("${XXXX}")
```

舉例來說，假設 application.properties 中的 key 為 printer.count

```java
@Value("${printer.count}")
private int count;
```

### 2. 使用 @Value 的注意事項之二：只有在 Bean 和 Configuration 中才能生效
只要是有使用到 @Value 的地方，該 class 本身得是一個 Bean、或是一個 Configuration 的設定 class，@Value 才能夠生效
> 補充：@Value 在那些帶有 @Configuration 的 class 中，也是能夠生效的

### 3. 使用 @Value 的注意事項之三：類型需要一致
在使用 @Value 去讀取 Spring Boot 設定檔 (application.properties 檔案) 中的值時，Java 中的變數的類型，必須要和 application.properties 中的類型一致才可以

舉例來說，在 application.properties 檔案裡面，我們定義了一組 key 和 value 如下：
```java
printer.count=5
```

上面這行程式，其實就是在暗示 printer.count 的值為「一個整數」，因此在 Spring Boot 程式中，我們就得將 @Value 加在一個「Int 或是 Long 類型」的變數上，這樣子在賦予值的時候才不會出現問題

```java
@Value("${printer.count}")
private int count;
```

#### 4. 使用 @Value 的注意事項之四：可以設定預設值
在使用 @Value 去讀取 Spring Boot 設定檔 (application.properties 檔案) 中的值時，有可能會發生一種情況，就是「該 key 不存在在 application.properties 檔案裡面」

如果大家想要避免這個問題的話，@Value 也是有提供另一種輔助方式可以讓我們使用，即是「設定預設值」

我們可以在 @Value 的 key 的後面，加上一個 :，並且在後面寫上想要的值，譬如說這裡寫上 200

```java
@Value("${printer.count:200}")
private int count;
```

## 什麼是 Spring AOP？

AOP 的全稱是 Aspect-Oriented Programming，中文是翻譯成「切面導向程式設計」或是「剖面導向程式設計」，**而 AOP 的概念，就是「透過切面，統一的去處理方法之間的共同邏輯」**

![img.png](img.png)

### 例子：測量時間的故事
如果我們想要去測量執行這個 print() 方法需要多長的時間，那我們可以在這個方法的最前面和最後面，去記錄當下的開始時間和結束時間，最後就可以去計算出 print() 方法總共執行了多久
![img_1.png](img_1.png)

雖然我們透過上面的寫法，是可以去測量 print() 方法的運作時間沒錯，但是大家如果觀察一下這段程式的話，就可以發現在這個 print() 方法裡面，充斥了許多跟「印東西」這個功能無關的程式

像是這個 print() 方法，他本來要做的事情，其實只是在 console 上面輸出「HP 印表機: ....」的這一行資訊而已，但是因為我們想要去測量時間，所以就加了很多不相關的程式進去，進而讓這個方法變得很複雜，不利於後續的程式維護

![img_2.png](img_2.png)

而且這樣子寫還會有一個衍生的問題，譬如說我們在 HpPrinter 裡面多新增了一個方法 printColor()，然後我們也想要去測量這個 printColor() 方法的時間的話，那我們就得複製貼上所有測量時間的程式到 printColor() 方法裡面

現在只有這兩個方法要測量時間，所以這樣複製貼上可能覺得還好，但是如果當很多方法都需要測量執行時間的時候，這樣子的複製貼上就不會是一個好選項

![img_3.png](img_3.png)

### 透過 AOP（切面）來輔助
我們可以來用一張圖來看一下，Spring AOP 是如何解決上面那個複製貼上的問題的

如果我們把剛剛的 HpPrinter 畫成圖的話，就會變成是下面這個樣子：

![img_4.png](img_4.png)

在上圖中呈現了 HpPrinter 中的 2 個方法：print() 和 printColor()，每一個箭頭代表的是一個方法，而箭頭右邊的程式碼就是這個方法裡面所寫的程式

在這張圖也可以看到，在這兩個方法裡面，最一開始都會去記錄方法的開始時間，後面則是去記錄方法的結束時間，最後並且把結束時間和開始時間相減，去取得這個方法的執行時間

那這時候，Spring AOP 就提出一個想法了，既然這些測量時間的程式是每個方法都要使用的共同邏輯，那我們就把這些程式，去獨立出來成一個「切面」，**由這個切面去橫貫所有的方法，替他們做測量時間的部分**

![img_5.png](img_5.png)

所以當我們使用了 Spring AOP 之後，我們就不用在方法裡面加上任何測量時間的程式了！我們只要將測量時間的共同邏輯，統一的交給切面去做處理，這個切面會去橫貫所有的的方法，分別去測量每一個方法的執行時間，所以每個方法就只要專注在各自要做的事情就好，世界和平！！

而這樣子使用切面的寫法，就會稱作是 **AOP**，也就是 **Aspect-Oriented Programming** (切面導向程式設計)


## 製造切面的方法：@Aspect

在使用 `@Aspect` 去創建新切面時，一定要特別注意，只有 Bean 才可以變成一個切面，所以換句話說的話，就是在使用 `@Aspect` 時，也要同時使用 `@Component`，將該 class 變成 Bean 的同時，切面的設定才會生效，如果只有在 class 上面加上 @Aspect 的話，是完全沒有任何效果的！

反正無論如何，大家只要記得，在使用切面時，**需要「@Component 和 @Aspect 要一起使用」就對了**

## 如何解讀 AOP 程式？

### 步驟一：先閱讀 @Before 小括號中的程式

在 `@Before` 後面的小括號中的程式，稱為「切入點 (Pointcut)」，即是去指定哪個方法要被切

舉例來說，假設我們想要測量的是 HpPrinter 中的方法的時間，那麼切面就是測量時間的程式，而 HpPrinter 中的方法，就是切入點 (Pointcut)

所以在 `@Before` 後面的小括號中的程式，即是去指定「切入點 (Pointcut)」，表示我們想要讓哪個方法，最後會被 MyAspect 這個切面所切

![img_6.png](img_6.png)

### 步驟二：查看前面的註解是什麼
確認好了切入點之後，接著就是查看前面所加上的註解是什麼，像是這邊所加上的，就是 @Before，而 @Before 的用途，就是指定要在小括號中的切入點 「執行前」，去執行下面的 before() 方法

所以簡單的說的話，前面的這個 @Before，他指定的就是 「時機點」，@Before 對應的就是切入點方法「執行前」執行，AOP 還有提供其他時機點的不同註解，像是 @After 和 @Around，後續也會再跟大家做介紹

因此在步驟二這裡，就是去確認「切面方法執行的時機點」

![img_7.png](img_7.png)

### 步驟三：要執行的切面方法

當我們確認好「切入點」和「時機點」之後，最後就是在下面的 before() 方法中，去撰寫切面的程式

像是在這裡我們就只在 before() 方法裡面寫上一行程式，去輸出「I'm before」的資訊到 console 上

![img_8.png](img_8.png)

### 小結:綜合上述三的步驟
所以綜合上述的三個步驟，就可以去解讀這一段 AOP 的程式的含義是什麼了

* 步驟一：我們指定了「切入點為 HpPrinter 中的所有方法」
* 步驟二：在切入點的方法「執行之前」
* 步驟三：執行下面的 before() 方法

### 其他用法：@After、@Around

在 Spring AOP 裡面，有三種時機點可以選擇：

* @Before：在方法「執行前」執行切面
* @After：在方法「執行後」執行切面
* @Around：在方法「執行前」和「執行後」，執行切面

### 切入點 (Pointcut) 怎麼撰寫？
在前面的程式中，我們有在 @Before 後面加上一段看起來很長的程式，那一段程式就是在指定方法的切入點為何
![img_9.png](img_9.png)

由於這個切入點寫起來還滿複雜的，而且使用頻率也不是很高，因此就建議大家有用到的時候再去查詢就可以了，以下提供幾種常見的寫法邏輯給大家：

1. 切入點為 com.example.demo.HpPrinter 底下的 print() 方法
```java
execution(* com.example.demo.HpPrinter.print())
```
2. 切入點為 com.example.demo.HpPrinter 底下的所有方法
```java
execution(* com.example.demo.HpPrinter.*(..))
```
3. 切入點為 com.example.demo 這個 package 中的所有 class 的所有方法（不包含子 package）
```java
execution(* com.example.demo.*(..))
```
4. 切入點為 com.example.demo 這個 package 及其底下所有子 package 中的所有 class 的所有方法
```java
execution(* com.example.demo..*(..))
```
5. 切入點為那些帶有 @MyAnnotation 的方法
```java
@annotation(com.example.demo.MyAnnotation)
```
### Spring AOP 的發展

pring AOP 以前最常被用在以下三個地方：

1. 權限驗證
2. 統一的 Exception 處理
3. log 記錄

但是由於 Spring Boot 發展逐漸成熟，因此上述這些功能，都已經被封裝成更好用的工具讓我們使用了，所以大家目前已經比較少直接使用 `@Aspect` 去創建一個切面出來了

舉例來說，像是權限驗證這一塊，我們就會改成使用 Spring Security 這個工具來完成，不過 Spring Security 的底層仍舊是透過 Spring AOP 來完成的，只是 Spring Security 把他封裝得更好、使用上更方便，因此在進行權限驗證時，使用 Spring Security 是更能提升大家開發的效率的

所以雖然 Spring AOP 已經漸漸淡出大家的日常使用，不過他作為 Spring 框架中的重要特性之一，還是常常生活在我們周邊的，只是我們可能感覺不太到而已XD

因此上述所介紹的 Spring AOP 的相關用法，大家就有個印象就可以了，重點是要把 AOP 的切面概念搞懂，至於其他的 `@Before`、`@After`、`@Around` 的用法，就有個印象就可以了

## Url 路徑對應：@RequestMapping

如果想要將這個 /test 的 url 路徑，去對應到 Spring Boot 的方法上的話，我們就只要使用 @RequestMapping 這個註解就可以達成了！

譬如說當我們在 MyController 中的 test() 方法上，去加上 @RequestMapping，並且在後面的小括號中指定 url 路徑的值 /test，這樣就可以成功將 url 路徑 /test 給對應到下面的 test 方法上

![img_10.png](img_10.png)

### 使用 @RequestMapping 的注意事項
在使用 @RequestMapping 去指定 url 路徑的對應時，是有一個非常重要的注意事項，**就是該 class 上面一定要上加 @Controller 或是 @RestController，否則 @RequestMapping 不會生效**

## @Controller 和 @RestController 的差別在哪裡？
@Controller 和 @RestController 的用途有點類似，不過他們是有一些細微的差別的，以下是他們的共同點和差別：
* 共同點：都可以將 class 變成 Bean、也都可以將裡面的 @RequestMapping 生效
* 差別：
    * @Controller：將方法的返回值自動轉換成 前端模板的名字
    * @RestController：將方法的返回值自動轉換成 Json 格式

> 在以前的古老時代，通常都是用 @Controller 來選擇要返回的前端模板給使用者，不過隨著前後端分離的盛行、以及 Json 格式的崛起，因此目前大部分的程式都是使用 @RestController 來實作了
> 所以大家後續如果需要使用的話，建議優先使用 @RestController 來回傳 Json 格式，而 @Controller 則是有搭配 jsp、thymeleaf...等等這類的前端模板引擎時，再考慮去使用

## 在 Spring Boot 中接住參數的四個註解
在 Spring Boot 中，有四個註解可以去接住前端的參數，分別是：

### 1. @RequestParam
接住那些放在 url 後面的參數

#### 如何運用 @RequestParam？
舉例來說，假設我們今天使用了 GET 請求，並且在 url 的最後面，加上一個 id=123 的參數的話，那麼實際的 url 就會長得像是這個樣子

```json
http://localhost:8080/test1?id=123
```

那麼當前端透過這個格式，傳遞 id=123 的參數的值來的話，在 Spring Boot 這邊，就只需要在 test1() 方法中，去新增一個 Integer 類型的參數 id，並且在這個參數的前面，去加上一個 `@RequestParam`

### 2. @RequestBody
就是去 **「接住放在 request body 中的參數」**，像是上面的 POST 方法在傳遞參數時，就是把參數放在 request body 中來傳遞

因此如果我們想要去接住這些放在 request body 中的參數的話，那麼就是要使用 @RequestBody 才能接住！

### 3. @RequestHeader
**接住那些放在 request header 中的參數**

### 4. @PathVariable
一樣是上面的例子，假設我們今天有一個 url 如下
```json
http://localhost:8080/test4/123
```
並且我們在 Spring Boot 裡面，想要去取得到 url 路徑 /test4/123 中的 123 的值的話，那我們必須要做以下這兩件事情：

首先是在使用 @RequestMapping 去定義 url 路徑時，先將 url 路徑定義成 "/test4/{id}"，這樣到時候 Spring Boot 就能夠把上述的 url，去給對應到 test4() 方法上

![img_11.png](img_11.png)

接著，就是在 test4() 的方法中，去添加一個 Integer 類型的參數 id，並且在這個參數的前面，去加上一個 `@PathVariable`

這樣到時候，這個 id 的值就會是 123，因此我們就成功的取得到 url 路徑中的 {id} 的值了！

### 小結：在 Spring Boot 中接住參數的四個註解
1. `@RequestParam`：接住放在 Url 後面的參數
2. `@RequestBody`：接住放在 request body 中的參數
3. `@RequestHeader`：接住放在 request header 中的參數
4. `@PathVariable`：取得放在 url 路徑中的值

## 滿足 RESTful API 的三個設計

1. 使用 Http method 來表示動作
2. 使用 url 路徑描述資源之間的階層關係
3. response body 返回 json 或是 xml 格式

![img_13.png](img_13.png)

> RESTful API 是指「你所設計的 API 符合 REST 風格」，而 RESTful API 的目的，**就是在「簡化工程師之間的溝通成本**」

### 範例 - CRUD 學生資料
![img_14.png](img_14.png)

### 常用的 RESTful API 註解
* @GetMapping
* @PostMapping
* @PutMapping
* @DeleteMapping

## [Http status code 中的分類](https://ithelp.ithome.com.tw/articles/10335795)
* 1xx：資訊
* 2xx : 成功
* 3xx : 重新導向
* 4xx : 前端請求錯誤
* 5xx : 後端處理有問題

## 什麼是 Spring JDBC？
Spring JDBC 的用途，就是讓我們 **「能夠在 Spring Boot 中執行 sql 語法，進而去操作資料庫」**

![img_15.png](img_15.png)

### 補充 1：Spring JDBC 和 Spring Data JPA 的差別在哪裡？

實際上 **「在 Spring Boot 中操作資料庫」** 這件事，是有許多工具可以選擇的，常見的操作資料庫的工具有：

* Spring JDBC
* MyBatis
* Spring Data JPA
* Hibernate

而在這些操作資料庫的工具中，又可以將他們分成兩類：
1. 在 Spring Boot 中執行 sql 語法，去操作資料庫
   * 這一類的工具，就是直接在 Spring Boot 中去執行原始的 sql 語法，然後透過這些 sql 語法去存取資料庫的數據這樣
   * Spring JDBC 和 MyBatis 都屬於這一類
2. 使用 ORM 的概念，去操作資料庫
   * 這一類的工具，則是會透過 ORM (Object Relational Mapping) 的概念，去操作資料庫
   * 所以只要使用這類的工具，基本上就很少寫 sql 語法了，而是會套用另一種新的概念（即是 ORM），去存取資料庫的數據
   * **Spring Data JPA** 和 **Hibernate** 都屬於這一類

所以回到最一開始的問題「Spring JDBC 和 Spring Data JPA 的差別在哪裡？」的話，就是 **Spring JDBC 是透過執行 sql 語法去操作資料庫，而 Spring Data JPA 則是透過 ORM 的概念去操作資料庫**

## 在 pom.xml 載入 Spring JDBC、資料庫 Driver

如果想要在 Spring Boot 中使用 Spring JDBC 的功能的話，首先會需要在 pom.xml 這個檔案裡面新增以下的程式，將 Spring JDBC 的功能給載入進來

```html
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```
除了加入 Spring JDBC 的功能之外，同時也得加入對應的資料庫 driver (驅動程式)，這樣 Spring Boot 後續才能去操作該資料庫

由于此系列文使用的是 MySQL 資料庫，因此大家可以在 pom.xml 檔案裡面，再新增以下的程式，將 MySQL 的 driver 給載入進來

```html
<dependency>
  <groupId>com.mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
  <version>8.0.33</version>
</dependency>
```

## Spring JDBC 根據 sql 分成兩大類：update 和 query
* 在 update 系列的方法中，可以去執行 INSERT、UPDATE、DELETE 這三種 sql 語法
* 而在 query 系列的方法中，只能執行 SELECT 這一種 sql 語法

![img_16.png](img_16.png)

### 補充：怎麼背哪個 sql 用哪個方法來執行？
`update()` 方法，他所代表的是「更新資料庫中的數據」的意思，而以下這三種情境
* INSERT sql：在資料庫中新增一筆數據
* UPDATE sql：修改資料庫中已存在的數據
* DELETE sql：刪除資料庫中的數據

`query()` 方法，他所代表的，則是「查詢資料庫中的數據」的意思，因此他就只會對應到 **SELECT sql**，專門負責去查詢資料庫中的資料

所以在使用 Spring JDBC 去執行 sql 語法時，是不用特別背哪一個 sql 要使用哪一個方法來執行的，只需要從名稱上面去推敲即可！

### update() 的基本用法

#### 步驟一：注入一個 NamedParameterJdbcTemplate

首先第一步，就是在你的 Bean 裡面，先去注入一個 NamedParameterJdbcTemplate 進來

因此我們就可以在 StudentController 裡面，先使用 `@Autowired`，去注入 NamedParameterJdbcTemplate 這個 Bean 進來

```java
@Autowired
private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
```

這個 NamedParameterJdbcTemplate 是 Spring JDBC 自動幫我們生成的 Bean，他會負責去處理和資料庫溝通的所有事項，因此我們後續就通通都會透過 NamedParameterJdbcTemplate，去幫我們執行 sql 語法

所以換句話說的話，只要你使用的是 Spring JDBC，那你基本上就都是在跟 NamedParameterJdbcTemplate 打交道，了解他所提供的用法有哪些

#### 步驟二：撰寫 sql 語法
注入好 NamedParameterJdbcTemplate 進來之後，接著第二步，就是去寫出想要執行的 sql 語法，所以我們就可以去創建一個 String 類型的變數 sql，並且在裡面寫上到時候想要執行的 sql 語法

像是下方就是去寫了一個 INSERT sql 出來，因此到時候 Spring JDBC 就會去執行這一條 sql，在 student table 中插入一筆「id 為 3、並且 name 為 John」的數據

```java
String sql = "INSERT INTO student(id, name) VALUES (3, 'John')";
```

#### 步驟三：新增一個 Map<String, object> 的 map 變數

接著第三步，是去新增一個類型為 Map<String, object> 的 map 變數出來，因此可以寫出如下的程式：

```java
Map<String, Object> map = new HashMap<>();
```

#### 步驟四：使用 update() 方法
當前面的步驟都完成之後，最後一步，就是去使用 namedParameterJdbcTemplate 的 `update()` 方法，並且把上面所新增的 sql 和 map 這兩個變數，依照順序的給傳進去

```java
namedParameterJdbcTemplate.update(sql, map);
```

只要完成了這四個步驟，到時候當前端請求過來，然後 Spring Boot 運行到第 24 行的 `update()` 方法時，這時候 `update()` 方法就會去執行 sql 參數中所儲存的 sql 語法（即是 `INSERT INTO student(id, name) VALUES (3, 'John')`），在 MySQL 資料庫中插入一筆新的數據了！

### update() 中的 map 參數用法
**「步驟三：新增一個 `Map<String, object>` 的 map 變數」**，我們當時是先跳過沒有介紹，現在可以回頭來介紹這個 map 變數的用法

這個 map 變數的用途，是去 **「放 sql 語法裡面的變數的值」**，這句話聽起來是有點抽象，所以我們可以直接透過一個例子來看一下 map 變數的用法

### 例子：根據前端的參數，動態的決定 sql 中的值
在上面那段程式中，我們是直接寫死一條 sql 語法 INSERT INTO student(id, name) VALUES (3, 'John') 在程式裡面，因此不管前端傳了什麼參數過來，我們始終都是只能夠在資料庫中，去新增一筆「id 為 3、並且 name 為 John」的數據

但是這樣子的寫法就非常的不彈性，因為如果我們想要改成是去新增一筆「id 為 4、name 為 Bob」的數據的話，就得停止先 Spring Boot，然後修改程式，然後再重新運行 Spring Boot，就會變得非常麻煩

因此，假設我們想要「動態的決定」當前 sql 語法中的值的話，那就需要依靠 map 這個變數來幫忙了！

### 前置準備
了解了 map 變數的大概用途之後，我們可以先做一些前置準備，去接住前端傳過來的參數

因此可以先創建一個 Student class 出來，並且在裡面創建兩個變數 id 和 name（和其對應的 getter 和 setter），程式如下：

```java
public class Student {

    private Integer id;
    private String name;

    // getter 和 setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

接著在 **StudentController** 裡，在 `insert()` 方法的參數部分，使用 `@RequestBody`，去接住前端傳過來的參數
![img_17.png](img_17.png)

### 修改 sql 語法、添加 map 變數中的值
接住前端所傳遞的變數之後，接著我們就可以來運用 map 變數，將前端所傳過來的 id 和 name 的值，插入一筆數據到資料庫中

這裡有兩個地方要修改：

1. **sql 語法**：將「3 和 John」的地方，改成「`:studentId` 和 `:studentName`」
2. **map 變數**：在 map 變數中 put 兩組 key-value 的值進去

![img_18.png](img_18.png)

在 Spring JDBC 裡面，只要在 sql 語法中加上了「:」，就表示這是一個「sql 中的變數」

* 譬如說像是 `:studentId`，就表示我們指定這是一個 sql 中的變數，名字叫做 studentId
* 而又像是 `:studentName`，就表示我們指定這又是另一個 sql 中的變數，名字則是叫做 studentName

因此我們也是可以透過這個邏輯，在同一句 sql 語法裡面，添加無數個 sql 中的變數的，只需要在名字前面加上「:」即可

而在 sql 中的這些 `:studentId`、`:studentName` 變數，他們的值，我們就可以在 map 裡面去做指定

在使用 map 變數時，前面要放的是「sql 變數的名字」，後面放的則是「這個 sql 變數的值是多少」

譬如說我們想要指定 `:studentId` 這個 sql 變數的值是 5 的話，那麼就可以寫成是：

```java
map.put("studentId", 5);
```
又或者是我們想要指定 `:studentId` 這個 sql 變數的值是 10 的話，那麼就可以寫成是：

```java
map.put("studentId", 10);
```
再或者，我們想要指定 `:studentId` 這個 sql 變數的值，是「前端傳過來的 id 的值」的話，那麼就可以寫成是：

```java
map.put("studentId", student.getId());
```
因此我們就可以透過 map 變數，動態的去決定 sql 中的變數的值了！

![img_19.png](img_19.png)

### 小結：update() 方法的用法
所以總結來說，想要使用 `update()` 方法，去執行 **INSERT、UPDATE、DELETE** 這三種 sql 的話，只要填入以下兩個參數即可：

1. sql 參數：放想要執行的 sql 語法
2. map 參數：動態的決定 sql 變數中的值

![img_20.png](img_20.png)