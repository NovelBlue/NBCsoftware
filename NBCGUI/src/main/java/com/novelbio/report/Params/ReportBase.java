package com.novelbio.report.Params;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.inject.Key;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.ReportImage;
import com.novelbio.report.ReportTable;

/**
 * 参数对象基本抽象类
 * @author novelbio
 *
 */
public abstract class ReportBase  implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger(ReportBase.class);
	/**
	 * 所有的参数集合
	 */
	Map<String, Object> mapKey2Param = new HashMap<String, Object>();
	/**
	 * 保存路径
	 */
	protected String savePath;
	
	List<ReportBase> lsReportBase = new ArrayList<ReportBase>();
	
	/**获取参数集合*/
	public Map<String, Object> getMapKey2Param() {
		return mapKey2Param;
	}
	
	/**获取子报告集合*/
	public List<ReportBase> getSubReportBase() {
		return lsReportBase;
	}
	
	/**
	 * 得到报告类型
	 * @return
	 */
	public abstract EnumTaskReport getEnumReport();
	
	
	/**
	 * 把对象本身写成二进制文件
	 * @param savePath 保存的路径，会添加.report目录
	 * @return
	 */
	public String writeAsFile(String savePath) {
		this.savePath = savePath;
		String reportPath = FileOperate.addSep(savePath) + ".report";
		FileOperate.createFolders(reportPath);
		FileOperate.delAllFile(reportPath);
		String randomReportFile = FileOperate.addSep(reportPath) +  getEnumReport().getReportFileName();
		FileOperate.writeObjectToFile(this, randomReportFile);
		return randomReportFile;
	}
	
	/**
	 * 把对象本身写成二进制文件
	 * @param savePath 保存的路径，会添加.report目录
	 * @param sufix 加在文件名中的后缀
	 * @return
	 */
	public String writeAsFile(String savePath, String sufix) {
		this.savePath = savePath;
		String reportPath = FileOperate.addSep(savePath) + ".report";
		FileOperate.createFolders(reportPath);
//		FileOperate.delAllFile(reportPath);
		String reportFile = FileOperate.addSep(reportPath) +  getEnumReport().getReportFileName(sufix);
		FileOperate.writeObjectToFile(this, reportFile);
		return reportFile;
	}
	
	/**
	 * 从文件中反序列化报告对象
	 * @param pathAndName 序列化文件的全路径
	 * @return
	 */
	public static ReportBase readReportFromFile(String pathAndName) {
		try {
			ReportBase reportBase = (ReportBase) FileOperate.readFileAsObject(pathAndName);
			return reportBase;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报告文件 :"+pathAndName +" 序列化失败");
			return null;
		}
	}
	
	/** 取得克隆的对象 */
	public ReportBase getClone() {
		try {
			return (ReportBase) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**获取ftl模板路径*/
	public String getFtlTempPathAndName() {
		String pathAndName = FileOperate.addSep(getEnumReport().getTempPath()) + getEnumReport().getFtlTempName();
		return pathAndName;
	}
	
	public String getMethodTempPathAndName() {
		String pathAndName = FileOperate.addSep(getEnumReport().getTempPath()) + getEnumReport().getFtlMethodTempName();
		return pathAndName;
	}
	
	/**添加子报告*/
	public void addSubReport(ReportBase reportBase) {
		lsReportBase.add(reportBase);
	}
	
//	/**添加图片*/
//	public void addReportImage(ReportImage reportImage) {
//		List<Map<String, Object>> lsReportImage = null;
//		if (!mapKey2Param.containsKey("lsImage")) {
//			lsReportImage = new ArrayList<Map<String, Object>>();
//		} else {
//			lsReportImage = (List<Map<String, Object>>) mapKey2Param.get("lsImage");
//		}
//		lsReportImage.add(reportImage.getMapKey2Param());
//		mapKey2Param.put("lsImage", lsReportImage);
//	}
	
	/**添加图片，图片对应的参数名默认为image*/
	public void addReportImage(ReportImage reportImage) {
		mapKey2Param.put("image", reportImage.getMapKey2Param());
	}
	
	/**添加图片，可以设定图片对应的参数名，imageName为图片对应的参数名*/
	public void addReportImage(String imageName, ReportImage reportImage) {
		mapKey2Param.put(imageName, reportImage.getMapKey2Param());
	}
	
	/** 添加表格，表格对应的参数名默认为table */
	public void addTable(Map<String, Object> mapKey2TableData) {
		mapKey2Param.put("table", mapKey2TableData);
	}
	
	/** 添加表格，表格所对应的参数名可以设定，tableName为对应的名称模板中会用到 */
	public void addTable(String tableName, Map<String, Object> mapKey2TableData) {
		mapKey2Param.put(tableName, mapKey2TableData);
	}
	
//	/**添加表格*/
//	public void addReportTable(ReportTable reportTable) {
//		List<Map<String, Object>> lsReportTable = null;
//		if (!mapKey2Param.containsKey("lsTable")) {
//			lsReportTable = new ArrayList<Map<String, Object>>();
//		} else {
//			lsReportTable = (List<Map<String, Object>>) mapKey2Param.get("lsTable");
//		}
//		lsReportTable.add(reportTable.getMapKey2Param());
//		mapKey2Param.put("lsTable", lsReportTable);
//	}
}