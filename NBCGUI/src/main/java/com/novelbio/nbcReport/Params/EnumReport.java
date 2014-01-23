package com.novelbio.nbcReport.Params;

import com.novelbio.base.PathDetail;




/**
 * 参数枚举类
 * 
 * @author novelbio
 * 
 */
public enum EnumReport {
	GeneExp("GeneExpression_result",new ReportGeneExpression()),
	GeneStructure("GeneStructure_result",null),
	GOAnalysis("GOAnalysis_result",new ReportGO()),
	PathWay("PathWayAnalysis_result",new ReportPathWay()),
	SamStatistics("SamStatistic_result",new ReportSamAndRPKM()),
	NCRNAstatistics("ncrnaStatistic_result",null),
	FastQC("QualityControl_result",new ReportQC()),
	DNASeqMap("DNASeqMap_result",new ReportDNASeqMap()),
	RNASeqMap("RNASeqMap_result",new ReportRNASeqMap()),
	DiffExp("DifferenceExpression_result",new ReportDifGene()),
	RfamStatistics("RfamStatistics_result", null),
	ReconstructTranscriptome("ReconstructTranscriptome_result", null),
	GOTree("GOTrees_result",null),
	GeneActNetwork("GeneActNetwork_result",null),
	MiRNATargetPredict("MiRNATargetPredict_result",null),
	MiRNATargetNetwork("MiRNATargetNetwork_result",null),
	PathwayActNetwork("PathwayActNetwork_result",null),
	CoExpNetLncRNA("CoExpNetLncRNA_result",null),
	ReportAll("Novelbio_result",new ReportAll()),
	@Deprecated 
	Picture("Picture",null),
	@Deprecated 
	Excel("Excel",null),
	MiRNASeqAnalysis("MiRNASeqAnalysis_result",null),
	SamConvert("SamConvert_result", null);
	
	/** 模板名称 也可以是结果文件夹名*/
	String tempName;
	/** 报告对象 */
	ReportBase reportBase;
	/**
	 * 
	 * @param tempName 模板名称 也可以是结果文件夹名
	 * @param reportBase 报告对象
	 */
	EnumReport(String tempName,ReportBase reportBase) {
		this.tempName = tempName;
		this.reportBase = reportBase;
	}

	/**
	 * 获得序列化随机文件名
	 * @return
	 */
	public String getReportRandomFileName(){
		return "report_" + this.name();
	}
	/**
	 * 得到word模板路径
	 */
	public String getTempPath(){
		return PathDetail.getReportTempPath();
	}
	
	/**
	 * 获取reportBase 报告对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getReportBase(){
		return (T) reportBase.getClone();
	}
	
	/**
	 * 得到模板文件名
	 * @return
	 */
	public String getTempName(){
		return tempName+".doc";
	}
	
	/**
	 * 得到结果目录文件名也可以是模板的名称
	 * @return
	 */
	public String getResultFolder(){
		return tempName;
	}
}
