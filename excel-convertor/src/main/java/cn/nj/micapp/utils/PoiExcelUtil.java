package cn.nj.micapp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.nj.micapp.annotation.ExcelProperty;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/11/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PoiExcelUtil
{
    private PoiExcelUtil()
    {
        
    }
    
    public static <T> void export(List<T> list, Class<T> tClass)
    {
        //sheet名
        String sheetName = tClass.getAnnotation(ExcelProperty.class).sheet();
        //excel标题
        Field[] fields = tClass.getDeclaredFields();
        List<String> titles = new ArrayList<>(fields.length);
        for (Field field : fields)
        {
            ExcelProperty property = field.getAnnotation(ExcelProperty.class);
            if (property != null && StringUtils.isNotBlank(property.label()))
            {
                titles.add(property.label());
            }
        }
        String[][] content = new String[list.size()][];
        for (int i = 0; i < list.size(); i++)
        {
            //动态创建第二维
            content[i] = new String[titles.size()];
            JSONObject obj = JSON.parseObject(JSON.toJSONString(list.get(i)));
            for (int j = 0; j < fields.length; j++)
            {
                JSONField jsonField = fields[j].getAnnotation(JSONField.class);
                if (jsonField != null && StringUtils.isNotBlank(jsonField.name()))
                {
                    content[i][j] = obj.get(jsonField.name()).toString();
                }
                else if (j == 0)
                {
                    content[i][0] = "" + (i + 1);
                }
            }
        }
        String[] array = new String[titles.size()];
        //创建HSSFWorkbook
        HSSFWorkbook wb = PoiExcelUtil.getHSSFWorkbook(sheetName, titles.toArray(array), content);
        
        //excel文件名
        String fileName = "货单记录表" + System.currentTimeMillis() + ".xls";
        //创建excel文件
        File file = new File(fileName);
        try
        {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream = FileUtils.openOutputStream(file);
            wb.write(stream);
            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values)
    {
        
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        //声明列对象
        HSSFCell cell = null;
        
        //创建标题
        for (int i = 0; i < title.length; i++)
        {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        
        //创建内容
        for (int i = 0; i < values.length; i++)
        {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++)
            {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }
}
