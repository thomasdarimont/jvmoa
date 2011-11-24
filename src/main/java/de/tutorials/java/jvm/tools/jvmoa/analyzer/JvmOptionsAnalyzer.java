package de.tutorials.java.jvm.tools.jvmoa.analyzer;

import java.lang.management.RuntimeMXBean;

import javax.management.MBeanServerConnection;

public interface JvmOptionsAnalyzer {
	boolean supports(RuntimeMXBean runtime);

	void listCurrentJVMOptions();

	void setMBeanServerConnection(MBeanServerConnection connection);

	void setRuntimeMXBean(RuntimeMXBean runtimeMxBean);
}
