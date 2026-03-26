package com.ring.welkin.common.core.passay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.passay.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 密码校验规则配置
 *
 * @author cloud
 * @date 2021年2月3日 上午11:21:45
 */
@Getter
@Setter
public class PassayConfig {

    /**
     * 1.密码是否允许有空格，如：密码不允许有空格
     */
    private Whitespace whitespace = new Whitespace();

    /**
     * 2.定义密码的长度限制，如：密码长度8~30位
     */
    private Length length = new Length();

    /**
     * 3.检查密码是否满足定义的N个规则，如：密码应涵盖大写字母、小写字母、数字和特殊符号4类中至少3类
     */
    private CharacterCharacteristics characterCharacteristics = new CharacterCharacteristics();

    /**
     * 4.非法字符限制，如：特殊字符
     */
    private IllegalSequence illegalSequence = new IllegalSequence();

    // /**
    // * 5.检查密码是否包含指定字典，如：密码应避免账号信息及其大小写变换
    // */
    // private DictionarySubstring dictionarySubstring = new DictionarySubstring();

    /**
     * 6.检查密码是否包含任何历史密码引用，如：密码与上次密码相同
     */
    private History history = new History();

    /**
     * 7.检查密码是否包含用户名，如：密码包含用户名字符串
     */
    private Username username = new Username();

    public List<Rule> getEffectRules() {
        List<Rule> rs = new ArrayList<Rule>();
        if (whitespace.isEnabled()) {
            rs.add(new WhitespaceRule());
        }
        if (length.isEnabled()) {
            rs.add(new LengthRule(length.getMinLength(), length.getMaxLength()));
        }
        if (characterCharacteristics.isEnabled()) {
            List<Character> characters = characterCharacteristics.getCharacters();
            rs.add(new CharacterCharacteristicsRule(characterCharacteristics.getNumCharacteristics(),
                    characters.stream().map(t -> new CharacterRule(t.getCharacterData(), t.getNumCharacters()))
                            .collect(Collectors.toList())));
        }
        if (illegalSequence.isEnabled()) {
            rs.add(new IllegalSequenceRule(illegalSequence.getSequenceData(), illegalSequence.getSequenceLength(),
                    illegalSequence.isWrapSequence()));
        }

        // DictionarySubstring dictionarySubstring = rules.getDictionarySubstring();
        // if (dictionarySubstring.isEnabled()) {
        // rs.add(new DictionarySubstringRule(
        // new WordListDictionary(new ArrayWordList(new String[] {
        // passwordData.getUsername() }, false))));
        // }

        if (history.isEnabled()) {
            rs.add(new SourceRule());
            rs.add(new HistoryRule());
        }

        if (username.isEnabled()) {
            rs.add(new UsernameRule());
        }
        return rs;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class Enabled {
        /**
         * Is the rule enabled,default false
         */
        protected boolean enabled = false;

        public Enabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    // 1.密码不能有空格
    @Setter
    @Getter
    public static final class Whitespace extends Enabled {
        public Whitespace() {
            super(true);
        }

        public Whitespace(boolean enabled) {
            super(enabled);
        }
    }

    // 2.密码长度8~30位
    @Setter
    @Getter
    public static final class Length extends Enabled {
        /**
         * Stores the minimum length of a password.
         */
        private int minLength = 8;
        /**
         * Stores the maximum length of a password.
         */
        private int maxLength = 30;

        public Length() {
            super(true);
        }

        public Length(boolean enabled, int minLength, int maxLength) {
            super(true);
            this.minLength = minLength;
            this.maxLength = maxLength;
        }
    }

    // 3.密码应涵盖大写字母、小写字母、数字和特殊符号4类中至少3类
    @Setter
    @Getter
    public static final class CharacterCharacteristics extends Enabled {

        /**
         * Number of rules to enforce. Default value is 1.
         */
        private int numCharacteristics = 3;

        /**
         * Rules to apply when checking a password.
         */
        private List<Character> characters = Arrays.asList(new Character(EnglishCharacterData.UpperCase, 1),
                new Character(EnglishCharacterData.LowerCase, 1), new Character(EnglishCharacterData.Digit, 1),
                new Character(EnglishCharacterData.Special, 1));

        public CharacterCharacteristics() {
            super(false);
        }

        public CharacterCharacteristics(boolean enabled, int numCharacteristics, List<Character> characters) {
            super(enabled);
            this.numCharacteristics = numCharacteristics;
            this.characters = characters;
        }

    }

    // 密码应涵盖大写字符
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Character {
        /**
         * Character data for this rule.
         */
        private EnglishCharacterData characterData;

        /**
         * Number of characters to require. Default value is 1.
         */
        private int numCharacters = 1;

    }

    // 4.密码应避免键盘排序，最多可以连续两位
    @Setter
    @Getter
    public static final class IllegalSequence extends Enabled {

        /**
         * Sequence data for this rule.
         */
        private EnglishSequenceData sequenceData = EnglishSequenceData.USQwerty;

        /**
         * Number of characters in sequence to match.
         */
        private int sequenceLength = 3;

        /**
         * Whether or not to wrap a sequence when searching for matches.
         */
        private boolean wrapSequence = false;

        public IllegalSequence() {
            super(false);
        }

        public IllegalSequence(boolean enabled, EnglishSequenceData sequenceData, int sequenceLength,
                               boolean wrapSequence) {
            super(enabled);
            this.sequenceData = sequenceData;
            this.sequenceLength = sequenceLength;
            this.wrapSequence = wrapSequence;
        }
    }

    // 5.密码应避免账号信息及其大小写变换
    @Setter
    @Getter
    public static final class DictionarySubstring extends Enabled {
        public DictionarySubstring() {
            super(false);
        }

        public DictionarySubstring(boolean enabled) {
            super(enabled);
        }
    }

    // 6.不能跟历史密码相同
    @Setter
    @Getter
    public static final class History extends Enabled {
        public History() {
            super(false);
        }

        public History(boolean enabled) {
            super(enabled);
        }
    }

    // 7.密码不包含用户名
    @Setter
    @Getter
    public static final class Username extends Enabled {
        public Username() {
            super(false);
        }

        public Username(boolean enabled) {
            super(enabled);
        }
    }
}
