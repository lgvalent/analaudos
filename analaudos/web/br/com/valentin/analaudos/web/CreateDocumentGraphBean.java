package br.com.valentin.analaudos.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.Cookie;

import br.com.orionsoft.monstrengo.core.exception.BusinessException;
import br.com.orionsoft.monstrengo.core.exception.MessageList;
import br.com.orionsoft.monstrengo.crud.entity.IEntity;
import br.com.orionsoft.monstrengo.crud.entity.dao.IDAO;
import br.com.orionsoft.monstrengo.crud.services.UtilsCrud;
import br.com.orionsoft.monstrengo.view.jsf.bean.BeanSessionBasic;
import br.com.orionsoft.monstrengo.view.jsf.util.FacesUtils;
import br.com.valentin.analaudos.entities.DocumentContent;
import br.com.valentin.analaudos.entities.DocumentGraph;
import br.com.valentin.analaudos.entities.ResearchSettings;

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
	public static final String REQUEST_PARAM_EXPERT = "expert";

	private ResearchSettings researchSettings;
	private long researchSettingsId = IDAO.ENTITY_UNSAVED;

	private DocumentContent documentContent = null;
	private long documentId = IDAO.ENTITY_UNSAVED;
	private int currentDocumentIdIndex = -1;
	private List<DocumentContent> documentContentList = new ArrayList<DocumentContent>(10);

	private String authorUUIDSession;
	private boolean soundOn = true;
	private boolean expert = true;

	//	private IEntity<DocumentContent> documentContent = null;
	private IEntity<DocumentGraph> documentGraph = null;


	@PostConstruct
	public void onLoad(){
		try {
			/* Try recovery authorId from cookie */
			Map<String,Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
			Cookie cookie = null;
			if(cookies.containsKey(DocumentGraph.AUTHOR)){
				cookie = (Cookie) cookies.get(DocumentGraph.AUTHOR);
				this.authorUUIDSession =  cookie.getValue();
			}else{
				this.authorUUIDSession =  UUID.randomUUID().toString();
				FacesContext.getCurrentInstance().getExternalContext().addResponseCookie(DocumentGraph.AUTHOR, this.authorUUIDSession, null);
			}

			/* Prepare this document to collect statistic data*/
			this.documentGraph = UtilsCrud.create(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentGraph.class, null);
			this.documentGraph.getObject().setAuthor(this.authorUUIDSession);
		} catch (BusinessException e) {
			e.printStackTrace();
			FacesUtils.addErrorMsg(e.getMessage());
		}
	}


	public ResearchSettings getResearchSettings() {
		return researchSettings;
	}

	public void setResearchSettings(ResearchSettings researchSettings) {
		this.researchSettings = researchSettings;
	}

	public long getResearchSettingsId() {
		return researchSettingsId;
	}

	public void setResearchSettingsId(long researchSettingsId) {
		this.researchSettingsId = researchSettingsId;

		try {
			this.researchSettings = UtilsCrud.objectRetrieve(this.getApplicationBean().getProcessManager().getServiceManager(), ResearchSettings.class, this.researchSettingsId, null);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	
	
	public long getDocumentId() {return documentId;}
	public void setDocumentId(long documentId) {
		this.documentId = documentId;

		if(documentId > 0){
			try{
				this.documentContent = UtilsCrud.objectRetrieve(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentContent.class, documentId, null);
			} catch (BusinessException e) {
				e.printStackTrace();// Prepara o conteúdo e texto	

				this.documentContent = new DocumentContent();
				this.documentContent.setContent("Error loading document:\n" + e.getMessage());
			}
		}
	}


	public DocumentContent getDocumentContent() {return documentContent;}
	public void setDocumentContent(DocumentContent documentContent) {this.documentContent = documentContent;}

	public int getCurrentDocumentIdIndex() {return currentDocumentIdIndex;}
	public void setCurrentDocumentIdIndex(int currentDocumentId) {this.currentDocumentIdIndex = currentDocumentId;}

	public int getDocumentIdsCount(){return this.documentContentList.size();}
	public int getDocumentIdsPosition(){return this.currentDocumentIdIndex + 1;}

	public IEntity<DocumentGraph> getDocumentGraph() {return documentGraph;}
	public void setDocumentGraph(IEntity<DocumentGraph> documentGraph) {this.documentGraph = documentGraph;}

	public boolean isSoundOn() {return soundOn;}
	public void setSoundOn(boolean soundOn) {this.soundOn = soundOn;}

	public boolean isExpert() {return expert;}
	public void setExpert(boolean expert) {this.expert = expert;}

	public List<SelectItem> getResearchSettingsList(){
		try {
			return this.getApplicationBean().getProcessManager().getServiceManager().getEntityManager().getEntitySelectItems(ResearchSettings.class, ResearchSettings.INACTIVE + "= false");
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String actionStart(){
		this.documentContentList.clear();

		// Pesquisa o documentId e prepara a visão para a primeira execução
		if(FacesUtils.getRequestParams().containsKey(REQUEST_PARAM_DOCUMENT_ID)){
			setDocumentId(Integer.parseInt(FacesUtils.getRequestParam(REQUEST_PARAM_DOCUMENT_ID)));
			this.documentContentList.add(this.documentContent);
		}else{
			for(DocumentContent content: this.researchSettings.getDocumentsContents()){
				this.documentContentList.add(content);
			}
		}
		
		if(FacesUtils.getRequestParams().containsKey(REQUEST_PARAM_AUTHOR))
			this.documentGraph.getObject().setAuthor(FacesUtils.getRequestParam(REQUEST_PARAM_AUTHOR));

		if(FacesUtils.getRequestParams().containsKey(REQUEST_PARAM_SOUND_ON))
			this.soundOn = FacesUtils.checkRequestParam(REQUEST_PARAM_SOUND_ON);

		this.currentDocumentIdIndex = -1;
		
		try {
			return prepareNextDocument();
		} catch (BusinessException e) {
			e.printStackTrace();
			FacesUtils.addErrorMsgs(MessageList.createSingleInternalError(e));
			return FacesUtils.FACES_VIEW_FAILURE;
		}
	}

	public String actionSave(){
		try {

			//	FacesUtils.addInfoMsg("Conexões semânticas salvas com sucesso!");
			UtilsCrud.update(this.getApplicationBean().getProcessManager().getServiceManager(), this.documentGraph, null);
			
			return prepareNextDocument();

		} catch (BusinessException e) {
			FacesUtils.addErrorMsg("Ocorreu um erro ao salvar!");
			FacesUtils.addErrorMsgs(e.getErrorList());
			return FacesUtils.FACES_VIEW_FAILURE;
		}


	}

	private String prepareNextDocument() throws BusinessException {
		this.currentDocumentIdIndex++;

		if(this.currentDocumentIdIndex >= this.documentContentList.size())			
			return FACES_VIEW_2;
		else{ 
			this.documentContent = this.documentContentList.get(this.currentDocumentIdIndex);
			this.documentId = this.documentContent.getId();
			
			DocumentGraph previousDocGraph = this.documentGraph.getObject(); 

			this.documentGraph = UtilsCrud.create(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentGraph.class, null);
			this.documentGraph.getObject().setAuthor(this.authorUUIDSession);
			this.documentGraph.getObject().setDocumentContent(this.documentContent);
			
			/* Copy previous data */
			DocumentGraph docGraph = this.documentGraph.getObject(); 
			docGraph.setGraduationYear(previousDocGraph.getGraduationYear());
			docGraph.setResidenceYear(previousDocGraph.getResidenceYear());
			docGraph.setSpecialistYear(previousDocGraph.getSpecialistYear());
			docGraph.setMasterYear(previousDocGraph.getMasterYear());
			docGraph.setDoctorYear(previousDocGraph.getDoctorYear());
			return FACES_VIEW_1;
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
