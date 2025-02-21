package com;

import com.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableElasticsearchRepositories(basePackages = "com.es")
//@EnableJpaRepositories(basePackages = {"com.dao", "com.pojo"})
public class Application {
    //    static {
//        PortUtil.checkPort(9300,"ElasticSearch 服务端",true);
//        PortUtil.checkPort(5601,"Kibana 工具", true);
//    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}