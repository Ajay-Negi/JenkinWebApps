package com.dmi.dao.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vhr_history database table.
 * 
 */
@Entity
@Table(name="vhr_history")
@NamedQuery(name="VhrHistory.findAll", query="SELECT v FROM VhrHistory v")
public class VhrHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VhrHistoryPK vhrHistoryPK;

	@Lob
	private byte[] content;

	public VhrHistory() {
	}

	public VhrHistoryPK getId() {
		return this.vhrHistoryPK;
	}

	public void setId(VhrHistoryPK vhrHistoryPK) {
		this.vhrHistoryPK = vhrHistoryPK;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}