package br.com.valentin.analaudos.process;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.orionsoft.monstrengo.core.annotations.ProcessMetadata;
import br.com.orionsoft.monstrengo.core.exception.BusinessException;
import br.com.orionsoft.monstrengo.core.exception.BusinessMessage;
import br.com.orionsoft.monstrengo.core.exception.MessageList;
import br.com.orionsoft.monstrengo.core.process.IRunnableEntityCollectionProcess;
import br.com.orionsoft.monstrengo.core.process.IRunnableEntityProcess;
import br.com.orionsoft.monstrengo.core.process.ProcessBasic;
import br.com.orionsoft.monstrengo.core.process.ProcessException;
import br.com.orionsoft.monstrengo.core.process.ProcessParamBasic;
import br.com.orionsoft.monstrengo.core.util.CalendarUtils;
import br.com.orionsoft.monstrengo.crud.entity.EntityException;
import br.com.orionsoft.monstrengo.crud.entity.IEntity;
import br.com.orionsoft.monstrengo.crud.entity.IEntityCollection;
import br.com.orionsoft.monstrengo.crud.entity.IEntityList;
import br.com.orionsoft.monstrengo.crud.services.UtilsCrud;
import br.com.valentin.analaudos.build.AnalaudosDocument;
import br.com.valentin.analaudos.build.AnalaudosGraph;
import br.com.valentin.analaudos.build.AnalaudosGraphStrategyDeltaWord;
import br.com.valentin.analaudos.entities.DocumentGraph;
import br.com.valentin.analaudos.entities.ResearchSettings;

/**
 * This process list DocumentGraph for selection and creation of AnalaudosGraph.
 * 
 * @author lucio
 * @version 20160524
 */
@ProcessMetadata(label="Constrói um AnalaudosGraph utilizando anotações realizadas pelos especialistas", hint="Processo de criação de AnalaudosGraph", description="Selecione as anotações para fazerem parte do AnalaudosGraph e teste a capacidade de reconhecimento utilizando um texto qualquer.")
public class AnalaudosGraphBuilderProcess extends ProcessBasic implements IRunnableEntityProcess, IRunnableEntityCollectionProcess{
	public static final String PROCESS_NAME = AnalaudosGraphBuilderProcess.class.getSimpleName();

	private ProcessParamBasic<Calendar> paramDateBegin = new ProcessParamBasic<Calendar>(Calendar.class, CalendarUtils.getCalendarFirstMonthDay(), true, this);
	private ProcessParamBasic<Calendar> paramDateEnd = new ProcessParamBasic<Calendar>(Calendar.class, CalendarUtils.getCalendarLastMonthDay(), true, this);
	private ProcessParamBasic<String> paramTag = new ProcessParamBasic<String>(String.class, "", false, this);
	private ProcessParamBasic<Long> paramDocumentId = new ProcessParamBasic<Long>(Long.class, false, this);
	
	private IEntityList<DocumentGraph> documentGraphList;
	private List<AnalaudosDocument> analaudosDocuments = new ArrayList<AnalaudosDocument>();
	private AnalaudosGraph analaudosGraph;

	private ProcessParamBasic<String> paramContent = new ProcessParamBasic<String>(String.class, "", false, this);
	private AnalaudosDocument contentAnalaudosDocument;

	@Override
	public void start() throws ProcessException {
		super.start();
		
		try {
			this.documentGraphList = this.getProcessManager().getServiceManager().getEntityManager().getEntityList(new ArrayList<DocumentGraph>(), DocumentGraph.class);
		} catch (EntityException e) {
			e.printStackTrace();
		}
	}

	public String getProcessName() {
		return PROCESS_NAME;
	}

	public boolean runListDocGraph() {
		super.beforeRun();
		
		StringBuffer filter = new StringBuffer();
		
		filter.append("(timeStamp BETWEEN  '").append(CalendarUtils.formatToSQLDate(this.paramDateBegin.getValue())).append("' AND '").append(CalendarUtils.formatToSQLDate(this.paramDateEnd.getValue())).append("')");
		
		if(!this.paramTag.isEmpty()){
			filter.append(" AND (");
			String[] tags = this.paramTag.getValue().split(" ");
			int count = 0;
			for(String tag: tags){
				if(count++>0)
					filter.append(" OR ");
				filter.append("(tag like '%").append(tag).append("%')");
			}
			filter.append(" )");
		}

		if(!this.paramDocumentId.isEmpty())
			filter.append(" AND (documentContent = ").append(this.paramDocumentId.getValue()).append(")");

		try {
			this.documentGraphList = UtilsCrud.list(this.getProcessManager().getServiceManager(), DocumentGraph.class, filter.toString(), null);
		} catch (BusinessException e) {
			this.getMessageList().add(MessageList.createSingleInternalError(e));

			e.printStackTrace();
		}

		return true;
	}

	
	public boolean runBuildGraph() {
		super.beforeRun();
		
		this.analaudosGraph = new AnalaudosGraph();
		this.analaudosDocuments.clear();
		
		for(IEntity<DocumentGraph> documentGraph: this.documentGraphList){
			if(documentGraph.isSelected()){
				AnalaudosDocument analDoc = new AnalaudosDocument(documentGraph.getObject().getGraphJson());
				analDoc.addActionLogData(documentGraph.getObject().getActions());

				this.analaudosDocuments.add(analDoc);

				this.analaudosGraph.addDocGraph(analDoc);
			}
		}
		
		return true;
	}
	
	public boolean runCreateDocGraph() {
		super.beforeRun();

		if(!this.paramContent.isEmpty()){
			this.contentAnalaudosDocument = this.analaudosGraph.createDocGraph(this.paramContent.getValue(), new AnalaudosGraphStrategyDeltaWord());
		}
		
		return true;
	}

	public ProcessParamBasic<Calendar> getParamDateBegin() {
		return paramDateBegin;
	}

	public ProcessParamBasic<Calendar> getParamDateEnd() {
		return paramDateEnd;
	}
	
	

	public IEntityList<DocumentGraph> getDocumentGraphList() {
		return documentGraphList;
	}

	public AnalaudosGraph getAnalaudosGraph() {
		return analaudosGraph;
	}
	
	public ProcessParamBasic<String> getParamTag() {
		return paramTag;
	}
	
	public ProcessParamBasic<Long> getParamDocumentId() {
		return paramDocumentId;
	}

	public AnalaudosDocument getContentAnalaudosDocument() {
		return contentAnalaudosDocument;
	}
	
	

	public ProcessParamBasic<String> getParamContent() {
		return paramContent;
	}

	public void setParamContent(ProcessParamBasic<String> paramContent) {
		this.paramContent = paramContent;
	}

	public List<AnalaudosDocument> getAnalaudosDocuments() {
		return analaudosDocuments;
	}

	public boolean runWithEntity(IEntity<?> entity) {
		super.beforeRun();
		boolean result = false;
		if (entity.getInfo().getType() == ResearchSettings.class) {
			// Select all DocumentGraph from this research...
			result = true;
		} else {
			this.getMessageList().add(new BusinessMessage(IRunnableEntityProcess.class, "ENTITY_NOT_COMPATIBLE", PROCESS_NAME, entity.getInfo().getType().getName()));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean runWithEntities(IEntityCollection<?> entities) {
		super.beforeRun();
		boolean result = false;
		if (entities.getInfo().getType() == DocumentGraph.class) {
			this.documentGraphList.clear();
			
			for(IEntity<?> docGraph: entities){
				docGraph.setSelected(true);
				this.documentGraphList.add((IEntity<DocumentGraph>) docGraph);
			}
			// Select all DocumentGraph from this research...
			result = runBuildGraph();
		} else {
			this.getMessageList().add(new BusinessMessage(IRunnableEntityProcess.class, "ENTITY_NOT_COMPATIBLE", PROCESS_NAME, entities.getInfo().getType().getName()));
		}
		return result;
	}

//
//	public boolean runImportReports(){
//		super.beforeRun();
//		boolean result = true;
//
//		final LuceneInfo lucene = this.paramLuceneInfo.getValue().getObject();
//		final IEntityList<MedicalReportType> types = this.paramMedicalReportTypes.getValue();
//		
//		/* Used in OntologyIndex only */
//		final Map<String, DictionaryTermCategory> termsMap = lucene.getDictionary()==null?null:OntologyUtil.dictionaryTermSetToMap(lucene.getDictionary().getTerms());
//		final Analyzer cogroo = lucene.getDictionary()==null?null:CogrooUtil.getInstance(lucene.getDictionary().getLanguage());
//		@SuppressWarnings("resource")
//		final SpellChecker spellChecker = lucene.getDictionary()==null?null:new SpellChecker(lucene.getDictionary());
//
//		StringBuilder auditStr = new StringBuilder();
//		auditStr.append("luceneInfo=").append(lucene).append(';');
//
//		final LuceneSearch search = new LuceneSearch(lucene.getIndexPath(), AbstractAnalyzer.getInstance(lucene.getAnalyzerClassName(), lucene.getAnalyzerProperties()));
//		try {
//
//			search.openIndex(); 
//			for(IEntity<ConnectionInfo> conn: this.paramConnectionInfos.getValue()){
//				if(conn.isSelected()){
//					auditStr.append("connectionInfo=").append(conn).append(';');
//					auditStr.append("medicalReportTypes=");
//					for(IEntity<MedicalReportType> type: types){
//						if(type.isSelected()){
//							long time = System.currentTimeMillis();
//							auditStr.append(type).append(',');
//							List<MedicalReport> reports = FirebirdCrawler.listMedicalReports(conn.getObject(), type.getObject(), null, this.paramDateBegin.getValue(), this.paramDateEnd.getValue(), this.paramLimit.getValue());
//
//							ExecutorService executor = Executors.newFixedThreadPool(this.paramThreads.getValue());
//
//							for(final MedicalReport report: reports){
//								System.out.println("Adding report to thread " + report.getReportId());
//								executor.execute(
//										new Runnable() {
//											@Override
//											public void run() {
//												System.out.println("Indexing report " + report.getReportId());
//												long time = System.currentTimeMillis();
//
//												/* Apply SpellChecker if some dictionary is defined for current index */
//												if(spellChecker != null)
//													// Add some extra mark of Context
//													report.setSpelledContent(SpellCheckerUtil.analyzeText(SpellCheckerUtil.applyReplecaExpressions(report.getContent(), SpellCheckerUtil.getReplaceExpressions()), spellChecker));
//
//												try {
//													/* Persisting report in local database to generate LOCAL ID */
//													UtilsCrud.objectUpdate(getProcessManager().getServiceManager(), report, null);
//												
//													if(lucene.getIndexType()== IndexType.TEXT){
//														search.addDoc(report);
//													}else{
//
//														Utils.logln("REPORT_ID:" + report.getReportId());
//														Utils.log("ORIGINAL:" + report.getContent().length() +"\n" + report.getContent());
//														Utils.log("SPELLED:" + report.getSpelledContent().length() +"\n" + report.getSpelledContent());
//
//														for(OntologyEntity entity: OntologyChecker.checkText(report.getSpelledContent(), termsMap, cogroo)){
//															entity.setMedicalReportId(report.getId());
//															search.addOntolyDoc(entity);
//
//															Utils.log("ENTITY:" + entity.toString());
//														}
//													}
//
//												} catch (Exception e) {
//													e.printStackTrace();
//													getMessageList().add(AnalaudosGraphBuilderProcess.class, "IMPORT_REPORT_ERROR", report.getId(), report.getReportId(), e.getMessage());
//												}
//												System.out.println("Ending report " + report.getId() + " in " + (System.currentTimeMillis()-time) + "ms");
//											}
//										});
//
//							}
//
//							try{
//								// Invoca um shutdown para terminar as tarefas remanecentes
//								executor.shutdown();
//								if(!executor.awaitTermination(TIME_OUT_FOR_ONE_REPORT_PROCCESSING*reports.size(), TimeUnit.SECONDS))
//									throw new RuntimeException(new BusinessMessage(getClass(), "TIME_OUT", reports.size(), TIME_OUT_FOR_ONE_REPORT_PROCCESSING).getMessage());
//
//								lucene.setReportCount(lucene.getReportCount() + reports.size());
//								UtilsCrud.update(this.getProcessManager().getServiceManager(), this.paramLuceneInfo.getValue(), null);
//
//								BusinessMessage message = new BusinessMessage(getClass(), "IMPORT_SUCCESS", reports.size(), type.getObject().getDescription(), lucene.toString(), (System.currentTimeMillis() - time)/1000, this.paramThreads.getValue(), conn);
//								this.getMessageList().add(message);
//								auditStr.append(';').append(message.getMessage());
//
//							}catch(InterruptedException e){
//								throw new Exception("Error processing import threads.", e);
//							}
//						}
//					}
//					auditStr.append(';');
//				}
//
//			}
//
//			UtilsAuditorship.auditProcess(this, auditStr.toString(), null);
//
//		} catch (RuntimeException e) {
//			this.getMessageList().add(MessageList.createSingleInternalError(e));
//			result = false;
//		} catch (Exception e) {
//			this.getMessageList().add(MessageList.createSingleInternalError(e));
//			result = false;
//		}finally{
//			try {
//				search.closeIndex();
//				if(spellChecker != null)
//					spellChecker.close();
//			} catch (Exception e) {
//				this.getMessageList().add(MessageList.createSingleInternalError(e));
//			}
//		}
//
//		return result;
//	}
//

}
