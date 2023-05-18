package com.suibibk.utils;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;

public class Html2Text extends HTMLEditorKit.ParserCallback{
    private static Html2Text h2t = new Html2Text();
    private Html2Text(){};
    private StringBuffer s;
    private void parse(String str) throws IOException {
        InputStream iin = new ByteArrayInputStream(str.getBytes());
        Reader in = new InputStreamReader(iin);
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        // the third parameter is TRUE to ignore charset directive
        delegator.parse(in, this, Boolean.TRUE);
        iin.close();
        in.close();
    }
    public void handleText(char[] text, int pos) {
        s.append(text);
    }

    public String getText() {
        return s.toString();
    }

    public static String getContent(String str) {
        try {
            h2t.parse(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return h2t.getText();
    }

    public static void main (String[] args) {
        System.out.println(Html2Text.getContent("<p>这里介绍PYTHON的头文件含义<br>1、第一行<br> 脚本语言的第一行，目的就是指出，你想要你的这个文件中的代码用什么可执行程序去运行它，就这么简单。</p>\n" +
                "<pre class=\"prettyprint linenums prettyprinted\" style=\"\"><ol class=\"linenums\"><li class=\"L0\"><code><span class=\"co"));
    }
}

