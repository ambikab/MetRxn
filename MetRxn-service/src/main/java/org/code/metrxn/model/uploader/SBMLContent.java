package org.code.metrxn.model.uploader;

import java.util.ArrayList;

/**
 * 
 * @author ambika_b
 *
 */
public class SBMLContent {

	int rxnCount;
	
	int metaboliteCnt;
	
	int comparatmentType;
	
	int version;
	
	ArrayList<Rxn> rxns;
	
	public SBMLContent() {
		super();
	}
	
	public SBMLContent(int rxnCount, int metaboliteCnt, int comparatmentType, int version) {
		super();
		this.rxnCount = rxnCount;
		this.metaboliteCnt = metaboliteCnt;
		this.comparatmentType = comparatmentType;
		this.version = version;
	}
	
	public SBMLContent(int rxnCount, int metaboliteCnt, int comparatmentType,
			int version, ArrayList<Rxn> rxns) {
		super();
		this.rxnCount = rxnCount;
		this.metaboliteCnt = metaboliteCnt;
		this.comparatmentType = comparatmentType;
		this.version = version;
		this.rxns = rxns;
	}

	public int getRxnCount() {
		return rxnCount;
	}

	public void setRxnCount(int rxnCount) {
		this.rxnCount = rxnCount;
	}

	public int getMetaboliteCnt() {
		return metaboliteCnt;
	}

	public void setMetaboliteCnt(int metaboliteCnt) {
		this.metaboliteCnt = metaboliteCnt;
	}

	public int getComparatmentType() {
		return comparatmentType;
	}

	public void setComparatmentType(int comparatmentType) {
		this.comparatmentType = comparatmentType;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public ArrayList<Rxn> getRxns() {
		return rxns;
	}

	public void setRxns(ArrayList<Rxn> rxns) {
		this.rxns = rxns;
	}
}