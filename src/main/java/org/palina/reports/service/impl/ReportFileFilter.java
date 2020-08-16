package org.palina.reports.service.impl;

import java.io.File;
import java.io.FileFilter;

import org.springframework.stereotype.Service;

@Service
public class ReportFileFilter implements FileFilter {

	public String getDescription() {
		return "Reports (*.sql)";
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		} else {
			String filename = f.getName().toLowerCase();
			return filename.endsWith(".sql");
		}
	}

}
