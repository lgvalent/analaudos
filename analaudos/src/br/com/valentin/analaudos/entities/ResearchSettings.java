package br.com.valentin.analaudos.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import br.com.orionsoft.monstrengo.crud.entity.dao.IDAO;

@Entity
@Table(name="analaudos_research_settings")
public class ResearchSettings {
	public static final String NAME = "name";
	public static final String INACTIVE = "inactive";
	public static final String DOCUMENTS_CONTENTS = "documentsContents";
	
	private long id = IDAO.ENTITY_UNSAVED;
	private String name;
	private boolean inactive = false;
	private Set<DocumentContent> documentsContents;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column
	public boolean isInactive() {
		return inactive;
	}
	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	@OneToMany(targetEntity=DocumentContent.class, fetch=FetchType.EAGER)
	@JoinTable(name="analaudos_research_settings_documents",
				joinColumns={@JoinColumn(name="researchSettingsId")},
				inverseJoinColumns={@JoinColumn(name="documentContentId")}
	)
	@OrderBy(DocumentContent.NAME)
	public Set<DocumentContent> getDocumentsContents() {
		return documentsContents;
	}
	public void setDocumentsContents(Set<DocumentContent> documentsContents) {
		this.documentsContents = documentsContents;
	}

	@Override
	public String toString() {
		return this.id + ":" + this.name;
	}

}
