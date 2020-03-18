package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoface.share.common.PageData;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
    /**
     * 分页查询 ${table.comment!}
     */
    PageData<${entity}> listPage(${entity} ${entity?lower_case});

    /**
     * 查询 ${table.comment!}
     */
    List<${entity}> listAll(${entity} ${entity?lower_case});
}
</#if>
