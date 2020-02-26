package com.nab.core.workflow;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.metadata.SimpleMetaDataMap;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.nab.core.DigitalCurrencyProfit;
import com.nab.workflow.DigitalCurrencyWorkFlowModel;

import io.wcm.testing.mock.aem.junit.AemContext;

@RunWith(PowerMockRunner.class)
public class DigitalCurrencyWorkFlowModelTest {
	
	@Rule
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	
	@Mock
	WorkItem workItem;
	
	@Mock
	WorkflowSession workflowSession;
	
	@Mock
	WorkflowData workflowData;
	
	MetaDataMap metaDataMap;
	
	@InjectMocks
	DigitalCurrencyWorkFlowModel dcModel;
	
	DigitalCurrencyWorkFlowModel dcWorkflowModel;
	
	@Mock
	ResourceResolverFactory resourceResolverFactory;
	
	Resource resource;
	
	@Mock
	Session session;
	
	private static  final String PAYLOAD_PATH ="/content/dam/nab/csv/20.csv";
	
	private static final String PAYLOAD_PATH_1 = "/content/dam/nab/csv/error.csv";
		
	Map<String, Object> param = new HashMap<String, Object>();
	
	@Before	
	public void setUp() throws LoginException  {		
		metaDataMap = new SimpleMetaDataMap(); 			
							
		Mockito.when(workItem.getWorkflowData()).thenReturn(workflowData);
		Mockito.when(workflowData.getPayload()).thenReturn(workflowData);
		Mockito.when(workflowData.getPayloadType()).thenReturn("JCR_PATH");
		Mockito.when(workItem.getWorkflowData().getPayload().toString()).thenReturn(PAYLOAD_PATH);
		session = MockJcr.newSession();
		
	}
	
	@Test
	public void testLoginException() throws LoginException, WorkflowException {		
		param.put(ResourceResolverFactory.SUBSERVICE, "getResourceResolver");
		param.put(ResourceResolverFactory.USER, "datawriteuser");
		Mockito.doThrow(LoginException.class).when(resourceResolverFactory).getServiceResourceResolver(param);
		dcModel.execute(workItem, workflowSession, metaDataMap);		
	}
	
	@Test
	public void testExecute() throws WorkflowException, LoginException, PathNotFoundException, RepositoryException {
		dcWorkflowModel = context.registerInjectActivateService(new DigitalCurrencyWorkFlowModel());
		InputStream is = Thread.currentThread().getContextClassLoader()
				    .getResourceAsStream("20.csv");
			/*context.create().resource("/content/dam/nab/csv/20.csv/jcr:content/renditions/original/jcr:content", 
					ImmutableMap.<String, Object>builder().put("jcr:data", is).build());*/
			/*session.getRootNode().addNode("/content/dam/nab/csv/20.csv/jcr:content/renditions/original/jcr:content")
								 .setProperty("jcr:data",is);*/
		context.create().asset("/content/dam/nab/csv/20.csv", is, "text/csv");	
		context.create().resource("/var/nab/digitalCurrency/details");
		dcWorkflowModel.execute(workItem, workflowSession, metaDataMap);
		Node node = context.currentResource("/var/nab/digitalCurrency/details").adaptTo(Node.class);
		Gson gson = new Gson();
		DigitalCurrencyProfit dfProfit= gson.fromJson(node.getProperty("BTCProfit").getString(), DigitalCurrencyProfit.class);
		assertEquals("BTC",dfProfit.getCurrency());
		assertEquals("14:00 PM",dfProfit.getMaxTime());
	}
	
	@Test
	public void testPathNotFoundException() throws RepositoryException, WorkflowException, PersistenceException {
		dcWorkflowModel = context.registerInjectActivateService(new DigitalCurrencyWorkFlowModel());
		context.create().resource("/content/dam/nab/csv/20.csv/jcr:content/renditions/original/jcr:content");
		dcWorkflowModel.execute(workItem, workflowSession, metaDataMap);
	}
	
	@Test
	public void testException() throws WorkflowException, IOException {
		dcWorkflowModel = context.registerInjectActivateService(new DigitalCurrencyWorkFlowModel());
		Mockito.when(workItem.getWorkflowData().getPayload().toString()).thenReturn(PAYLOAD_PATH_1);
		InputStream is = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream("error.csv");
		context.create().resource("/content/dam/nab/csv/error.csv/jcr:content/renditions/original/jcr:content", 
				ImmutableMap.<String, Object>builder().put("jcr:data", is).build());
		dcWorkflowModel.execute(workItem, workflowSession, metaDataMap);
	} 
	
	
	
}
