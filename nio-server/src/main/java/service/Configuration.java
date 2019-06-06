package service;

import org.dom4j.*;

import java.io.*;
import java.util.*;

public class Configuration {
    private static final String REDIRECTION_TYPE = "type";
    private static final String REDIRECTION_SOURCE = "source";
    private static final String REDIRECTION_TARGET = "target";
    private static Configuration config;
    private List<Redirection> redirectionList = new ArrayList<>();

    private Configuration() {
        load();
    }

    public Iterator<Redirection> getRedirectionList() {
        return redirectionList.iterator();
    }

    public static Configuration getConfiguration() {
        if (config == null) {
            config = new Configuration();
        }
        return config;
    }

    private void load() {
        String xmlStr = readFromXml();
        assert !xmlStr.isEmpty();
        try {
            Document document = DocumentHelper.parseText(xmlStr);

            Element rootElement = document.getRootElement();// 获取根节点
            for (Iterator<?> iterator = rootElement.elementIterator(); iterator.hasNext(); ) {
                Element element = (Element) iterator.next();
                Redirection redirection = new Redirection();
                redirection.setType(element.attributeValue(REDIRECTION_TYPE));
                redirection.setSource(element.attributeValue(REDIRECTION_SOURCE));
                redirection.setTarget(element.attributeValue(REDIRECTION_TARGET));
                redirectionList.add(redirection);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String readFromXml() {
        InputStream is = Configuration.class.getClassLoader().getResourceAsStream("redirection-config.xml");
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.lines().reduce((str1, str2)->str1+str2).orElse("");
    }

}
