package com.novelbio.report.generateReport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.dbInfo.model.order.NBCExperimentNew;
import com.novelbio.dbInfo.model.order.NBCOrderNew;
import com.novelbio.dbInfo.model.order.NBCSample;
import com.novelbio.dbInfo.model.project.NBCFile;
import com.novelbio.dbInfo.model.project.NBCProject;
import com.novelbio.dbInfo.model.project.NBCTask;
import com.novelbio.report.ReportImage;
import com.novelbio.report.ReportTable;
import com.novelbio.report.TemplateRender;
import com.novelbio.report.Params.EnumTaskClass;
import com.novelbio.report.Params.EnumTaskType;
import com.novelbio.report.Params.ReportAbstract;
import com.novelbio.report.Params.ReportAll;
import com.novelbio.report.Params.ReportBase;
import com.novelbio.report.Params.ReportEnd;

public class ProjectReport {
	
	/** sampleInfo表格的label，用于表格的引用 */
	private static final String TABLELABEL = "tablsampleInfo";
	/** 项目结果tex文件的名称 */
	public static final String PROJECTRESULTNAME = "projectReport.tex";
	private ReportAll reportAll = new ReportAll();
	/** 结果报告中从METHOD REFERENCE到最后结尾的部分（\end{document}） */
	private ReportEnd reportEnd = new ReportEnd();
	private ReportAbstract reportAbstract = new ReportAbstract();
	/** task的种类及其对应的taskId */
	LinkedHashMap<EnumTaskClass, List<String>> mapTaskClass2LsTaskId = EnumTaskClass.getMapClass2LsTaskId(); 
	/** 全部的taskId */
	private List<String> lsTaskId = new ArrayList<String>();
	
	public ProjectReport() {}
	
	public ProjectReport(String projectId, List<String> lsTaskId) {
		getProjectInfo(projectId);
		classifyTaskId(lsTaskId);
		this.lsTaskId = lsTaskId;
	}
	
	/** 获取project报告所需要的信息 */
	private void getProjectInfo(String projectId) {
		//TODO project的参数未全部完成
		NBCProject nbcProject = NBCProject.findInstance(projectId);
		reportAll.setProjectName(nbcProject.getProjectName());
		// 通过订单获取样本
		NBCOrderNew nbcOrderNew = NBCOrderNew.findInstance(nbcProject.getReferOrderId());
		Set<NBCExperimentNew> setNBCExperiment = nbcOrderNew.findLsNBCExperiment();
		List<NBCSample> lsNBCSample = new ArrayList<NBCSample>();
		for (NBCExperimentNew nbcExperimentNew : setNBCExperiment) {
			lsNBCSample.addAll(nbcExperimentNew.findLsSample());
		}
		//获取物种名称
		String speciesName = getSpeciesName(lsNBCSample);
		reportAll.setSpeciesName(speciesName);
		//获取样本信息
		List<String[]> lsSampleInfo = getSampleInfoByOrder(lsNBCSample);
		ReportTable reportTable = new ReportTable();
		reportAll.addTable("tbSampleInfo", reportTable.getMapKey2Param("", TABLELABEL, lsSampleInfo, 2));
		// 获取平台名称
		NBCExperimentNew nbcExperimentNew = setNBCExperiment.iterator().next();
		String sequence = EnumSequence.valueOf(nbcExperimentNew.getSequencingPlatform()).toString();
		reportAll.setSequence(sequence);
	}
	
	/** 获取多个物种名字组成的字符串 */
	private String getSpeciesName(List<NBCSample> lsNBCSample) {
		Set<String> setSpeciesName = new HashSet<String>();
		// 去除重复的样本名称
		for (NBCSample nbcSample : lsNBCSample) {
			setSpeciesName.add(nbcSample.getSpeciesName());
		}
		List<String> lsSpeciesName = new ArrayList<String>(setSpeciesName);
		String allSpeciesName = "";
		// 连接样本名称
		for (int i = 0; i < lsSpeciesName.size(); i++) {
			if (i != lsSpeciesName.size()-1) {
				allSpeciesName = allSpeciesName + lsSpeciesName.get(i) + ",";
			} else {
				allSpeciesName = allSpeciesName + lsSpeciesName.get(i);
			}
		}
		return allSpeciesName;
	}
	
	/** 将taskId分类 */
	private void classifyTaskId(List<String> lsTaskId) {
		for (String taskId : lsTaskId) {
			NBCTask nbcTask = NBCTask.findInstance(taskId);
			EnumTaskClass taskClass = EnumTaskType.valueOf(nbcTask.getTaskType().toString()).getTaskClass();
			List<String> lsClassTaskId = mapTaskClass2LsTaskId.get(taskClass);
			lsClassTaskId.add(taskId);
			mapTaskClass2LsTaskId.put(taskClass, lsClassTaskId);
		}
	}

	/** 生成项目报告，参数为保存的路径 */
	public void saveReport(String savePath) throws IOException {
		String projectReportPath = FileOperate.addSep(savePath) + PROJECTRESULTNAME;
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream(projectReportPath, true)));
		TemplateRender templateRender = new TemplateRender();
		// 渲染项目信息
		templateRender.render(reportAll.getFtlTempPathAndName(), reportAll.getMapKey2Param(), out);
		// 渲染Abstract
		templateRender.render(reportAbstract.getFtlTempPathAndName(), reportAll.getMapKey2Param(), out);
		// 按不同的分类进行渲染报告
		for (EnumTaskClass taskClass : mapTaskClass2LsTaskId.keySet()) {
			out.write("\\subsection{" + taskClass.getTitle() + "}\n");
			renderTaskReport(mapTaskClass2LsTaskId.get(taskClass), templateRender, out);
		}
		// 生成method
		out.write("\\section{METHOD}\n");
		renderTaskMethod(templateRender, out);
		// 复制图片到结果报告的目录下面
		copyImages(savePath);
		// 渲染整个报告的结尾部分
		// TODO 加参数
		templateRender.render(reportEnd.getFtlTempPathAndName(), null, out);
		out.close();
	}
	
	/** 渲染task结果报告 */
	private void renderTaskReport(List<String> lsClassifyTaskId, TemplateRender templateRender, Writer out) {
		// 获取task结果的路径
		List<String> lsTaskResultPath = getLsTaskResultPath(lsClassifyTaskId);
		for (String taskResultPath : lsTaskResultPath) {
			// 获取.report下的序列化文件
			List<String> lsReportFilePath = FileOperate.getFoldFileNameLs(FileOperate.addSep(taskResultPath) + ".report");
			for (String reportFilePath : lsReportFilePath) {
				ReportBase reportBase = ReportBase.readReportFromFile(reportFilePath);
				templateRender.render(reportBase, out);
			}
		}
	}
	
	/** 渲染task对应的method报告 */
	private void renderTaskMethod(TemplateRender templateRender, Writer out) {
		List<String> lsTaskResultPath = getLsTaskResultPath(lsTaskId);
		for (String taskResultPath : lsTaskResultPath) {
			List<String> lsReportFilePath = FileOperate.getFoldFileNameLs(FileOperate.addSep(taskResultPath) + ".report");
			for (String reportFilePath : lsReportFilePath) {
				ReportBase reportBase = ReportBase.readReportFromFile(reportFilePath);
				templateRender.renderMethodReport(reportBase, out);
			}
		}
	}
	
	/** 获取所有的Task结果文件路径 */
	private List<String> getLsTaskResultPath(List<String> lsClassifyTaskId) {
		List<String> lsTaskResultPath = new ArrayList<String>();
		for (String taskId : lsClassifyTaskId) {
			NBCTask nbcTask = NBCTask.findInstance(taskId);
			if (nbcTask == null) continue;
			List<NBCFile> lsNBCFile = nbcTask.getResultFolders();
			for (NBCFile nbcFile : lsNBCFile) {
				lsTaskResultPath.add(nbcFile.getRealPathAndName());
			}
		}
		return lsTaskResultPath;
	}
	
	/** 获取样本信息， 为List<String[]>形式，用于模板中表格的生成*/
	private List<String[]> getSampleInfoByOrder(List<NBCSample> lsNBCSample) {
		List<String[]> lsSampleInfo = new ArrayList<String[]>();
		String[] sampleInfo = new String[2];
		// 添加表格的列标题
		sampleInfo[0] = "SampleName";
		sampleInfo[1] = "group";
		lsSampleInfo.add(sampleInfo);
		// 添加表格的内容
		for (NBCSample nbcSample : lsNBCSample) {
			sampleInfo = new String[2];
			sampleInfo[0] = nbcSample.getSampleName();
			sampleInfo[1] = nbcSample.getGroupName();
			lsSampleInfo.add(sampleInfo);
		}
		return lsSampleInfo;
	}
	
	/** 复制各个task的结果图片，复制到结果报告路径下的image_taskId文件夹（image_54aca51a8314525ab6dc8cb8） */
	public void copyImages(String savePath) {
		for (String taskId : lsTaskId) {
			NBCTask nbcTask = NBCTask.findInstance(taskId);
			if (nbcTask == null) continue;
			String imagePath = FileOperate.addSep(savePath) + ReportImage.IMAGE + taskId;
			FileOperate.createFolders(imagePath);
			List<NBCFile> lsNBCFile = nbcTask.getResultFolders();
			for (NBCFile nbcFile : lsNBCFile) {
				List<String> lsFilePath = getAllFilePath(nbcFile.getRealPathAndName());
				for (String filePath : lsFilePath) {
					if (filePath.endsWith(".png")) {
						String newImgPath = FileOperate.addSep(imagePath) + FileOperate.getFileName(filePath);
						FileOperate.copyFile(filePath, newImgPath, true);
					}
				}
			}
		}
	}
	
	/** 递归获取文件夹下的所有文件路径 */
	public List<String> getAllFilePath(String folderPath) {
		List<String> lsFoldFilePath = FileOperate.getFoldFileNameLs(folderPath);
		List<String> lsFilePath = new ArrayList<String>();
		for (String filePath : lsFoldFilePath) {
			if (FileOperate.isFileDirectory(filePath)) {
				lsFilePath.addAll(getAllFilePath(filePath));
			} else {
				lsFilePath.add(filePath);
			}
		}
		return lsFilePath;
	}

}