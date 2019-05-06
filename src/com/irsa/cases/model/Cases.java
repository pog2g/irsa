package com.irsa.cases.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 案件
 * @author EZ
 *
 */
public class Cases {
    /**受理程序*/
    public static String MODE_1 = "1";
    /**受理结案*/
    public static String MODE_2 = "2";
    /**中转程序*/
    public static String MODE_3 = "3";
    /**审理程序*/
    public static String MODE_4 = "4";
    /**审理结案*/
    public static String MODE_5 = "5";
    /**查阅下载*/
    public static String MODE_6 = "6";
    /**规范性文件审查中止*/
    public static String MODE_13 = "13";
    
    // 立案申请、批准受理转办、批准补正、批准告知、批准转送、批准不予受理、逾期未处理改为逾期未补正、主动放弃改为受理前放弃。
    /**立案申请，受理程序*/
    public static String STATE_1 = "1";
    /**批准补正，受理程序*/
    public static String STATE_2 = "2";
    /**批准告知，受理结案*/
    public static String STATE_3 = "3";
    /**批准受理转办，受理结案*/
    public static String STATE_4 = "4";
    /**批准转送，受理结案*/
    public static String STATE_5 = "5";
    /**批准不予受理，受理结案*/
    public static String STATE_6 = "6";
    /**逾期未补正，受理结案*/
    public static String STATE_7 = "7";
    /**受理前放弃，受理结案*/
    public static String STATE_8 = "8";
    // 待被申请人答复改为批准受理待答复、（被申请人未答复、被申请人已答复不动），增加部分被申请人已答复、答复前撤回申请（直接流转到审理系统不再变成审理中，终止（自愿撤回）界面）
    /**批准受理待答复，受理中转*/
    public static String STATE_9 = "9";
    /**被申请人未答复，受理中转*/
    public static String STATE_10 = "10";
    /**被申请人已答复，受理中转*/
    public static String STATE_11 = "11";
    /**审理中（受理），审理程序*/
    public static String STATE_12 = "12";
    /**批准延期，程序性，审理程序*/
    public static String STATE_13 = "13";
    /**批准中止，程序性，审理程序*/
    public static String STATE_14 = "14";
    /**批准听证，程序性，审理程序*/
    public static String STATE_20 = "20";
    /**支持（批准变更），审理结案*/
    public static String STATE_17 = "17";
    /**支持（批准撤销），审理结案*/
    public static String STATE_21 = "21";
    /**支持（批准确认违法），审理结案*/
    public static String STATE_22 = "22";
    /**支持（批准责令履行），审理结案*/
    public static String STATE_23 = "23";
    /**支持（批准确认无效），审理结案*/
    public static String STATE_24 = "24";
    /**支持（批准调解与决定），审理结案*/
    public static String STATE_25 = "25";
    /**支持（批准其他决定），审理结案*/
    public static String STATE_26 = "26";
    /**否定（批准驳回），审理结案*/
    public static String STATE_18 = "18";
    /**否定（批准维持），审理结案*/
    public static String STATE_19 = "19";
    /**终止（自愿撤回），审理结案*/
    public static String STATE_15 = "15";
    /**终止（法定或其他），审理结案*/
    public static String STATE_16 = "16";
    
    /**提交前申请人撤回*/
    public static String STATE_27 = "27";
    /**责令停止，程序性，审理程序*/
    public static String STATE_28 = "28";
    /**恢复审理，程序性，审理程序：状态从批准中止变为审理中，即恢复审理*/
    public static String STATE_29 = "29";
    /**规范性文件审查中止*/
    public static String STATE_30 = "30";
    /**全案调解协议生效，程序性，审理程序：状态从批准中止变为审理中，即恢复审理*/
    public static String STATE_31 = "31";
    /**部分被申请人答复*/
    public static String STATE_32 = "32";
    
    public final static Map<String, String> STATE_COLOR = new HashMap<String, String>();
    public final static Map<String, String> STATE_LABEL = new HashMap<String, String>();
    public final static Map<String, String> DOCUMENT_NUMBER = new HashMap<String, String>();
    public final static Map<String, String> DOCUMENT_HTML = new HashMap<String, String>();
    public final static String DOCUMENT_TITLE = "郑州市人民政府行政复议办公室";
    
    static {
        STATE_COLOR.put("1", "#3c8dbc");
        STATE_COLOR.put("2", "#00c0ef");
        STATE_COLOR.put("3", "#39cccc");
        STATE_COLOR.put("4", "#155e63");
        STATE_COLOR.put("5", "#76b39d");
        STATE_COLOR.put("6", "#db456f");
        STATE_COLOR.put("7", "#cd5555");
        STATE_COLOR.put("8", "#882042");
        STATE_COLOR.put("9", "#f39c12");
        STATE_COLOR.put("10", "#ea4c4c");
        STATE_COLOR.put("11", "#00a65a");
        STATE_COLOR.put("12", "#605ca8");
        STATE_COLOR.put("13", "#7971ea");
        STATE_COLOR.put("14", "#cc99f9");
        STATE_COLOR.put("20", "#7a4579");
        STATE_COLOR.put("17", "#35d0ba");
        STATE_COLOR.put("21", "#45b7b7");
        STATE_COLOR.put("22", "#00a8b5");
        STATE_COLOR.put("23", "#00bbf0");
        STATE_COLOR.put("24", "#448ef6");
        STATE_COLOR.put("25", "#2f89fc");
        STATE_COLOR.put("26", "#0060ca");
        STATE_COLOR.put("18", "#f73859");
        STATE_COLOR.put("19", "#d81b60");
        STATE_COLOR.put("15", "#f9499e");
        STATE_COLOR.put("16", "#8b104e");
        
        STATE_LABEL.put("1", "立案申请");
        STATE_LABEL.put("2", "批准补正");
        STATE_LABEL.put("3", "批准告知");
        STATE_LABEL.put("4", "批准受理转办");
        STATE_LABEL.put("5", "批准转送");
        STATE_LABEL.put("6", "批准不予受理");
        STATE_LABEL.put("7", "逾期未补正");
        STATE_LABEL.put("8", "受理前放弃");
        STATE_LABEL.put("9", "批准受理待答复");
        STATE_LABEL.put("10", "被申请人未答复");
        STATE_LABEL.put("11", "被申请人已答复");
        STATE_LABEL.put("12", "受理");
        STATE_LABEL.put("13", "批准延期");
        STATE_LABEL.put("14", "批准中止");
        STATE_LABEL.put("20", "批准听证");
        STATE_LABEL.put("17", "支持（批准变更）");
        STATE_LABEL.put("21", "支持（批准撤销）");
        STATE_LABEL.put("22", "支持（批准确认违法）");
        STATE_LABEL.put("23", "支持（批准责令履行）");
        STATE_LABEL.put("24", "支持（批准确认无效）");
        STATE_LABEL.put("25", "支持（批准调解与决定）");
        STATE_LABEL.put("26", "支持（批准其他决定）");
        STATE_LABEL.put("18", "否定（批准驳回）");
        STATE_LABEL.put("19", "否定（批准维持）");
        STATE_LABEL.put("15", "终止（自愿撤回）");
        STATE_LABEL.put("16", "终止（法定或其他）");
        
        DOCUMENT_NUMBER.put("2", "郑政行复办（补复通）字,,号");
        DOCUMENT_NUMBER.put("3", "郑政行复办（复告知）字,,号");
        DOCUMENT_NUMBER.put("4", "郑政行复办（行复转）字,,号");
        DOCUMENT_NUMBER.put("5", "郑政行复办（行复转）字,,号");
        DOCUMENT_NUMBER.put("6", "郑政行复办（不受复决）字,,号");
        DOCUMENT_NUMBER.put("9", "郑政行复办（复受通）字,,号");
        DOCUMENT_NUMBER.put("10", "郑政行复办（复答通）字,,号");
        DOCUMENT_NUMBER.put("11", "郑政行复办（复受通）字,,号");
        DOCUMENT_NUMBER.put("13", "郑政行复办（延复通）字,,号");
        DOCUMENT_NUMBER.put("14", "郑政行复办（行复通）字,,号");
        DOCUMENT_NUMBER.put("15", "郑政行复办（行复终决）字,,号");
        DOCUMENT_NUMBER.put("16", "郑政行复办（行复终决）字,,号");
        DOCUMENT_NUMBER.put("17", "郑政行复办（行复决）字,,号");
        DOCUMENT_NUMBER.put("18", "郑政行复办（行复驳决）字,,号");
        DOCUMENT_NUMBER.put("19", "郑政行复办（行复决）字,,号");
        
        DOCUMENT_HTML.put("2", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-2\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">补正行政复议申请通知书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"> <div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-2-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-2-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-2-3\">{3}</span></div><div><span class=\"font-apply\"></span>：</div><div style=\"text-indent: 2em;\"> 根据《中华人民共和国行政复议法实施条例》第十九条的规定，如以<span class=\"font-defendant\"></span>为被申请人提起行政复议申请，请补正以下材料：<span id=\"font_input_2_1\"></span></div><div style=\"text-indent: 2em;\">请自收到本补正通知之日起<span id=\"font_limit_day_2\"></span>个工作日内予以补正。无正当理由逾期不补正的，视为放弃行政复议申请。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此告知。</div><div style=\"text-align: right;margin-right: 4em;\"><span class=\"font-date\"></span></div></div>");
        DOCUMENT_HTML.put("3", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-3\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议告知书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-3-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-3-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-3-3\">{3}</span></div><div><span class=\"font-apply\"></span>：</div><div style=\"text-indent: 2em;\"><span class=\"font-apply-type\"></span>不服<span class=\"font-defendant\"></span><span class=\"font-reason\"></span>，向本机关提出行政复议申请。经审查，根据《中华人民共和国行政复议法》第十七条之规定，本机关认为该行政复议案件不属于本机关管辖，请向<span id=\"font_input_3\"></span>申请行政复议。</div> <div style=\"text-indent: 2em;margin-bottom: 37px;\">特此告知。</div><div style=\"text-align: right;margin-right: 4em\"><span class=\"font-date\"></span></div></div>");
        DOCUMENT_HTML.put("4", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-4\">{0}</span></div> <div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议案件转办函</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-4-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-4-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-4-3\">{3}</span></div><div><span id=\"font_input_4\"></span>：</div><div style=\"text-indent: 2em;\"><span class=\"font-apply\"></span>不服<span class=\"font-defendant\"></span><span class=\"font-reason\"></span>，提出行政复议申请。</div><div style=\"text-indent: 2em;\">经审查，该申请符合《中华人民共和国行政复议法》和《中华人民共和国行政复议法实施条例》的有关规定，本机关已于<span class=\"font-apply-time\"></span>受理。</div><div style=\"text-indent: 2em;\">根据《郑州市集中受理审理行政复议案件暂行规定》第十七条有关规定，现将该行政复议案件转办你机关依法审理。请在案件审结后十五日内，将行政复议决定报本机关备案。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此函告。</div> <div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span class=\"font-date\"></span></div><div>附：行政复议申请书及相关材料</div></div>");
        DOCUMENT_HTML.put("5", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-5\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议案件转送函</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-5-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-5-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-5-3\">{3}</span></div><div><span id=\"font_input_5\"></span>：</div><div style=\"text-indent: 2em;\"><span class=\"font-apply\"></span>不服<span class=\"font-defendant\"></span><span class=\"font-reason\"></span>，提出行政复议申请。</div><div style=\"text-indent: 2em;\">经审查，该申请符合《中华人民共和国行政复议法》和《中华人民共和国行政复议法实施条例》的有关规定，本机关已于<span class=\"font-apply-time\"></span>受理。</div><div style=\"text-indent: 2em;\">根据《郑州市集中受理审理行政复议案件暂行规定》第十七条有关规定，现将该行政复议案件转送你机关依法审理。请在案件审结后十五日内，将行政复议决定报本机关备案。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此函告。</div><div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span class=\"font-date\"></span></div><div>附：行政复议申请书及相关材料</div></div>");
        DOCUMENT_HTML.put("6", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-6\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">不予受理行政复议申请决定书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-6-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-6-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-6-3\">{3}</span></div><div class=\"div-apply\"></div><div class=\"div-third-party\"></div><div class=\"div-agent\"></div><div class=\"div-defendant\"></div><div style=\"text-indent: 2em;\">申请人不服被申请人<span class=\"font-reason\"></span>，向本机关提出行政复议申请，本机关已于<span class=\"font-apply-time\"></span>收悉。</div><div style=\"text-indent: 2em;\">经审查，本机关认为：<span id=\"font_input_6\"></span>，根据《中华人民共和国行政复议法实施条例》第二十八条及《中华人民共和国行政复议法》第十七条的规定，本机关决定：对申请人的行政复议申请不予受理。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;\"><span class=\"font-date\"></span></div></div>");
        DOCUMENT_HTML.put("9", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-9\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议受理通知书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-9-1\">{1}</span>﹝<span class=\"font-year-no\">{2}</span>﹞<span class=\"font-number-9-2\">{3}</span><span class=\"font-no\">{4}</span><span class=\"font-number-9-3\">{5}</span></div><div><span class=\"font-apply\">{6}</span>：</div><div style=\"text-indent: 2em;\"><span class=\"font-apply-type\">{7}</span>不服<span class=\"font-defendant\">{8}</span><span class=\"font-reason\">{9}</span>，于<span class=\"font-apply-time\">{10}</span>向本机关提出行政复议申请。本机关依法予以受理。</div><div style=\"text-indent: 2em;\">根据<span class=\"font-regulations-9\">{11}</span>的有关规定，<span class=\"font-apply-type\">{12}</span>可以直接参加行政复议，也可以委托代理人代为参加行政复议。<span class=\"font-apply-type\">{13}</span>收到本通知后如委托代理人，需向案件审理机关出具委托书并载明委托事项、权限和期限。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;\"><span class=\"font-date\">{14}</span></div></div>");
        DOCUMENT_HTML.put("10", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-10\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议答复通知书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-10-1\">{1}</span>﹝<span class=\"font-year-no\">{2}</span>﹞<span class=\"font-number-10-2\">{3}</span><span class=\"font-no\">{4}</span><span class=\"font-number-10-3\">{5}</span></div><div><span class=\"font-defendant\">{6}</span>：</div><div style=\"text-indent: 2em;\"><span class=\"font-apply\">{7}</span>不服你单位<span class=\"font-reason\">{8}</span>，提出行政复议申请，本机关已于<span class=\"font-apply-time\">{9}</span>受理。</div><div style=\"text-indent: 2em;\">根据<span class=\"font-regulations-9\">{10}</span>有关规定，现将行政复议申请书副本发送你机关，请自收到行政复议申请书副本之日起十日内，对该行政复议申请提出书面答复，并提交当初作出该具体行政行为的证据、依据和其他有关材料。逾期未提出书面答复，未提交当初作出该具体行政行为的证据、依据和其他有关材料的，视为该具体行政行为没有证据、依据，将依法撤销。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span class=\"font-date\">{11}</span></div><div>附：行政复议申请书副本一份</div></div>");
        DOCUMENT_HTML.put("11", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-9\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议受理通知书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-9-1\">{1}</span>﹝<span class=\"font-year-no\">{2}</span>﹞<span class=\"font-number-9-2\">{3}</span><span class=\"font-no\">{4}</span><span class=\"font-number-9-3\">{5}</span></div><div><span class=\"font-third-party\">{6}</span>：</div><div style=\"text-indent: 2em;\"><span class=\"font-apply\">{7}</span>不服<span class=\"font-defendant\">{8}</span><span class=\"font-reason\">{9}</span>，提出行政复议申请，本机关已于<span class=\"font-apply-time\">{10}</span>受理。因你同申请行政复议的具体行政行为有利害关系，本机关通知你作为第三人参加行政复议。根据<span class=\"font-regulations-9\">{11}</span>的有关规定，现将行政复议申请书副本发送你，请自收到之日起十日内，提交书面答辩和有关证据，逾期不提交不影响作出行政复议决定。</div><div style=\"text-indent: 2em;\">根据<span class=\"font-regulations-9\">{12}</span>的有关规定，你可以直接参加行政复议，也可以委托代理人代为参加行政复议。你收到本通知后，如委托代理人，需向案件审理机关出具委托书并载明委托事项、权限和期限。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span class=\"font-date\">{13}</span></div><div>附：行政复议申请书副本一份</div></div>");
        DOCUMENT_HTML.put("13", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-13\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">延期审理行政复议通知书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-13-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-13-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-13-3\">{3}</span></div><div><span class=\"font-apply\"></span>：</div><div style=\"text-indent: 2em;\"><span class=\"font-apply-type\"></span>不服<span class=\"font-defendant\"></span><span class=\"font-reason\"></span>，于<span class=\"font-apply-time\"></span>提出复议申请，本机关依法予以受理。</div><div style=\"text-indent: 2em;\">因情况复杂，不能在规定期限内作出行政复议决定。根据<span class=\"font-regulations-13\"></span>有关规定，本机关决定：行政复议决定延期<span id=\"font_limit_day_13\"></span>日作出。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span class=\"font-date\"></span></div><div>抄送：<span class=\"font-defendant\"></span></div></div>");
        DOCUMENT_HTML.put("14", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-14\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议中止通知书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-14-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-14-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-14-3\">{3}</span></div><div><span class=\"font-apply\"></span>：</div><div style=\"text-indent: 2em;\"><span class=\"font-apply-type\"></span>不服<span class=\"font-defendant\"></span><span class=\"font-reason\"></span>，于<span class=\"font-apply-time\"></span>提出行政复议申请，本机关依法已予受理。</div><div style=\"text-indent: 2em;\">行政复议过程中，因以下事由：<span id=\"font_input_14_1\"></span></div><div style=\"text-indent: 2em;\">根据《中华人民共和国行政复议法实施条例》（第四十一条第一款第八项）的规定，本机关决定自<span class=\"font-date\"></span>起中止对该案的审理，待中止原因消除后，本机关将恢复该案件的审理。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span class=\"font-date\"></span></div><div>抄送：<span class=\"font-defendant\"></span></div></div>");
        DOCUMENT_HTML.put("15", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-15\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议终止决定书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-15-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-15-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-15-3\">{3}</span></div><div class=\"div-apply\"></div><div class=\"div-third-party\"></div><div class=\"div-agent\"></div><div class=\"div-defendant\"></div><div style=\"text-indent: 2em;\">申请人不服被申请人<span class=\"font-reason\"></span>，于<span class=\"font-apply-time\"></span>提出复议申请，本机关依法予以受理。</div><div style=\"text-indent: 2em;\"><span id=\"font_input_15_1\"></span></div><div style=\"text-indent: 2em;\">经审查，<span class=\"font-regulations-15\"></span>本机关决定：终止对该案的审理。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span class=\"font-date\"></span></div><div>抄送：<span class=\"font-defendant\"></span></div></div>");
        DOCUMENT_HTML.put("16", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-16\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议终止决定书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-16-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-16-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-16-3\">{3}</span></div><div class=\"div-apply\"></div><div class=\"div-third-party\"></div><div class=\"div-agent\"></div><div class=\"div-defendant\"></div><div style=\"text-indent: 2em;\">申请人不服被申请人<span class=\"font-reason\"></span>，于<span class=\"font-apply-time\"></span>提出复议申请，本机关依法予以受理。</div><div style=\"text-indent: 2em;\"><span id=\"font_input_16_1\"></span></div><div style=\"text-indent: 2em;\">经审查，<span class=\"font-regulations-16\"></span>本机关决定：终止对该案的审理。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;margin-bottom: 37px;\"><span class=\"font-date\"></span></div><div>抄送：<span class=\"font-defendant\"></span></div></div>");
        DOCUMENT_HTML.put("17", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-17\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议决定书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-17-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-17-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-17-3\">{3}</span></div><div class=\"div-apply\"></div><div class=\"div-third-party\"></div><div class=\"div-agent\"></div> <div class=\"div-defendant\"></div><div style=\"text-indent: 2em;\">申请人不服被申请人<span class=\"font-reason\"></span>，于<span class=\"font-apply-time\"></span>提出复议申请，本机关依法予以受理。</div><div style=\"text-indent: 2em;\">申请人请求：<span id=\"font_input_17_1\"></span></div><div style=\"text-indent: 2em;\">申请人称：<span id=\"font_input_17_2\"></span></div><div style=\"text-indent: 2em;\">被申请人请称：<span id=\"font_input_17_3\"></span></div><div style=\"text-indent: 2em;\">经审理查明：<span id=\"font_input_17_5\"></span></div><div style=\"text-indent: 2em;\">本机关认为：<span id=\"font_input_17_6\"></span></div><div style=\"text-indent: 2em;\">综上，本机关决定如下：<span id=\"font_input_17_7\"></span></div><div style=\"text-indent: 2em;\">申请人如不服本行政复议决定，可以在收到本行政复议决定书之日起15日内，依法向人民法院起诉。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">本行政复议决定书一经送达，即发生法律效力。</div><div style=\"text-align: right;margin-right: 4em;\"><span class=\"font-date\"></span></div></div>");
        DOCUMENT_HTML.put("18", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-18\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议决定书（驳回）</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-18-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-18-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-18-3\">{3}</span></div><div class=\"div-apply\"></div><div class=\"div-third-party\"></div><div class=\"div-agent\"></div><div class=\"div-defendant\"></div><div style=\"text-indent: 2em;\">申请人不服被申请人<span class=\"font-reason\"></span>，于<span class=\"font-apply-time\"></span>提出复议申请，本机关依法予以受理。</div><div style=\"text-indent: 2em;\">申请人请求：<span id=\"font_input_18_1\"></span></div><div style=\"text-indent: 2em;\">申请人称：<span id=\"font_input_18_2\"></span></div><div style=\"text-indent: 2em;\">被申请人请称：<span id=\"font_input_18_3\"></span></div><div style=\"text-indent: 2em;\">经审理查明：<span id=\"font_input_18_5\"></span></div><div style=\"text-indent: 2em;\">本机关认为：<span id=\"font_input_18_6\"></span></div><div style=\"text-indent: 2em;\"><span class='font-regulations-18'></span>本机关决定：驳回申请人的行政复议申请。</div><div style=\"text-indent: 2em;\">申请人如不服本行政复议决定，可以在收到本行政复议决定书之日起15日内，依法向人民法院起诉。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">本行政复议决定书一经送达，即发生法律效力。</div><div style=\"text-align: right;margin-right: 4em;\"><span class=\"font-date\"></span></div></div>");
        DOCUMENT_HTML.put("19", "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\"><span class=\"font-title-19\">{0}</span></div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议决定书（维持）</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\"><span class=\"font-number-19-1\">{1}</span>﹝<span class=\"font-year-no\"></span>﹞<span class=\"font-number-19-2\">{2}</span><span class=\"font-no\"></span><span class=\"font-number-19-3\">{3}</span> </div><div class=\"div-apply\"></div><div class=\"div-third-party\"></div><div class=\"div-agent\"></div><div class=\"div-defendant\"></div><div style=\"text-indent: 2em;\">申请人不服被申请人<span class=\"font-reason\"></span>，于<span class=\"font-apply-time\"></span>提出复议申请，本机关依法予以受理。</div><div style=\"text-indent: 2em;\">申请人请求：<span id=\"font_input_19_1\"></span></div><div style=\"text-indent: 2em;\">申请人称：<span id=\"font_input_19_2\"></span></div><div style=\"text-indent: 2em;\">被申请人请称：<span id=\"font_input_19_3\"></span></div><div style=\"text-indent: 2em;\">经审理查明：<span id=\"font_input_19_5\"></span></div><div style=\"text-indent: 2em;\">本机关认为：<span id=\"font_input_19_6\"></span></div><div style=\"text-indent: 2em;\">《中华人民共和国行政复议法》第二十八条第一款第（一）项之规定，本机关决定：维持被申请人作出的具体行政行为。</div><div style=\"text-indent: 2em;\">申请人如不服本行政复议决定，可以在收到本行政复议决定书之日起15日内，依法向人民法院起诉。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">本行政复议决定书一经送达，即发生法律效力。</div><div style=\"text-align: right;margin-right: 4em;\"><span class=\"font-date\"></span></div></div>");
    }
    
    /**个人*/
    public static String TYPE_1 = "1";
    /**多人*/
    public static String TYPE_2 = "2";
    /**法人组织*/
    public static String TYPE_3 = "3";
    /**其他申请*/
    public static String TYPE_4 = "4";
    /**默认*/
    public static String APPLY_MODE_1 = "1";
    /**邮寄*/
    public static String APPLY_MODE_2 = "2";
    /**其他备注*/
    public static String APPLY_MODE_3 = "3";
    /**网件*/
    public static String APPLY_MODE_4 = "4";
    
    String irsa_cases;
    String id;
    String createtime;
    String trash;
    
    /**
    1=立案申请，
    2=限期补正，
    3=告知，
    4=转办，
    5=转送，
    6=不予受理，
    7=逾期未补正，
    8=主动放弃申请
    
    9=待被申请人答复，
    10=被申请人未答复，
    11=被申请人已答复，
    
    12=受理，
    
    13=延期，
    14=中止，
    20=听证，
    
    17=支持（变更），
    21=支持（撤销），
    22=支持（确认违法），
    23=支持（责令限期履行），
    24=支持（确认无效），
    25=支持（调解与决定），
    26=支持（自定义决定），
    
    18=否定（驳回），
    19=否定（维持），
    
    15=终止（撤回终止），
    16=终止（法定/自定义终止），
     */
    String state;//案件状态
    String type;//案件主体类型，1=个人，2=多人，3=法人组织，4=其他申请 【等待调试 原因：在受理/审理案件列表中显示申请人类型】
    String year_number;//案件年号
    String number;//案件编号
    String apply_time;//申请时间，示例：2019-10-01

    String apply_mode;//申请方式，1=默认，2=邮寄，3=其他
    String apply_mode_another;//申请方式其他备注
    String reason_id;//案由
    String reason_another;//其他自定义案由 【新加字段】
    String request_category_id;//请求类别编号 【新加字段】
    String administrative_category_id;//行政类别编号
    String whether_normativedoc_review;//是否提出规范性文件审查

    String specific_behavior;//不服具体行政行为
    String apply_request;//行政复议请求
    String facts_reasons;//事实和理由

    String expected_time;//预计结案时间，默认立案时间+60天（2个月），示例：2018-12-01，
    String entry_account_id;//录入人
    String last_time;//最后修改时间，示例：2018-10-01 10:01:23
    String defendant_reply;//被申请人答复
    String defendant_remark;//被申请人备注
    
    public static String INSERT = "insert into irsa_cases(createtime,id,state,type,year_number,number,administrative_category_id,reason_id,specific_behavior,apply_request,facts_reasons," +
    		"apply_mode,apply_mode_another,apply_time,expected_time,entry_account_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
    
    public static String UPDATE_LASTTIME = "update irsa_cases set last_time = ? where id = ?";
    public static String UPDATE_STATE = "update irsa_cases set state = ? where id = ?";
    public static String UPDATE_CONTENT_1 = "update irsa_cases set specific_behavior = ? where id = ?";
    public static String UPDATE_CONTENT_2 = "update irsa_cases set apply_request = ? where id = ?";
    public static String UPDATE_CONTENT_3 = "update irsa_cases set facts_reasons = ? where id = ?";
    public static String UPDATE_CONTENT_5 = "update irsa_cases set defendant_reply = ? where id = ?";
    public static String UPDATE_CONTENT_6 = "update irsa_cases set defendant_remark = ? where id = ?";
    public static String UPDATE_REPLY = "update irsa_cases set defendant_reply = ?,defendant_remark = ?,state = ? where id = ?";
    public static String UPDATE_DEFENDANT_REPLY_NUM = "update irsa_cases set  defendant_reply_num=? where id = ?";
    public static String UPDATE_MORE_REPLY = "update irsa_cases set defendant_reply = ?,defendant_remark = ?,state = ?,defendant_reply_num=defendant_reply_num-1 where id = ?";
    
    public static String DELETE ="delete from irsa_cases where id = ?";
    
    private static String SELECT_EXIST = "select id,type,state,defendant_reply_num from irsa_cases where trash = '0' ";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "and id = ?";
    
    public static String SELECT = "select c.createtime,c.id,c.state,c.type,c.year_no,c.no,c.administrative_category_id,c.reason_id," +
            "c.specific_behavior,c.apply_request,c.facts_reasons,c.apply_mode,c.apply_mode_another,c.defendant_reply,c.defendant_remark," +
            "c.label_type,c.label_administrative_category,c.label_reason,c.label_apply_mode,c.labels,c.apply,c.apply_third_party,c.third_party,c.defendant," +
            "c.apply_time,c.expected_time,c.create_time,c.create_account,c.create_department_id,c.create_department,c.last_time,c.limit_time_1,c.limit_time_2,c.limit_time_3," +
            "CONCAT('不服',defendant,specific_behavior) title from (" +
    		"select a.createtime,a.id,a.state,a.type,a.year_number year_no,a.number no,a.administrative_category_id,a.reason_id," +
    		"a.specific_behavior,a.apply_request,a.facts_reasons,a.apply_mode,a.apply_mode_another,a.defendant_reply,a.defendant_remark," +
            "case a.type when '1' then '个人' when '2' then '多人' when '3' then '法人组织' else '其他申请' end label_type," +
            "(select CONCAT(p.title,'-',r.title) from irsa_administrative_category r,irsa_administrative_category p where p.id = r.parent_id and r.id = a.administrative_category_id) label_administrative_category," +
            "(select r.title from irsa_cases_reason r where r.id = a.reason_id) label_reason," +
            "case a.apply_mode when '1' then '收件' when '2' then '邮寄' when '4' then '网件' else a.apply_mode_another end label_apply_mode," +
    		"(select GROUP_CONCAT(l.title separator '，') from irsa_cases_labels cl,irsa_label l where l.id = cl.label_id and cl.cases_id = a.id) labels," +
    		"(select GROUP_CONCAT(p.name separator '，') from irsa_cases_personnel p where p.personnel_type = '1' and p.cases_id = a.id) apply," +
    		"(select GROUP_CONCAT(p.name separator '，') from irsa_cases_personnel p where p.personnel_type = '7' and p.cases_id = a.id) apply_third_party," +
    		"(select GROUP_CONCAT(p.name separator '，') from irsa_cases_personnel p where p.personnel_type = '3' and p.cases_id = a.id) third_party," +
    		"(select GROUP_CONCAT(p.name) from irsa_cases_personnel p where p.personnel_type = '2' and p.cases_id = a.id and p.name != '' ) defendant," +
    		"date_format(a.apply_time,'%Y-%m-%d') apply_time," +
    		"date_format(a.expected_time,'%Y-%m-%d') expected_time," +
    		"date_format(a.createtime,'%Y-%m-%d %H:%i:%s') create_time," +
    		"(select aa.nickname from irsa_console_account aa where aa.id = a.entry_account_id) create_account," +
    		"(select (select r.department_id from irsa_console_role r where r.id = aa.role_id) from irsa_console_account aa where aa.id = a.entry_account_id) create_department_id," +
    		"(select (select (select d.title from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = aa.role_id) from irsa_console_account aa where aa.id = a.entry_account_id) create_department," +
    		"date_format(a.last_time,'%Y-%m-%d %H:%i:%s') last_time," +
    		"(select date_format(t.limit_time,'%Y-%m-%d') from irsa_cases_time t where t.cases_id = a.id and t.type = '1' limit 1) limit_time_1," +
    		"(select date_format(t.limit_time,'%Y-%m-%d') from irsa_cases_time t where t.cases_id = a.id and t.type = '2' limit 1) limit_time_2," +
    		"(select date_format(t.limit_time,'%Y-%m-%d') from irsa_cases_time t where t.cases_id = a.id and t.type = '3' limit 1) limit_time_3 " +
    		"from irsa_cases a where a.trash = '0') c ";
    public static String SELECT_BY_ID = SELECT + "where c.id = ? ";
    
    public static String SELECT_EXIST_BY_NO = SELECT + "where c.create_department_id = ? and c.year_no = ? and c.no = ?";
    
    public static String SELECT_LABELS = "select l.id, l.title text from irsa_cases_labels cl,irsa_label l where l.id = cl.label_id and cl.cases_id = ? order by l.title";
    
    public static String SELECT_4_DOC = "select a.createtime,a.id," +
    		"a.specific_behavior,a.apply_request,a.facts_reasons,a.defendant_reply,a.defendant_remark," +
            "date_format(a.createtime,'%Y年%m月%d日') create_time," +
            "(select (select (select d.title from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = aa.role_id) from irsa_console_account aa where aa.id = a.entry_account_id) create_department " +
            "from irsa_cases a where a.trash = '0' and a.id = ?";
    
    public static void main(String[] args) {
        System.out.println(STATE_COLOR);
    }
}

