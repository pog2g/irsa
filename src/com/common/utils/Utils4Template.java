package com.common.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Utils4Template {
    public static final String TEMPLATE_HTML_STATE_0 = "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;margin-bottom: 37px;\">行政复议申请书</div>" +
            "<div style=\"font-family: '仿宋';line-height: 37px;font-size: 21px;\">" +
            "<div>{0}</div>" +
            "<div>{1}</div>" +
            "<div>{2}</div>" +
            "<div style=\"text-indent: 2em;\">行政复议请求：{3}</div>" +
            "<div style=\"text-indent: 2em;\">事实与理由：{4}</div>" +
            "<div style=\"text-indent: 2em;\">此致</div>" +
            "<div style=\"text-indent: 2em;margin-bottom: 37px;\">{5}</div>" +
            "<div style=\"text-align: right;\">申请人：（签名或盖公章）</div>" +
            "<div style=\"text-align: right;margin-right: 4em;\">{6}</div>" +
            "</div>";

//    public static final String TEMPLATE_HTML_STATE_4_1 = "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\">{0}</div>" +
//    		"<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议受理通知书</div>" +
//    		"<div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\">" +
//    		"<div style=\"text-align: right;margin-bottom: 37px;\">" +
//    		"<span>{1}</span>﹝<span>{2}</span>﹞<span>{3}</span><span>{4}</span><span>{5}</span>" +
//    		"</div>" +
//    		"<div><span>{6}</span>：</div>" +
//    		"<div style=\"text-indent: 2em;\">" +
//    		"你不服<span>{7}</span><span>{8}</span>，于<span>{9}</span>向本机关提出行政复议申请。经审查，该申请符合《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》的有关规定，现决定予以受理。" +
//    		"</div>" +
//    		"<div style=\"text-indent: 2em;\">" +
//    		"根据《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》的有关规定，你可以直接参加行政复议，也可以委托代理人代为参加行政复议。你收到本通知后如委托代理人，需向案件审理机关出具委托书并载明委托事项、权限和期限。" +
//    		"</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div>" +
//    		"<div style=\"text-align: right;margin-right: 4em;\"><span>{10}</span></div>" +
//    		"</div>" +
//    		"</div>";
//    public static final String TEMPLATE_HTML_STATE_4_2 = "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span>{0}</span></div>" +
//    		"<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议答复通知书</div>" +
//    		"<div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\">" +
//    		"<div style=\"text-align: right;margin-bottom: 37px;\">" +
//    		"<span>{1}</span>﹝<span>{2}</span>﹞<span>{3}</span><span>{4}</span><span>{5}</span>" +
//    		"</div>" +
//    		"<div><span>{6}</span>：</div>" +
//    		"<div style=\"text-indent: 2em;\">" +
//    		"<span>{7}</span>不服你单位<span>{8}</span>，提出行政复议申请，本机关已于<span>{9}</span>受理。" +
//    		"</div>" +
//    		"<div style=\"text-indent: 2em;\">" +
//    		"根据《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》有关规定，现将行政复议申请书副本发送你机关，请自收到行政复议申请书副本之日起十日内，对该行政复议申请提出书面答复，并提交当初作出该具体行政行为的证据、依据和其他有关材料。逾期未提出书面答复，未提交当初作出该具体行政行为的证据、依据和其他有关材料的，视为该具体行政行为没有证据、依据，将依法撤销。" +
//    		"</div>" +
//    		"<div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div>" +
//    		"<div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span>{10}</span></div>" +
//    		"<div>附：行政复议申请书副本一份</div>" +
//    		"</div>" +
//    		"</div>";
//    public static final String TEMPLATE_HTML_STATE_4_3 = "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-9\">{0}</span></div>" +
//            "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议受理通知书</div>" +
//            "<div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\">" +
//            "<div style=\"text-align: right;margin-bottom: 37px;\">" +
//            "<span>{1}</span><span>﹝{2}﹞</span><span>{3}</span><span>{4}</span><span>{5}</span>" +
//            "</div><div><span>{6}</span>：</div>" +
//            "<div style=\"text-indent: 2em;\">" +
//            "<span>{7}</span>不服<span>{8}</span><span>{9}</span>，提出行政复议申请，本机关已于<span>{10}</span>受理。因你同申请行政复议的具体行政行为有利害关系，本机关通知你作为第三人参加行政复议。根据《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》的有关规定，现将行政复议申请书副本发送你，请自收到之日起十日内，提交书面答辩和有关证据，逾期不提交不影响作出行政复议决定。" +
//            "</div>" +
//            "<div style=\"text-indent: 2em;\">" +
//            "根据《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》的有关规定，你可以直接参加行政复议，也可以委托代理人代为参加行政复议。你收到本通知后，如委托代理人，需向案件审理机关出具委托书并载明委托事项、权限和期限。" +
//            "</div>" +
//            "<div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div>" +
//            "<div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span>{11}</span></div>" +
//            "<div>附：行政复议申请书副本一份</div>" +
//            "</div>" +
//            "</div>";

    public static final String TEMPLATE_STATE_0 = "template_state_0.html";
    private static Configuration freemarkerCfg = null;

    static {
        freemarkerCfg = new Configuration();
        try {
            freemarkerCfg.setDirectoryForTemplateLoading(new File("d:/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("apply", "<div>申请人：淡永强，证件号码，410121197110224810，手机号码，18538725570，邮编，450015，联系地址，河南省郑州市二七区，详细地址，华中路11号院</div>");
        data.put("agent", "<div>申请人：淡永强，证件号码，410121197110224810，手机号码，18538725570，邮编，450015，联系地址，河南省郑州市二七区，详细地址，华中路11号院</div>");
        data.put("defendant", "<div>申请人：淡永强，证件号码，410121197110224810，手机号码，18538725570，邮编，450015，联系地址，河南省郑州市二七区，详细地址，华中路11号院</div>");
        data.put("apply_request", "<p>2018-10-08</p>");
        data.put("facts_reasons", "<p>默认</p>");
        data.put("department", "郑州市人民政府");
        data.put("date", "2018年10月18日");
        String content = freeMarkerRender(data, TEMPLATE_STATE_0);
        StringBuffer buffer = new StringBuffer(content);
        File outFile = new File("D:/test1.doc");
        byte[] contentBytes = buffer.toString().getBytes();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(contentBytes);
        POIFSFileSystem poifSystem = new POIFSFileSystem();
        DirectoryNode root = poifSystem.getRoot();
        root.createDocument("WordDocument", byteStream);
        FileOutputStream outStream = new FileOutputStream(outFile);
        poifSystem.writeFilesystem(outStream);
        byteStream.close();
        outStream.close();
    }

    /**
     * freemarker渲染html
     */
    public static String freeMarkerRender(Map<String, Object> data, String htmlTmp) {
        Writer out = new StringWriter();
        try {
            // 获取模板,并设置编码方式
            Template template = freemarkerCfg.getTemplate(htmlTmp);
            template.setEncoding("UTF-8");
            // 合并数据模型与模板
            template.process(data, out); //将合并后的数据和模板写入到流中，这里使用的字符流
            out.flush();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
