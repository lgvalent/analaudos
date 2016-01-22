package br.com.valentin.analaudos.web;

import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.orionsoft.monstrengo.core.exception.BusinessException;
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
	public static final String FACES_VIEW_1 = "/public/analaudos/createDocumentGraph?faces-redirect=true";
		
	public static final String REQUEST_PARAM_DOCUMENT_ID = "documentId";
	public static final String REQUEST_PARAM_AUTHOR = "author";

	private long documentId = IDAO.ENTITY_UNSAVED;
	private String author;

	private DocumentContent documentContent = null;
	private String text = "ULTRASSONOGRAFIA TRANSVAGINAL Bexiga vazia. Útero visualizado (histerectomia sub-total). O colo mede: 3,1 x 3,0 x 1,8 cm. Ovário direito: Medindo 3,1 x 2,2 x 2,3 cm nos seus maiores eixos. Volume de 3,4 cm³. Aprsentando uma imagem cistica, de aspecto simples, medindo 21 mm (funcional?). Ovário esquerdo: nao visualizado (grande interposicao gasosa). Ausência de líquido livre na escavação retro uterina. Não evidenciam-se massas ou tumores nas regiões anexiais. CONCLUSÃO Cisto em ovario direito.";
	private String textNormalized;
	private String graphDot;
	private String actions;
	

	public long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public DocumentContent getDocumentContent() {
		return documentContent;
	}

	public void setDocumentContent(DocumentContent documentContent) {
		this.documentContent = documentContent;
	}

	public String getText() {
		return text;
	}

	public void setText(String document) {
		this.text = document;
	}

	public String getTextNormalized() {
		return textNormalized;
	}

	public void setTextNormalized(String documentNormalized) {
		this.textNormalized = documentNormalized;
	}

	public String getGraphDot() {
		return graphDot;
	}

	public void setGraphDot(String graphSource) {
		this.graphDot = graphSource;
	}
	
	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}
	
	public boolean isHasAuthor(){ return this.author != null || !this.author.equals("");}

	public String actionStart(){
		// Limpa os dados antigos se houver
		System.out.println("==============================================START");
		// Pesquisa o documentId e prepara a visão para aprimeira execução
		if(FacesUtils.getRequestParams().containsKey(REQUEST_PARAM_DOCUMENT_ID))
			this.documentId = Integer.parseInt(FacesUtils.getRequestParam(REQUEST_PARAM_DOCUMENT_ID));
		if(FacesUtils.getRequestParams().containsKey(REQUEST_PARAM_AUTHOR))
			this.author = FacesUtils.getRequestParam(REQUEST_PARAM_AUTHOR);
		
		if(this.documentId > 0){
			try {
				this.documentContent = UtilsCrud.objectRetrieve(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentContent.class, this.documentId, null);
			} catch (BusinessException e) {
				this.text = "Error loading document:\n" + e.getMessage();
				e.printStackTrace();
			}
		}

		return FACES_VIEW_1;
	};
	
	public String doSave(){
		DocumentGraph doc = new DocumentGraph();
		doc.setAuthor(this.author);
		doc.setDocumentContent(this.documentContent);
		doc.setGraphDot(this.graphDot);
		doc.setActions(this.actions);
		doc.setTimeStamp(Calendar.getInstance());
		
		try {
			UtilsCrud.objectUpdate(this.getApplicationBean().getProcessManager().getServiceManager(), doc, null);
			FacesUtils.addInfoMsg("Conexões semânticas salvas com sucesso!");
			return FacesUtils.FACES_VIEW_SUCCESS;
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
