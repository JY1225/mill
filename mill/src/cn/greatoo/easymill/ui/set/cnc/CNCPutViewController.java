package cn.greatoo.easymill.ui.set.cnc;

import java.util.ArrayList;
import java.util.List;

import cn.greatoo.easymill.entity.Process;
import cn.greatoo.easymill.entity.Smooth;
import cn.greatoo.easymill.ui.main.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CNCPutViewController extends Controller{
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
	@FXML
	private Button beBt;
	@FXML
	private Button aftBt;
	List<Button> bts;
	Smooth loadCNCSmooth = new Smooth();
	public void init() {
		bts = new ArrayList<Button>();
		bts.add(beBt);
		bts.add(aftBt);
		isClicked(bts, beBt);
		
		loadCNCSmooth.setStep(Process.Step.LOADCNC);
		XField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1, Boolean arg2) {
	        	 
	        }
		});	
		YField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1, Boolean arg2) {
	        	
	        }
		});	
		ZField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1, Boolean arg2) {
	        	
	        }
		});	
		
	}
	
	@FXML
	public void resetBtAction(ActionEvent event) {
		
	}
	
	@FXML
	public void beBtAction(ActionEvent event) {
		isClicked(bts, beBt);
		
	}
	
	@FXML
	public void aftBtAction(ActionEvent event) {
		isClicked(bts, aftBt);
		
	}

	@Override
	public void setMessege(String mess) {
		// TODO Auto-generated method stub
		
	}
	
}
