package cn.greatoo.easymill.ui.set.table.unload;

import cn.greatoo.easymill.entity.Smooth;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class PlaceViewController {
	@FXML
	private GridPane gridPane;
	@FXML
	private TextField XField;
	@FXML
	private TextField YField;
	@FXML
	private TextField ZField;
	@FXML
	private Button resetBt;

	public static Smooth smooth = new Smooth();
	public void init() {
		
		XField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1, Boolean arg2) {
	        	smooth.setX(Float.parseFloat(XField.getText()));  
	        }
		});	
		YField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1, Boolean arg2) {
	        	smooth.setY(Float.parseFloat(YField.getText()));  
	        }
		});	
		ZField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1, Boolean arg2) {
	        	smooth.setZ(Float.parseFloat(ZField.getText()));  
	        }
		});	
		
	}

	@FXML
	public void resetBtAction(ActionEvent event) {
		// TODO Autogenerated
	}
	
}
