package com.lexiangbao.tool.generator.mybatis.entity;

/**
 * 
 * 表字段信息类
 * </p>
 * 用户加载表的每个字段信息
 */
public class Column {
    
    /**        
     * 字段名       
     * @since 1.0    
     */    
    private String columnName;

    /**        
     * 字段备注    
     * @since 1.0    
     */    
    private String remark;

    /**        
     * 字段类型 
     * @since 1.0    
     */ 
    private String dataType;

    /**        
     * 该字段是否是主键  
     * @since 1.0    
     */     
    private boolean isPrimaryKey;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

}
