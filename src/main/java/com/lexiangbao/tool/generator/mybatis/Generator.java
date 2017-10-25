package com.lexiangbao.tool.generator.mybatis;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import com.lexiangbao.tool.generator.mybatis.entity.GenerateTypes;
import com.lexiangbao.tool.generator.mybatis.util.DbUtil;
import com.lexiangbao.tool.generator.mybatis.util.TemplateUtil;

public class Generator {
	
	public static String mapper_prefix;
	public static String mapper_suffix;

	public static String dao_prefix;
	public static String dao_suffix;
	public static String daoimpl_prefix;
	public static String daoimpl_suffix;
	public static String model_prefix;
	public static String model_suffix;
	public static String querymapper_prefix;
	public static String querymapper_suffix;
	public static String item_prefix;
	public static String item_suffix;
	public static String example_prefix;
	public static String example_suffix;
	public static String src;
	static {
		String resourceFile = "config";
		try {
			ResourceBundle rb = ResourceBundle.getBundle(resourceFile);
			mapper_prefix = rb.getString("mapper_prefix");
			mapper_suffix = rb.getString("mapper_suffix");
			dao_prefix = rb.getString("dao_prefix");
			dao_suffix = rb.getString("dao_suffix");
			daoimpl_prefix = rb.getString("daoimpl_prefix");
			daoimpl_suffix = rb.getString("daoimpl_suffix");
			model_prefix = rb.getString("model_prefix");
			model_suffix = rb.getString("model_suffix");
			querymapper_prefix = rb.getString("querymapper_prefix");
			querymapper_suffix = rb.getString("querymapper_suffix");
			item_prefix = rb.getString("item_prefix");
			item_suffix = rb.getString("item_suffix");
			example_prefix = rb.getString("example_prefix");
			example_suffix = rb.getString("example_suffix");
			src = rb.getString("src");
		} catch (Exception e) {
			
		}

	}

	
   public static void run(String packageName,GenerateTypes generateTypes,List<String>tableNames){
        try{
        int createFileCounts=0;
        for(String tableN:tableNames){
            Map<String, Object> message = DbUtil.getTableColumns(tableN);
        	message.put("mapper_prefix", mapper_prefix);
        	message.put("mapper_suffix", mapper_suffix);
        	message.put("dao_prefix", dao_prefix);
        	message.put("dao_suffix", dao_suffix);
        	message.put("daoimpl_prefix", daoimpl_prefix);
        	message.put("daoimpl_suffix", daoimpl_suffix);
        	message.put("model_prefix", model_prefix);
        	message.put("model_suffix", model_suffix);
        	message.put("example_prefix", example_prefix);
        	message.put("example_suffix", example_suffix);
            if (generateTypes.getIsCreateModel()) {// 生成model
            	 message.put("basePackageName", packageName);
            	 if((Boolean)message.get("hasPk")==Boolean.FALSE){
            		 JOptionPane.showMessageDialog(null, "Table "+(String)message.get("tableName")+" with no PK not supported. Use the first column as default PK");
            	 }
                if(generateJava(src+packageName+"."+model_prefix, message.get("className").toString()+model_suffix, "/template/Model.ftl",
                        message)){
                	createFileCounts++;
                };
            }
            if (generateTypes.getIsCreateMapper()) {
            	 message.put("basePackageName", packageName);
            	if(generateMapper(src+packageName+"."+mapper_prefix, message.get("className").toString() + mapper_suffix,
                        "/template/mapper.ftl", message)){
            		createFileCounts++;
            	}
            	
            	/*if(generateJava(src+packageName+"."+example_prefix, message.get("className").toString() + example_suffix,
                        "/template/example.ftl", message)){
            		createFileCounts++;
            	}*/
            }
            if (generateTypes.getIsCreateDao()) {
            	 if(generateJava(src+packageName+"."+dao_prefix, message.get("className").toString()+dao_suffix, "/template/dao.ftl",
                         message)){
                 	createFileCounts++;
                 };
                 /*if(generateJava(src+packageName+"."+daoimpl_prefix, message.get("className").toString()+daoimpl_suffix, "/template/daoImpl.ftl",
                         message)){
                 	createFileCounts++;
                 };*/
            }
        }
        JOptionPane.showMessageDialog(null, "done！共生成"+createFileCounts+"个文件！");
        }catch(Exception e){
        	 System.out.println(e.toString());
        	 JOptionPane.showMessageDialog(null, e.toString());
        }
    }
   
   public static void createQuery(String packageName,String sql,String selectKey){
       try{
       int createFileCounts=0;

       Map<String, Object> message = DbUtil.getBySql(sql, selectKey);
       message.put("selectKey", selectKey);
        message.put("sqlquery", sql);
        message.put("mapper_prefix", mapper_prefix);
    	message.put("mapper_suffix", mapper_suffix);
    	message.put("dao_prefix", dao_prefix);
    	message.put("dao_suffix", dao_suffix);
    	message.put("daoimpl_prefix", daoimpl_prefix);
    	message.put("daoimpl_suffix", daoimpl_suffix);
    	message.put("model_prefix", model_prefix);
    	message.put("model_suffix", model_suffix);
    	message.put("example_prefix", example_prefix);
    	message.put("example_suffix", example_suffix);
      	message.put("querymapper_prefix", querymapper_prefix);
    	message.put("querymapper_suffix", querymapper_suffix);
    	message.put("item_prefix", item_prefix);
    	message.put("item_suffix", item_suffix);
       message.put("basePackageName", packageName);
       message.put("querymapper_suffix", querymapper_prefix);
   	   if(generateMapper(src+packageName+"."+querymapper_prefix, message.get("className").toString() + querymapper_suffix,
               "/template/queryMapper.ftl", message)){
   		createFileCounts++;
   	   }
   	   if(generateJava(src+packageName+"."+item_prefix, message.get("itemName").toString()+item_suffix, "/template/item.ftl",
             message)){
     	createFileCounts++;
      };
       JOptionPane.showMessageDialog(null, "done！共生成"+createFileCounts+"个文件！");
       }catch(Exception e){
       	 JOptionPane.showMessageDialog(null, e.toString());
       }
   }
   
   

    public static boolean generateJava(String packageName, String className, String templateName,
            Map<String, Object> dataModel) throws Exception {
        String b = packageName + "." + className;
        String a = b.replace(".", "/") + ".java";

        return generateFile(a, templateName, dataModel);
    }
    
    public static boolean generateMapper(String packageName, String className, String templateName,
            Map<String, Object> dataModel) throws Exception {
        String b = packageName + "." + className;
        String a = b.replace(".", "/") + ".xml";

        return generateFile(a, templateName, dataModel);
    }

    protected static boolean generateFile(String fileName, String templateName, Map<String, Object> dataModel)
            throws Exception {
        System.out.println("Generating " + fileName + "...");
        File b;
        if ((b = new File(fileName)).exists()) {
          //  System.out.println("File：" + fileName + " already exists, overwrite?(Y/N)");
            if(JOptionPane.showConfirmDialog(null, "File：" + fileName + "already exists, overwrite?(Y/N)","提示",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
            	 JOptionPane.showMessageDialog(null, "Exists. Skipped");
                 return false;
            }
        }
        
        String a = TemplateUtil.process(templateName, dataModel);

        FileUtils.writeStringToFile(b, a, "UTF-8");
        return true;
    }

}
