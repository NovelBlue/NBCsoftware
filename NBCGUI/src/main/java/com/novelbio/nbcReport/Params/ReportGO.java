package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.nbcReport.XdocTmpltPic;

/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportGO extends ReportBase {
	private String testMethod;
	private String finderCondition;
	private int upRegulation;
	private int downRegulation;
	private List<String> lsResultFiles;
	private List<XdocTmpltPic> lsXdocTmpltPics;
	
	/**
	 * 添加图片模板
	 * @param xdocTmpltPic
	 */
	public void addXdocTempPic(XdocTmpltPic xdocTmpltPic) {
		if (lsXdocTmpltPics == null) {
			lsXdocTmpltPics = new ArrayList<XdocTmpltPic>();
		}
		lsXdocTmpltPics.add(xdocTmpltPic);
	}
	
	
	
	@Override
	protected Map<String, Object> addParamMap() {
		Map<String,Object> mapKey2Params = new HashMap<String, Object>();
		mapKey2Params.put("testMethod", testMethod);
		mapKey2Params.put("finderCondition", finderCondition);
		mapKey2Params.put("upRegulation", upRegulation);
		mapKey2Params.put("downRegulation", downRegulation);
		mapKey2Params.put("lsResultFiles", lsResultFiles);
		mapKey2Params.put("lsPictures", getPictures());
		return mapKey2Params;
	}



	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GOAnalysis;
	}



	public String getTestMethod() {
		return testMethod;
	}
	
	/**
	 * 取得所有的图片集合
	 * @return
	 */
	public List<String> getPictures(){
		List<String> lsPictures = new ArrayList<String>();
		for (XdocTmpltPic xdocTmpltPic : lsXdocTmpltPics) {
			lsPictures.add(xdocTmpltPic.toString());
		}
		return lsPictures;
	}
	
	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public String getFinderCondition() {
		return finderCondition;
	}

	public void setFinderCondition(String finderCondition) {
		this.finderCondition = finderCondition;
	}

	public int getUpRegulation() {
		return upRegulation;
	}

	public void setUpRegulation(int upRegulation) {
		this.upRegulation = upRegulation;
	}

	public int getDownRegulation() {
		return downRegulation;
	}

	public void setDownRegulation(int downRegulation) {
		this.downRegulation = downRegulation;
	}

	public List<String> getLsResultFiles() {
		return lsResultFiles;
	}

	public void addResultFile(String resultFile) {
		if (lsResultFiles == null) {
			lsResultFiles = new ArrayList<String>();
		}
		lsResultFiles.add(resultFile);
	}
}