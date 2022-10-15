package com.ymx.ibatis.pasexml;

import com.ymx.ibatis.config.Configuration;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 爱java的小于
 *
 * 解析配置文件
 */
public class ParseXml {
    private Map<String, SqlStatement> sqlMap;
    private Configuration configuration;
    private SAXReader reader = new SAXReader();

    public ParseXml(){}

    public SqlSet getMapper(){
        return new SqlSetMpl(this.sqlMap);
    }

    public Configuration getConfiguration(){
        return this.configuration;
    }

    public void read(InputStream stream) throws DocumentException, FileNotFoundException, ClassNotFoundException {
        //获得整个文档对象
        Document document = reader.read(stream);
        //获得跟元素
        Element root = document.getRootElement();
        //解析标签
        this.configuration = parseConfiguration(root.element("configuration"));
        this.sqlMap = parseMapper(root.element("mappers"));
    }

    /**
     * 读取主配置文件中的configuration标签
     * @param element
     * @return
     */
    private Configuration parseConfiguration(Element element){
        Element driverE = element.element("driver");
        Element urlE = element.element("url");
        Element userNameE = element.element("username");
        Element passwordE = element.element("password");
        Element maxActiveE = element.element("connection_max_active");
        Element initialSizeE = element.element("connection_initial_size");
        Element maxWaitTimeE = element.element("connection_wait_time");
        String driver = driverE == null ? null : (String)driverE.getData();
        String url = urlE == null ? null : (String)urlE.getData();
        String userName = userNameE == null ? null : (String)userNameE.getData();
        String password = passwordE == null ? null : (String)passwordE.getData();
        String maxActive = maxActiveE == null ? null : (String)maxActiveE.getData();
        String initialSize = initialSizeE == null ? null : (String)initialSizeE.getData();
        String maxWaitTime = maxWaitTimeE == null ? null : (String)maxWaitTimeE.getData();


        return new Configuration() {
            public String getDriver() {
                return driver;
            }
            public String getDataBaseUrl() {
                return url;
            }
            public String getUser() {
                return userName;
            }
            public String getPassword() {
                return password;
            }
            public String getInitialSize() {
                return initialSize;
            }
            public String getMaxActive() {
                return maxActive;
            }
            public String getTimeBetweenEvictionRunsMillis() {
                return maxWaitTime;
            }
        };
    }


    /**
     * 读取主配置文件中的mapper标签
     * @param element
     * @return
     */
    private Map<String, SqlStatement> parseMapper(Element element) throws DocumentException, FileNotFoundException, ClassNotFoundException {
        if(element == null){
            return null;
        }
        Map<String, SqlStatement> mapperMap = new HashMap<String, SqlStatement>();
        List<Element> elements = element.elements();
        for(Element ele : elements){
            Document document = this.reader.read
                    (getStream((String)ele.attribute("resource").getData()));
            Attribute resource = ele.attribute("resource");
            //合并每个mapper配置文件中的sql集合
            mapperMap.putAll(this.addToMap(document));
        }

        return mapperMap;
    }

    /**
     * 返回类路径下的文件的输入流
     * @param classPath
     * @return
     */
    private InputStream getStream(String classPath) throws FileNotFoundException {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(classPath);
        if(stream == null){
            throw new FileNotFoundException("识别不了路径:"+classPath);
        }

        return stream;
    }

    /**
     * 将文件中的sql语句以键值对的形式返回
     * @param document
     * @return Map
     */
    private Map<String, SqlStatement> addToMap(Document document) throws ClassNotFoundException {
        //创建map
        Map<String, SqlStatement> map = new HashMap<String, SqlStatement>();
        //获取根元素
        Element root = document.getRootElement();
        //获取根节点的namespace属性
        String namespace = (String) root.attribute("namespace").getData();
        //遍历根节点中的每个元素
        List<Element> elements = root.elements();
        for(Element ele : elements){
            String id = namespace+"."+((String)ele.attribute("id").getData());
            SqlStatement value = this.createMapper(ele,id);
            map.put(id,value);
        }

        return map;
    }

    /**
     * 根据sql的标签创建一个Mapper
     * @param element
     * @return SqlStatement
     */
    private SqlStatement createMapper(Element element, String id) throws ClassNotFoundException {
        Attribute resultType = element.attribute("resultType");
        String className = resultType == null ? null : (String) resultType.getData();
        Attribute keyName = element.attribute("keyName");
        String key = keyName == null ? null : (String) keyName.getData();
        String sql = (String)element.getData();
        Class c = null;
        if(className != null){
            try{
                c = Class.forName(className);
            }catch (ClassNotFoundException e){
                throw new ClassNotFoundException(className);
            }
        }
        SqlStatement re = new SqlStatement(sql,c,key,id);

        return re;
    }
}
