package de.tutorials.java.jvm.tools.jvmoa.model;

import javax.management.openmbean.CompositeData;

public class JvmOption {

	private String name;

	private String origin;

	private String value;

	private boolean writeable;

	protected JvmOption(String name) {
		this.name = name;
	}

	public static JvmOption from(CompositeData cd) {
		String name = String.valueOf(cd.get("name"));
		JvmOption o = new JvmOption(name);
		o.origin = String.valueOf(cd.get("origin"));
		o.value = String.valueOf(cd.get("value"));
		o.writeable = Boolean.parseBoolean(cd.get("writeable").toString());
		return o;
	}

	public String getName() {
		return name;
	}

	public String getOrigin() {
		return origin;
	}

	public String getValue() {
		return value;
	}

	public boolean isWriteable() {
		return writeable;
	}

	@Override
	public String toString() {
		return String.format("%s [name=%s, origin=%s, value=%s, writeable=%s]", getClass().getSimpleName(), name, origin, value, writeable);
	}
}
