package com.common.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.irsa.cases.model.CasesPersonnel;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.ChapterAutoNumber;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class Utils4PDF {
    private static String FONT = "d:/simfang.ttf"; 
    private static Font FONT_CHAPTER = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 24, Font.NORMAL);
    private static Font FONT_SECTION = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 20, Font.NORMAL);
    private static Font FONT_CONTENT = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 17, Font.NORMAL);
    
    private static String PATH_PARTY = "d:/file/irsa/party/";
    private static String PATH_DEFENDANT = "d:/file/irsa/defendant/";
    private static String PATH_AGENT = "d:/file/irsa/agent/";
    private static String PATH_CASES = "d:/file/irsa/cases/";
    
    /**
     * 
     * @param type 1=一级目录，2=二级目录，3=内容
     * @param content
     * @return
     */
    public static Paragraph getParagraph(String type, String content) {
        if ("1".equals(type)) {
            Paragraph p = new Paragraph(content, FONT_CHAPTER);
            p.setSpacingAfter(15);
            return p;
        }
        if ("2".equals(type)) {
            Paragraph p = new Paragraph(content, FONT_SECTION);
            p.setSpacingAfter(10);
            return p;
        }
        Paragraph p = new Paragraph(content, FONT_CONTENT);
        p.setSpacingAfter(5);
        return p;
    }
    
    public static Image getImage(String filePath) throws BadElementException, MalformedURLException, IOException {
        Image img = Image.getInstance(filePath);
        img.scaleToFit(500, 500); 
        img.setSpacingAfter(5);
        return img;
    }
    
    
    public static boolean isNotBlank(Object obj) {
        if (obj != null && !"".equals(obj) && !"无".equals(obj)) {
            return true;
        }
        return false;
    }
    
    public static String formart(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString().replaceAll("\\s*", "").replaceAll("<p>", "\n    ").replaceAll("</p>", "").replaceAll("&rdquo;", "”").replaceAll("&ldquo;", "“");
    }
    
    public static String toString4Party(Map<String, Object> personnel) {
        StringBuffer sb = new StringBuffer();
        sb.append(personnel.get("name"));
        if ("1".equals(personnel.get("type"))) {
            if (isNotBlank(personnel.get("legal_person"))) {
                sb.append("，英文名/其他语言名/化名/曾用名，").append(personnel.get("legal_person"));
            }
        } else {
            sb.append(personnel.get("name")).append("，法定代表人（负责人），").append(personnel.get("legal_person"));
        }
        sb.append("，证件号码，").append(personnel.get("id_no")).append("，手机号码，").append(personnel.get("phone"));
        if (isNotBlank(personnel.get("contact"))) {
            sb.append("，其他联系方式，").append(personnel.get("contact"));
        } 
        sb.append("，联系地址，").append(personnel.get("province")).append(personnel.get("city")).append(personnel.get("county")).append("，详细地址，").append(personnel.get("address")).append("。");
        return sb.toString();
    }
    
    public static String toString4Agent(Map<String, Object> personnel) {
        StringBuffer sb = new StringBuffer();
        sb.append(personnel.get("name")).append("，证件号码，").append(personnel.get("id_no")).append("，手机号码，").append(personnel.get("phone"));
        if (isNotBlank(personnel.get("contact"))) {
            sb.append("，其他联系方式，").append(personnel.get("contact"));
        } 
        sb.append("，联系地址，").append(personnel.get("province")).append(personnel.get("city")).append(personnel.get("county")).append("，详细地址，").append(personnel.get("address"));
        if (isNotBlank(personnel.get(""))) {
            sb.append("，工作单位，").append(personnel.get("unit_name")).append("。");
        } else {
            sb.append("。");
        }
        return sb.toString();
    }
    
    public static String toString4Defendant(Map<String, Object> personnel) {
        StringBuffer sb = new StringBuffer();
        sb.append(personnel.get("name")).append("，法定代表人，").append(personnel.get("legal_person")).append("，住所，").append(personnel.get("address")).append("。");
        return sb.toString();
    }
    
    public static void addSection(Chapter chapter, Map<String, Object> personnel) throws BadElementException, MalformedURLException, IOException {
        Paragraph subTitle = getParagraph("2", personnel.get("name").toString()); 
        Section section = chapter.addSection(subTitle); 
        String content = "";
        String path = "";
        if (CasesPersonnel.PERSONNEL_TYPE_1.equals(personnel.get("personnel_type")) ||
                CasesPersonnel.PERSONNEL_TYPE_3.equals(personnel.get("personnel_type"))) {
            content = toString4Party(personnel);
            path = PATH_PARTY;
        } else if (CasesPersonnel.PERSONNEL_TYPE_4.equals(personnel.get("personnel_type")) ||
                CasesPersonnel.PERSONNEL_TYPE_5.equals(personnel.get("personnel_type"))) {
            content = toString4Agent(personnel);
            path = PATH_AGENT;
        }
        Paragraph p = getParagraph("3", content);
        p.setFirstLineIndent(40);
        section.add(p);
        
//        Section idFile = section.addSection(getParagraph("2", "证件信息"));
//        idFile.add(getImage(path + personnel.get("id_file")));
        section.add(getImage(path + personnel.get("id_file")));
    }
    
    public static void addSection4Img(Chapter chapter, Map<String, Object> file) throws BadElementException, MalformedURLException, IOException {
        chapter.add(getImage(PATH_CASES + file.get("file_name")));
    }
    
    public static void createPDF(Map<String, Object> cases, 
            List<Map<String, Object>> personnelList1, List<Map<String, Object>> personnelList3, Map<String, Object> personnel2, List<Map<String, Object>> personnelList6,
            List<Map<String, Object>> casesFilelList4, List<Map<String, Object>> casesFilelList1, List<Map<String, Object>> casesFilelList3,
            List<Map<String, Object>> casesFilelList7, List<Map<String, Object>> casesFilelList2, List<Map<String, Object>> casesFilelList6) throws DocumentException, MalformedURLException, IOException {
        Document document = new Document(new Rectangle(PageSize.A4), 50, 50, 25, 25);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/HelloWorld.pdf"));
        writer.setStrictImageSequence(true); 
        document.open();
        
        // 案件详情
        Chapter chapter1 = new ChapterAutoNumber(getParagraph("1", "案件详情"));
        chapter1.add(getParagraph("3", "案件年号：" + cases.get("year_no")));
        chapter1.add(getParagraph("3", "案件编号：" + cases.get("no")));
        chapter1.add(getParagraph("3", "案件类型："  + cases.get("label_type")));
        chapter1.add(getParagraph("3", "申请时间：" + cases.get("apply_time")));
        chapter1.add(getParagraph("3", "申请方式：" + cases.get("label_apply_mode")));
        chapter1.add(getParagraph("3", "行政类别：" + cases.get("label_administrative_category")));
        chapter1.add(getParagraph("3", "案由：" + cases.get("label_reason")));
        chapter1.add(getParagraph("3", "被诉具体行政行为：\n    " + cases.get("specific_behavior")));
        chapter1.add(getParagraph("3", "行政复议请求：" + formart(cases.get("apply_request"))));
        chapter1.add(getParagraph("3", "事实和理由：" + formart(cases.get("facts_reasons"))));
        chapter1.add(getParagraph("3", "被申请人答复书：" + formart(cases.get("defendant_reply"))));
        chapter1.add(getParagraph("3", "被申请人备注：" + formart(cases.get("defendant_remark"))));
        document.add(chapter1);
        
        // 申请人
        Chapter chapter2 = new ChapterAutoNumber(getParagraph("1", "申请人"));
        for (Map<String, Object> personnel : personnelList1) {
            addSection(chapter2, personnel);
        }
        document.add(chapter2);
        
        // 第三人
        if (personnelList3 != null && personnelList3.size() > 0) {
            Chapter chapter3 = new ChapterAutoNumber(getParagraph("1", "第三人"));
            for (Map<String, Object> personnel : personnelList3) {
                addSection(chapter3, personnel);
            }
            document.add(chapter3);
        }
        
        // 被申请人
        Chapter chapter4 = new ChapterAutoNumber(getParagraph("1", "被申请人"));
        Paragraph p = getParagraph("3", toString4Defendant(personnel2));
        p.setFirstLineIndent(40);
        chapter4.add(p);
        if (StringUtils.isNotBlank(Utils.toString(personnel2.get("file_1")))) {
//            Section section = chapter4.addSection(getParagraph("2", "社会统一信用代码证")); 
//            section.add(getImage(PATH_DEFENDANT + personnel2.get("file_1")));
            chapter4.add(getImage(PATH_DEFENDANT + personnel2.get("file_1")));
        }
        if (StringUtils.isNotBlank(Utils.toString(personnel2.get("file_2")))) {
//            Section section = chapter4.addSection(getParagraph("2", "法定代表人证明书")); 
//            section.add(getImage(PATH_DEFENDANT + personnel2.get("file_2")));
            chapter4.add(getImage(PATH_DEFENDANT + personnel2.get("file_2")));
        }
        if (StringUtils.isNotBlank(Utils.toString(personnel2.get("file_3")))) {
//            Section section = chapter4.addSection(getParagraph("2", "法定代表人证件信息")); 
//            section.add(getImage(PATH_DEFENDANT + personnel2.get("file_3")));
            chapter4.add(getImage(PATH_DEFENDANT + personnel2.get("file_3")));
        }
        document.add(chapter4);
        
        // 委托代理人
//        if (personnelList6 != null && personnelList6.size() > 0) {
//            Chapter chapter5 = new ChapterAutoNumber(getParagraph("1", "委托代理人"));
//            for (Map<String, Object> personnel : personnelList6) {
//                addSection(chapter5, personnel);
//            }
//            document.add(chapter5);
//        }
        
        // 代理人委托书
//        if (casesFilelList4 != null && casesFilelList4.size() > 0) {
//            Chapter chapter6 = new ChapterAutoNumber(getParagraph("1", "代理人委托书"));
//            for (Map<String, Object> casesFile : casesFilelList4) {
//                addSection4Img(chapter6, casesFile);
//            }
//            document.add(chapter6);
//        }
        
        // 申请人证据及相关材料
        if (casesFilelList1 != null && casesFilelList1.size() > 0) {
            Chapter chapter7 = new ChapterAutoNumber(getParagraph("1", "申请人证据及相关材料"));
            for (Map<String, Object> casesFile : casesFilelList1) {
                addSection4Img(chapter7, casesFile);
            }
            document.add(chapter7);
        }
        
        // 第三人证据及相关材料
        if (casesFilelList3 != null && casesFilelList3.size() > 0) {
            Chapter chapter8 = new ChapterAutoNumber(getParagraph("1", "第三人证据及相关材料"));
            for (Map<String, Object> casesFile : casesFilelList3) {
                addSection4Img(chapter8, casesFile);
            }
            document.add(chapter8);
        }
        
        // 被申请人答复书照片
        if (casesFilelList7 != null && casesFilelList7.size() > 0) {
            Chapter chapter9 = new ChapterAutoNumber(getParagraph("1", "被申请人答复书照片"));
            for (Map<String, Object> casesFile : casesFilelList7) {
                addSection4Img(chapter9, casesFile);
            }
            document.add(chapter9);
        }
        
        // 被申请人证据及相关材料
        if (casesFilelList2 != null && casesFilelList2.size() > 0) {
            Chapter chapter10 = new ChapterAutoNumber(getParagraph("1", "被申请人证据及相关材料"));
            for (Map<String, Object> casesFile : casesFilelList2) {
                addSection4Img(chapter10, casesFile);
            }
            document.add(chapter10);
        }
        
        document.close();
        writer.close();
    }
}

