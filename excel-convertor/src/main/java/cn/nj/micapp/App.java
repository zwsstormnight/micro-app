package cn.nj.micapp;

import cn.nj.micapp.bean.CigDto;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static cn.nj.micapp.utils.PoiExcelUtil.export;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/11/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class App
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static String read = null;
    
    public static void main(String[] args)
    {
        System.out.println("application has started...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        process(br);
    }
    
    private static void process(BufferedReader br)
    {
        if (StringUtils.isBlank(read))
        {
            readInputs(br);
            process(br);
            return;
        }
        List<CigDto> list = null;
        try
        {
            list = JSONArray.parseArray(read, CigDto.class);
        }
        catch (Exception e)
        {
            logger.error("{}|{}|{}", "输入数据格式异常", e);
            readInputs(br);
            process(br);
        }
        if (CollectionUtils.isEmpty(list))
        {
            readInputs(br);
            process(br);
        }
        export(list, CigDto.class);
        System.out.println("请选择是否继续工作：Y.是；N.否");
        readInputs(br);
        menu(br);
    }
    
    private static void readInputs(BufferedReader br)
    {
        try
        {
            System.out.print("请输入：");
            read = br.readLine();
            System.out.println("已输入：" + read);
        }
        catch (IOException e)
        {
            logger.error("{}|{}|{}", "输入数据读取异常", e);
        }
    }
    
    private static void menu(BufferedReader br)
    {
        if (StringUtils.isBlank(read))
        {
            readInputs(br);
        }
        if ("Y".equalsIgnoreCase(read))
        {
            readInputs(br);
            process(br);
        }
        else if ("N".equalsIgnoreCase(read))
        {
            System.out.println("system is willing shut down...");
            try
            {
                Thread.sleep(2000L);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
