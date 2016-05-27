package br.com.valentin.analaudos.process;

import br.com.orionsoft.monstrengo.core.process.IProcess;
import br.com.orionsoft.monstrengo.core.process.IRunnableEntityCollectionProcessController;
import br.com.orionsoft.monstrengo.core.process.IRunnableEntityProcessController;
import br.com.orionsoft.monstrengo.core.process.ProcessException;
import br.com.orionsoft.monstrengo.core.process.RunnableProcessControllerBasic;
import br.com.orionsoft.monstrengo.crud.entity.IEntity;
import br.com.valentin.analaudos.entities.DocumentGraph;
import br.com.valentin.analaudos.entities.ResearchSettings;

/**
 * @author Lucio 20160524
 */
public class AnalaudosGraphBuilderProcessController extends RunnableProcessControllerBasic implements IRunnableEntityProcessController, IRunnableEntityCollectionProcessController {
	public static final Class<?>[] RUNNABLE_ENTITIES = {ResearchSettings.class};
	public static final Class<?>[] RUNNABLE_ENTITIES_COLLECTION = {DocumentGraph.class};

	public Class<? extends IProcess> getProcessClass() {return AnalaudosGraphBuilderProcess.class;}

	public boolean canRunWithEntity(IEntity<?> entity) throws ProcessException {
		if (entity.getInfo().getType() == ResearchSettings.class) {
			/* Nenhuma restrição */
			return true;
		}
		
		return false;
	}
	
	public Class<?>[] getRunnableEntities() {
		return RUNNABLE_ENTITIES;
	}

	@Override
	public Class<?>[] getRunnableEntitiesCollection() {
		return RUNNABLE_ENTITIES_COLLECTION;
	}

}
