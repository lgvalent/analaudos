package br.com.valentin.analaudos.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.jsoup.Jsoup;

import br.com.orionsoft.monstrengo.crud.entity.dao.IDAO;

@Entity
@Table(name="analaudos_document_content")
public class DocumentContent {
	public static final String NAME = "name";
	public static final String CONTENT = "content";
	public static final String CATEGORY = "category";
	
	private long id = IDAO.ENTITY_UNSAVED;
	private String name;
	private String content;
	private DocumentCategory documentCategory;
	
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
	
	@Column(columnDefinition="TEXT", length=5096)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Transient
	public String getContentPlain(){
		return Jsoup.parse(this.content).text();
	}
	
	@ManyToOne
	@ForeignKey(name=CATEGORY)
	@Index(name=CATEGORY)
	public DocumentCategory getDocumentCategory() {
		return documentCategory;
	}
	public void setDocumentCategory(DocumentCategory documentCategory) {
		this.documentCategory = documentCategory;
	}
	@Override
	public String toString() {
		return this.id + ":" + this.name;
	}

}
