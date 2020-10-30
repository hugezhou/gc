package com.wosai.generate.plugin;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by LW on 2018/3/2
 * 添加自定义的BaseEntity用于model的继承，删除model中的一些字段，这些字段在BaseEntity中定义
 * 使用lombok注解，删除所有setter、getter方法
 */
public class ModelPlugin extends PluginAdapter {

    private String superClass;

    private String isSamePackage;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        superClass = properties.getProperty("superClass");
        isSamePackage = properties.getProperty("isSamePackage");
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //todo===============山东联通临时修改, 在model名字后面加上Entity=================
        Class<TopLevelClass> tableClass = TopLevelClass.class;
        try {
            java.lang.reflect.Field typeField = tableClass.getSuperclass().getDeclaredField("type");
            typeField.setAccessible(true);

            FullyQualifiedJavaType tableType = (FullyQualifiedJavaType) typeField.get(topLevelClass);

            java.lang.reflect.Field baseShortNameField = FullyQualifiedJavaType.class.getDeclaredField("baseShortName");
            java.lang.reflect.Field baseQualifiedNameField = FullyQualifiedJavaType.class.getDeclaredField("baseQualifiedName");
            baseShortNameField.setAccessible(true);
            baseQualifiedNameField.setAccessible(true);
            baseShortNameField.set(tableType, baseShortNameField.get(tableType).toString());
            baseQualifiedNameField.set(tableType, baseQualifiedNameField.get(tableType).toString());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //==================================================

        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        List<String> columnNames = new ArrayList<>();
        for (IntrospectedColumn column : columns) {
            columnNames.add(column.getActualColumnName());
        }

        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            if ("false".equals(isSamePackage))
                topLevelClass.addImportedType(superClass);
        }
        //有这三个字段的继承BaseEntity,删除这三个字段和id字段
        /*-
        if (columnNames.contains("create_date") && columnNames.contains("update_date") && columnNames.contains("expire_date")) {
            removeField(topLevelClass.getFields());
            //添加继承
            if (superClass != null) {
                topLevelClass.setSuperClass(superClass);
                if ("false".equals(isSamePackage))
                    topLevelClass.addImportedType(superClass);
            }
        } else {//把id的@GeneratedValue注解改一下，原来是@GeneratedValue(strategy = GenerationType.IDENTITY)
            //topLevelClass.getFields().get(0).getAnnotations().add("@GeneratedValue(generator = \"JDBC\")");
            topLevelClass.getFields().get(0).getAnnotations().clear();
        }*/
//            topLevelClass.getFields().get(0).getAnnotations().clear();
        //去除所有方法，使用lombok注解
        topLevelClass.getMethods().clear();

        topLevelClass.addImportedType("lombok.Getter");
        topLevelClass.addImportedType("lombok.Setter");
        topLevelClass.addImportedType("lombok.ToString");
        topLevelClass.addAnnotation("@Getter");
        topLevelClass.addAnnotation("@Setter");
        topLevelClass.addAnnotation("@ToString");
        topLevelClass.addImportedType("java.io.Serializable");
        FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType("Serializable");
        topLevelClass.addSuperInterface(fullyQualifiedJavaType);

        String id = "";
        List<Field> fields = topLevelClass.getFields();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if(!id.equals("")){
                break;
            }
            List<String> stringList = field.getAnnotations();
            for (int j = 0; j < stringList.size(); j++) {
                String str = stringList.get(j);
                if (str.equals("@Id")) {
                    id = field.getName();
                }
            }
        }

        /*- 构造函数中添加id生成
        topLevelClass.addImportedType("com.wosai.djd.utils.ID");
        String full = topLevelClass.getType().getFullyQualifiedName();
        String[] ss = full.split("\\.");
        Method method = new Method(ss[ss.length - 1]);
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);

        method.addBodyLine(id + "=ID.next();");

        topLevelClass.addMethod(method);
        */
/*-
        List<Method> all = topLevelClass.getMethods();
        if (CollectionUtils.isNotEmpty(all)) {
            System.out.println(all.size());
            for (int i = 0; i < all.size(); i++) {
                Method method = new Method();
                if(method.isConstructor()){
                    System.out.println("is Constructor");
                }
            }
        }
*/
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Class<TopLevelClass> tableClass = TopLevelClass.class;
        try {
            java.lang.reflect.Field typeField = tableClass.getSuperclass().getDeclaredField("type");
            typeField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Component"));
        interfaze.addAnnotation("@Component");
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    public void removeField(List<Field> fieldList) {
        removeField(fieldList, "id");
        removeField(fieldList, "createDate");
        removeField(fieldList, "updateDate");
        removeField(fieldList, "expireDate");
    }

    public void removeField(List<Field> fieldList, String columnName) {
        for (int i = 0; i < fieldList.size(); i++) {
            if (fieldList.get(i).getName().equals(columnName)) {
                fieldList.remove(i);
            }
        }
    }
}
