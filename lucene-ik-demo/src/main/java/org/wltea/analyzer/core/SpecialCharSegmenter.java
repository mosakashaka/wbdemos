package org.wltea.analyzer.core;

import java.util.Arrays;

public class SpecialCharSegmenter implements ISegmenter {

    // 子分词器标签
    static final String SEGMENTER_NAME = "SPEC_CHAR_SEGMENTER";

    /*
     * 词元的开始位置， 同时作为子分词器状态标识 当start > -1 时，标识当前的分词器正在处理字符
     */
    private int start;
    /*
     * 记录词元结束位置 end记录的是在词元中最后一个出现的Letter但非Sign_Connector的字符的位置
     */
    private int end;

    SpecialCharSegmenter() {
        this.start = -1;
        this.end = -1;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.wltea.analyzer.core.ISegmenter#analyze(org.wltea.analyzer.core.
     * AnalyzeContext)
     */
    public void analyze(AnalyzeContext context) {
        boolean bufferLockFlag = false;
        // 处理英文字母
        bufferLockFlag = this.processEnglishLetter(context);

        // 判断是否锁定缓冲区
        if (bufferLockFlag) {
            context.lockBuffer(SEGMENTER_NAME);
        } else {
            // 对缓冲区解锁
            context.unlockBuffer(SEGMENTER_NAME);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.wltea.analyzer.core.ISegmenter#reset()
     */
    public void reset() {
        this.start = -1;
        this.end = -1;
    }

    /**
     * 处理纯英文字母输出
     *
     * @param context
     * @return
     */
    private boolean processEnglishLetter(AnalyzeContext context) {
        if (MyCharacterUtil.CHAR_SPECIAL == context.getCurrentCharType()) {
            // 遇到特殊字符，输出词元
            Lexeme newLexeme = new Lexeme(context.getBufferOffset(), context.getCursor(), 1,
                    Lexeme.TYPE_LETTER);
            context.addLexeme(newLexeme);
        }
        return false;
    }
}
