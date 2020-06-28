### spring-boot+shiro+mybatis API开发使用基础框架

>**关于SpringBoot和Shiro的依赖参考**[spring-boot+shiro+jpa集成](https://blog.csdn.net/qq_32530561/article/details/106942187)
#### Mybatis依赖过程
##### 添加依赖
```xml
      <!--数据库连接驱动-->
      <dependency>
          <groupId>mysql</groupId>
          <artifactId>mysql-connector-java</artifactId>
          <version>8.0.15</version>
      </dependency>
<!--      mybatis-->
      <dependency>
          <groupId>org.mybatis.spring.boot</groupId>
          <artifactId>mybatis-spring-boot-starter</artifactId>
          <version>2.0.1</version>
      </dependency>
```
##### 自动生成XML、Entity、Mapper功能实现
添加自动生成插件
```xml
 <!-- mybatis-generator 代码自动生成 -->
          <plugin>
              <groupId>org.mybatis.generator</groupId>
              <artifactId>mybatis-generator-maven-plugin</artifactId>
              <version>1.3.6</version>
               <!--配置属性-->
			<!--<configuration>-->
              <!--默认文件名为generatorConfig.xml,文件名若为其它，需在这里配置-->
<!--<configurationFile>src/main/resources/generatorConfig.xml</configurationFile>-->
<!--              </configuration>-->
              <dependencies>
                  <dependency>
                      <groupId>mysql</groupId>
                      <artifactId>mysql-connector-java</artifactId>
                      <version>8.0.15</version>
                  </dependency>
                  <dependency>
                      <groupId>com.itfsw</groupId>
                      <artifactId>mybatis-generator-plugin</artifactId>
                      <version>1.3.2</version>
                  </dependency>
              </dependencies>
          </plugin>
```
`reousrces`文件下新建`generatorConfig.xml`,内容如下：
```xml
<?xml version="1.0" encoding="UTF-8" ?><!DOCTYPE generatorConfiguration PUBLIC
        "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
<generatorConfiguration>
    <context id="context" targetRuntime="MyBatis3">
        <!-- 非官方插件 https://github.com/itfsw/mybatis-generator-plugin -->
        <!-- 查询单条数据插件 -->
        <plugin type="com.itfsw.mybatis.generator.plugins.SelectOneByExamplePlugin"/>
        <!-- 查询结果选择性返回插件 -->
        <plugin type="com.itfsw.mybatis.generator.plugins.SelectSelectivePlugin"/>
        <!-- Example Criteria 增强插件 -->
        <plugin type="com.itfsw.mybatis.generator.plugins.ExampleEnhancedPlugin"/>
        <!-- 数据Model属性对应Column获取插件 -->
        <plugin type="com.itfsw.mybatis.generator.plugins.ModelColumnPlugin"/>
        <!-- 逻辑删除插件 -->
        <plugin type="com.itfsw.mybatis.generator.plugins.LogicalDeletePlugin">
            <!-- 这里配置的是全局逻辑删除列和逻辑删除值，当然在table中配置的值会覆盖该全局配置 -->
            <!-- 逻辑删除列类型只能为数字、字符串或者布尔类型 -->
            <property name="logicalDeleteColumn" value="deleted"/>
            <!-- 逻辑删除-已删除值 -->
            <property name="logicalDeleteValue" value="1"/>
            <!-- 逻辑删除-未删除值 -->
            <property name="logicalUnDeleteValue" value="0"/>
        </plugin>

        <commentGenerator>
            <property name="suppressAllComments" value="false" />
            <property name="suppressDate" value="true" />
        </commentGenerator>
		
		<!--数据库连接配置-->
        <jdbcConnection userId="root" password="123456" driverClass="com.mysql.cj.jdbc.Driver" connectionURL="jdbc:mysql://localhost:3306/framework?serverTimezone=UTC&amp;useUnicode=true&amp;characterEncoding=utf-8
&amp;useSSL=true" />

        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>
		
		<!--实体类配置-->
        <javaModelGenerator targetPackage="com.huchx.entity" targetProject="src\main\java">
            <property name="enableSubPackages" value="false" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
		<!--Mapper.xml配置-->
        <sqlMapGenerator targetPackage="com.huchx.mapper" targetProject="src\main\resources">
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>
		
		<!--Mapper配置-->
        <javaClientGenerator targetPackage="com.huchx.mapper" type="XMLMAPPER" targetProject="src\main\java">
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>
		<!--根据数据库表生成对应的实体类/xml文件/mapper-->
        <table tableName="m_user" domainObjectName="MUserEntity" mapperName="ShiroMapper" sqlProviderName="ShiroMapper"  enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false">
            <generatedKey column="id" sqlStatement="MySql" identity="true"/>
        </table>
    </context>
</generatorConfiguration>
```
配置mvn命令`mybatis-generator:generate -e`
![配置mvn命令符](https://img-blog.csdnimg.cn/20200628141611372.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMyNTMwNTYx,size_16,color_FFFFFF,t_70)
#### 代码修改
在Application文件中添加自动扫描mapper
```java
@EntityScan("com.huchx.entity")
@MapperScan("com.huchx.mapper")
@SpringBootApplication
public class Application
{
    public static void main( String[] args )
    {
        SpringApplication.run(Application.class,args);
    }
}
```
>==**提示:**==
>1. 需要注意`mysql-connector-java` ` 5.x`版本和`8.x`版本的区别
>2. 推荐一个Idea的插件`MyBatisX`,可以快速查看mapper中方法与xml中的方法对应关系
#### 分页功能
添加依赖
```xml
<!--      mybatis分页-->
      <dependency>
          <groupId>com.github.pagehelper</groupId>
          <artifactId>pagehelper-spring-boot-starter</artifactId>
          <version>1.2.5</version>
      </dependency>
```
添加配置
```yml
########## 分页插件 ##########
pagehelper:
  helper-dialect: mysql
  params=count: countSql
  reasonable: false
  support-methods-arguments: true
```
使用方法
```java
  public PageInfo<MUserEntity> findUserByPage() {
        PageHelper.startPage(0,10);
        PageInfo<MUserEntity> pageInfo = new PageInfo<>(userMapper.findAll());//自动将查询到的数据装配到分页对象中
        return pageInfo;
    }
```
>demo地址：[https://github.com/huiyiwu/springboot-shiro-mybatis-framework](https://github.com/huiyiwu/springboot-shiro-mybatis-framework)