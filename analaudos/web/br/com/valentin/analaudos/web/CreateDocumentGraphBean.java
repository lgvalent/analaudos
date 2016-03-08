package br.com.valentin.analaudos.web;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.orionsoft.monstrengo.core.exception.BusinessException;
import br.com.orionsoft.monstrengo.crud.entity.IEntity;
import br.com.orionsoft.monstrengo.crud.entity.dao.IDAO;
import br.com.orionsoft.monstrengo.crud.services.UtilsCrud;
import br.com.orionsoft.monstrengo.view.jsf.bean.BeanSessionBasic;
import br.com.orionsoft.monstrengo.view.jsf.util.FacesUtils;
import br.com.valentin.analaudos.entities.DocumentContent;
import br.com.valentin.analaudos.entities.DocumentGraph;

@ManagedBean
@SessionScoped
public class CreateDocumentGraphBean extends BeanSessionBasic{
	private static final long serialVersionUID = 1L;

	/** Define a view JSF que é ativada para cada view */
	public static final String VIEW_NAME = "createDocumentGraphBean";
	public static final String FACES_VIEW_0 = "/public/analaudos/welcome?faces-redirect=true";
	public static final String FACES_VIEW_1 = "/public/analaudos/createDocumentGraph?faces-redirect=true";
	public static final String FACES_VIEW_2 = "/public/analaudos/thanks?faces-redirect=true";
		
	public static final String REQUEST_PARAM_DOCUMENT_ID = "documentId";
	public static final String REQUEST_PARAM_AUTHOR = "author";
	public static final String REQUEST_PARAM_SOUND_ON = "soundOn";

	private long documentId = IDAO.ENTITY_UNSAVED;
	private int currentDocumentIdIndex = -1;
	private long[] documentIds = new long[]{1,2,3,4,5,6,7,8,9,10};
	private String authorUUIDSession;
	private boolean soundOn = true;

	private IEntity<DocumentContent> documentContent = null;
	private IEntity<DocumentGraph> documentGraph = null;
	private String text = "ULTRASSONOGRAFIA TRANSVAGINAL Bexiga vazia. Útero visualizado (histerectomia sub-total). O colo mede: 3,1 x 3,0 x 1,8 cm. Ovário direito: Medindo 3,1 x 2,2 x 2,3 cm nos seus maiores eixos. Volume de 3,4 cm³. Aprsentando uma imagem cistica, de aspecto simples, medindo 21 mm (funcional?). Ovário esquerdo: nao visualizado (grande interposicao gasosa). Ausência de líquido livre na escavação retro uterina. Não evidenciam-se massas ou tumores nas regiões anexiais. CONCLUSÃO Cisto em ovario direito.";
//	private String textNormalized;
//	private String graphDot;
//	private String graphJson;
//	private String actions;
//	private String suggestions;

	
	@PostConstruct
	public void onLoad(){
		try {
			this.authorUUIDSession =  UUID.randomUUID().toString();

			this.documentGraph = UtilsCrud.create(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentGraph.class, null);
			this.documentGraph.getObject().setAuthor(this.authorUUIDSession);
		} catch (BusinessException e) {
			e.printStackTrace();
			FacesUtils.addErrorMsg(e.getMessage());
		}
	}
	
	public long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;

		try {
			this.documentContent = UtilsCrud.retrieve(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentContent.class, this.documentId, null);
			this.text = this.documentContent.getObject().getContent();
		} catch (BusinessException e) {
			this.text = "Error loading document:\n" + e.getMessage();
			e.printStackTrace();
		}
		
	}

	public IEntity<DocumentContent> getDocumentContent() {
		return documentContent;
	}

	public void setDocumentContent(IEntity<DocumentContent> documentContent) {
		this.documentContent = documentContent;
	}

	public String getText() {
		return text;
	}

	public void setText(String document) {
		this.text = document;
	}

	public int getCurrentDocumentIdIndex() {return currentDocumentIdIndex;}
	public void setCurrentDocumentIdIndex(int currentDocumentId) {this.currentDocumentIdIndex = currentDocumentId;}

	public int getDocumentIdsCount(){return this.documentIds.length;}
	public int getDocumentIdsPosition(){return this.currentDocumentIdIndex + 1;}
	
	public long[] getDocumentIds() {return documentIds;}
	public void setDocumentIds(long[] documentIds) {this.documentIds = documentIds;}

	public IEntity<DocumentGraph> getDocumentGraph() {return documentGraph;}
	public void setDocumentGraph(IEntity<DocumentGraph> documentGraph) {this.documentGraph = documentGraph;}

	public boolean isHasAuthor(){ return this.documentGraph.getObject().getAuthor() != null && !this.documentGraph.getObject().getAuthor().equals("");}
	
		public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}

	public String actionStart(){
		this.currentDocumentIdIndex = 0;
		
		// Limpa os dados antigos se houver
		System.out.println("==============================================START");
		// Pesquisa o documentId e prepara a visão para aprimeira execução
		if(FacesUtils.getRequestParams().containsKey(REQUEST_PARAM_DOCUMENT_ID))
			this.documentId = Integer.parseInt(FacesUtils.getRequestParam(REQUEST_PARAM_DOCUMENT_ID));
		else
			this.documentId = this.documentIds[this.currentDocumentIdIndex];
		if(FacesUtils.getRequestParams().containsKey(REQUEST_PARAM_AUTHOR))
			this.documentGraph.getObject().setAuthor(FacesUtils.getRequestParam(REQUEST_PARAM_AUTHOR));
		if(FacesUtils.getRequestParams().containsKey(REQUEST_PARAM_SOUND_ON))
			this.soundOn = FacesUtils.checkRequestParam(REQUEST_PARAM_SOUND_ON);
		
		if(this.documentId > 0){
			// Prepara o conteúdo e texto	
			setDocumentId(this.documentId);
		}

		return FACES_VIEW_1;
	};
	
	public String actionSave(){
//		DocumentGraph doc = new DocumentGraph();
//		doc.setAuthor(this.author);
//		doc.setDocumentContent(this.documentContent);
//		doc.setGraphDot(this.graphDot);
//		doc.setGraphJson(this.graphJson);
//		doc.setActions(this.actions);
//		doc.setSuggestions(this.suggestions);
//		doc.setTimeStamp(Calendar.getInstance());
//		
		try {
			UtilsCrud.update(this.getApplicationBean().getProcessManager().getServiceManager(), this.documentGraph, null);
//			FacesUtils.addInfoMsg("Conexões semânticas salvas com sucesso!");
			
			this.documentGraph = UtilsCrud.create(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentGraph.class, null);
			this.documentGraph.getObject().setAuthor(this.authorUUIDSession);
			this.currentDocumentIdIndex++;
			
			if(this.currentDocumentIdIndex >= this.documentIds.length)			
				return FACES_VIEW_2;
			else{ 
				setDocumentId(this.documentIds[this.currentDocumentIdIndex]);
				return "";
			}
		} catch (BusinessException e) {
			FacesUtils.addErrorMsg("Ocorreu um erro ao salvar!");
			FacesUtils.addErrorMsgs(e.getErrorList());
			return FacesUtils.FACES_VIEW_FAILURE;
		}
		
		
	}

	public String getViewName() {
		return VIEW_NAME;
	}

	@Override
	public void doReload() throws BusinessException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doReset() throws BusinessException, Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}
