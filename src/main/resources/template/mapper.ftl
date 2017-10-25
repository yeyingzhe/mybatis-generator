<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${basePackageName}.${dao_prefix}.${className}${dao_suffix}">  

	<resultMap id="BaseResultMap" type="${basePackageName}.${model_prefix}.${className}${model_suffix}">
	<#list columns as column>
	<#if column.isPk>
		<id column="${column.columnName}" jdbcType="${column.jdbcType}" property="${column.fieldName}"/>
	</#if>
  	</#list>
	<#list columns as column>
	<#if column.isPk>
	<#else>
		<result column="${column.columnName}" jdbcType="${column.jdbcType}" property="${column.fieldName}" />
	</#if>
  	</#list>
	</resultMap>
	
	<sql id="Base_Where_Clause">
		<where>
		<#list columns as column>
			<#if column.isPk>
			<if test="${column.fieldName} != null">
				and ${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
			</if>
			<#else>
			<if test="${column.fieldName} != null">
				<#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
				and DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') = DATE_FORMAT(${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"},'%Y-%m-%d %H:%i:%S')
				<#else>
				and ${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
				</#if>
			</if>
			</#if>
		</#list>
		</where>
	</sql>

	<sql id="Base_Column_List">
  	<#list columns as column>
  	<#if column_index!=columns?size-1>
  		${column.columnName},
 	<#else>
  		${column.columnName}
  	</#if>
  	</#list>
 	</sql>

	<select id="selectByPrimaryKey" parameterType="java.lang.String"
		resultMap="BaseResultMap">
		select
		<include refid="Base_Column_List" />
		from ${tableName}
		<#list columns as column>
		<#if column.isPk>
		 where ${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
		</#if>
  		</#list>
		
	</select>

	<select id="select" parameterType="map" resultMap="BaseResultMap">
		select
		<include refid="Base_Column_List" />
		from ${tableName}
		<include refid="Base_Where_Clause" />
		<if test="offset != null and limit != null">
			limit ${"#{"}offset${"}"}, ${"#{"}limit${"}"}
		</if>
	</select>

	<select id="selectCount" parameterType="map"
		resultType="java.lang.Long">
		select count(*)
		from ${tableName}
		<include refid="Base_Where_Clause" />
	</select>

	<insert id="insert" parameterType="${basePackageName}.${model_prefix}.${className}${model_suffix}">
		insert into ${tableName} (
		<#list columns as column>
		<#if column.isPk>
		${column.columnName},
		<#else>
		<#if column_index!=columns?size-1>
  		${column.columnName},
 		<#else>
  		${column.columnName}
  		</#if>
		</#if>
  		</#list>
		)
		values (
		<#list columns as column>
		<#if column.isPk>
		${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"},
		<#else>
		<#if column_index!=columns?size-1>
  		${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"},
 		<#else>
  		${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
  		</#if>
		</#if>
  		</#list>
		
		)
	</insert>
	
	<insert id="insertSelective" parameterType="${basePackageName}.${model_prefix}.${className}${model_suffix}">
		insert into ${tableName}
		<trim prefix="(" suffix=")" suffixOverrides=",">
		<#list columns as column>
		<#if column.isPk>
			   ${column.columnName},
		<#else>
		<#if column_index!=columns?size-1>
  		<if test="${column.fieldName} != null">
				${column.columnName},
		</if>
 		<#else>
  		<if test="${column.fieldName} != null">
				${column.columnName}
		</if>
  		</#if>
		</#if>
  		</#list>
			
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides=",">
		<#list columns as column>
		<#if column.isPk>
				${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"},
		<#else>
		<#if column_index!=columns?size-1>
  		<if test="${column.fieldName} != null">
				${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"},
		</if>
 		<#else>
  		<if test="${column.fieldName} != null">
				${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
		</if>
  		</#if>
		</#if>
  		</#list>
		</trim>
	</insert>

	<update id="updateByPrimaryKeySelective" parameterType="${basePackageName}.${model_prefix}.${className}${model_suffix}">
		update ${tableName}
		<set>
		<#list columns as column>
		<#if column.isPk>
		<#else>
		<#if column_index!=columns?size-1>
  		<if test="${column.fieldName} != null">
				${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"},
		</if>
 		<#else>
  		<if test="${column.fieldName} != null">
				${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
		</if>
  		</#if>
		</#if>
  		</#list>
		</set>
		<#list columns as column>
		<#if column.isPk>
		 where ${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
		</#if>
		</#list>
	</update>
	
	<update id="updateByPrimaryKey" parameterType="${basePackageName}.${model_prefix}.${className}${model_suffix}">
		update ${tableName}
		set 
		<#list columns as column>
		<#if column.isPk>
		<#else>
		<#if column_index!=columns?size-1>
				${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"},
 		<#else>
				${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
  		</#if>
		</#if>
  		</#list>
		<#list columns as column>
		<#if column.isPk>
		 where ${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
		</#if>
		</#list>
	</update>

	<delete id="delete" parameterType="${basePackageName}.${model_prefix}.${className}${model_suffix}">
		delete from ${tableName}
		<include refid="Base_Where_Clause" />
	</delete>

	<delete id="deleteById" parameterType="java.lang.String">
		delete from ${tableName}
		<#list columns as column>
		<#if column.isPk>
		 where ${column.columnName} = ${"#{"}${column.fieldName},jdbcType=${column.jdbcType}${"}"}
		</#if>
		</#list>
	</delete>

</mapper>