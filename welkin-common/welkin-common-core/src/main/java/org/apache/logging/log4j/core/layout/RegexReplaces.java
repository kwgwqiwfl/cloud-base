package org.apache.logging.log4j.core.layout;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.pattern.RegexReplacement;

/**
 * 自定义标签replaces， 用于多个正则表达式替换
 */
@Slf4j
@Plugin(name = "RegexReplaces", category = Node.CATEGORY, printObject = true)
public final class RegexReplaces {
    // replace标签，复用log4j已有plugin， replaces 下可以0，1，多个replace
    private final RegexReplacement[] replaces;

    private RegexReplaces(RegexReplacement[] replaces) {
        this.replaces = replaces;
    }

    public RegexReplacement[] getReplaces() {
        return replaces;
    }

    /**
     * 格式化输出日志信息， 此方法会执行多个正则表达式匹配与替换
     *
     * @param msg
     * @return
     */
    public String format(String msg) {
        for (RegexReplacement replace : replaces) {
            msg = replace.format(msg);
        }
        return msg;
    }

    @PluginFactory
    public static RegexReplaces createRegexReplacement(@PluginElement("replaces") final RegexReplacement[] replaces) {
        if (replaces == null) {
            log.info("no replaces is defined");
            return null;
        }
        if (replaces.length == 0) {
            log.warn("have the replaces , but no replace is set");
            return null;
        }
        return new RegexReplaces(replaces);
    }

}
