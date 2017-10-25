package ${basePackageName}.${model_prefix};

import java.io.Serializable;
/**
 * <p>${className}</p> 
 * @version ${currentDate}
 */
public class ${className}${model_suffix} implements Serializable {
	private static final long serialVersionUID = 1L;
	
	<#list columns as column>
	private ${column.shortFieldType} ${column.fieldName};
	</#list>
	
	public ${className}${model_suffix}() {
		super();
	}
	
	<#list columns as column>
	/**
	 * 获取${column.label}
	 */
	public ${column.shortFieldType} ${column.getterMethodName}(){
		return this.${column.fieldName};
	}
	
	/**
	 * 设置${column.label}
	 */
	public void ${column.setterMethodName}(${column.shortFieldType} ${column.fieldName}) {
		this.${column.fieldName} = ${column.fieldName};
	}
	
	</#list>
}
