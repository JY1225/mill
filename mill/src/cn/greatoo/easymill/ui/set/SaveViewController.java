package cn.greatoo.easymill.ui.set;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import cn.greatoo.easymill.db.util.DBHandler;
import cn.greatoo.easymill.db.util.Programhandler;
import cn.greatoo.easymill.entity.Program;
import cn.greatoo.easymill.process.DuplicateProcessFlowNameException;
import cn.greatoo.easymill.ui.general.NotificationBox;
import cn.greatoo.easymill.ui.main.Controller;
import cn.greatoo.easymill.util.TextInputControlListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class SaveViewController extends Controller {
	@FXML
	private GridPane saveGridPane;
	@FXML
	private Button saveBt;
	@FXML
	private TextField fulltxtName;

	public void init(GeneralViewController generalViewController) {
		buildAlarmHBox(saveGridPane, 0, 0, 3, 1);
		fulltxtName.setText(generalViewController.getFulltxtName().getText());
		Program program = DBHandler.getInstance().getProgramBuffer().get(DBHandler.getInstance().getProgramName());
		if (!isConfig(program)) {
			showNotification("配置未完成", NotificationBox.Type.WARNING);
		} else {
			hideNotification();
		}
	}

	@FXML
	public void save(MouseEvent event) throws DuplicateProcessFlowNameException {
		String programName = fulltxtName.getText();
		Program program = DBHandler.getInstance().getProgramBuffer().get(DBHandler.getInstance().getProgramName());
		if (!isConfig(program)) {
			showNotification("配置未完成", NotificationBox.Type.WARNING);
		} else if (programName == null || programName.endsWith("null") || "".equals(programName.trim())) {
			showNotification("名称错误", NotificationBox.Type.WARNING);
		} else {
			saveProgram(program);
		}
	}

	private void saveProgram(Program program) {
		String programName = fulltxtName.getText();
		Timestamp creatTime = new Timestamp(System.currentTimeMillis());
		program.setName(programName);
		program.setTimeCreate(creatTime);
		program.setTimeLastOpen(null);
		program.setHasTeach(false);
		Map<String, Program> map = DBHandler.getInstance().getProgramBuffer();
		boolean isOProgram = false;
		for (Program p : map.values()) {
			if (programName.equals(p.getName())) {
				program.setId(p.getId());
				program.getCncSetting().setId(p.getCncSetting().getId());
				program.getRobotSetting().setId(p.getRobotSetting().getId());
				program.getUnloadstacker().setId(p.getUnloadstacker().getId());
				program.getLoadCNC().setId(p.getLoadCNC().getId());
				program.getUnloadCNC().setId(p.getUnloadCNC().getId());
				program.getLoadstacker().setId(p.getLoadstacker().getId());
				program.getRawWorkPiece().setId(p.getRawWorkPiece().getId());
				program.getFinishedWorkPiece().setId(p.getFinishedWorkPiece().getId());
				isOProgram = true;
			}
		}
		if (!isOProgram) {
			program.setId(0);
		}

		try {
			Programhandler.saveProgram(program);
			Programhandler.getProgram();
			if (!isOProgram) {
				showNotification("保存成功", NotificationBox.Type.OK);
			} else {
				showNotification("更新成功", NotificationBox.Type.OK);
			}
		} catch (SQLException e) {
			if (!isOProgram) {
				showNotification("保存失败", NotificationBox.Type.OK);
			} else {
				showNotification("更新失败", NotificationBox.Type.OK);
			}
		}
	}

	private boolean isConfig(Program program) {
		if (program.getUnloadstacker().getGripper().getName() == null
				|| program.getUnloadCNC().getGripper().getName() == null 
				|| program.getRawWorkPiece().getLength() <= 0
				|| program.getRawWorkPiece().getWidth() <= 0 
				|| program.getRawWorkPiece().getHeight() <= 0
				|| program.getRawWorkPiece().getLength() < program.getRawWorkPiece().getWidth()
				|| program.getRawWorkPiece().getWeight() < 0 
				|| program.getAmount() <= 0 
				|| program.getLayers() <= 0
				|| program.getStudHeight_Workpiece() < 0

				|| program.getFinishedWorkPiece().getLength() <= 0 
				|| program.getFinishedWorkPiece().getWidth() <= 0
				|| program.getFinishedWorkPiece().getHeight() <= 0
				|| program.getFinishedWorkPiece().getLength() > program.getRawWorkPiece().getLength()
				|| program.getFinishedWorkPiece().getWidth() > program.getRawWorkPiece().getWidth()
				|| program.getFinishedWorkPiece().getHeight() > program.getRawWorkPiece().getHeight()) {
			return false;
		} else {
			return true;
		}
	}

	public TextField getFulltxtName() {
		return fulltxtName;
	}

	public void setFulltxtName(TextField fulltxtName) {
		this.fulltxtName = fulltxtName;
	}

	@Override
	public void setMessege(String mess) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTextFieldListener(TextInputControlListener listener) {
		// TODO Auto-generated method stub

	}
}
