package org.code.metrxn.model.uploader;

/**
 * 
 * @author ambika_b
 *
 */
public class SpeciesRef {
	
	Double stoichiometry;
	
	String name;
	
	String speciesId;
	
	String id;

	public SpeciesRef() {
		super();
	}

	public SpeciesRef(Double stoichiometry, String name, String speciesId,
			String id) {
		super();
		this.stoichiometry = stoichiometry;
		this.name = name;
		this.speciesId = speciesId;
		this.id = id;
	}

	public Double getStoichiometry() {
		return stoichiometry;
	}

	public void setStoichiometry(Double stoichiometry) {
		this.stoichiometry = stoichiometry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(String speciesId) {
		this.speciesId = speciesId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
