package edu.pitt.sis.exp.colfusion.tests.bll;

import junit.framework.TestCase;

import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.BasicTableBL;
import edu.pitt.sis.exp.colfusion.responseModels.AttachmentListResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.StoryStatusResponseModel;

public class BasicTableBLTest extends TestCase {
	
//	@Test
//	public void testGetTableInfo() {
//		int sid=1751;
//		String tableName="";
//		
//		BasicTableBL basicBL=new BasicTableBL();
////		List<BasicTableInfoViewModel> result= basicBL.getTableInfo(sid, tableName);
//		BasicTableResponseModel result= basicBL.getTableInfo(sid, tableName);
//		try {
//			System.out.println(result);
//			
//			assertEquals(true, result.isSuccessful);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			fail(e.getMessage());
//		}	
//	}
	
//	@Test
//	public void testGetTableDataBySidAndName(){
//		int sid = 1751;
//		String tableName="Sheet1";
//		
//		BasicTableBL basicBL=new BasicTableBL();
//		JointTableByRelationshipsResponeModel result= basicBL.getTableDataBySidAndName(sid, tableName, 10, 1);
//		try {
//			System.out.println("111111111111");
//			System.out.println(result);
//			System.out.println("111111111111");
//			assertEquals(true, true);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			fail(e.getMessage());
//		}	
//	}
	
//	@Test
//	public void testGetStoryStatus(){
//		int sid = 1751;
//		BasicTableBL basicBL=new BasicTableBL();
//		StoryStatusResponseModel result= basicBL.getStoryStatus(sid);
//		try {
//			System.out.println("111111111111");
//			System.out.println(result);
//			System.out.println(result.getPayload().get(0).getStatus());
//			System.out.println("111111111111");
//			assertEquals(true, true);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			fail(e.getMessage());
//		}	
//	}
	
	@Test
	public void testGetAttachmentList(){
		int sid = 1751;
		BasicTableBL basicBL=new BasicTableBL();
		AttachmentListResponseModel result = basicBL.getAttachmentList(sid);
		result.getPayload().get(0).getUploadTime();
		assertEquals(true, true);
	}
}
