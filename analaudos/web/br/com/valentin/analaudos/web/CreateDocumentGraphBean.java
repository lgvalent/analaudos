package br.com.valentin.analaudos.web;

import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.orionsoft.monstrengo.core.exception.BusinessException;
import br.com.orionsoft.monstrengo.crud.entity.dao.IDAO;
import br.com.orionsoft.monstrengo.crud.services.UtilsCrud;
import br.com.orionsoft.monstrengo.view.jsf.bean.BeanSessionBasic;
import br.com.orionsoft.monstrengo.view.jsf.util.FacesUtils;
import br.com.valentin.analaudos.entities.DocumentGraph;

@ManagedBean
@SessionScoped
public class CreateDocumentGraphBean extends BeanSessionBasic{
	private static final long serialVersionUID = 1L;

	/** Define a view JSF que é ativada para cada view */
	public static final String VIEW_NAME = "createDocumentGraphBean";
	public static final String FACES_VIEW_1 = "/public/analaudos/createDocumentGraph?faces-redirect=true";
		
	private long documentId = IDAO.ENTITY_UNSAVED;
	private String author;

	private String document = "ULTRASSONOGRAFIA TRANSVAGINAL Bexiga vazia. Útero visualizado (histerectomia sub-total). O colo mede: 3,1 x 3,0 x 1,8 cm. Ovário direito: Medindo 3,1 x 2,2 x 2,3 cm nos seus maiores eixos. Volume de 3,4 cm³. Aprsentando uma imagem cistica, de aspecto simples, medindo 21 mm (funcional?). Ovário esquerdo: nao visualizado (grande interposicao gasosa). Ausência de líquido livre na escavação retro uterina. Não evidenciam-se massas ou tumores nas regiões anexiais. CONCLUSÃO Cisto em ovario direito.";
	private String documentNormalized;
	private String graphSource;
	
	
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

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getDocumentNormalized() {
		return documentNormalized;
	}

	public void setDocumentNormalized(String documentNormalized) {
		this.documentNormalized = documentNormalized;
	}

	public String getGraphSource() {
		return graphSource;
	}

	public void setGraphSource(String graphSource) {
		this.graphSource = graphSource;
	}

	public String actionStart(){
		// Limpa os dados antigos se houver
		System.out.println("==============================================START");
		// Pesquisa o documentId e prepara a visão para aprimeira execução
		this.documentId = -1;
		this.document = "START()";
		this.author = "Lucio";
		
		return FACES_VIEW_1;
	};
	
	public void doSave(){
		DocumentGraph doc = new DocumentGraph();
		doc.setAuthor(this.author);
		doc.setSource(this.graphSource);
		doc.setTimeStamp(Calendar.getInstance());
		
		try {
			UtilsCrud.objectUpdate(this.getApplicationBean().getProcessManager().getServiceManager(), doc, null);
			FacesUtils.addInfoMsg("Conexões semânticas salvas com sucesso!");
		} catch (BusinessException e) {
			FacesUtils.addErrorMsg("Ocorreu um erro ao salvar!");
			FacesUtils.addErrorMsgs(e.getErrorList());
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
