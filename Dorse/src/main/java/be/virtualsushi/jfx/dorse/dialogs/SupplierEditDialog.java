package be.virtualsushi.jfx.dorse.dialogs;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Supplier;

@Component
@FxmlFile("IdNamePairEditDialog.fxml")
public class SupplierEditDialog extends IdNamePairEditDialog<Supplier> {

}
