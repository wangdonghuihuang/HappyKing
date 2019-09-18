package com.king.Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.king.model.People;
import com.king.service.ITestService;

@Controller
@RequestMapping("/test/")
public class TestController {
	@Autowired
	private ITestService testService;
	@PostMapping("/import")
	@ResponseBody
	public boolean addUser(@RequestParam("file") MultipartFile file) {
		boolean a=false;
		String fileName=file.getOriginalFilename();
		try {
            a = testService.batchImport(fileName, file);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return  a;

	}
	
	@RequestMapping(value = "UserExcelDownloads", method = RequestMethod.GET)
	@ResponseBody
    public String downloadAllClassmate(HttpServletResponse response) throws IOException {
		System.out.println("你好");
		HSSFWorkbook workbook=new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("信息表");
		List<People> list=testService.daochu();
		//设置要导出的文件的名字
		String fileName="导出信息"+".xls";
		//新增数据行
		int rowNum=1;
		String[] headers= {"姓名","电话","地址","备注"};
		 //headers表示excel表中第一行的表头
		HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //在表中存放查询到的数据放入对应的列
        for (People teacher : list) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(teacher.getName());
            row1.createCell(1).setCellValue(teacher.getPhone());
            row1.createCell(2).setCellValue(teacher.getAddress());
            row1.createCell(3).setCellValue(teacher.getDes());
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename="+fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
		return "成功";
	}
	@RequestMapping("/getExcel")
    public void getExcel (HttpServletResponse response) throws Exception {
		List<People> userList=testService.daochu();
 
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet =wb.createSheet("获取excel测试表格");
        HSSFRow row = null;
 
        row = sheet.createRow(0);
        row.setHeight((short)(26.25*20));
        row.createCell(0).setCellValue("用户信息列表");
        row.getCell(0).setCellStyle(getStyle(wb,0));//设置样式
        for(int i = 1;i <= 4;i++){
            row.createCell(i).setCellStyle(getStyle(wb,0));
        }
        CellRangeAddress rowRegion = new CellRangeAddress(0,0,0,3);
        sheet.addMergedRegion(rowRegion);
 
        CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
        sheet.addMergedRegion(columnRegion);
 
        row = sheet.createRow(1);
        row.createCell(0).setCellStyle(getStyle(wb,3));
        row.setHeight((short)(22.50*20));
        row.createCell(1).setCellValue("用户名");
        row.createCell(2).setCellValue("用户电话");
        row.createCell(3).setCellValue("用户地址");
        row.createCell(4).setCellValue("用户备注");
        //表格有几列，就要小于等于几才能保证每一个表头都是设置的字体格式
        for(int i = 1;i <= 4;i++){
            row.getCell(i).setCellStyle(getStyle(wb,1));
        }
 
        for(int i = 0;i<userList.size();i++){
            row = sheet.createRow(i+2);
            People user = userList.get(i);
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getPhone());
            row.createCell(3).setCellValue(user.getAddress());
            row.createCell(4).setCellValue(user.getDes());
            for(int j = 1;j <= 4;j++){
                row.getCell(j).setCellStyle(getStyle(wb,2));
            }
        }
        //默认行高
        sheet.setDefaultRowHeight((short)(16.5*20));
        //列宽自适应
        for(int i=0;i<=13;i++){
            sheet.autoSizeColumn(i);
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }
 
    /**
     * 获取样式
     * @param hssfWorkbook
     * @param styleNum
     * @return
     */
    public HSSFCellStyle getStyle(HSSFWorkbook hssfWorkbook, Integer styleNum){
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setBorderBottom(BorderStyle.THIN);//下边框
 
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontName("微软雅黑");//设置字体为微软雅黑
 
        HSSFPalette palette = hssfWorkbook.getCustomPalette();//拿到palette颜色板,可以根据需要设置颜色
        switch (styleNum){
            case(0):{
                style.setAlignment(HorizontalAlignment.CENTER_SELECTION);//跨列居中
                font.setBold(true);//粗体
                font.setFontHeightInPoints((short) 14);//字体大小
                style.setFont(font);
                palette.setColorAtIndex(HSSFColor.BLUE.index,(byte)184,(byte)204,(byte)228);//替换颜色板中的颜色
                style.setFillForegroundColor(HSSFColor.BLUE.index);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            break;
            case(1):{
                font.setBold(true);//粗体
                font.setFontHeightInPoints((short) 11);//字体大小
                style.setFont(font);
            }
            break;
            case(2):{
                font.setFontHeightInPoints((short)10);
                style.setFont(font);
            }
            break;
            case(3):{
                style.setFont(font);
 
                palette.setColorAtIndex(HSSFColor.GREEN.index,(byte)0,(byte)32,(byte)96);//替换颜色板中的颜色
                style.setFillForegroundColor(HSSFColor.GREEN.index);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            break;
        }
 
        return style;
    }
}
