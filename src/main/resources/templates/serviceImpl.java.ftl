package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoface.share.common.PageData;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public PageData<${entity}> listPage(${entity} ${entity?lower_case}){
        Page<${entity}> page = ${entity?lower_case}.getPage();
        baseMapper.listPage(page, ${entity?lower_case});
        return PageData.<${entity}>builder().total(page.getTotal()).data(page.getRecords()).build();
    }

    @Override
    public List<${entity}> listAll(${entity} ${entity?lower_case}){
        return baseMapper.listAll(${entity?lower_case});
    }
}
</#if>
