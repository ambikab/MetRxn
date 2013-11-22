package org.code.metrxn.model.uploader;

import java.util.ArrayList;

/**
 * 
 * @author ambika_b
 *
 */
public class Rxn {

	int sboTerm;
	
	String sboTermId;
	
	String name;
	
	ArrayList<SpeciesRef> products;
	
	ArrayList<SpeciesRef> reactants;

	public Rxn() {
		super();
	}

	public Rxn(int sboTerm, String sboTermId, String name,
			ArrayList<SpeciesRef> products, ArrayList<SpeciesRef> reactants) {
		super();
		this.sboTerm = sboTerm;
		this.sboTermId = sboTermId;
		this.name = name;
		this.products = products;
		this.reactants = reactants;
	}

	public int getSboTerm() {
		return sboTerm;
	}

	public void setSboTerm(int sboTerm) {
		this.sboTerm = sboTerm;
	}

	public String getSboTermId() {
		return sboTermId;
	}

	public void setSboTermId(String sboTermId) {
		this.sboTermId = sboTermId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SpeciesRef> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<SpeciesRef> products) {
		this.products = products;
	}

	public ArrayList<SpeciesRef> getReactants() {
		return reactants;
	}

	public void setReactants(ArrayList<SpeciesRef> reactants) {
		this.reactants = reactants;
	}
	
	
	
}
