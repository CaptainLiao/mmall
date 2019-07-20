## mybatis 三剑客

### mybatis-generator 插件
自动根据数据库生成 pojo，dao 和对应的 xml 文件。
*   pojo 存放和 db 中字段一一对应的对象
*   dao 存放操作数据库数据的接口，供 service 调用
*   xml 是 dao 层接口的实现，sql 语句都写在 xml 里面

首先在 pom.xml 中引入 mybatis.generator 插件。

然后在 resources/ 中创建 generatorConfig.xml。

### mybatis-plugin
idea 中的 mybatis-plugin 插件，可以方便我们查看接口和对应的实现。

### mybatis-pagehelper 
[分页插件](https://github.com/pagehelper/Mybatis-PageHelper)





















