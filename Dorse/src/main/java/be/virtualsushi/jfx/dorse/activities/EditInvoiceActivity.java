package be.virtualsushi.jfx.dorse.activities;

import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.model.Invoice;

@Component
@Scope("prototype")
public class EditInvoiceActivity extends AbstractEditActivity<VBox, Invoice> {

	@Override
	protected String getEditTitleKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getNewTitleKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Invoice newEntityInstance() {
		return new Invoice();
	}

	@Override
	protected void mapFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Invoice getEditedEntity() {
		return null;
	}

}
