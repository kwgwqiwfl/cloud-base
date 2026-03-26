package com.ring.welkin.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;
import com.ring.welkin.common.utils.XmlUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtilsTests {

    @Test
    public void test() throws Exception {
//        HdfsConf conf = HdfsConf.builder()
//                .properties(Lists.newArrayList(HdfsConfProperty.builder().name("a").value("1").build(),
//                        HdfsConfProperty.builder().name("b").value("2").build()))
//                .build();
//        String xml = XmlUtils.toXml(conf);
//        System.out.println(xml);
//
//        // 写到文件中
//        File file = new File("D://core-site.xml");
//        XmlUtils.write(file, conf);
//        HdfsConf fromXml = XmlUtils.fromXml(file, HdfsConf.class);
//        System.out.println(JsonUtils.toJson(fromXml));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JacksonXmlRootElement(localName = "configuration")
    public static class HdfsConf {

        @JacksonXmlProperty(localName = "property")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<HdfsConfProperty> properties;

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (ICollections.hasElements(properties)) {
                for (HdfsConfProperty p : properties) {
                    map.put(p.getName(), p.getValue());
                }
            }
            return map;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HdfsConfProperty {
        private String name;
        private Object value;
    }
}
