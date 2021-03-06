package cn.greatoo.easymill.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.greatoo.easymill.entity.Coordinates;
import cn.greatoo.easymill.entity.Gripper;
import cn.greatoo.easymill.entity.GripperHead;
import cn.greatoo.easymill.entity.Smooth;
import cn.greatoo.easymill.entity.Step;
import cn.greatoo.easymill.entity.WorkPiece;

public class Stephandler {
	
	static Connection conn = DBHandler.getInstance().getConnection();
	
	public static void saveProgramStep(Step step) throws SQLException {
		if(step.getId() <= 0) {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO STEP(GRIPPERHEAD,GRIPPER, USERFRAME,SMOOTH,OFFSET) VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, step.getGripperHead().getId());
			stmt.setInt(2, step.getGripper().getId());
			stmt.setInt(3, step.getUserFrame());
			stmt.setInt(4, step.getSmooth().getId());			
			stmt.setInt(5, step.getOffset().getId());
			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			if ((keys != null) && (keys.next())) {
				step.setId(keys.getInt(1));
			}

		}else {
			PreparedStatement stmt = conn.prepareStatement("UPDATE STEP SET GRIPPERHEAD = ?, GRIPPER = ?, USERFRAME = ?, SMOOTH = ?, OFFSET = ? WHERE ID = ?");
			stmt.setInt(1, step.getGripperHead().getId());
			stmt.setInt(2, step.getGripper().getId());
			stmt.setInt(3, step.getUserFrame());
			stmt.setInt(4, step.getSmooth().getId());			
			stmt.setInt(5, step.getOffset().getId());
            stmt.setInt(6, step.getId());
            stmt.executeUpdate();
		}
	}
		
	public static Step getProgramStepsById(final int stepId) throws SQLException {
		Step step =null;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM STEP WHERE ID = ?");
        stmt.setInt(1, stepId);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
        	int gripperHeadId = results.getInt("GRIPPERHEAD");       	 
            GripperHead gripperHead = GripperHeadHandle.getGripperHeadById(gripperHeadId);
                         
        	int GripperId = results.getInt("GRIPPER");
        	Gripper gripper =Gripperhandler.getGripperById(GripperId);  
            
            int UserFrameId = results.getInt("USERFRAME");
            
            int SmoothId = results.getInt("SMOOTH");         
            Smooth smooth = SmoothHandler.getSmoothById(SmoothId);
                  
            
            int OffSetId = results.getInt("OFFSET");
            Coordinates offSet = CoordinatesHandler.getCoordinatesById(OffSetId);
            
            step = new Step(gripperHead,gripper, UserFrameId, smooth, offSet);
            step.setId(stepId);
        }
        return step;          
	}
     

}
