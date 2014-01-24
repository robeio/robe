package io.robe.dto;

public class BasicPair {
	private String name;
	private String value;

	public BasicPair() {
	}

	public BasicPair(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name + "=" + value;
	}
}
