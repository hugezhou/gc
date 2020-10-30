package com.wosai.generate.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import java.util.List;
import java.util.Properties;

/**
 * Created by LW on 2018/1/27
 * generatorConfig.xml中table标签用%匹配表名时自动生成别名
 * 每个表配置一条table时无需使用本插件，直接指定"alias"属性即可
 */
public class AliasPlugin extends PluginAdapter {
    private String type;
    private int length;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        type = properties.getProperty("type") != null ? properties.getProperty("type") : "1";
        length = properties.getProperty("length") != null ? Integer.parseInt(properties.getProperty("length")) : 2;
        System.out.println(type + ":" + length);
    }

    /**
     * 初始化
     * xml中不生"成ResultMapWithBLOBs"和"Blob_Column_List"，全部放在"BaseResultMap"和"Base_Column_List"中
     * 定制别名
     */
    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.getBaseColumns().addAll(introspectedTable.getBLOBColumns());
        introspectedTable.getBLOBColumns().clear();
        transRealAlias(introspectedTable);
    }

    /**
     * 去除BaseResultMap中的列前缀
     */
    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        element.getElements().forEach(e -> {
            if (e instanceof XmlElement) {
                List<Attribute> list = ((XmlElement) e).getAttributes();
                for (Attribute attr : list) {
                    if ("column".equals(attr.getName())) {
                        String value = attr.getValue().substring(attr.getValue().indexOf('_') + 1);
                        list.remove(attr);
                        list.add(new Attribute("column", value));
                        break;
                    }

                }
            }
        });
        return true;
    }

    /**
     * 转换别名
     */
    public void transRealAlias(IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        String[] names = tableName.split("_");
        String alias = "";
        switch (type) {
            case "1"://首字母相加模式
                if (names.length == 1)
                    alias = tableName.substring(0, 1);
                else {
                    for (String name : names) {
                        alias += name.substring(0, 1);
                    }
                }
                break;
            case "2"://表名为一个单词的使用全名，多个单词使用首字母相加
                if (names.length == 1)
                    alias = tableName;
                else
                    for (String name : names) {
                        alias += name.substring(0, 1);
                    }
                break;
            case "3"://表名为一个单词的从头截取?个字符，长度不够则取全名，多个单词使用首字母相加
                if (names.length == 1) {
                    if (length > tableName.length())
                        length = tableName.length();
                    alias = tableName.substring(0, length);
                } else
                    for (String name : names) {
                        alias += name.substring(0, 1);
                    }
                break;
            case "4"://全名
                alias = tableName;
                break;
        }
        final String realAlias = alias;
        //主键
        introspectedTable.getPrimaryKeyColumns().forEach(column -> {
            column.setTableAlias(realAlias);
        });
        //其它列
        introspectedTable.getBaseColumns().forEach(column -> {
            column.setTableAlias(realAlias);
        });
    }
}
