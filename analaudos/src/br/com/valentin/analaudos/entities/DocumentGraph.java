package br.com.valentin.analaudos.entities;

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
	public static final String AUTHOR = "author";
	public static final String DOCUMENT_CONTENT = "documentContent";
	public static final String GRAPH_DOT= "graphDot";
	public static final String GRAPH_JSON= "graphJson";
	public static final String TIME_STAMP = "timeStamp";
	public static final String GRADUATION_YEAR = "graduationYear";
	public static final String RESIDENCE_YEAR = "residenceYear";
	public static final String SPECIALIST_YEAR = "specialistYear";
	public static final String MASTER_YEAR = "masterYear";
	public static final String DOCTOR_YEAR = "doctorYear";
	
	private long id = IDAO.ENTITY_UNSAVED;
	private String author;
	private Integer graduationYear;
	private Integer residenceYear;
	private Integer specialistYear;
	private Integer masterYear;
	private Integer doctorYear;
	private DocumentContent documentContent;
	private String graphDot;
	private String graphJson;
	private String actions;
	private String suggestions;
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
	
	@Column(length=2048, columnDefinition="TEXT")
	public String getGraphDot() {
		return graphDot;
	}
	public void setGraphDot(String source) {
		this.graphDot = source;
	}
	
	@Column(length=2048, columnDefinition="TEXT")
	public String getGraphJson() {
		return graphJson;
	}
	public void setGraphJson(String graphJson) {
		this.graphJson = graphJson;
	}

	@Column(length=2048, columnDefinition="TEXT")
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	
	@Column(length=2048, columnDefinition="TEXT")
	public String getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}
	
	@Transient
	public String getGraphDotEncoded() {
		try {
			return URLEncoder.encode(this.graphDot, "UTF-8");
		} catch (Exception e) {
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
	
	public Integer getGraduationYear() {
		return graduationYear;
	}
	public void setGraduationYear(Integer graduationYear) {
		this.graduationYear = graduationYear;
	}
	public Integer getResidenceYear() {
		return residenceYear;
	}
	public void setResidenceYear(Integer residenceYear) {
		this.residenceYear = residenceYear;
	}
	public Integer getSpecialistYear() {
		return specialistYear;
	}
	public void setSpecialistYear(Integer specialistYear) {
		this.specialistYear = specialistYear;
	}
	public Integer getMasterYear() {
		return masterYear;
	}
	public void setMasterYear(Integer masterYear) {
		this.masterYear = masterYear;
	}
	public Integer getDoctorYear() {
		return doctorYear;
	}
	public void setDoctorYear(Integer doctorYear) {
		this.doctorYear = doctorYear;
	}
	@Override
	public String toString() {
		return this.author + "(" + this.id + ")";
	}

}
