package edu.pitt.sis.exp.colfusion.bll;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.BasicTableBL;
import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.responseModels.BasicTableResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.StoryStatusResponseModel;

public class BasicTableBLTest extends DatabaseUnitTestBase {
	
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
//	
//	@Test
//	public void testGetAttachmentList(){
//		int sid = 1751;
//		BasicTableBL basicBL=new BasicTableBL();
//		AttachmentListResponseModel result = basicBL.getAttachmentList(sid);
//		result.getPayload().get(0).getUploadTime();
//		assertEquals(true, true);
//	}
	
	public ColfusionSourceinfo testStory = new ColfusionSourceinfo();
	public BasicTableBL basicBL = new BasicTableBL();
	
	//The var and fun in beforeclass should be static.
	
	@Before
	public void testBefore() throws Exception{
		testStory = setUpTestStory(TEST_TARGET_TABLE_NAME, TEST_TARGET_COLUMNS_NAMES);
		basicBL = new BasicTableBL();
	}
	
	//TODO: redo with DatabaseTestBase
	@Ignore
	@Test 
	public void testGetTableInfo() throws Exception{
		BasicTableResponseModel result = basicBL.getTableInfo(testStory.getSid(), TEST_TARGET_TABLE_NAME);
		assertEquals("Wrong number of columns.", TEST_TARGET_COLUMNS_NAMES.length,  result.getPayload().size());
		assertEquals("Wrong column name", TEST_TARGET_COLUMNS_NAMES[0], result.getPayload().get(0).getDname_original_name());
	}
	
	@Ignore
	@Test
	public void testGetTableDataBySidAndName() throws Exception{
		int perPage = 3;
		int pageNumber = 2;
		int currentPageSize = 0;
		JointTableByRelationshipsResponeModel result = basicBL.getTableDataBySidAndName(testStory.getSid(), TEST_TARGET_TABLE_NAME, perPage, pageNumber);
		assertEquals("Wrong number of pageNo.", pageNumber,  result.getPayload().getPageNo());
		if(Math.ceil(DEFAULT_NUM_GENERATE_TUPLES/perPage) + 1 < pageNumber){
			currentPageSize = perPage;
		}else{
			currentPageSize = DEFAULT_NUM_GENERATE_TUPLES % perPage;
		}
		assertEquals("Wrong number of pageNo.", currentPageSize, result.getPayload().getJointTable().getRows().size());
		assertEquals("Wrong table name", TEST_TARGET_TABLE_NAME, result.getPayload().getJointTable().getRows().get(0).getColumnGroups().get(0).getTableName()); 
		assertEquals("Wrong column name", TEST_TARGET_COLUMNS_NAMES.length, result.getPayload().getJointTable().getRows().get(0).getColumnGroups().get(0).getColumns().size());
	}
	
	@Ignore
	@Test
	public void testGetTableDataBySidAndName2() throws Exception{
		JointTableByRelationshipsResponeModel result = basicBL.getTableDataBySidAndName(testStory.getSid(), TEST_TARGET_TABLE_NAME);
		assertEquals("Wrong number of pageNo.", DEFAULT_NUM_GENERATE_TUPLES,  result.getPayload().getJointTable().getRows().size());
		assertEquals("Wrong table name", TEST_TARGET_TABLE_NAME, result.getPayload().getJointTable().getRows().get(0).getColumnGroups().get(0).getTableName()); 
		assertEquals("Wrong column name", TEST_TARGET_COLUMNS_NAMES.length, result.getPayload().getJointTable().getRows().get(0).getColumnGroups().get(0).getColumns().size());
	}
	
	@Ignore
	@Test
	public void testGetStoryStatus() throws Exception{
		StoryStatusResponseModel result = basicBL.getStoryStatus(testStory.getSid());
		assertEquals("Wrong table name", TEST_TARGET_TABLE_NAME, result.getPayload().get(0).getTableName()); 
	}
	
	@Ignore
	@Test
	public void testGetRelationships() throws Exception{
		
	}
	
	@Ignore
	@Test
	public void testGetAttachmentList() throws Exception{
		
	}
}
