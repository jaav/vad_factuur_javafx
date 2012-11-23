package be.virtualsushi.jfx.dorse.export;

import au.com.bytecode.opencsv.CSVWriter;
import be.virtualsushi.jfx.dorse.model.*;
import be.virtualsushi.jfx.dorse.report.DorseFormatFactory;
import be.virtualsushi.jfx.dorse.report.ReportServiceException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
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

  public String createCsv(ArticleResponse response, File target, List<Supplier> suppliers){
    CSVWriter writer = null;
    try {
      writer = new CSVWriter(new FileWriter(target), '\t');
      writer.writeNext(getArticleHeader());
    // feed in your array (or convert your data to an array)
      for (Article baseEntity : response.getData()) {
        writer.writeNext(stringify(baseEntity, suppliers));
      }
    writer.close();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return target.getAbsolutePath();
  }

  public String createCsv(CustomerResponse response, File target, List<Sector> sectors){
    CSVWriter writer = null;
    try {
      writer = new CSVWriter(new FileWriter(target), '\t');
      writer.writeNext(getCustomerHeader());
    // feed in your array (or convert your data to an array)
      for (Customer baseEntity : response.getData()) {
        writer.writeNext(stringify(baseEntity, sectors));
      }
    writer.close();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return target.getAbsolutePath();
  }

  public String createCsv(InvoiceResponse response, File target){
    CSVWriter writer = null;
    try {
      writer = new CSVWriter(new FileWriter(target), '\t');
      writer.writeNext(getInvoiceHeader());
    // feed in your array (or convert your data to an array)
      for (Invoice baseEntity : response.getData()) {
        writer.writeNext(stringify(baseEntity));
      }
    writer.close();
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return target.getAbsolutePath();
  }

  private String[] getInvoiceHeader(){
    String[] header = {"Id", "Customer", "Code", "Remark", "Shipping", "Vat",
        "Total", "CreationDate", "DeliveryDate", "PaidDate", "Weight",
        "Status", "Creator", };
    return header;
  }

  private String[] stringify(Invoice invoice){
    String[] line = new String[13];
    line[0] = ""+invoice.getId();
    line[1] = invoice.getCustomer().getName();
    line[2] = invoice.getCode();
    line[3] = invoice.getRemark();
    line[4] = ""+invoice.getShipping();
    line[5] = ""+invoice.getVat();
    line[6] = ""+invoice.getTotal();
    if(invoice.getCreationDate()!=null) line[7] = new SimpleDateFormat(sqlDateFormat).format(invoice.getCreationDate());
    else line[7] = "";
    if(invoice.getDeliveryDate()!=null) line[8] = new SimpleDateFormat(sqlDateFormat).format(invoice.getDeliveryDate());
    else line[8] = "";
    if(invoice.getPaidDate()!=null) line[9] = new SimpleDateFormat(sqlDateFormat).format(invoice.getPaidDate());
    else line[9] = "";
    line[10] = ""+invoice.getWeight();
    line[11] = ""+invoice.getStatus();
    line[12] = ""+invoice.getCreator();
    return line;
  }


  private String[] getCustomerHeader(){
    String[] header = {"Id", "Name", "Vat", "Iban", "Remark", "Sector", "Subsector"};
    return header;
  }

  private String[] stringify(Customer customer, List<Sector> sectors){
    String[] line = new String[7];
    line[0] = ""+customer.getId();
    line[1] = customer.getName();
    line[2] = customer.getVat();
    line[3] = customer.getIban();
    line[4] = customer.getRemark();
    if(customer.getSector()!=null){
      int index = sectors.indexOf(new Sector(customer.getSector()));
      if(index>=0)
        line[5] = sectors.get(index).getName();
      else
        line[5] = "UNKNOWN";
    }
    else line[5] = "";
    if(customer.getSubsector()!=null){
      int index = sectors.indexOf(new Sector(customer.getSubsector()));
      if(index>=0)
        line[6] = sectors.get(index).getName();
      else
        line[5] = "UNKNOWN";
    }
    else line[6] = "";
    return line;
  }

  private String[] getArticleHeader(){
    String[] header = {"Id", "Name", "Code", "Description", "Price", "Free quantity",
        "Type", "Copyright date", "Creation date", "Stock", "Supplier",
        "Unit", "Vat", "Weight"};
    return header;
  }

  private String[] stringify(Article article, List<Supplier> suppliers){
    String[] line = new String[14];
    line[0] = ""+article.getId();
    line[1] = article.getArticleName();
    line[2] = article.getCode();
    line[3] = article.getDescription();
    line[4] = ""+article.getPrice();
    line[5] = ""+article.getFreeQuantity();
    line[6] = ""+article.getArticleType();
    if(article.getCopyDate()!=null) line[7] = new SimpleDateFormat(sqlDateFormat).format(article.getCopyDate());
    else line[7] = "";
    if(article.getCreationDate()!=null) line[8] = new SimpleDateFormat(sqlDateFormat).format(article.getCreationDate());
    else line[8] = "";
    line[9] = ""+article.getStock().getQuantity();
    if(article.getSupplier()!=null) line[10] = suppliers.get(suppliers.indexOf(new Supplier(article.getSupplier()))).getName();
    else line[10] = "";
    line[11] = ""+article.getUnit();
    line[12] = ""+article.getVat();
    line[13] = ""+article.getWeight();
    return line;
  }

}
