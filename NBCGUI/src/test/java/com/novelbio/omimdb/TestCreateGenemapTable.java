package com.novelbio.omimdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.fop.fo.properties.ForcePageCount;

import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.omimdb.mgmt.MgmtOMIM;
import com.novelbio.omimdb.model.OmimGeneMap;
import com.novelbio.omimdb.util.CreatGenemapTable;

import junit.framework.TestCase;

public class TestCreateGenemapTable extends TestCase {
//	String genemapFilePath = "D:\\OMIM\\genemap2_v2.txt";
	String genemapFilePath = "D:\\OMIM\\genemap2_v2.txt";
	
	public void testaddInf() {
		CreatGenemapTable creatGenemapTable = new CreatGenemapTable();
		GeneID geneID = new GeneID("tp53",9606);
		String test=geneID.getAccID();
		System.out.println("gene ID is "+ test);
//		creatGenemapTable.creatGenemapTable(genemapFilePath);
//		MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
//		List<OmimGeneMap> lsOminGeneMaps = mgmtOMIM.findByGenMimId(geneId);

//		assertEquals(1, lsOminGeneMaps.size());
	}
}
