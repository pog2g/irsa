package com.irsa.cases.console.action;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.utils.ExcelUtils;
import com.common.utils.Utils;
import com.irsa.cases.console.service.CasesConsoleService;

@Controller
@RequestMapping("/console/cases")
public class CasesConsoleAction {
    Logger log = LoggerFactory.getLogger(CasesConsoleAction.class);
    @Autowired
    CasesConsoleService casesConsoleService;
    
    @RequestMapping("/get_draft_list")
    @ResponseBody
    public Map<String, Object> getDraftList(HttpServletRequest request, String mode, String page) {
        Map<String, Object> result = casesConsoleService.getDraftList(Utils.toString(request.getSession().getAttribute("account")), mode, page);
        log.info("[/console/cases/get_draft_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save_draft")
    @ResponseBody
    public Map<String, Object> saveDraft(HttpServletRequest request, String id, String type, 
            @RequestParam(required=false, name="year_no") String yearNo, String no,
            @RequestParam(required=false, name="reason_id") String reasonId,
            @RequestParam(required=false, name="administrative_category_id") String administrativeCategoryId, 
            @RequestParam(required=false, name="reason_another") String reasonAnother,
            @RequestParam(required=false, name="specific_behavior") String specificBehavior, 
            @RequestParam(required=false, name="apply_request") String applyRequest,
            @RequestParam(required=false, name="facts_reasons") String factsReasons,
            @RequestParam(required=false, name="apply_mode") String applyMode,
            @RequestParam(required=false, name="apply_mode_another") String applyModeAnother,
            @RequestParam(required=false, name="request_category[]") String [] requestCategoryArr,
            @RequestParam(required=false, name="apply_time") String applyTime, String labels) {
        Map<String, Object> result = casesConsoleService.draft(id, type, yearNo, Utils.toString(no), administrativeCategoryId, reasonId, Utils.toString(reasonAnother), Utils.toString(specificBehavior), Utils.toString(applyRequest), Utils.toString(factsReasons), 
                applyMode, Utils.toString(applyModeAnother), Utils.toString(applyTime), labels, Utils.toString(request.getSession().getAttribute("account")),requestCategoryArr);
        log.info("[/console/cases/save_draft] {}", result);
        return result;
    }
    
    
    @RequestMapping("/dowload")
    @ResponseBody
    public void exportVehicleInfo(HttpServletRequest req, HttpServletResponse resp) {
        String filename = req.getParameter("filename");
        DataInputStream in = null;
        OutputStream out = null;
        try{
            resp.reset();// 清空输出流
            
            String resultFileName = filename + System.currentTimeMillis() + ".doc";
            resultFileName = URLEncoder.encode(resultFileName,"UTF-8");  
            resp.setCharacterEncoding("UTF-8");  
            resp.setHeader("Content-disposition", "attachment; filename=" + resultFileName);// 设定输出文件头
            resp.setContentType("application/msexcel");// 定义输出类型
            //输入流：本地文件路径
            in = new DataInputStream(new FileInputStream(new File(System.getProperty("user.dir")+"/"+filename+".doc")));
            //输出流
            out = resp.getOutputStream();
            //输出文件
            int bytes = 0;
            byte[] bufferOut = new byte[1024];  
            while ((bytes = in.read(bufferOut)) != -1) {  
                out.write(bufferOut, 0, bytes);  
            }
        } catch(Exception e){
            e.printStackTrace();
            resp.reset();
            try {
                OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");  
                String data = "<script language='javascript'>alert(\"\\u64cd\\u4f5c\\u5f02\\u5e38\\uff01\");</script>";
                writer.write(data); 
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    
    @RequestMapping("/delete_draft")
    @ResponseBody
    public Map<String, Object> deleteDraft(String id, String mode) {
        Map<String, Object> result = casesConsoleService.deleteDraft(id, mode);
        log.info("[/console/cases/delete_draft] {}", result);
        return result;
    }
    
    @RequestMapping("/exist_draft")
    @ResponseBody
    public Map<String, Object> existDraft(String id, String mode) {
        Map<String, Object> result = casesConsoleService.existDraft(id, mode);
        log.info("[/console/cases/exist_draft] {}", result);
        return result;
    }
    
    @RequestMapping("/get_draft")
    @ResponseBody
    public Map<String, Object> getDraft(String id) {
        Map<String, Object> result = casesConsoleService.getDraft(id);
        log.info("[/console/cases/get_draft] {}", result);
        return result;
    }
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList23(HttpServletRequest request, String mode, String page, 
            @RequestParam(required=false, name="search_year_no") String yearNo, 
            @RequestParam(required=false, name="search_no") String no, 
            @RequestParam(required=false, name="search_apply") String apply,
            @RequestParam(required=false, name="search_defendant") String defendant, 
            @RequestParam(required=false, name="search_state") String state,
            @RequestParam(required=false, name="search_apply_time_1") String applyTime1,
            @RequestParam(required=false, name="search_apply_time_2") String applyTime2,
            @RequestParam(required=false, name="search_key") String key) {
        Map<String, Object> result = casesConsoleService.getList(Utils.toString(request.getSession().getAttribute("account")), 
                page, Utils.toString(yearNo), Utils.toString(no), Utils.toString(apply), Utils.toString(defendant), state, 
                Utils.toString(applyTime1), Utils.toString(applyTime2), Utils.toString(key), mode);
        log.info("[/console/cases/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/switchState")
    @ResponseBody
    public Map<String, Object> switchState(HttpServletRequest request, String res_id) {
    	Map<String,Object> result=new HashMap<String,Object>();
    	int count = casesConsoleService.getSwitchState(res_id);
    	result.put("rs", count);
    	log.info("[/console/cases/switchState] {}", count);
    	return result;
    }
    
    /**
     * 导出_受理情况
     */
    @RequestMapping(value = "/exportCasesExcel")
    public void exportCasesExcel(HttpServletResponse response,HttpServletRequest request, String mode, String page, 
            @RequestParam(required=false, name="search_year_no") String yearNo, 
            @RequestParam(required=false, name="search_no") String no, 
            @RequestParam(required=false, name="search_apply") String apply,
            @RequestParam(required=false, name="search_defendant") String defendant, 
            @RequestParam(required=false, name="search_state") String state,
            @RequestParam(required=false, name="search_apply_time_1") String applyTime1,
            @RequestParam(required=false, name="search_apply_time_2") String applyTime2,
            @RequestParam(required=false, name="search_key") String key,
            @RequestParam(required=false, name="export_filename") String export_filename,
            @RequestParam(required=false, name="export_clms") String export_clms,
            @RequestParam(required=false, name="export_titles") String export_titles
            ) {
    	 List<Map<String, Object>> list =casesConsoleService.getCasesList(Utils.toString(request.getSession().getAttribute("account")), 
                page, Utils.toString(yearNo), Utils.toString(no), Utils.toString(apply), Utils.toString(defendant), state, 
                Utils.toString(applyTime1), Utils.toString(applyTime2), Utils.toString(key), mode);
        exportToExcel(request, response, list,export_filename,export_clms,export_titles);
    }
    
    /**
     * 导出_Excel
     */
    public void exportToExcel(HttpServletRequest req,HttpServletResponse response, List<Map<String, Object>> list,String filename,String clms,String titles) {
       try {
            if(Utils.isNotEmpty(filename)){
                filename= URLDecoder.decode(filename,"UTF-8");
            }
            if(Utils.isNotEmpty(clms)){
            	clms= URLDecoder.decode(clms,"UTF-8");
            }
            if(Utils.isNotEmpty(titles)){
                titles=URLDecoder.decode(titles,"UTF-8");
            }
            ExcelUtils.mapListDataToExcel(list, titles, clms, filename, req, response);
            
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
    }
    
    
    //保存案件
    @RequestMapping("/save")
    @ResponseBody
    //参数(Servlet请求,主键ID,案件类型,案件年份,案件编号,行政管理类别,案由,自定义案由,不服具体行政行为,行政复议请求,事实与理由,申请方式,其他申请方式,申请时间,关键字,案件预览文书)
    public Map<String, Object> save(HttpServletRequest request, String id, String type, 
            @RequestParam(required=false, name="year_no") String yearNo, String no,
            @RequestParam(required=false, name="administrative_category_id") String administrativeCategoryId, 
            @RequestParam(required=false, name="reason_id") String reasonId, 
            @RequestParam(required=false, name="reason_another") String reasonAnother,
            @RequestParam(required=false, name="request_category[]") String [] requestCategoryArr,
            @RequestParam(required=false, name="normative_doc") String normativeDoc,
            @RequestParam(required=false, name="specific_behavior") String specificBehavior,
            @RequestParam(required=false, name="apply_request") String applyRequest,
            @RequestParam(required=false, name="facts_reasons") String factsReasons,
            @RequestParam(required=false, name="apply_mode") String applyMode,
            @RequestParam(required=false, name="apply_mode_another") String applyModeAnother,
            @RequestParam(required=false, name="apply_time") String applyTime, String labels, String html) {
        Map<String, Object> result = casesConsoleService.save(id, type, yearNo, Utils.toString(no), administrativeCategoryId, reasonId, Utils.toString(reasonAnother), Utils.toString(specificBehavior), Utils.toString(applyRequest), Utils.toString(factsReasons), 
                applyMode, Utils.toString(applyModeAnother), Utils.toString(applyTime), labels, html, Utils.toString(request.getSession().getAttribute("account")),requestCategoryArr);

        for (int i = 0; i < requestCategoryArr.length; i++) {
            System.out.println("请求类别的值========" + requestCategoryArr[i]);
        }
        System.out.println("规范性文件审查==" + normativeDoc);
        log.info("[/console/cases/save] {}", result);
        return result;
    }
    
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> getMap(String id, @RequestParam(required=false, name="is_with_personnel") String isWithPersonnel) {
        Map<String, Object> result = casesConsoleService.getMap(id, isWithPersonnel);
        log.info("[/console/cases/get_object] {}", result);
        return result;
    }
    
    @RequestMapping("/save_content")
    @ResponseBody
    public Map<String, Object> saveContent(String type, String id, String content) {
        Map<String, Object> result = casesConsoleService.saveContent(type, id, Utils.toString(content));
        log.info("[/console/cases/save_content] {}", result);
        return result;
    }
    
    @RequestMapping("/save_labels")
    @ResponseBody
    public Map<String, Object> saveLabels(String id, String labels) {
        Map<String, Object> result = casesConsoleService.saveLabels(id, labels);
        log.info("[/console/cases/save_labels] {}", result);
        return result;
    }
    
    @RequestMapping("/labels")
    @ResponseBody
    public Map<String, Object> getLabelList(@RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesConsoleService.getLabelList(casesId);
        log.info("[/console/cases/labels] {}", result);
        return result;
    }
    
    @RequestMapping("/total")
    @ResponseBody
    public Map<String, Object> total(HttpServletRequest request) {
        Map<String, Object> result = casesConsoleService.total(request);
        log.info("[/console/cases/total] {}", result);
        return result;
    }
    
    @RequestMapping("/create_pdf")
    @ResponseBody
    public Map<String, Object> createPDF( @RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesConsoleService.createPDF(casesId);
        log.info("[/console/cases/create_pdf] {}", result);
        return result;
    }

    public CasesConsoleService getCasesConsoleService() {
        return casesConsoleService;
    }
    public void setCasesConsoleService(CasesConsoleService casesConsoleService) {
        this.casesConsoleService = casesConsoleService;
    }
}
