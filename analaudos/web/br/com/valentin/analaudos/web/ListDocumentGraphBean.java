package br.com.valentin.analaudos.web;

import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.orionsoft.monstrengo.core.exception.BusinessException;
import br.com.orionsoft.monstrengo.core.util.CalendarUtils;
import br.com.orionsoft.monstrengo.crud.entity.IEntityList;
import br.com.orionsoft.monstrengo.crud.entity.dao.IDAO;
import br.com.orionsoft.monstrengo.crud.services.UtilsCrud;
import br.com.orionsoft.monstrengo.view.jsf.bean.BeanSessionBasic;
import br.com.orionsoft.monstrengo.view.jsf.util.FacesUtils;
import br.com.valentin.analaudos.entities.DocumentGraph;

@ManagedBean
@SessionScoped
public class ListDocumentGraphBean extends BeanSessionBasic{
	private static final long serialVersionUID = 1L;

	/** Define a view JSF que é ativada para cada view */
	public static final String VIEW_NAME = "listDocumentGraphBean";
	public static final String FACES_VIEW_1 = "/public/analaudos/listDocumentGraph?faces-redirect=true";
		
	private Long documentId = IDAO.ENTITY_UNSAVED;
	private String author;
	private boolean filterByDate = true;
	private Calendar dateFrom = CalendarUtils.getCalendarFirstMonthDay();
	private Calendar dateTo = CalendarUtils.getCalendarLastMonthDay();
	

	private IEntityList<DocumentGraph> documents;

	public String getGraphDot(Long documentId){
		try {
			return UtilsCrud.retrieve(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentGraph.class, documentId, null).getObject().getGraphDot();
		} catch (BusinessException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public Long getDocumentId() {return documentId;}
	public void setDocumentId(Long documentId) {this.documentId = documentId;}

	public String getAuthor() {return author;}
	public void setAuthor(String author) {this.author = author;}
	
	public Calendar getDateFrom() {return dateFrom;}
	public void setDateFrom(Calendar dateFrom) {this.dateFrom = dateFrom;}

	public Calendar getDateTo() {return dateTo;}
	public void setDateTo(Calendar dateTo) {this.dateTo = dateTo;}
	
	public boolean isFilterByDate() {return filterByDate;}
	public void setFilterByDate(boolean filterByDate) {this.filterByDate = filterByDate;}

	public IEntityList<DocumentGraph> getDocuments() {
		return documents;
	}

	public String actionStart(){
		
		return FACES_VIEW_1;
	};
	
	public void doList(){
		StringBuilder filter = new StringBuilder();
		if(this.filterByDate){
			
			filter.append(" (timeStamp BETWEEN  '" + CalendarUtils.formatToSQLDate(this.dateFrom) + "' AND '" + CalendarUtils.formatToSQLDate(this.dateTo) + "') AND");
		}

		
		if(this.author != null && !this.author.isEmpty()){
			
			filter.append(" (author like '%" + this.author + "%') AND");
		}
		
		if(this.documentId != null && this.documentId > -1){
			
			filter.append(" (documentContent =  " + this.documentId + ") AND");
		}
		
		if(filter.length()>0){
			filter.delete(filter.length()-3, filter.length());
		}
		try {
			this.documents = UtilsCrud.list(this.getApplicationBean().getProcessManager().getServiceManager(), DocumentGraph.class, filter.toString(), null);
		} catch (BusinessException e) {
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
