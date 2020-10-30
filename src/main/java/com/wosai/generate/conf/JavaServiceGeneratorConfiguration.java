package com.wosai.generate.conf;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyHolder;

public class JavaServiceGeneratorConfiguration  extends PropertyHolder {
    private String targetPackage;
 
    private String targetProject;
 
    /**
     *
     */
    public JavaServiceGeneratorConfiguration() {
        super();
    }
 
    public String getTargetProject() {
        return targetProject;
    }
 
    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }
 
    public String getTargetPackage() {
        return targetPackage;
    }
 
    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }
 
    public XmlElement toXmlElement() {
        XmlElement answer = new XmlElement("javaServiceGenerator"); //$NON-NLS-1$
 
        if (targetPackage != null) {
            answer.addAttribute(new Attribute("targetPackage", targetPackage)); //$NON-NLS-1$
        }
 
        if (targetProject != null) {
            answer.addAttribute(new Attribute("targetProject", targetProject)); //$NON-NLS-1$
        }
 
        addPropertyXmlElements(answer);
 
        return answer;
    }
 
 
}