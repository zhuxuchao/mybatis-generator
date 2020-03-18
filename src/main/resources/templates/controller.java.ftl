package ${package.Controller};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
<#--import org.springframework.web.bind.annotation.RequestMapping;-->
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import ${package.Service }.${table.serviceName};
import ${package.Entity }.${table.entityName};
import com.maoface.share.common.PageData;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 * @author ${author}
 * @since ${date}
 */
@Api(tags="${table.comment!}管理")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
<#--@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")-->
<#if kotlin>
public class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

    @Autowired
    private ${table.serviceName} ${table.entityName?lower_case}Service;

    @PostMapping("/private/${table.entityName?lower_case}/add")
    @ApiOperation(value = "新增${table.comment!}", notes = "")
    public ${table.entityName} add(@RequestBody ${table.entityName} ${table.entityName?lower_case}){
        ${table.entityName?lower_case}Service.save(${table.entityName?lower_case});
        return ${table.entityName?lower_case};
    }

    @PostMapping("/private/${table.entityName?lower_case}/list")
    @ApiOperation(value = "${table.comment!}列表", notes = "不分页")
    public List<${table.entityName}> list(@RequestBody ${table.entityName} ${table.entityName?lower_case}){
        return ${table.entityName?lower_case}Service.listAll(${table.entityName?lower_case});
    }

    @PostMapping("/private/${table.entityName?lower_case}/listPage")
    @ApiOperation(value = "${table.comment!}列表", notes = "分页")
    public PageData<${table.entityName}> listPage(@RequestBody ${table.entityName} ${table.entityName?lower_case}){
        return ${table.entityName?lower_case}Service.listPage(${table.entityName?lower_case});
    }

    @GetMapping("/private/${table.entityName?lower_case}<#noparse>/{id:\\d+}"</#noparse>)
    @ApiOperation(value = "查询${table.comment!}", notes = "")
    public ${table.entityName} get(@PathVariable Long id){
        return ${table.entityName?lower_case}Service.getById(id);
    }

    @PostMapping("/private/${table.entityName?lower_case}/edit")
    @ApiOperation(value = "编辑${table.comment!}", notes = "")
    public ${table.entityName} edit(@RequestBody ${table.entityName} ${table.entityName?lower_case}){
        ${table.entityName?lower_case}Service.updateById(${table.entityName?lower_case});
        return ${table.entityName?lower_case};
    }

    @PostMapping("/private/${table.entityName?lower_case}/remove")
    @ApiOperation(value = "删除${table.comment!}", notes = "")
    public void remove(@RequestParam String ids){
        List<Long> idList = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s)).collect(Collectors.toList());
        ${table.entityName?lower_case}Service.removeByIds(idList);
    }
}
</#if>
