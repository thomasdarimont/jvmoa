package de.tutorials.java.jvm.tools.jvmoa.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tutorials.java.jvm.tools.jvmoa.accessor.HotspotDiagnosticJvmOptionAccessor;
import de.tutorials.java.jvm.tools.jvmoa.accessor.JvmOptionAccessor;

public class SunOracleJvmOptionsAnalyzer extends AbstractJvmOptionsAnalyzer {

	//TODO make supported option categories configurable
	// private final String supportedOptionCategories =
	// "diagnostic|develop|notproduct|product_pd|product";
	private final String supportedOptionCategories = "product";

	/**
	 * 
	 */
	private final boolean refetchJvmOptions = Boolean.getBoolean(getClass().getSimpleName() + ".refetchJvmOptions");

	/*
	 * Thank god for the invention of code conventions *g*
	 */
	private final Pattern defaultOpenJdkJvmOptionExtractorPattern = 
			Pattern.compile("\\s*(" + supportedOptionCategories + ")\\([\\w\\d]+,\\s*([\\w\\d]+)\\s*,\\s[\\w\\d]+.*\\\\");

	@SuppressWarnings("serial")
	private final Map<String, Pattern> jvmOptionSourceUrlsToOptionPatternMap = new LinkedHashMap<String, Pattern>() {
		{
			// TODO make sun / oracle jvm options configurable
			put("http://hg.openjdk.java.net/jdk7/hotspot-gc/hotspot/raw-file/162b62460264/src/share/vm/runtime/globals.hpp",
					defaultOpenJdkJvmOptionExtractorPattern);
			put("http://hg.openjdk.java.net/jdk7/build/hotspot/raw-file/cd8e33b2a8ad/src/share/vm/gc_implementation/g1/g1_globals.hpp",
					defaultOpenJdkJvmOptionExtractorPattern);
		}
	};

	public void listCurrentJVMOptions() {
		loadJvmOptionsCatalog();
		super.listCurrentJVMOptions();
	}

	public boolean supports(RuntimeMXBean runtime) {
		return runtime.getVmVendor().toLowerCase().startsWith("sun") || runtime.getVmVendor().toLowerCase().startsWith("oracle");
	}

	@Override
	protected JvmOptionAccessor getJvmOptionAccessor() {
		return new HotspotDiagnosticJvmOptionAccessor(mbeanServerConnection);
	}

	protected void loadJvmOptionsCatalog() {
		if (refetchJvmOptions) {
			fetchJvmOptions();
		} else {
			if (!tryLoadOptionsFromDisk()) {
				fetchJvmOptions();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean tryLoadOptionsFromDisk() {
		if (getCacheFile().exists()) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(getCacheFile()));
				alreadyFoundJvmOptions = (Map<String, Set<String>>) ois.readObject();
				return true;
			} catch (FileNotFoundException e) {
				System.err.println("Could not load cache file: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("Could not load cache file: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.err.println("Could not load cache file: " + e.getMessage());
			} finally {
				try {
					ois.close();
				} catch (IOException e) {
					System.err.println("Could not load cache file: " + e.getMessage());
				}
			}
		}

		return false;
	}

	private void fetchJvmOptions() {
		downloadOpenJdkVMOptionList();
		saveOptionsToDisk();
	}

	private void saveOptionsToDisk() {
		ObjectOutputStream oos = null;
		try {
			File cacheFile = getCacheFile();
			oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
			oos.writeObject(alreadyFoundJvmOptions);
			oos.flush();
		} catch (IOException e) {
			System.err.println("Could not save cache file: " + e.getMessage());
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					System.err.println("Could not save cache file: " + e.getMessage());
				}
			}
		}

	}

	private File getCacheFile() {
		return new File(System.getProperty("java.io.tmpdir"), getClass().getSimpleName() + ".cache");
	}

	private void downloadOpenJdkVMOptionList() {
		for (Map.Entry<String, Pattern> sourceUrlToOptionPatternMap : jvmOptionSourceUrlsToOptionPatternMap.entrySet()) {
			String jvmOptionSourceUrl = sourceUrlToOptionPatternMap.getKey();
			Pattern optionExtractorPattern = sourceUrlToOptionPatternMap.getValue();
			loadJvmOptionsFromSourceUrl(jvmOptionSourceUrl, alreadyFoundJvmOptions, optionExtractorPattern);
		}
	}

	private void loadJvmOptionsFromSourceUrl(String jvmOptionSourceUrl, Map<String, Set<String>> alreadyFoundJvmOptions, Pattern optionExtractorPattern) {
		System.out.println("#### Start loading options from: " + jvmOptionSourceUrl);
		Scanner scanner = null;
		try {
			scanner = new Scanner(new URL(jvmOptionSourceUrl).openStream());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Matcher matcher = optionExtractorPattern.matcher(line);
				if (matcher.matches()) {
					String optionCategory = matcher.group(1);
					String optionName = matcher.group(2);
					registerJvmOption(optionName, optionCategory, alreadyFoundJvmOptions);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		System.out.println("#### End loading options.");
	}
}