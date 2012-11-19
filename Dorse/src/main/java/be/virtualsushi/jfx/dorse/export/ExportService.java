package be.virtualsushi.jfx.dorse.export;

import au.com.bytecode.opencsv.CSVWriter;
import be.virtualsushi.jfx.dorse.model.*;
import be.virtualsushi.jfx.dorse.report.DorseFormatFactory;
import be.virtualsushi.jfx.dorse.report.ReportServiceException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("exportService")
public class ExportService {

	@Autowired
	private ResourceBundle resources;

	@Value("#{systemProperties.getProperty('user.home')}")
	private String userHome;

  @Value("${sql.date.format}")
  private String sqlDateFormat;

  public String createCsv(ArticleResponse response, File target){


    return null;
  }

	public String createCsv(ServerResponse response, File target)
			throws ExportServiceException {
    CSVWriter writer = null;
    try {
      writer = new CSVWriter(new FileWriter(target), '\t');
    // feed in your array (or convert your data to an array)
      for (BaseEntity baseEntity : response.getData()) {
        writer.writeNext(Stringify(baseEntity));
      }
    writer.close();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }


		File out = new File(createOutputFileUri(invoice.getCode()));
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(out);
			JasperPrint print = JasperFillManager.fillReport(new ClassPathResource("invoice_printout.jasper").getInputStream(), parameters, new JRBeanCollectionDataSource(
          decoupled));
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
			exporter.exportReport();
		} catch (JRException e) {
			throw new ReportServiceException();
		} catch (IOException e) {
			throw new ReportServiceException();
		} finally {
			IOUtils.closeQuietly(outStream);
		}
		return out.getAbsolutePath();
	}

	private String createOutputFileUri(String invoiceCode) {
		return userHome + File.separator + String.format(PDF_REPORT_FILE_NAME_PATTER, invoiceCode);
	}

  private String[] stringify(Article article){
    String[] line = new String[14];
    line[0] = ""+article.getId();
    line[1] = article.getName();
    line[2] = article.getCode();
    line[3] = article.getDescription();
    line[4] = ""+article.getPrice();
    line[5] = ""+article.getFreeQuantity();
    line[6] = ""+article.getArticleType();
    line[7] = new SimpleDateFormat(sqlDateFormat).format(article.getCopyDate());
    line[8] = new SimpleDateFormat(sqlDateFormat).format(article.getCreationDate());
    line[9] = ""+article.getStock();
    line[10] = ""+article.getSupplier();
    line[11] = ""+article.getUnit();
    line[12] = ""+article.getVat();
    line[13] = ""+article.getWeight();
    return line;
  }

}
