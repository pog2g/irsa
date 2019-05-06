import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class TestDoc {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        createDoc();
        System.out.println("ok");
    }
    
    public static void createDoc() throws IOException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>");
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;\">郑州市人民政府行政复议办公室</div><div style=\"line-height: 37px;font-size: 29px;text-align: center;font-family: simsun;color: red;font-weight: 100;margin-bottom: 37px;\">行政复议受理通知书</div><div style=\"line-height: 37px;font-size: 21px;font-family: '仿宋';\"><div style=\"text-align: right;margin-bottom: 37px;\">郑政行复办（复受通）字﹝<span class=\"font-year-no\">2018</span>﹞<span class=\"font-no\">006</span>号</div><div><span class=\"font-apply\">鲍旭超</span>：</div><div style=\"text-indent: 2em;\">你不服<span class=\"font-defendant\">郑州市公安局柳林分局</span><span class=\"font-reason\">不履行法定职责（不作为）</span>决定，于<span class=\"font-apply-time\">2018年8月27日</span>向本机关提出行政复议申请。经审查，该申请符合<span class=\"font-regulations-9\">《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》、《郑州市集中受理审理行政复议案件暂行规定》</span>的有关规定，现决定予以受理。</div><div style=\"text-indent: 2em;\">根据<span class=\"font-regulations-9\">《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》、《郑州市集中受理审理行政复议案件暂行规定》</span>的有关规定，你可以直接参加行政复议，也可以委托代理人代为参加行政复议。你收到本通知后如委托代理人，需向案件审理机关出具委托书并载明委托事项、权限和期限。</div><div style=\"text-indent: 2em;margin-bottom: 37px;\">特此通知。</div><div style=\"text-align: right;margin-right: 4em;\"><span class=\"font-date\">2018年9月14日</span></div></div></div>");
        buffer.append("</body></html>");
        File outFile = new File("D:/test.doc");
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
}
