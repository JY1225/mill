package cn.greatoo.easymill.ui.set.cnc;

import cn.greatoo.easymill.db.util.DBHandler;
import cn.greatoo.easymill.entity.WorkPiece;
import cn.greatoo.easymill.ui.general.NotificationBox;
import cn.greatoo.easymill.ui.main.Controller;
import cn.greatoo.easymill.util.NumericTextField;
import cn.greatoo.easymill.util.TextInputControlListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class CNCFinishedWPViewController extends Controller {
	@FXML
	private GridPane generalGridPane;
	@FXML
	private NumericTextField fullnumL;
	@FXML
	private NumericTextField fullnumW;
	@FXML
	private NumericTextField fullnumH;
	@FXML
	private Button hResetBt;
	@FXML
	private NumericTextField fullnumWT;
	@FXML
	private Button calculateBt;
	@FXML
	private Button resetBt;
	@FXML
	private Button wResetBt;
	@FXML
	private Button LResetBt;
	private String programName;

	public void init() {
		refresh();
		setTextFieldListener(this);
		buildAlarmHBox(generalGridPane, 0, 0, 2, 1);
		programName = DBHandler.getInstance().getProgramName();
		if (programName != null) {
			fullnumL.setText(String.valueOf(
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece().getLength()));
			fullnumW.setText(String.valueOf(
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece().getWidth()));
			fullnumH.setText(String.valueOf(
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece().getHeight()));
			fullnumWT.setText(String.valueOf(
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece().getWeight()));
		} else {
			fullnumL.setText(String.valueOf(
					DBHandler.getInstance().getProgramBuffer().get(programName).getRawWorkPiece().getLength()));
			fullnumW.setText(String
					.valueOf(DBHandler.getInstance().getProgramBuffer().get(programName).getRawWorkPiece().getWidth()));
			fullnumH.setText(String.valueOf(
					DBHandler.getInstance().getProgramBuffer().get(programName).getRawWorkPiece().getHeight()));
			fullnumWT.setText(String.valueOf(
					DBHandler.getInstance().getProgramBuffer().get(programName).getRawWorkPiece().getWeight()));
			DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
					.setLength(Float.valueOf(fullnumL.getText()));
			DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
					.setWidth(Float.valueOf(fullnumW.getText()));
			DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
					.setHeight(Float.valueOf(fullnumH.getText()));
			DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
					.setWeight(Float.valueOf(fullnumWT.getText()));
		}
		DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
				.setType(WorkPiece.Type.FINISHED);
		DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
				.setShape(WorkPiece.WorkPieceShape.CUBIC);
		DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece().setMaterial(
				DBHandler.getInstance().getProgramBuffer().get(programName).getRawWorkPiece().getMaterial());

		fullnumL.setOnChange(new ChangeListener<Float>() {
			@Override
			public void changed(final ObservableValue<? extends Float> observable, final Float oldValue,
					final Float newValue) {
				notification();
				if (newValue > 0 && newValue <= DBHandler.getInstance().getProgramBuffer().get(programName)
						.getRawWorkPiece().getLength()) {
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
							.setLength(newValue);
				}
			}
		});

		fullnumW.setOnChange(new ChangeListener<Float>() {
			@Override
			public void changed(final ObservableValue<? extends Float> observable, final Float oldValue,
					final Float newValue) {
				notification();
				if (newValue > 0 && newValue <= DBHandler.getInstance().getProgramBuffer().get(programName)
						.getRawWorkPiece().getWidth()) {
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
							.setWidth(newValue);
				}
			}
		});

		fullnumH.setOnChange(new ChangeListener<Float>() {
			@Override
			public void changed(final ObservableValue<? extends Float> observable, final Float oldValue,
					final Float newValue) {
				notification();
				if (newValue > 0 && newValue <= DBHandler.getInstance().getProgramBuffer().get(programName)
						.getRawWorkPiece().getHeight()) {
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
							.setHeight(newValue);
				}
			}
		});

		fullnumWT.setOnChange(new ChangeListener<Float>() {
			@Override
			public void changed(final ObservableValue<? extends Float> observable, final Float oldValue,
					final Float newValue) {
				notification();
				if (newValue > 0 && newValue <= DBHandler.getInstance().getProgramBuffer().get(programName)
						.getRawWorkPiece().getWeight()) {
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
							.setWeight(newValue);
				}
			}
		});

	}

	private void notification() {
		if (Float.valueOf(fullnumWT.getText()) > DBHandler.getInstance().getProgramBuffer().get(programName)
				.getRawWorkPiece().getWeight()
				|| Float.valueOf(fullnumL.getText()) > DBHandler.getInstance().getProgramBuffer().get(programName)
						.getRawWorkPiece().getLength()
				|| Float.valueOf(fullnumW.getText()) > DBHandler.getInstance().getProgramBuffer().get(programName)
						.getRawWorkPiece().getWidth()
				|| Float.valueOf(fullnumH.getText()) > DBHandler.getInstance().getProgramBuffer().get(programName)
						.getRawWorkPiece().getHeight()) {

			showNotification("已加工工件不得大于待加工工件", NotificationBox.Type.WARNING);

		} else if (Float.valueOf(fullnumWT.getText()) <= 0 || Float.valueOf(fullnumL.getText()) <= 0
				|| Float.valueOf(fullnumW.getText()) <= 0 || Float.valueOf(fullnumH.getText()) <= 0) {

			showNotification("数据应该大于0", NotificationBox.Type.WARNING);
		} else {
			hideNotification();
		}
	}

	private void refresh() {
		fullnumL.setText("");
		fullnumW.setText("");
		fullnumH.setText("");
		fullnumWT.setText("");
	}

	@FXML
	public void calculateBtAction(ActionEvent event) {
		notification();
		if (Float.valueOf(fullnumL.getText()) > 0 && Float.valueOf(fullnumW.getText()) > 0
				&& Float.valueOf(fullnumH.getText()) > 0) 
		{
			DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece().calculateWeight();
			fullnumWT.setText(String.valueOf(
					DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece().getWeight()));
			DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
					.setWeight(Float.parseFloat(fullnumWT.getText()));
			DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
					.setWeight(Float.parseFloat(fullnumWT.getText()));
		}
	}

	@FXML
	public void LResetBtAction(ActionEvent event) {
		notification();
		fullnumL.setText(String.valueOf(DBHandler.getInstance().getOProgram().getFinishedWorkPiece().getLength()));
		DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
				.setLength(DBHandler.getInstance().getOProgram().getFinishedWorkPiece().getLength());
	}

	@FXML
	public void wResetBtAction(ActionEvent event) {
		notification();
		fullnumW.setText(String.valueOf(DBHandler.getInstance().getOProgram().getFinishedWorkPiece().getWidth()));
		DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
				.setWidth(DBHandler.getInstance().getOProgram().getFinishedWorkPiece().getWidth());
	}

	@FXML
	public void hResetBtAction(ActionEvent event) {
		notification();
		fullnumH.setText(String.valueOf(DBHandler.getInstance().getOProgram().getFinishedWorkPiece().getHeight()));
		DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
				.setHeight(DBHandler.getInstance().getOProgram().getFinishedWorkPiece().getHeight());
	}

	@FXML
	public void resetBtAction(ActionEvent event) {
		notification();
		fullnumWT.setText(String.valueOf(DBHandler.getInstance().getOProgram().getFinishedWorkPiece().getWeight()));
		DBHandler.getInstance().getProgramBuffer().get(programName).getFinishedWorkPiece()
				.setWeight(DBHandler.getInstance().getOProgram().getFinishedWorkPiece().getWeight());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTextFieldListener(final TextInputControlListener listener) {
		fullnumL.setFocusListener(listener);
		fullnumW.setFocusListener(listener);
		fullnumH.setFocusListener(listener);
		fullnumWT.setFocusListener(listener);
	}

	@Override
	public void setMessege(String mess) {
		// TODO Auto-generated method stub

	}

}
