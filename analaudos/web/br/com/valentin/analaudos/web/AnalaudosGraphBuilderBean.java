package br.com.valentin.analaudos.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.orionsoft.monstrengo.core.exception.BusinessException;
import br.com.orionsoft.monstrengo.core.process.ProcessException;
import br.com.orionsoft.monstrengo.crud.entity.IEntity;
import br.com.orionsoft.monstrengo.crud.entity.IEntityCollection;
import br.com.orionsoft.monstrengo.view.jsf.bean.BeanSessionBasic;
import br.com.orionsoft.monstrengo.view.jsf.bean.IRunnableProcessView;
import br.com.orionsoft.monstrengo.view.jsf.util.FacesUtils;
import br.com.valentin.analaudos.process.AnalaudosGraphBuilderProcess;

@ManagedBean
@SessionScoped
public class AnalaudosGraphBuilderBean extends BeanSessionBasic implements IRunnableProcessView{
	private static final long serialVersionUID = 1L;

	/** Define a view JSF que é ativada para cada view */
	public static final String VIEW_NAME = "analaudosGraphBuilderBean";
	public static final String FACES_VIEW_1 = "/pages/analaudos/graphBuilder1?faces-redirect=true";
	public static final String FACES_VIEW_2 = "/pages/analaudos/graphBuilder2?faces-redirect=true";
		
	private AnalaudosGraphBuilderProcess process = null;

	public AnalaudosGraphBuilderProcess getProcess() throws ProcessException {
		if (process == null)
			process = (AnalaudosGraphBuilderProcess) this.getApplicationBean().getProcessManager().createProcessByName(
					AnalaudosGraphBuilderProcess.PROCESS_NAME, this.getUserSessionBean().getUserSession());
		return process;
	}

	public String actionStart(){
		return FACES_VIEW_1;
	};

	public void doListDocGraph() throws BusinessException, Exception {
		if(this.getProcess().runListDocGraph()){
			FacesUtils.addInfoMsgs(this.getProcess().getMessageList());
		}else{
			FacesUtils.addErrorMsgs(this.getProcess().getMessageList());
		}
	}
	
	public String actionBuildGraph(){
		try {
			if(this.getProcess().runBuildGraph()){
				FacesUtils.addInfoMsgs(this.getProcess().getMessageList());
				return FACES_VIEW_2;
			}
			FacesUtils.addErrorMsgs(this.getProcess().getMessageList());
			return FacesUtils.FACES_VIEW_FAILURE;

		} catch (ProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
		
	}
	
	public void doCreateDocGraph() throws BusinessException, Exception {
		if(this.getProcess().runCreateDocGraph()){
			FacesUtils.addInfoMsgs(this.getProcess().getMessageList());
		}else{
			FacesUtils.addErrorMsgs(this.getProcess().getMessageList());
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

	@Override
	public String getRunnableEntityProcessName() {
		return AnalaudosGraphBuilderProcess.PROCESS_NAME;
	}

	@Override
	public String runWithEntity(IEntity<?> entity) {
		try {
			/* Limpa os atuais lançamentos do processo */
			if(this.getProcess().runWithEntity(entity))
				return FACES_VIEW_1; 

			FacesUtils.addErrorMsgs(this.getProcess().getMessageList());
			return FacesUtils.FACES_VIEW_FAILURE;
		} catch (ProcessException e) {
			FacesUtils.addErrorMsgs(e.getErrorList());
			return FacesUtils.FACES_VIEW_FAILURE;
		}
	}

	@Override
	public String runWithEntities(IEntityCollection<?> entities) {
		try {
			if(this.getProcess().runWithEntities(entities))
				return FACES_VIEW_2;
			
			FacesUtils.addErrorMsgs(this.getProcess().getMessageList());
			return FacesUtils.FACES_VIEW_FAILURE;
		} catch (ProcessException e) {
			FacesUtils.addErrorMsgs(e.getErrorList());
			return FacesUtils.FACES_VIEW_FAILURE;
		}
	}
	
	
}
