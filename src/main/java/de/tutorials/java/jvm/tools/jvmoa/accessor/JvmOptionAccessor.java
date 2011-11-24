package de.tutorials.java.jvm.tools.jvmoa.accessor;

import de.tutorials.java.jvm.tools.jvmoa.model.JvmOption;

/**
 * The {@code JvmOptionAccessor} interface should be implemented by any class
 * whose instances provide access to options of the current jvm.
 */
public interface JvmOptionAccessor {

	/**
	 * Access a JVM Option
	 * 
	 * @param optionName
	 * @return an instance of {@code JvmOption} of the JVM supports a JVM Option
	 *         for the given name. Otherwise an instance of
	 *         {@code UnknownJvmOption} is returned
	 */
	JvmOption getVMOption(String optionName);
}
