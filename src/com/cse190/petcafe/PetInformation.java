package com.cse190.petcafe;

public class PetInformation {
	private String petName;
	private String petSpecies;
	private String petBreed;
	private String petGender;
	private int petAge;
	private String petDescription;
	
	public PetInformation(String petName, String petSpecies, String petBreed, String petGender, int petAge, String petDescription)
	{
		this.setPetName(petName);
		this.setPetSpecies(petSpecies);
		this.setPetBreed(petBreed);
		this.setPetGender(petGender);
		this.setPetAge(petAge);
		this.setPetDescription(petDescription);
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

	public int getPetAge() {
		return petAge;
	}

	public void setPetAge(int petAge) {
		this.petAge = petAge;
	}

	public String getPetBreed() {
		return petBreed;
	}

	public void setPetBreed(String petBreed) {
		this.petBreed = petBreed;
	}

	public String getPetDescription() {
		return petDescription;
	}

	public void setPetDescription(String petDescription) {
		this.petDescription = petDescription;
	}
}
