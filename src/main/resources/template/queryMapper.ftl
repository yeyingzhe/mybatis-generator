<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${basePackageName}.${dao_prefix}.${className}${dao_suffix}">  
 <resultMap id="${selectKey}ResultMap" type="${basePackageName}.${item_prefix}.${itemName}${item_suffix}">
 <#list columns as column>
	<result column="${column.columnName}" jdbcType="${column.jdbcType}" property="${column.fieldName}" />
  </#list>
 </resultMap>
  <select id="${selectKey}" parameterType="map" resultMap="${selectKey}ResultMap">
    ${sqlquery}
  </select>
</mapper>