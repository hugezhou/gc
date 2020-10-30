package com.wosai.generate;

import com.wosai.generate.conf.JavaServiceGeneratorConfiguration;
import com.wosai.generate.conf.ServiceTemplateEntity;
import com.wosai.generate.util.FileUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BatisGeneratorCreate {

    public  static  Configuration config = null;

    public static void main(String[] args) throws Exception {
        //先清空test文件夹
        String classPath = BatisGeneratorCreate.class.getResource("/").getPath();
        String test = classPath.substring(0, classPath.lastIndexOf("/target")) + "/src/test";
        File testFile = new File(test);
        if (testFile.exists())
            FileUtils.deleteFile(testFile);
        File javaFile = new File(test + "/java");
        javaFile.mkdir();
        File testResources = new File(test + "/resources");
        testResources.mkdir();

        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        try {
//            File file = new File(BatisGeneratorCreate.class.getResource("/supervise/generatorConfig.xml").toURI());
//            File file = new File(BatisGeneratorCreate.class.getResource("/galaxy/generatorConfig.xml").toURI());
//            File file = new File(BatisGeneratorCreate.class.getResource("/djd/generatorConfig.xml").toURI());
            File file = new File(BatisGeneratorCreate.class.getResource("/loyalsafe/generatorConfig.xml").toURI());
            config = cp.parseConfiguration(file);

            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                    callback, warnings);
            myBatisGenerator.generate(null);
            warnings.forEach(warning -> System.out.println(warning));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}