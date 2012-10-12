package be.virtualsushi.jfx.dorse.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import be.virtualsushi.jfx.dorse.model.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import be.virtualsushi.jfx.dorse.model.report.OrderLineReport;
import be.virtualsushi.jfx.dorse.utils.EntityCollectionUtils;

@Service("reportSerivce")
public class ReportService {

	public static final String PDF_REPORT_FILE_NAME_PATTER = "invoice-%s.pdf";

	@Autowired
	private ResourceBundle resources;

	@Value("#{systemProperties.getProperty('user.home')}")
	private String userHome;

	public String createInvoiceReport(Invoice invoice, Address invoiceAddress, Address deliveryAddress, List<Article> articles, List<OrderLine> orderLines)
			throws ReportServiceException {
		List<OrderLineReport> reportCollection = new ArrayList<OrderLineReport>();
		for (OrderLine orderLine : orderLines) {
			OrderLineReport reportItem = new OrderLineReport();
			reportItem.setOrderLine(orderLine);
			Article article = EntityCollectionUtils.findById(articles, orderLine.getArticle());
      if(article.getName()==null)
			  reportItem.setArticleName(article.getDescription());
      else
        reportItem.setArticleName(article.getName());
			reportItem.setCode(article.getCode());
			reportItem.setPrice(article.getPrice());
			reportCollection.add(reportItem);
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("invoiceCode", invoice.getCode());
		parameters.put("invoiceId", invoice.getId());
		parameters.put("created", invoice.getCreationDate());
    parameters.put("now", new Date());
    parameters.put("goods", invoice.getShipping());
    parameters.put("tpt", invoice.getTotal()-invoice.getShipping());
    parameters.put("total", invoice.getTotal());
		parameters.put(JRParameter.REPORT_FORMAT_FACTORY, new DorseFormatFactory(resources));
    if(invoice.getCustomer()!=null){
      Customer customer = invoice.getCustomer();
      if(StringUtils.isNotBlank(customer.getName())){
        parameters.put("customer", customer.getName());
      }
      if(customer.getPerson()!=null){
        List<Person> persons = customer.getPerson();
        if(persons.size()>0){
          Person first = persons.get(0);
          if(StringUtils.isNotBlank(first.getName())){
            parameters.put("person", first.getName());
          }
        }
      }
    }
		if (invoiceAddress != null) {
			parameters.put("invoiceAddress", invoiceAddress);
		}
		if (deliveryAddress != null) {
			parameters.put("deliveryAddress", deliveryAddress);
		}

		File out = new File(createOutputFileUri(invoice.getCode()));
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(out);
			JasperPrint print = JasperFillManager.fillReport(new ClassPathResource("invoice_printout.jasper").getInputStream(), parameters, new JRBeanCollectionDataSource(
					reportCollection));
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

}
