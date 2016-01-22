package br.com.valentin.analaudos.entities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import br.com.orionsoft.monstrengo.crud.entity.dao.IDAO;

@Entity
@Table(name="analaudos_document_graph")
public class DocumentGraph {
	public static final String AUTHOR = "autor";
	public static final String DOCUMENT_CONTENT = "documentContent";
	public static final String SOURCE = "source";
	public static final String TIME_STAMP = "timeStamp";
	
	private long id = IDAO.ENTITY_UNSAVED;
	private String author;
	private DocumentContent documentContent;
	private String graphDot;
	private String actions;
	private Calendar timeStamp = Calendar.getInstance();
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(length=50)
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@ManyToOne
	@ForeignKey(name=DOCUMENT_CONTENT)
	@Index(name=DOCUMENT_CONTENT)
	public DocumentContent getDocumentContent() {
		return documentContent;
	}
	public void setDocumentContent(DocumentContent documentContent) {
		this.documentContent = documentContent;
	}
	
	@Column(length=1024, columnDefinition="TEXT")
	public String getGraphDot() {
		return graphDot;
	}
	public void setGraphDot(String source) {
		this.graphDot = source;
	}
	@Column(length=1024, columnDefinition="TEXT")
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}

	@Transient
	public String getSourceEncoded() {
		try {
			return URLEncoder.encode(this.graphDot, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "ERROR: " + e.getMessage();
		}
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Calendar timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		return this.author + "(" + this.id + ")";
	}

}
