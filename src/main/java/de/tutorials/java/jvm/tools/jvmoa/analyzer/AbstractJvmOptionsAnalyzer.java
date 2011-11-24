package de.tutorials.java.jvm.tools.jvmoa.analyzer;

import java.lang.management.RuntimeMXBean;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;

import de.tutorials.java.jvm.tools.jvmoa.accessor.JvmOptionAccessor;
import de.tutorials.java.jvm.tools.jvmoa.model.JvmOption;

public abstract class AbstractJvmOptionsAnalyzer implements JvmOptionsAnalyzer {

	protected Map<String, Set<String>> alreadyFoundJvmOptions = new TreeMap<String, Set<String>>();

	protected MBeanServerConnection mbeanServerConnection;

	protected RuntimeMXBean runtimeMXBean;

	protected abstract JvmOptionAccessor getJvmOptionAccessor();

	public void listCurrentJVMOptions() {
		printCurrentJVMVersion();
		printJvmOptionSummary();
		printCurrentJVMOptions();
	}

	protected void printCurrentJVMVersion() {
		// ugly ugly ugly JMX Spec...
		String javaVersionString = String.valueOf(((javax.management.openmbean.TabularDataSupport) (Object) runtimeMXBean
				.getSystemProperties()).get(new Object[] { "java.version" }).get("value"));
		String vmVersion = runtimeMXBean.getVmVersion();
		String vmName = runtimeMXBean.getVmName();
		String vmVendor = runtimeMXBean.getVmVendor();
		System.out.printf("%s %s %s %s", javaVersionString, vmVersion, vmName, vmVendor).println();
	}

	protected void printCurrentJVMOptions() {
		JvmOptionAccessor joa = getJvmOptionAccessor();
		for (Map.Entry<String, Set<String>> options : alreadyFoundJvmOptions.entrySet()) {
			System.out.println("#### Start JVM Options Listing");
			for (String optionName : options.getValue()) {
				JvmOption vmOption = joa.getVMOption(optionName);
				System.out.println(vmOption);
			}
			System.out.println("#### End JVM Options Listing");
		}
	}

	protected void printJvmOptionSummary() {
		System.out.println("##### Start JVM Options Summary");
		for (String optionCategory : alreadyFoundJvmOptions.keySet()) {
			System.out.printf("Jvm Option in Category %s: %s", optionCategory, alreadyFoundJvmOptions.get(optionCategory).size()).println();
		}
		System.out.println("##### End JVM Options Summary");

	}

	protected void registerJvmOption(String optionName, String optionCategory, Map<String, Set<String>> alreadyFoundJvmOptions) {
		Set<String> optionNames = alreadyFoundJvmOptions.get(optionCategory);
		if (optionNames == null) {
			optionNames = new TreeSet<String>();
			alreadyFoundJvmOptions.put(optionCategory, optionNames);
		}
		optionNames.add(optionName);
	}

	public void setMBeanServerConnection(MBeanServerConnection connection) {
		this.mbeanServerConnection = connection;
	}

	public void setRuntimeMXBean(RuntimeMXBean runtimeMXBean) {
		this.runtimeMXBean = runtimeMXBean;
	}
}
