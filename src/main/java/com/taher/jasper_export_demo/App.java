package com.taher.jasper_export_demo;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * Hello world!
 *
 */
public class App 
{
	public static String exportFile(String exportType, List<Employee> emploeeList) throws JRException, IOException {
		
		String base64Str;
		
		 // converting employee list into jasper data source
        // "CollectionBeanParam" is name you gave in jasper template
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(emploeeList);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("CollectionBeanParam", beanCollectionDataSource);
        
        // local jasper template
        InputStream inputStream = new FileInputStream(new File("JasperReport_A4.jrxml"));
        JasperDesign design = JRXmlLoader.load(inputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(design);
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        
//        String outputFile = "/home/i-exceed.com/taher.talikoti/JaspersoftWorkspaceV2/MyJasperProject/download.pdf";
//        FileOutputStream outputStream = new FileOutputStream(new File(outputFile));
//      JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        
        
        String fileName = new Date().toString() + "." + exportType;
        File outputFile1 = new File(fileName);
        
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setIgnoreGraphics(false);
		
		try (ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile1)) {
			
			
			if(exportType.equals("xlsx")) {
				
				JRXlsxExporter exporter = new JRXlsxExporter();
			    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportStream));
			    exporter.exportReport();
			    reportStream.writeTo(fileOutputStream);
			    base64Str = Base64.getEncoder().encodeToString(reportStream.toByteArray());
			    System.out.println(base64Str);
				
			} else {
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportStream));
			    exporter.exportReport();
			    
			    reportStream.writeTo(fileOutputStream);
			    base64Str = Base64.getEncoder().encodeToString(reportStream.toByteArray());
			    System.out.println(base64Str);
			}
		}
		System.out.println(exportType + "File generated");
		return base64Str;
	}
	
	
	// prepare and gets hard coded  employee details
	public static List<Employee> getEmployees() {
		
		// list of employees
        List<Employee> employeeList = new ArrayList<Employee>();
        
        // create employee
        Employee e1 = new Employee();
        e1.setId(1);
        e1.setFirstName("taher");
        e1.setLastName("talikoti");
        
        // create employee	
        Employee e2 = new Employee();
        e2.setId(2);
        e2.setFirstName("aban");
        e2.setLastName("talikoti");
        
        // add employees to list	
        employeeList.add(e1);
        employeeList.add(e2);
        
        return employeeList;
		
	}
	
	
    public static void main( String[] args ) throws JRException, IOException
    {
        
    	
        // get employee list details
    	List<Employee> employeeList = getEmployees();
    	
    	String exportType = "xlsx";
    	
		String base64Str = exportFile(exportType , employeeList);
		
		System.out.println("Base 64 encoded result: " + base64Str);
		
        
    }
}
