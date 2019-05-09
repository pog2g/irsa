package com.common.utils;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Logger log = LoggerFactory.getLogger(Utils.class);
    public static SimpleDateFormat SDF_YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    public static Map<String, Object> getErrorMap(String msg) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("result", 0);
        data.put("error_msg", msg == null ? "系统繁忙，请重试" : msg);
        return data;
    }

    public static Map<String, Object> getSuccessMap(Object obj) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("result", 1);
        data.put("data", obj);
        log.info(data.toString());
        return data;
    }

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getCreateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static BigDecimal changeToBigDecimal(Object obj) {
        BigDecimal bd = new BigDecimal(0);
        try {
            if (obj == null) {
                return bd;
            }

            String str = toString(obj);
            if (StringUtils.isBlank(str)) {
                return bd;
            }

            bd = new BigDecimal(str);
            return bd;
        } catch (Exception e) {
            log.info("[{}] 转换 [BigDecimal] 失败", obj);
            return bd;
        }
    }

    public static String changeISO88591ToUTF8(String str) {
        try {
            return new String(str.getBytes("ISO8859-1"), "UTF-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return str;
        }
    }

    public static String httpGet(String url) {
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isReader = null;
        BufferedReader reader = null;
        try {
            URL httpUrl = new URL(url);
            connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setDoOutput(false);//输出
            connection.setDoInput(true);//输入
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);//是否使用缓存
            connection.setInstanceFollowRedirects(false);//是否自动执行重定向
            connection.setConnectTimeout(30 * 1000);
            connection.connect();
            int state = connection.getResponseCode();
            if (state != 200) {
                throw new RuntimeException("调用接口失败");
            }
            is = connection.getInputStream();
            isReader = new InputStreamReader(is);
            reader = new BufferedReader(isReader);
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("[" + url + "] " + result);
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (isReader != null) {
                try {
                    isReader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static List<Map<String, Object>> changeToSelectList(List<Map<String, Object>> list) {
        Map<String, Object> index = new HashMap<String, Object>();
        index.put("id", "-1");
        index.put("text", "请选择");
        list.add(0, index);
        return list;
    }

    public static boolean checkPwd(String str) {
        try {
            String check = "^[a-zA-Z0-9_]{6,18}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(str);
            return matcher.matches();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public static boolean checkPhone(String phone) {
        try {
            String check = "^1[0-9]{10}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(phone);
            return matcher.matches();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public static boolean checkIdNo(String idNo) {
        try {
            if (idNo.length() != 15 && idNo.length() != 18) {
                return false;
            }
            if (idNo.length() == 15) {
                String check = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(idNo);
                return matcher.matches();
            }
            String check = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(idNo);
            return matcher.matches();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public static int random(int number) {
        return (int) (1 + Math.random() * number);
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString().trim();
    }

    public static String modifyDate(String mode, String type, int num, String dateTime) throws ParseException {
        Date date = new Date();
        if (StringUtils.isNotBlank(dateTime)) {
            date = SDF_YYYYMMDD.parse(dateTime);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if ("+".equals(mode)) {
            if ("Y".equals(type)) {
                calendar.add(Calendar.YEAR, num);
            } else if ("M".equals(type)) {
                calendar.add(Calendar.MONTH, num);
            } else {
                calendar.add(Calendar.DATE, num);
            }
        } else {
            if ("Y".equals(type)) {
                calendar.add(Calendar.YEAR, -num);
            } else if ("M".equals(type)) {
                calendar.add(Calendar.MONTH, -num);
            } else {
                calendar.add(Calendar.DATE, -num);
            }
        }
        return SDF_YYYYMMDD.format(calendar.getTime());
    }

    public static String createDocument(String html, String name) throws JDOMException, IOException {
        SAXBuilder sb = new SAXBuilder();
        StringReader read = new StringReader("<div>" + html + "</div>");
        Document doc = sb.build(read);
        Element response = doc.getRootElement();
        Element content = (Element) (((Element) ((Element) response.getChildren().get(2)).getChildren().get(1)).getChildren().get(0));
        content.setText(name);
        XMLOutputter outputter = new XMLOutputter();
        Format f = Format.getPrettyFormat();
        f.setEncoding("UTF-8");//default=UTF-8  
        outputter.setFormat(f);
        return outputter.outputString(doc);
    }

    public static String MD5(String s) {
        return encrypt("MD5", s);
    }

    private static String encrypt(String type, String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes("UTF-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(type);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            //System.out.println("密文="+new String(md));
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static void main(String[] args) throws ParseException, JDOMException, IOException {
//        String result = Utils.httpGet("https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code");
//        JSONObject js = new JSONObject(result);
//        System.out.println(js.has("errcode"));
//        for (int i = 0; i < 10; i++)
//            System.out.println(random(10));

//        String file = "123.doc";
//        System.out.println(file.substring(0, file.lastIndexOf(".")));

//        String html = "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\">郑州市人民政府行政复议办公室</div>" +
//                      "<div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议受理通知书</div>" +
//                      "<div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\">" +
//                          "<div style=\"text-align: right;margin-bottom: 37px;\">" +"郑政行复办（复受通）字﹝<span class=\"font-year-no\">2018</span>﹞<span class=\"font-no\">006</span>号" +"</div>" +
//                          "<div><span class=\"font-apply\">周善平、吕子乔</span>：</div>" +
//                          "<div style=\"text-indent: 2em;\">" +
//                          "你不服<span class=\"font-defendant\">郑州市公安局柳林分局</span><span class=\"font-reason\">不履行法定职责（不作为）</span>决定，于<span class=\"font-apply-time\">2018年8月27日</span>向本机关提出行政复议申请。经审查，该申请符合<span class=\"font-regulations-9\">《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》、《郑州市集中受理审理行政复议案件暂行规定》</span>的有关规定，现决定予以受理。" +
//                          "</div>" +
//                          "<div style=\"text-indent: 2em;\">" +
//                          "根据<span class=\"font-regulations-9\">《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》、《郑州市集中受理审理行政复议案件暂行规定》</span>的有关规定，你可以直接参加行政复议，也可以委托代理人代为参加行政复议。你收到本通知后如委托代理人，需向案件审理机关出具委托书并载明委托事项、权限和期限。" +
//                          "</div>" +
//                          "<div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div>" +
//                          "<div style=\"text-align: right;margin-right: 4em;\"><span class=\"font-date\">2018年9月14日</span></div>" +
//                      "</div>";
//        SAXBuilder sb = new SAXBuilder();
//        StringReader read = new StringReader("<div>"+html+"</div>"); 
//        Document doc = sb.build(read);
//        Element response = doc.getRootElement();
//        Element content = (Element)(((Element)((Element)response.getChildren().get(2)).getChildren().get(1)).getChildren().get(0));
//        content.setText("鲍旭超");
//        
//        XMLOutputter out = new XMLOutputter();
//        System.out.println(out.outputString(doc).replaceAll("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
//        System.out.println("Ok");

//        String number = "1,2,,";
//        System.out.println(number.split(",").length);

        //1，判断是否在节假日中 或 是周么 
        //2，判断是否加班，判断是否周末
        //3，

        // 
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = sdf.parse("2018-09-07");
//        String dateTime = "2018-09-07";
//        for (int i=1;i<=4;i++) {
//            dateTime = modifyDate("+", "D", 1, dateTime);
//            while (!isWorkDay(dateTime, null, null)){
//                dateTime = modifyDate("+", "D", 1, dateTime);
//            }
//            System.out.println(dateTime);
//        }

        String html = "因为你们大家";
        html = html.replaceAll("你们", "你");
        System.out.println(html);
    }

    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            week = 7;
        }
        return week;
    }

    public static boolean isWorkDay(String dateTime, List<String> restList, List<String> workList) throws ParseException {
        if (workList != null && workList.size() > 0) {
            if (workList.contains(dateTime)) {
                return true;
            }
        }
        if (restList != null && restList.size() > 0) {
            if (restList.contains(dateTime)) {
                return false;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateTime);
        int week = getDayOfWeek(date);
        if (week == 6 || week == 7) {
            return false;
        }
        return true;
    }

    /**
     * 判断对象是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        boolean flag = true;
        if (str != null && !str.equals("")) {
            if (str.toString().length() > 0) {
                flag = true;
            }
        } else {
            flag = false;
        }
        return flag;
    }
}
