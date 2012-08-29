package be.virtualsushi.jfx.dorse.fxml;

import java.util.ResourceBundle;

import javafx.scene.Node;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

public class UiComponentBean implements IUiComponent {

	private ResourceBundle resources;

	private UiBinder uiBinder;

	private Node node;

	public ResourceBundle getResources() {
		return resources;
	}

	@Autowired
	public void setResources(ResourceBundle resources) {
		this.resources = resources;
	}

	@Override
	@PostConstruct
	public void bindUi() {
		node = uiBinder.bind(this);
	}

	public Node getNode() {
		return node;
	}

	@Override
	public Node asNode() {
		return getNode();
	}

	public UiBinder getUiBinder() {
		return uiBinder;
	}

	@Autowired
	public void setUiBinder(UiBinder uiBinder) {
		this.uiBinder = uiBinder;
	}

}
