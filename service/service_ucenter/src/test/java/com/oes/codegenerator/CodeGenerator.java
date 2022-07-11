    package com.oes.codegenerator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

// 代码生成器
public class CodeGenerator {

    @Test
    public void run() {
        // 1、创建代码生成器
        AutoGenerator codeGenerator = new AutoGenerator();

        // 2、全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/src/main/java");

        globalConfig.setAuthor("oes");
        globalConfig.setOpen(false); //生成后是否打开资源管理器
        globalConfig.setFileOverride(false); //重新生成时文件是否覆盖
        globalConfig.setServiceName("%sService");    //去掉Service接口的首字母I
        globalConfig.setIdType(IdType.ID_WORKER_STR); //主键策略
        globalConfig.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
        globalConfig.setSwagger2(true);//开启Swagger2模式

        codeGenerator.setGlobalConfig(globalConfig);

        // 3、数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        // 设置jdbc连接
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/oes?serverTimezone=GMT%2B8");
        // 设置驱动类
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        // 设置数据库用户名
        dataSourceConfig.setUsername("root");
        // 设置数据库密码
        dataSourceConfig.setPassword("52583344zh?");
        // 设置数据库为MySQL数据库
        dataSourceConfig.setDbType(DbType.MYSQL);
        // 完成数据源配置
        codeGenerator.setDataSource(dataSourceConfig);

        // 4、包配置
        PackageConfig packageConfig = new PackageConfig();
        //模块名
        packageConfig.setModuleName("ucenter");
        // 包：com.oes.service_edu
        packageConfig.setParent("com.oes");
        //控制器包
        packageConfig.setController("controller");
        //实体类包
        packageConfig.setEntity("entity");
        //service包
        packageConfig.setService("service");
        //mapper包
        packageConfig.setMapper("mapper");
        codeGenerator.setPackageInfo(packageConfig);

        // 5、策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setInclude("ucenter_member");
        //数据库表映射到实体的命名策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        //生成实体时去掉表前缀
        strategyConfig.setTablePrefix(packageConfig.getModuleName() + "_");
        //数据库表字段映射到实体的命名策略
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //使用lombok注解@Accessors(chain = true) setter链式操作
        strategyConfig.setEntityLombokModel(true);

        //restful api风格控制器
        strategyConfig.setRestControllerStyle(true);
        //url中驼峰转连字符
        strategyConfig.setControllerMappingHyphenStyle(true);
        codeGenerator.setStrategy(strategyConfig);

        // 6、执行
        codeGenerator.execute();
    }
}
