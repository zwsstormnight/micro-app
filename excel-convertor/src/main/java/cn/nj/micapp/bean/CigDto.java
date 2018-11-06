package cn.nj.micapp.bean;

import cn.nj.micapp.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zhengweishun
 * @version [版本号, 2018/11/5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@ExcelProperty(sheet = "烟")
public class CigDto
{

    @ExcelProperty(label = "序号")
    @JSONField(serialize = false, deserialize = false)
    private Long seriesNum;

    @ExcelProperty(label = "商品名称")
    @JSONField(name = "cgt_name")
    private String name;

    @ExcelProperty(label = "确认量")
    @JSONField(name = "ord_qty")
    private String orderQty;
}
