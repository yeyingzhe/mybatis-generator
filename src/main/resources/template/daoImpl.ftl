package ${basePackageName}.${daoimpl_prefix};

import ${basePackageName}.${model_prefix}.${className}${model_suffix};
import cn.gnw.common.core.dao.BaseDaoAdapter;
import ${basePackageName}.${dao_prefix}.${className}${dao_suffix};
import org.springframework.stereotype.Repository;

@Repository("${proxyName}")
public class ${className}${daoimpl_suffix} extends BaseDaoAdapter<${className}> implements ${className}${dao_suffix}{

}