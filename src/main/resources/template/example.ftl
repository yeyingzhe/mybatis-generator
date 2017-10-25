package ${basePackageName}.${example_prefix};

import java.util.ArrayList;
import java.util.List;
import cn.gnw.common.core.entity.BaseExample;
import ${basePackageName}.${model_prefix}.${className}${model_suffix};
public class ${className}${example_suffix} extends BaseExample<${className}>{
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ${className}Example() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

       
		<#list columns as column>
		<#if column.isPk>
        public Criteria andIdIsNull() {
              addCriterion("${column.columnName}  is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
              addCriterion("${column.columnName}  is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(${column.shortFieldType} value) {
        	<#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') =", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} =", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') <>", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} <>", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') >", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} >", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') >=", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} >=", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria andIdLessThan(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') <", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} <", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') <=", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} <=", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }
		<#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
		<#else>
        public Criteria andIdLike(${column.shortFieldType} value) {
               addCriterion("${column.columnName} like", value, "${column.XfieldName}");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(${column.shortFieldType} value) {
               addCriterion("${column.columnName} not like", value, "${column.XfieldName}");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<${column.shortFieldType}> values) {
               addCriterion("${column.columnName} in", values, "${column.XfieldName}");
            return (Criteria) this;
        }
		
        public Criteria andIdNotIn(List<${column.shortFieldType}> values) {
               addCriterion("${column.columnName} not in", values, "${column.XfieldName}");
            return (Criteria) this;
        }
        </#if>

        public Criteria andIdBetween(${column.shortFieldType} value1, ${column.shortFieldType} value2) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') between", value1,value2, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			      addCriterion("${column.columnName} between", value1, value2, "${column.XfieldName}");
			</#if>
			return (Criteria) this;
        }

        public Criteria andIdNotBetween(${column.shortFieldType} value1, ${column.shortFieldType} value2) {
             <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') not between", value1,value2, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} not between", value1,value2, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }
		
		<#else>
        public Criteria and${column.XfieldName}IsNull() {
              addCriterion("${column.columnName}  is null");
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}IsNotNull() {
              addCriterion("${column.columnName}  is not null");
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}EqualTo(${column.shortFieldType} value) {
        	<#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') =", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} =", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}NotEqualTo(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') <>", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} <>", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}GreaterThan(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') >", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} >", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}GreaterThanOrEqualTo(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') >=", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} >=", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}LessThan(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') <", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} <", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}LessThanOrEqualTo(${column.shortFieldType} value) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') <=", value, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} <=", value, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }
		<#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
		<#else>
        public Criteria and${column.XfieldName}Like(${column.shortFieldType} value) {
               addCriterion("${column.columnName} like", value, "${column.XfieldName}");
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}NotLike(${column.shortFieldType} value) {
               addCriterion("${column.columnName} not like", value, "${column.XfieldName}");
            return (Criteria) this;
        }

        public Criteria and${column.XfieldName}In(List<${column.shortFieldType}> values) {
               addCriterion("${column.columnName} in", values, "${column.XfieldName}");
            return (Criteria) this;
        }
		
        public Criteria and${column.XfieldName}NotIn(List<${column.shortFieldType}> values) {
               addCriterion("${column.columnName} not in", values, "${column.XfieldName}");
            return (Criteria) this;
        }
        </#if>

        public Criteria and${column.XfieldName}Between(${column.shortFieldType} value1, ${column.shortFieldType} value2) {
            <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') between", value1,value2, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			      addCriterion("${column.columnName} between", value1, value2, "${column.XfieldName}");
			</#if>
			return (Criteria) this;
        }

        public Criteria and${column.XfieldName}NotBetween(${column.shortFieldType} value1, ${column.shortFieldType} value2) {
             <#if column.jdbcType =="TIMESTAMP" || column.jdbcType =="DATE">
               addCriterion("DATE_FORMAT(${column.columnName},'%Y-%m-%d %H:%i:%S') not between", value1,value2, "DATE_FORMAT(${column.XfieldName},'%Y-%m-%d %H:%i:%S')");
            <#else>
			   addCriterion("${column.columnName} not between", value1,value2, "${column.XfieldName}");
			</#if>
            return (Criteria) this;
        }
		</#if>
       </#list>
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}