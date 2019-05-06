package com.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @author EZ
 * 导出
 */
public class ExcelUtils {

	/**
	 * 数据列表导出为Excel
	 * @param excelList 导出的数据列表
	 * @param titles 导出的Excel表头
	 * @param filename 导出的文件名
	 * @param request
	 * @param response
	 */
	public static void listDataToExcel(List<Object[]> excelList, String titles, String filename,
									   HttpServletRequest request, HttpServletResponse response){
		OutputStream outputStream = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(filename);
		sheet.setDefaultColumnWidth((short) 15);
		sheet.setColumnWidth(0, 20 * 256);
		HSSFCellStyle headStyle = getHeadStyle(wb);
		HSSFCellStyle bodyStyle = getBodyStyle(wb);
		String[] tit = titles.split(",");
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(20);
		for (int i = 0; i < tit.length; i++) {
			HSSFCell cell = titleRow.createCell(i);
			//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(headStyle);
			cell.setCellValue(tit[i]);
		}
		for (int i = 0; i < excelList.size(); i++) {
			HSSFRow row = sheet.createRow(i + 1);
			Object[] obj = excelList.get(i);
			for (int j = 0; j < obj.length; j++) {
				HSSFCell cell = row.createCell(j);
				//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(obj[j] == null ? "" : String.valueOf(obj[j]));
			}
		}
		try {
			outputStream = response.getOutputStream();
			// 火狐
			if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
				filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
			} else {
				filename = URLEncoder.encode(filename, "UTF-8");
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xls");

			wb.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 数据列表导出为Excel
	 * @param maplist 导出的数据列表
	 * @param titles 导出的Excel表头
	 * @param filename 导出的文件名
	 * @param request
	 * @param response
	 */
	public static void mapListDataToExcel(List<Map<String, Object>> maplist, String titles, String clms, String filename,
										  HttpServletRequest request, HttpServletResponse response){
		OutputStream outputStream = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(filename);
		sheet.setDefaultColumnWidth((short) 15);
		sheet.setColumnWidth(0, 20 * 256);
		HSSFCellStyle headStyle = getHeadStyle(wb);
		HSSFCellStyle bodyStyle = getBodyStyle(wb);
		String[] tit = titles.split(",");
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(20);
		for (int i = 0; i < tit.length; i++) {
			HSSFCell cell = titleRow.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(tit[i]);
		}
		for (int r = 0; r < maplist.size(); r++) {
			HSSFRow row = sheet.createRow(r + 1);
			Map<String, Object> datarow = maplist.get(r);
			String[] f = clms.split(",");
			for (int i = 0; i < f.length; i++) {
				if (!f[i].equals("")) {
					HSSFCell cell = row.createCell(i);
					String ff = f[i];
					if("LV".equals(ff)||"BI".equals(ff)){
						cell.setCellValue((datarow.get(ff) == null ? "" : (String.valueOf(datarow.get(ff)))+"%"));
					}else{
						cell.setCellValue((datarow.get(ff) == null ? "" : String.valueOf(datarow.get(ff))));
					}
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(bodyStyle);
				}
			}
		}
		try {
			outputStream = response.getOutputStream();
			// 火狐
			if (request.getHeader("User-Agent").toLowerCase()
					.indexOf("firefox") > 0) {
				filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
			} else {
				filename = URLEncoder.encode(filename, "UTF-8");
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
			wb.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void mergeCells(HSSFSheet sheet, int startRowIndex,int mergeColumnIndex,int referColumnIndex){
		String cellStr = sheet.getRow(startRowIndex).getCell(referColumnIndex).getStringCellValue();
		int index = startRowIndex;
        HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);//添加空行，合并需要最后一行比较
        HSSFCell cell = row.createCell(referColumnIndex);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("");
		for (int i= startRowIndex+1;i<=sheet.getLastRowNum();i++) {
			String cellS = sheet.getRow(i).getCell(referColumnIndex).getStringCellValue();
			if(!cellS.equals(cellStr)){
				if(i-index>1){//超过两行，合并
					sheet.addMergedRegion(new Region(index,(short)mergeColumnIndex,i-1,(short)mergeColumnIndex));
					HSSFCellStyle style = sheet.getRow(index).getCell(mergeColumnIndex).getCellStyle();
					style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					sheet.getRow(index).getCell(mergeColumnIndex).setCellStyle(style);
				}
				cellStr = cellS;
				index = i;
			}
		}
	}

	/**
	 * 数据列表导出为Excel
	 * @param maplist 导出的数据列表
	 * @param titles 导出的Excel表头
	 * @param filename 导出的文件名
	 * @param request
	 * @param response
	 */
	public static HSSFWorkbook ListMapDataToExcel(List<Map<String, Object>> maplist, String titles, String clms, String filename,int startRowIndex,int[] mergeColumnIndexs,int referColumnIndex){
		OutputStream outputStream = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(filename);
		sheet.setDefaultColumnWidth((short) 15);
		sheet.setColumnWidth(0, 20 * 256);
		String[] tit = titles.split(",");
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(20);
		for (int i = 0; i < tit.length; i++) {
			HSSFCell cell = titleRow.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(tit[i]);
		}
		for (int r = 0; r < maplist.size(); r++) {
			HSSFRow row = sheet.createRow(r + 1);
			Map<String, Object> datarow = maplist.get(r);
			String[] f = clms.split(",");
			for (int i = 0; i < f.length; i++) {
				if (!f[i].equals("")) {
					HSSFCell cell = row.createCell(i);
					String ff = f[i];
					if("LV".equals(ff)||"BI".equals(ff)){
						cell.setCellValue((datarow.get(ff) == null ? "" : (String.valueOf(datarow.get(ff)))+"%"));
					}else{
						cell.setCellValue((datarow.get(ff) == null ? "" : String.valueOf(datarow.get(ff))));
					}
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				}
			}
		}
		for (int column:mergeColumnIndexs) {
			mergeCells(sheet,startRowIndex,column,referColumnIndex);
		}
		return wb;
	}

	public static String[][] Read(InputStream inputStream, int sheetIdx) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		int sheetNum = workbook.getNumberOfSheets();
		//System.out.println(sheetNum);
		String temp[][] = null;
		//for循环遍历单元格内容
		for (int sheetIndex = sheetIdx; sheetIndex < sheetIdx + 1; sheetIndex++) {
			//根据下标获取sheet
			HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			//workbook.getSheetName(sheetIndex) 根据下标获取sheet 名称
			System.out.println("sheet序号：" + sheetIndex + "，sheet名称：" + workbook.getSheetName(sheetIndex));
			//循环该sheet页中的有数据的每一行
			//打印行号，某人起始是0  System.out.println(sheet.getLastRowNum());
			//打印行数
			System.out.println(sheet.getPhysicalNumberOfRows());
			//遍历每行内容从行号为0开始
			if (sheet.getPhysicalNumberOfRows() > 0) {
				temp = new String[sheet.getPhysicalNumberOfRows()][];
			} else {
				return null;
			}

			for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
				//System.out.println(rowIndex);打印遍历行号
				//根据行号，遍历该行
				HSSFRow row = sheet.getRow(rowIndex);
				//如果该行为空，则结束本次循环
				if (row == null) {
					continue;
				}
				//num 为该行 有效单元格个数，取 num的话，取值会不全。   lastnum为 有效单元格最后各个单元格的列号，这样可以遍历取到该行所有的单元格
				//System.out.println("num  " + row.getPhysicalNumberOfCells());
				//System.out.println("lastnum " + row.getLastCellNum());

				if (row.getLastCellNum() == -1) {
					continue;
				}
				temp[rowIndex] = new String[row.getLastCellNum()];

				for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
					HSSFCell cell = row.getCell(cellnum);
					if (cell != null) {
						String value = "";
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								//  如果是date类型则 ，获取该cell的date值
								Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
								SimpleDateFormat sdf=new SimpleDateFormat("YYYY/MM/dd");
								value=sdf.format(date);
							} else { // 纯数字
								value = String.valueOf(cell.getNumericCellValue());
							}
						} else {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							value=cell.getRichStringCellValue().getString();
						}

						//cell.setCellType(Cell.CELL_TYPE_STRING); 是为了修改数据类型，因为我的单元格中有数字类型。如果不这样写会出现下面的错误。
                /*        Exception in thread "main" java.lang.IllegalStateException:
                         Cannot get a text value from a numeric cell
                        at org.apache.poi.xssf.usermodel.XSSFCell.typeMismatch(XSSFCell.java:991)
                        at org.apache.poi.xssf.usermodel.XSSFCell.getRichStringCellValue(XSSFCell.java:399)
                        at net.oschina.excel.ReadExcel.read(ReadExcel.java:55)
                        at net.oschina.excel.ReadExcel.main(ReadExcel.java:68)

                        POI操作Excel时数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串并写入数据库时，
                        就会出现Cannot get a text value from a numeric cell的异常错误，解决办法就是先设置Cell的类型，
                        然后就可以把纯数字作为String类型读进来了：

                        */
						//打印出读出的数据。
						System.out.println("第" + rowIndex + "行      第" + cellnum + "列    内容为：" + value);
						temp[rowIndex][cellnum] = (value);
					}
				}
			}
			System.out.println("------------------+++++++++++++++++++--------------------");
		}
		return temp;
	}

	/**
	 * 设置表头的单元格样式
	 */
	private static HSSFCellStyle getHeadStyle(HSSFWorkbook wb){
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格的背景颜色为淡蓝色
		cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 设置单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体样式
		HSSFFont font = wb.createFont();
		// 设置字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 设置表体的单元格样式
	 */
	private static HSSFCellStyle getBodyStyle(HSSFWorkbook wb) {
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		//cellStyle.setWrapText(true);
		// 设置单元格字体样式
		HSSFFont font = wb.createFont();
		// 设置字体加粗
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

		return cellStyle;
	}
}
