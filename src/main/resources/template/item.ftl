package ${basePackageName}.${item_prefix};

import java.io.Serializable;
/**
 * ${className} entity. @author Andy Code tools
 */
public class ${itemName}${item_suffix} implements Serializable{
	// Fields
	<#list columns as column>
	//${column.label}
	private ${column.shortFieldType} ${column.fieldName};
	</#list>
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