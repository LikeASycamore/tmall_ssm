package generator;

/**
 * 逆向工程生成mapper映射，实体类，以及dao
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MybatisGenerator {
	public void generator() throws Exception{

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        System.out.println(this.getClass().getClassLoader().getResource("generatorConfig.xml").getPath());
        //指定 逆向工程配置文件
        File configFile = new File(this.getClass().getClassLoader().getResource("generatorConfig.xml").getPath()); 
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);
        System.out.println("生成代码成功");

    } 
    public static void main(String[] args) throws Exception {
    	
        try {
            MybatisGenerator generatorSqlmap = new MybatisGenerator();
            generatorSqlmap.generator();
        	
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
