package org.wltea.analyzer.core;

import java.util.Arrays;

public class MyCharacterUtil extends CharacterUtil {

    //需要进行索引的特殊字符
    public static final int CHAR_SPECIAL = 0X00000010;

    //需要进行索引的特殊字符
    private static final char[] Letter_Special = new char[]{'#', '-', '@', '.'};

    static {
        Arrays.sort(Letter_Special);
    }

    /**
     * 识别字符类型
     *
     * @param input
     * @return int CharacterUtil定义的字符类型常量
     */
    static int identifyCharType(char input) {
        if (input >= '0' && input <= '9') {
            return CHAR_ARABIC;

        } else if ((input >= 'a' && input <= 'z') || (input >= 'A' && input <= 'Z')) {
            return CHAR_ENGLISH;

        } else {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(input);

            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
                // 目前已知的中文字符UTF-8集合
                return CHAR_CHINESE;

            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS // 全角数字字符和日韩字符
                    // 韩文字符集
                    || ub == Character.UnicodeBlock.HANGUL_SYLLABLES || ub == Character.UnicodeBlock.HANGUL_JAMO
                    || ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
                    // 日文字符集
                    || ub == Character.UnicodeBlock.HIRAGANA // 平假名
                    || ub == Character.UnicodeBlock.KATAKANA // 片假名
                    || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS) {
                return CHAR_OTHER_CJK;

            } else if (isSpecialChar(input)) {
                return CHAR_SPECIAL;
            }
        }
        // 其他的不做处理的字符
        return CHAR_USELESS;
    }

    /**
     * 是否时需要索引的特殊字符
     *
     * @param input
     * @return
     */
    static boolean isSpecialChar(char input) {
        int index = Arrays.binarySearch(Letter_Special, input);
        return index >= 0;
    }
}
