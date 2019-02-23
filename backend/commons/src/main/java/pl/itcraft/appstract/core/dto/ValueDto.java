package pl.itcraft.appstract.core.dto;

public class ValueDto<T> {

	private T value;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
