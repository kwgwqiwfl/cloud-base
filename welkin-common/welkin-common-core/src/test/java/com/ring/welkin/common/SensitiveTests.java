package com.ring.welkin.common;

import com.github.houbb.sensitive.annotation.strategy.*;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

@Slf4j
public class SensitiveTests {

    // @formatter:off
    private final String json = "{"
            + "\"id\":1,"
            + "\"username\":\"admin\","
            + "\"password\":\"1234`!@#$%^&*we56\","
            + "\"pwd\\\":\\\"1234`!@#$%^&*we56\\\","
            + "\"realname\":\"座山雕\","
            + "\"cardId\":\"6225800267798329\","
            + "\"email\":\"zuoshandiao@163.com\","
            + "\"idNo\":\"411323198708134633\","
            + "\"phone\":\"13520439383\""
            + "}";
    // @formatter:on

    @Test
    public void testRegexReplacement() throws Exception {
        String regex = "(pwd|Pwd|PWD|pass|Pass|PASS|password|Password|PASSWORD)(\\\\*\\\\?\":\\\\*\\\\?\")(['`~!@#$%^&*\\(\\)\\w]{1,})(\\\\?\")";
        RegexReplacement regexReplacement = RegexReplacement.createRegexReplacement(Pattern.compile(regex), "$1$2******$4");
        String format = regexReplacement.format(json);
        System.out.println(format);
    }

    @Test
    public void testSimpleMessage() throws Exception {
        System.out.println(json);
        log.debug(json);
    }

    @Test
    public void testObjectMessage() throws Exception {
        U u = U.builder().id(1L).username("admin").password("123456").realname("座山雕").email("zuoshandiao@badao.com")
                .cardId("6225800267798329").idNo("411323198708134633").phone("13520439383").build();
        log.info("u=>{}", u);
    }

    @Test
    public void test1() throws Exception {
        U u = U.builder().id(1L).username("admin").password("123456").realname("座山雕").email("zuoshandiao@badao.com")
                .cardId("6225800267798329").idNo("411323198708134633").phone("13520439383").build();
        String desJson = SensitiveUtil.desJson(u);
        log.debug(desJson);
    }

    @Getter
    @Setter
    @Builder
    public static class U {
        private Long id;
        private String username;

        @SensitiveStrategyPassword
        private String password;

        @SensitiveStrategyChineseName
        private String realname;

        @SensitiveStrategyEmail
        private String email;

        private String address;

        @SensitiveStrategyCardId
        private String cardId;

        @SensitiveStrategyIdNo
        private String idNo;

        @SensitiveStrategyPhone
        private String phone;

        @Override
        public String toString() {
            return "U [id=" + id + ", username=" + username + ", password=" + password + ", realname=" + realname
                    + ", email=" + email + ", address=" + address + ", cardId=" + cardId + ", idNo=" + idNo + ", phone="
                    + phone + "]";
        }
    }
}