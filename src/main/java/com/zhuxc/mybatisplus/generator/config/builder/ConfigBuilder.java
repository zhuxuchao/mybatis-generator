package com.zhuxc.mybatisplus.generator.config.builder;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.zhuxc.mybatisplus.generator.InjectionConfig;
import com.zhuxc.mybatisplus.generator.config.DefaultDataSourceConfig;
import com.zhuxc.mybatisplus.generator.config.DefaultGlobalConfig;
import com.zhuxc.mybatisplus.generator.config.DefaultPackageConfig;
import com.zhuxc.mybatisplus.generator.config.DefaultStrategyConfig;
import com.zhuxc.mybatisplus.generator.consts.Constants;
import com.zhuxc.mybatisplus.generator.consts.Module;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zhuxuchao
 * @date 2019-12-26
 */
public class ConfigBuilder {
    private TemplateConfig template;
    private DefaultDataSourceConfig dataSourceConfig;
    private Connection connection;
    private IDbQuery dbQuery;
    private String superEntityClass;
    private String superMapperClass;
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    private List<TableInfo> tableInfoList;
    private Map<String, String> packageInfo;
    private Map<String, String> pathInfo;
    private DefaultStrategyConfig strategyConfig;
    private DefaultGlobalConfig globalConfig;
    private InjectionConfig injectionConfig;
    private boolean commentSupported;
    private static final Pattern REGX = Pattern.compile("[~!/@#$%^&*()-_=+\\\\|[{}];:\\'\\\",<.>/?]+");

    public ConfigBuilder(DefaultPackageConfig packageConfig, DefaultDataSourceConfig dataSourceConfig, DefaultStrategyConfig strategyConfig, TemplateConfig template, DefaultGlobalConfig globalConfig) {
        if (null == globalConfig) {
            this.globalConfig = new DefaultGlobalConfig();
        } else {
            this.globalConfig = globalConfig;
        }

        if (null == template) {
            this.template = new TemplateConfig();
        } else {
            this.template = template;
        }

        if (null == packageConfig) {
            this.handlerPackage(this.template, this.globalConfig, new DefaultPackageConfig());
        } else {
            this.handlerPackage(this.template, this.globalConfig, packageConfig);
        }

        this.dataSourceConfig = dataSourceConfig;
        this.handlerDataSource(dataSourceConfig);
        if (null == strategyConfig) {
            this.strategyConfig = new DefaultStrategyConfig();
        } else {
            this.strategyConfig = strategyConfig;
        }

        this.commentSupported = !dataSourceConfig.getDbType().equals(DbType.SQLITE);
        this.handlerStrategy(this.strategyConfig);
    }

    public Map<String, String> getPackageInfo() {
        return this.packageInfo;
    }

    public Map<String, String> getPathInfo() {
        return this.pathInfo;
    }

    public String getSuperEntityClass() {
        return this.superEntityClass;
    }

    public String getSuperMapperClass() {
        return this.superMapperClass;
    }

    public String getSuperServiceClass() {
        return this.superServiceClass;
    }

    public String getSuperServiceImplClass() {
        return this.superServiceImplClass;
    }

    public String getSuperControllerClass() {
        return this.superControllerClass;
    }

    public List<TableInfo> getTableInfoList() {
        return this.tableInfoList;
    }

    public ConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
        return this;
    }

    public TemplateConfig getTemplate() {
        return this.template == null ? new TemplateConfig() : this.template;
    }

    private void handlerPackage(TemplateConfig template, DefaultGlobalConfig globalConfig, DefaultPackageConfig packageConfig) {
        this.packageInfo = new HashMap(8);
        this.packageInfo.put("ModuleName", packageConfig.getModuleName());
        this.packageInfo.put("Entity", this.joinPackage(packageConfig.getParent(), packageConfig.getEntity()));
        this.packageInfo.put("Mapper", this.joinPackage(packageConfig.getParent(), packageConfig.getMapper()));
        this.packageInfo.put("Xml", this.joinPackage(packageConfig.getParent(), packageConfig.getXml()));
        this.packageInfo.put("Service", this.joinPackage(packageConfig.getParent(), packageConfig.getService()));
        this.packageInfo.put("ServiceImpl", this.joinPackage(packageConfig.getParent(), packageConfig.getServiceImpl()));
        this.packageInfo.put("Controller", this.joinPackage(packageConfig.getParent(), packageConfig.getController()));
        Map<String, String> configPathInfo = packageConfig.getPathInfo();

        if (null != configPathInfo) {
            this.pathInfo = configPathInfo;
        } else {
            this.pathInfo = new HashMap(6);
            if (globalConfig.isMultiModule()) {
                handlerMultiModulePackage(template, globalConfig);
            } else {
                String outputDir = globalConfig.getOutputDir() + Constants.JAVA_SRC_ROOT;
                handlerSingleModulePackage(template, outputDir);
            }
        }
    }

    private void handlerSingleModulePackage(TemplateConfig template, String outputDir) {
        this.setPathInfo(this.pathInfo, template.getEntity(this.getGlobalConfig().isKotlin()), outputDir, "entity_path", "Entity");
        this.setPathInfo(this.pathInfo, template.getMapper(), outputDir, "mapper_path", "Mapper");
        this.setPathInfo(this.pathInfo, template.getXml(), outputDir, "xml_path", "Xml");
        this.setPathInfo(this.pathInfo, template.getService(), outputDir, "service_path", "Service");
        this.setPathInfo(this.pathInfo, template.getServiceImpl(), outputDir, "service_impl_path", "ServiceImpl");
        this.setPathInfo(this.pathInfo, template.getController(), outputDir, "controller_path", "Controller");
    }

    private void handlerMultiModulePackage(TemplateConfig template, DefaultGlobalConfig globalConfig) {
        this.setPathInfo(this.pathInfo, template.getEntity(this.getGlobalConfig().isKotlin()), globalConfig.getModuleJavaSrcPath(Module.entity), "entity_path", "Entity");
        this.setPathInfo(this.pathInfo, template.getMapper(), globalConfig.getModuleJavaSrcPath(Module.mapper), "mapper_path", "Mapper");
        this.setPathInfo(this.pathInfo, template.getXml(), globalConfig.getModuleJavaSrcPath(Module.mapper), "xml_path", "Xml");
        this.setPathInfo(this.pathInfo, template.getService(), globalConfig.getModuleJavaSrcPath(Module.service), "service_path", "Service");
        this.setPathInfo(this.pathInfo, template.getServiceImpl(), globalConfig.getModuleJavaSrcPath(Module.service_impl), "service_impl_path", "ServiceImpl");
        this.setPathInfo(this.pathInfo, template.getController(), globalConfig.getModuleJavaSrcPath(Module.controller), "controller_path", "Controller");
    }

    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
        if (StringUtils.isNotBlank(template)) {
            pathInfo.put(path, this.joinPath(outputDir, this.packageInfo.get(module)));
        }
    }

    private void handlerDataSource(DataSourceConfig config) {
        this.connection = config.getConn();
        this.dbQuery = config.getDbQuery();
    }

    private void handlerStrategy(StrategyConfig config) {
        this.processTypes(config);
        this.tableInfoList = this.getTablesInfo(config);
    }

    private void processTypes(StrategyConfig config) {
        if (StringUtils.isBlank(config.getSuperServiceClass())) {
            this.superServiceClass = "com.baomidou.mybatisplus.extension.service.IService";
        } else {
            this.superServiceClass = config.getSuperServiceClass();
        }

        if (StringUtils.isBlank(config.getSuperServiceImplClass())) {
            this.superServiceImplClass = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";
        } else {
            this.superServiceImplClass = config.getSuperServiceImplClass();
        }

        if (StringUtils.isBlank(config.getSuperMapperClass())) {
            this.superMapperClass = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
        } else {
            this.superMapperClass = config.getSuperMapperClass();
        }

        this.superEntityClass = config.getSuperEntityClass();
        this.superControllerClass = config.getSuperControllerClass();
    }

    private List<TableInfo> processTable(List<TableInfo> tableList, NamingStrategy strategy, StrategyConfig config) {
        String[] tablePrefix = config.getTablePrefix();

        TableInfo tableInfo;
        for (Iterator var5 = tableList.iterator(); var5.hasNext(); this.checkImportPackages(tableInfo)) {
            tableInfo = (TableInfo) var5.next();
            INameConvert nameConvert = this.strategyConfig.getNameConvert();
            String entityName;
            if (null != nameConvert) {
                entityName = nameConvert.entityNameConvert(tableInfo);
            } else {
                entityName = NamingStrategy.capitalFirst(this.processName(tableInfo.getName(), strategy, tablePrefix));
            }

            if (StringUtils.isNotBlank(this.globalConfig.getEntityName())) {
                tableInfo.setConvert(true);
                tableInfo.setEntityName(String.format(this.globalConfig.getEntityName(), entityName));
            } else {
                tableInfo.setEntityName(this.strategyConfig, entityName);
            }

            if (StringUtils.isNotBlank(this.globalConfig.getMapperName())) {
                tableInfo.setMapperName(String.format(this.globalConfig.getMapperName(), entityName));
            } else {
                tableInfo.setMapperName(entityName + "Mapper");
            }

            if (StringUtils.isNotBlank(this.globalConfig.getXmlName())) {
                tableInfo.setXmlName(String.format(this.globalConfig.getXmlName(), entityName));
            } else {
                tableInfo.setXmlName(entityName + "Mapper");
            }

            if (StringUtils.isNotBlank(this.globalConfig.getServiceName())) {
                tableInfo.setServiceName(String.format(this.globalConfig.getServiceName(), entityName));
            } else {
                tableInfo.setServiceName("I" + entityName + "Service");
            }

            if (StringUtils.isNotBlank(this.globalConfig.getServiceImplName())) {
                tableInfo.setServiceImplName(String.format(this.globalConfig.getServiceImplName(), entityName));
            } else {
                tableInfo.setServiceImplName(entityName + "ServiceImpl");
            }

            if (StringUtils.isNotBlank(this.globalConfig.getControllerName())) {
                tableInfo.setControllerName(String.format(this.globalConfig.getControllerName(), entityName));
            } else {
                tableInfo.setControllerName(entityName + "Controller");
            }
        }

        return tableList;
    }

    private void checkImportPackages(TableInfo tableInfo) {
        if (StringUtils.isNotBlank(this.strategyConfig.getSuperEntityClass())) {
            tableInfo.getImportPackages().add(this.strategyConfig.getSuperEntityClass());
        } else if (this.globalConfig.isActiveRecord()) {
            tableInfo.getImportPackages().add(Model.class.getCanonicalName());
        }

        if (null != this.globalConfig.getIdType()) {
            tableInfo.getImportPackages().add(IdType.class.getCanonicalName());
            tableInfo.getImportPackages().add(TableId.class.getCanonicalName());
        }

        if (StringUtils.isNotBlank(this.strategyConfig.getVersionFieldName()) && CollectionUtils.isNotEmpty(tableInfo.getFields())) {
            tableInfo.getFields().forEach((f) -> {
                if (this.strategyConfig.getVersionFieldName().equals(f.getName())) {
                    tableInfo.getImportPackages().add(Version.class.getCanonicalName());
                }

            });
        }

    }

    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = null != config.getInclude() && config.getInclude().length > 0;
        boolean isExclude = null != config.getExclude() && config.getExclude().length > 0;
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        } else if (config.getNotLikeTable() != null && config.getLikeTable() != null) {
            throw new RuntimeException("<strategy> 标签中 <likeTable> 与 <notLikeTable> 只能配置一项！");
        } else {
            List<TableInfo> tableList = new ArrayList();
            List<TableInfo> includeTableList = new ArrayList();
            List<TableInfo> excludeTableList = new ArrayList();
            HashSet notExistTables = new HashSet();

            try {
                String tablesSql = this.dbQuery.tablesSql();
                String schema;
                if (DbType.POSTGRE_SQL == this.dbQuery.dbType()) {
                    schema = this.dataSourceConfig.getSchemaName();
                    if (schema == null) {
                        schema = "public";
                        this.dataSourceConfig.setSchemaName(schema);
                    }

                    tablesSql = String.format(tablesSql, schema);
                } else if (DbType.KINGBASE_ES == this.dbQuery.dbType()) {
                    schema = this.dataSourceConfig.getSchemaName();
                    if (schema == null) {
                        schema = "PUBLIC";
                        this.dataSourceConfig.setSchemaName(schema);
                    }

                    tablesSql = String.format(tablesSql, schema);
                } else if (DbType.DB2 == this.dbQuery.dbType()) {
                    schema = this.dataSourceConfig.getSchemaName();
                    if (schema == null) {
                        schema = "current schema";
                        this.dataSourceConfig.setSchemaName(schema);
                    }

                    tablesSql = String.format(tablesSql, schema);
                } else if (DbType.ORACLE == this.dbQuery.dbType()) {
                    schema = this.dataSourceConfig.getSchemaName();
                    if (schema == null) {
                        schema = this.dataSourceConfig.getUsername().toUpperCase();
                        this.dataSourceConfig.setSchemaName(schema);
                    }

                    tablesSql = String.format(tablesSql, schema);
                }

                StringBuilder sql = new StringBuilder(tablesSql);
                if (config.getLikeTable() != null) {
                    sql.append(" AND ").append(this.dbQuery.tableName()).append(" LIKE '").append(config.getLikeTable().getValue()).append("'");
                } else if (config.getNotLikeTable() != null) {
                    sql.append(" AND ").append(this.dbQuery.tableName()).append(" NOT LIKE '").append(config.getNotLikeTable().getValue()).append("'");
                }

                if (isInclude) {
                    sql.append(" AND ").append(this.dbQuery.tableName()).append(" IN (").append((String) Arrays.stream(config.getInclude()).map((tb) -> {
                        return "'" + tb + "'";
                    }).collect(Collectors.joining(","))).append(")");
                } else if (isExclude) {
                    sql.append(" AND ").append(this.dbQuery.tableName()).append(" NOT IN (").append((String) Arrays.stream(config.getInclude()).map((tb) -> {
                        return "'" + tb + "'";
                    }).collect(Collectors.joining(","))).append(")");
                }

                PreparedStatement preparedStatement = this.connection.prepareStatement(sql.toString());
                Throwable var12 = null;

                try {
                    ResultSet results = preparedStatement.executeQuery();
                    Throwable var14 = null;

                    try {
                        while (results.next()) {
                            String tableName = results.getString(this.dbQuery.tableName());
                            if (StringUtils.isNotBlank(tableName)) {
                                TableInfo tableInfo = new TableInfo();
                                tableInfo.setName(tableName);
                                if (this.commentSupported) {
                                    String tableComment = results.getString(this.dbQuery.tableComment());
                                    if (config.isSkipView() && "VIEW".equals(tableComment)) {
                                        continue;
                                    }

                                    tableInfo.setComment(tableComment);
                                }

                                int var17;
                                int var18;
                                String excludeTable;
                                String[] var51;
                                if (isInclude) {
                                    var51 = config.getInclude();
                                    var17 = var51.length;

                                    for (var18 = 0; var18 < var17; ++var18) {
                                        excludeTable = var51[var18];
                                        if (this.tableNameMatches(excludeTable, tableName)) {
                                            includeTableList.add(tableInfo);
                                        } else if (!REGX.matcher(excludeTable).find()) {
                                            notExistTables.add(excludeTable);
                                        }
                                    }
                                } else if (isExclude) {
                                    var51 = config.getExclude();
                                    var17 = var51.length;

                                    for (var18 = 0; var18 < var17; ++var18) {
                                        excludeTable = var51[var18];
                                        if (this.tableNameMatches(excludeTable, tableName)) {
                                            excludeTableList.add(tableInfo);
                                        } else if (!REGX.matcher(excludeTable).find()) {
                                            notExistTables.add(excludeTable);
                                        }
                                    }
                                }

                                tableList.add(tableInfo);
                            } else {
                                System.err.println("当前数据库为空！！！");
                            }
                        }
                    } catch (Throwable var43) {
                        var14 = var43;
                        throw var43;
                    } finally {
                        if (results != null) {
                            if (var14 != null) {
                                try {
                                    results.close();
                                } catch (Throwable var42) {
                                    var14.addSuppressed(var42);
                                }
                            } else {
                                results.close();
                            }
                        }

                    }
                } catch (Throwable var45) {
                    var12 = var45;
                    throw var45;
                } finally {
                    if (preparedStatement != null) {
                        if (var12 != null) {
                            try {
                                preparedStatement.close();
                            } catch (Throwable var41) {
                                var12.addSuppressed(var41);
                            }
                        } else {
                            preparedStatement.close();
                        }
                    }

                }

                Iterator var48 = tableList.iterator();

                while (var48.hasNext()) {
                    TableInfo tabInfo = (TableInfo) var48.next();
                    notExistTables.remove(tabInfo.getName());
                }

                if (notExistTables.size() > 0) {
                    System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
                }

                if (isExclude) {
                    tableList.removeAll(excludeTableList);
                    includeTableList = tableList;
                }

                if (!isInclude && !isExclude) {
                    includeTableList = tableList;
                }

                includeTableList.forEach((ti) -> {
                    this.convertTableFields(ti, config);
                });
            } catch (SQLException var47) {
                var47.printStackTrace();
            }

            return this.processTable(includeTableList, config.getNaming(), config);
        }
    }

    private boolean tableNameMatches(String setTableName, String dbTableName) {
        return setTableName.equalsIgnoreCase(dbTableName) || StringUtils.matches(setTableName, dbTableName);
    }

    private TableInfo convertTableFields(TableInfo tableInfo, StrategyConfig config) {
        boolean haveId = false;
        List<TableField> fieldList = new ArrayList();
        List<TableField> commonFieldList = new ArrayList();
        DbType dbType = this.dbQuery.dbType();
        String tableName = tableInfo.getName();

        try {
            String tableFieldsSql = this.dbQuery.tableFieldsSql();
            Set<String> h2PkColumns = new HashSet();
            PreparedStatement pkQueryStmt;
            Throwable var11;
            ResultSet results;
            Throwable var13;
            if (DbType.POSTGRE_SQL == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, this.dataSourceConfig.getSchemaName(), tableName);
            } else if (DbType.KINGBASE_ES == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, this.dataSourceConfig.getSchemaName(), tableName);
            } else if (DbType.DB2 == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, this.dataSourceConfig.getSchemaName(), tableName);
            } else if (DbType.ORACLE == dbType) {
                tableName = tableName.toUpperCase();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", this.dataSourceConfig.getSchemaName()), tableName);
            } else if (DbType.DM == dbType) {
                tableName = tableName.toUpperCase();
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            } else if (DbType.H2 == dbType) {
                tableName = tableName.toUpperCase();
                pkQueryStmt = this.connection.prepareStatement(String.format("select * from INFORMATION_SCHEMA.INDEXES WHERE TABLE_NAME = '%s'", tableName));
                var11 = null;

                try {
                    results = pkQueryStmt.executeQuery();
                    var13 = null;

                    try {
                        while (results.next()) {
                            String primaryKey = results.getString(this.dbQuery.fieldKey());
                            if (Boolean.valueOf(primaryKey)) {
                                h2PkColumns.add(results.getString(this.dbQuery.fieldName()));
                            }
                        }
                    } catch (Throwable var97) {
                        var13 = var97;
                        throw var97;
                    } finally {
                        if (results != null) {
                            if (var13 != null) {
                                try {
                                    results.close();
                                } catch (Throwable var92) {
                                    var13.addSuppressed(var92);
                                }
                            } else {
                                results.close();
                            }
                        }

                    }
                } catch (Throwable var99) {
                    var11 = var99;
                    throw var99;
                } finally {
                    if (pkQueryStmt != null) {
                        if (var11 != null) {
                            try {
                                pkQueryStmt.close();
                            } catch (Throwable var91) {
                                var11.addSuppressed(var91);
                            }
                        } else {
                            pkQueryStmt.close();
                        }
                    }

                }

                tableFieldsSql = String.format(tableFieldsSql, tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }

            pkQueryStmt = this.connection.prepareStatement(tableFieldsSql);
            var11 = null;

            try {
                results = pkQueryStmt.executeQuery();
                var13 = null;

                try {
                    while (results.next()) {
                        TableField field = new TableField();
                        String columnName = results.getString(this.dbQuery.fieldName());
                        boolean isId;
                        if (DbType.H2 == dbType) {
                            isId = h2PkColumns.contains(columnName);
                        } else {
                            String key = results.getString(this.dbQuery.fieldKey());
                            if (DbType.DB2 != dbType && DbType.SQLITE != dbType) {
                                isId = StringUtils.isNotBlank(key) && "PRI".equals(key.toUpperCase());
                            } else {
                                isId = StringUtils.isNotBlank(key) && "1".equals(key);
                            }
                        }

                        if (isId && !haveId) {
                            field.setKeyFlag(true);
                            if (DbType.H2 == dbType || DbType.SQLITE == dbType || this.dbQuery.isKeyIdentity(results)) {
                                field.setKeyIdentityFlag(true);
                            }

                            haveId = true;
                        } else {
                            field.setKeyFlag(false);
                        }

                        String[] fcs = this.dbQuery.fieldCustom();
                        if (null != fcs) {
                            Map<String, Object> customMap = new HashMap(fcs.length);
                            String[] var19 = fcs;
                            int var20 = fcs.length;

                            for (int var21 = 0; var21 < var20; ++var21) {
                                String fc = var19[var21];
                                customMap.put(fc, results.getObject(fc));
                            }

                            field.setCustomMap(customMap);
                        }

                        field.setName(columnName);
                        field.setType(results.getString(this.dbQuery.fieldType()));
                        INameConvert nameConvert = this.strategyConfig.getNameConvert();
                        if (null != nameConvert) {
                            field.setPropertyName(nameConvert.propertyNameConvert(field));
                        } else {
                            field.setPropertyName(this.strategyConfig, this.processName(field.getName(), config.getNaming()));
                        }

                        field.setColumnType(this.dataSourceConfig.getTypeConvert().processTypeConvert(this.globalConfig, field));
                        if (this.commentSupported) {
                            field.setComment(results.getString(this.dbQuery.fieldComment()));
                        }

                        if (this.strategyConfig.includeSuperEntityColumns(field.getName())) {
                            commonFieldList.add(field);
                        } else {
                            List<TableFill> tableFillList = this.getStrategyConfig().getTableFillList();
                            if (null != tableFillList) {
                                tableFillList.stream().filter((tf) -> {
                                    return tf.getFieldName().equalsIgnoreCase(field.getName());
                                }).findFirst().ifPresent((tf) -> {
                                    field.setFill(tf.getFieldFill().name());
                                });
                            }

                            fieldList.add(field);
                        }
                    }
                } catch (Throwable var93) {
                    var13 = var93;
                    throw var93;
                } finally {
                    if (results != null) {
                        if (var13 != null) {
                            try {
                                results.close();
                            } catch (Throwable var90) {
                                var13.addSuppressed(var90);
                            }
                        } else {
                            results.close();
                        }
                    }

                }
            } catch (Throwable var95) {
                var11 = var95;
                throw var95;
            } finally {
                if (pkQueryStmt != null) {
                    if (var11 != null) {
                        try {
                            pkQueryStmt.close();
                        } catch (Throwable var89) {
                            var11.addSuppressed(var89);
                        }
                    } else {
                        pkQueryStmt.close();
                    }
                }

            }
        } catch (SQLException var101) {
            System.err.println("SQL Exception：" + var101.getMessage());
        }

        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
        return tableInfo;
    }

    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty("java.io.tmpdir");
        }

        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir = parentDir + File.separator;
        }

        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    private String joinPackage(String parent, String subPackage) {
        return StringUtils.isBlank(parent) ? subPackage : parent + "." + subPackage;
    }

    private String processName(String name, NamingStrategy strategy) {
        return this.processName(name, strategy, this.strategyConfig.getFieldPrefix());
    }

    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        boolean removePrefix = false;
        if (prefix != null && prefix.length != 0) {
            removePrefix = true;
        }

        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            propertyName = name;
        }

        return propertyName;
    }

    public DefaultStrategyConfig getStrategyConfig() {
        return this.strategyConfig;
    }

    public ConfigBuilder setStrategyConfig(DefaultStrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }

    public DefaultGlobalConfig getGlobalConfig() {
        return this.globalConfig;
    }

    public ConfigBuilder setGlobalConfig(DefaultGlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    public InjectionConfig getInjectionConfig() {
        return this.injectionConfig;
    }

    public ConfigBuilder setInjectionConfig(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }
}
