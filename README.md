
### package
com.mmall
*   controller
*   service
*   dao：与数据交互，是一个接口，供 service 调用

*   vo：view object or value object
*   pojo：存放和 db 中字段一一对应的对象，通过 vo 进行封装后，返给 controller

*   util：工具包
*   common


### IDEA setting
*   jkd
*   maven
*   实时problem：Compiler->Make project automatically
*   @Autowire 报错：inspections ->spring-> spring core -> autowiring for bean class


### 静态代码块
静态代码块在类加载的时候执行，且只会执行一次。一般用于初始化静态变量。

执行顺序：静态代码块 > 普通代码块 > 构造代码块
````java
public class CodeBlock {
  // 静态代码块
  static {
    
  }
  // 普通代码块
  {
    
  }
  // 构造代码块，每次构造对象都会执行
  public CodeBlock() {
    
  }
}

````


