package com.base.DaoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.base.Dao.TeacherDao;
import com.base.Po.teacherList;
import com.base.Po.teachers;
import com.base.utils.BaseUtils;
import com.base.utils.SqlConnectionUtils;


@Repository("TeacherDao")
public class TeacherDaoImpl implements TeacherDao{
	 @Autowired
	 private SessionFactory sessionFactory;

	 //增加教师
	 @Override
	 public int addTeacher(String teacherId,String teacherName,String sex,String Areason,String password) {
		 	int flag=0;
			Connection conn = null;
			PreparedStatement ps=null;
			ResultSet rs = null;
			CallableStatement sp = null;
			try {
			    conn = (Connection) SessionFactoryUtils.getDataSource(
				    sessionFactory).getConnection();
			    sp = (CallableStatement) conn
						.prepareCall("{CALL innovatespoc.check_teacher(?,?,?,?,?,?)}");
				sp.setString(1, teacherId);
				sp.setString(2, teacherName);
				sp.setString(3, sex);
				sp.setString(4, Areason);
				sp.setString(5, password);
				sp.execute();
			    flag = sp.getInt(6);
			} catch (SQLException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} finally {
			    SqlConnectionUtils.free(conn, ps, rs);
			}
			return flag;
		}
	
	 
	// 删除教师
	@Override
	public String deleteTeacher(String str) {
		int flag = 0;
		String message = null;
		Connection conn = null;
		CallableStatement sp = null;
		try {
			conn = (Connection) SessionFactoryUtils.getDataSource(
					sessionFactory).getConnection();
			sp = (CallableStatement) conn
					.prepareCall("{CALL innovatespoc.delete_teacher(?,?)}");
			sp.setString(1, str);
			sp.execute();
			flag = sp.getInt(2); 
			message=BaseUtils.getException(flag);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SqlConnectionUtils.free(conn, sp, null);
		}
		return message;
	}
	
	//修改教师信息
	@Override
	public void updateTeacher(String teacherName,String Areason,String password) {
		Connection conn = null;
		PreparedStatement ps=null;
		ResultSet rs = null;		
		try {
		    conn = (Connection) SessionFactoryUtils.getDataSource(
			    sessionFactory).getConnection();
		    String sql =("update teacher set mname=? where mid=?");
		    ps = conn.prepareStatement(sql);
		    ps.setString(1, teacherName);
		    ps.setString(2, Areason);
		    ps.setString(2, password);
		    ps.executeUpdate();
		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} finally {
		    SqlConnectionUtils.free(conn, ps, rs);
		}
	}
	
	// 分页展示教师信息
	@Override
	public teacherList query_teacher(Integer size, Integer pageindex,
			String columnName, String orderDir, String searchValue) {
		// TODO Auto-generated method stub
		int recordsTotal = 0;
		Connection conn = null;
		CallableStatement sp = null;
		ResultSet rs = null;
		List<teachers> list = new ArrayList<teachers>();
		try {
			conn = (Connection) SessionFactoryUtils.getDataSource(
					sessionFactory).getConnection();
			sp = (CallableStatement) conn
					.prepareCall("{call innovatespoc.query_teacher(?,?,?,?,?,?)}");
			sp.setInt(1, size);
			sp.setInt(2, pageindex);
			sp.setString(3, columnName);
			sp.setString(4, orderDir);
			sp.setString(5, searchValue);
			sp.registerOutParameter(6, java.sql.Types.INTEGER);
			sp.execute();
			recordsTotal = sp.getInt(6);
			rs = sp.getResultSet();
			while (rs.next()) {
				teachers ch = new teachers();
				ch.setTid(rs.getString("id"));
				ch.setTname(rs.getString("name"));
				ch.setSex(rs.getString("sex"));
//				ch.setTeacher_introduce(rs.getString("teacher_introduce"));
//				ch.setPhoto_address(rs.getString("photo_address"));
				list.add(ch);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SqlConnectionUtils.free(conn, sp, rs);
		}
		teacherList ck = new teacherList();
		ck.setRecordsTotal(recordsTotal);
		ck.setData(list);
		return ck;

	}
	
	//检查教师id是否已存在
	@Override
	public int CheckteacherId(int teacherId) {
		Connection conn = null;
		CallableStatement sp = null;
		int flag = 0;
		try {
			conn = (Connection) SessionFactoryUtils.getDataSource(
					sessionFactory).getConnection();
			sp = (CallableStatement) conn
					.prepareCall("{call baseweb.check_teacherId(?,?)}");
			sp.setInt(1, teacherId);
			sp.registerOutParameter(2, java.sql.Types.INTEGER);
		    sp.execute();
		    flag=sp.getInt(2);		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SqlConnectionUtils.free(conn, sp, null);
		}
		return flag;
	    }


	@Override
	public void updateteacher(String tid, String tintroduce) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps=null;
		ResultSet rs = null;		
		try {
		    conn = (Connection) SessionFactoryUtils.getDataSource(
			    sessionFactory).getConnection();
		    String sql =("update teacher set tintroduce=? where tid=?");
		    ps = conn.prepareStatement(sql);
		    ps.setString(1, tintroduce);
		    ps.setString(2, tid);
		    ps.executeUpdate();
		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} finally {
		    SqlConnectionUtils.free(conn, ps, rs);
		}
	}
	
}