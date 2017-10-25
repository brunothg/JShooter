package com.github.brunothg.jshooter.utils;

/**
 * Element that can be used for Lists, ComboBoxes etc. so that the displayed
 * text is independent from the toString method.
 * 
 * @author Marvin Bruns
 *
 */
public class TitledElement<T> {

	private T element;
	private TitleCallback<T> titleCallback;

	public TitledElement(T obj, TitleCallback<T> callback) {
		setElement(obj);
		setTitleCallback(callback);
	}

	public TitledElement(T obj) {
		this(obj, null);

		if (obj instanceof TitleCallback) {
			try {
				@SuppressWarnings("unchecked")
				TitleCallback<T> titleCallback = (TitleCallback<T>) obj;
				setTitleCallback(titleCallback);
			} catch (Exception e) {
			}
		}
	}

	public TitledElement() {
		this(null, null);
	}

	public T getElement() {
		return element;
	}

	public void setElement(T element) {
		this.element = element;
	}

	public TitleCallback<T> getTitleCallback() {
		return titleCallback;
	}

	public void setTitleCallback(TitleCallback<T> titleCallback) {
		this.titleCallback = titleCallback;
	}

	@Override
	public String toString() {
		T element = getElement();
		TitleCallback<T> callback = getTitleCallback();
		return (element != null) ? ((callback != null) ? callback.getTitle(element) : element.toString()) : null;
	}

	@Override
	public int hashCode() {
		T element = getElement();
		return (element == null) ? 0 : element.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		T element = getElement();

		if (this == obj)
			return true;
		if (obj == null && element != null)
			return false;
		if (element.getClass() != obj.getClass())
			return false;
		if (!element.equals(obj))
			return false;
		return true;
	}

	public static interface TitleCallback<T> {
		public String getTitle(T element);
	}

}
