 /*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package be.virtualsushi.jfx.dorse.control.table;

 import be.virtualsushi.jfx.dorse.model.Article;
 import be.virtualsushi.jfx.dorse.model.OrderLine;
 import be.virtualsushi.jfx.dorse.model.OrderLineProperty;
 import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;
 import com.sun.javafx.logging.PlatformLogger;
 import com.sun.javafx.property.PropertyReference;
 import com.sun.javafx.scene.control.Logging;
 import javafx.beans.property.ReadOnlyObjectWrapper;
 import javafx.beans.value.ObservableValue;
 import javafx.scene.control.TableColumn.CellDataFeatures;
 import javafx.util.Callback;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 import org.springframework.stereotype.Service;

 import java.util.List;


 /**
  * A convenience implementation of the Callback interface, designed specifically
  * for use within the {@link javafx.scene.control.TableColumn}
  * {@link javafx.scene.control.TableColumn#cellValueFactoryProperty() cell value factory}. An example
  * of how to use this class is:
  *
  * <pre><code>
  * TableColumn&lt;Person,String&gt; firstNameCol = new TableColumn&lt;Person,String&gt;("First Name");
  * firstNameCol.setCellValueFactory(new PropertyValueFactory&lt;Person,String&gt;("firstName"));
  * </code></pre>
  *
  * In this example, the "firstName" string is used as a reference to an assumed
  * <code>firstNameProperty()</code> method in the <code>Person</code> class type
  * (which is the class type of the TableView
  * {@link javafx.scene.control.TableView#itemsProperty() items} list). Additionally, this method must
  * return a {@link javafx.beans.property.Property} instance. If a method meeting these requirements
  * is found, then the {@link javafx.scene.control.TableCell} is populated with this ObservableValue<T>.
  * In addition, the TableView will automatically add an observer to the
  * returned value, such that any changes fired will be observed by the TableView,
  * resulting in the cell immediately updating.
  *
  * <p>If no method matching this pattern exists, there is fall-through support
  * for attempting to call get&lt;property&gt;() or is&lt;property&gt;() (that is,
  * <code>getFirstName()</code> or <code>isFirstName()</code> in the example
  * above). If a  method matching this pattern exists, the value returned from this method
  * is wrapped in a {@link javafx.beans.property.ReadOnlyObjectWrapper} and returned to the TableCell.
  * However, in this situation, this means that the TableCell will not be able
  * to observe the ObservableValue for changes (as is the case in the first
  * approach above).
  *
  * <p>For reference (and as noted in the TableColumn
  * {@link javafx.scene.control.TableColumn#cellValueFactory cell value factory} documentation), the
  * long form of the code above would be the following:
  *
  * <pre><code>
  * TableColumn&lt;Person,String&gt; firstNameCol = new TableColumn&lt;Person,String&gt;("First Name");
  * firstNameCol.setCellValueFactory(new Callback&lt;CellDataFeatures&lt;Person, String&gt;, ObservableValue&lt;String&gt;&gt;() {
  *     public ObservableValue&lt;String&gt; call(CellDataFeatures&lt;Person, String&gt; p) {
  *         // p.getValue() returns the Person instance for a particular TableView row
  *         return p.getValue().firstNameProperty();
  *     }
  *  });
  * }
  * </code></pre>
  *
  * @see javafx.scene.control.TableColumn
  * @see javafx.scene.control.TableView
  * @see javafx.scene.control.TableCell
  * @param <S> The type of the class contained within the TableView.items list.
  * @param <T> The type of the class contained within the TableColumn cells.
  */


 public class MyRelatedPropertyValueFactory<S,T> implements Callback<CellDataFeatures<S,T>, ObservableValue<T>> {
     private String relatedProperty = null;

     private Class columnClass;
     private String previousProperty;
     private PropertyReference<T> propertyRef;

   private List<Article> articles;

     /**
      * Creates a default PropertyValueFactory to extract the value from a given
      * TableView row item reflectively, using the given property name.
      *
      * @param relatedProperty The name of the property with which to attempt to
      *      reflectively extract a corresponding value for in a given object.
      */
     public MyRelatedPropertyValueFactory(String relatedProperty, List<Article> articles) {
       this.articles = articles;
       this.relatedProperty = relatedProperty;
     }

     /** {@inheritDoc} */
     @Override public ObservableValue<T> call(CellDataFeatures<S,T> param) {
        T test = (T)param.getValue();//test is an OrdereLineProperty containing real values
         return getCellDataReflectively((T)param.getValue());
     }

     /**
      * Returns the property name provided in the constructor.
      */
     public final String getRelatedProperty() { return relatedProperty; }

     private ObservableValue<T> getCellDataReflectively(T rowData) {
       OrderLineProperty line = (OrderLineProperty)rowData;
         if (getRelatedProperty() == null || getRelatedProperty().isEmpty() || line == null) return null;

         try {
             // we attempt to cache the property reference here, as otherwise
             // performance suffers when working in large data models. For
             // a bit of reference, refer to RT-13937.
             if (columnClass == null || previousProperty == null ||
                     ! previousProperty.equals(getRelatedProperty())) {

                 // create a new PropertyReference
                 this.previousProperty = getRelatedProperty();
                 this.propertyRef = new PropertyReference<T>(Article.class, getRelatedProperty());
             }
           Long id = line.getId();
           Article a = findArticleById(line.getId());
             return propertyRef.getProperty(findArticleById(line.getId()));
         } catch (IllegalStateException e) {
             try {
                 // attempt to just get the value
                 T value = propertyRef.get(line);
                 return new ReadOnlyObjectWrapper<T>(value);
             } catch (IllegalStateException e2) {
                 // fall through to logged exception below
             }

             // log the warning and move on
             final PlatformLogger logger = Logging.getControlsLogger();
             if (logger.isLoggable(PlatformLogger.WARNING)) {
                logger.finest("Can not retrieve property '" + getRelatedProperty() +
                         "' in PropertyBindingFactory: " + this +
                         " with provided class type: " + rowData.getClass(), e);
             }
         }

         return null;
     }

   private Article findArticleById(Long articleId) {
  		for (Article article : articles) {
  			if (articleId.equals(article.getId())) {
  				return article;
  			}
  		}
  		throw new IllegalStateException("Article with id " + articleId + " doesn't exists.");
  	}
 }
