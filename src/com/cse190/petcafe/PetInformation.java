package com.cse190.petcafe;

public class PetInformation {
	private String petName;
	private String petSpecies;
	private String petGender;
	private String petAge;
	
	public PetInformation(String petName, String petSpecies, String petGender, String petAge)
	{
		this.setPetName(petName);
		this.setPetSpecies(petSpecies);
		this.setPetGender(petGender);
		this.setPetAge(petAge);
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetSpecies() {
		return petSpecies;
	}

	public void setPetSpecies(String petSpecies) {
		this.petSpecies = petSpecies;
	}

	public String getPetGender() {
		return petGender;
	}

	public void setPetGender(String petGender) {
		this.petGender = petGender;
	}

	public String getPetAge() {
		return petAge;
	}

	public void setPetAge(String petAge) {
		this.petAge = petAge;
	}
}
