package com.example.demo;			
			
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;			
			
public class Model{	
	
	private JdbcTemplate jdbcTemplate;
	
	//IDの存在チェック
	//引数:syainID … 入力された社員ID
	//戻り値:check2 … 社員IDが社員マスタに登録されている数
	public int idExistJudge(String syainID) throws SQLException, ClassNotFoundException
	{
		//変数宣言
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		int judge = 1;

		//登録判定の呼び出し
		List<?> check = jdbcTemplate.queryForList("SELECT COUNT(*) FROM syainmst where syainID = ?",syainID);
		LinkedCaseInsensitiveMap<?> syainList = (LinkedCaseInsensitiveMap<?>) check.get(0);
		
		//判定結果の数値を返却
		judge = Integer.parseInt(syainList.get("COUNT(*)").toString());
		return judge;
	}
	
	
	//IDとNAMEでの組み合わせ存在チェック
	//引数:syainID … 入力された社員ID
	//引数:syainNAME … 入力された社員名
	//戻り値:check2 … 社員ID,社員名の組み合わせが社員マスタに登録されている数
	public int existCheck(String syainID,String syainNAME) throws SQLException
	{
		//DB接続処理の呼び出し
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		
		//変数宣言
		int judge ;
		
		//社員ID、社員名組み合わせ存在判定の呼び出し
		List<?> check = jdbcTemplate.queryForList("SELECT COUNT(*) FROM syainmst where syainID = ? AND "
				+ "syainNAME = ?",syainID,syainNAME);
		LinkedCaseInsensitiveMap<?> syain_List = (LinkedCaseInsensitiveMap<?>) check.get(0);
		
		//判定結果の返却値を設定
		judge = Integer.parseInt(syain_List.get("COUNT(*)").toString());
		return judge;
	}
	
	
	
	//ID、NAME、BIRTHでの組み合わせ存在チェック
	//引数:syainID … 入力された社員ID
	//引数:syainNAME … 入力された社員名
	//引数:syainBIRTH … 選択された生年月日
	//戻り値:check2 … 社員ID,社員名,社員生年月日の組み合わせが社員マスタに登録されている数
	public int existCheck(String syainID,String syainNAME,String syainBIRTH) throws SQLException
	{
		//DB接続処理の呼び出し
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		
		//変数宣言
		int judge ;
		
		//社員ID、社員名組み合わせ存在判定の呼び出し
		List<?> check = jdbcTemplate.queryForList("SELECT COUNT(*) FROM syainmst where syainID = ? AND "
				+ "syainNAME = ? AND birth = ?",syainID,syainNAME,syainBIRTH);
		LinkedCaseInsensitiveMap<?> syain_List = (LinkedCaseInsensitiveMap<?>) check.get(0);
		
		//判定結果の返却値を設定
		judge = Integer.parseInt(syain_List.get("COUNT(*)").toString());
		return judge;
	}

	
	
	//オーバーロード(登録機能)
	//引数:HashTable hshの情報
	//戻り値:voidのため無し
	public void dbAccess(Hashtable<String, Object> hsh)
	{
		//DB接続処理の呼び出し
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		
		//登録処理呼び出し
		//社員マスタに社員情報を登録
		jdbcTemplate.update("INSERT INTO syainmst(syainID,syainNAME,birth,age,gender) VALUES(?,?,?,?,?)", 
				hsh.get("key0") ,hsh.get("key1"),hsh.get("key2"),hsh.get("key3"),hsh.get("key4"));
		
		//給与マスタに必要な情報を登録
		jdbcTemplate.update("INSERT INTO salary_mst(syainID,基本給,交通費,残業代,固定残業代) VALUES(?,?,?,?,?)", 
				hsh.get("key0") ,hsh.get("key5") ,hsh.get("key6"),0,30000);
		
		//勤怠マスタに検索時表示するのに必要な情報を登録(本来なら別機能でやる)
		jdbcTemplate.update("INSERT INTO attendance_mst(syainID,就業日数,出勤日数,労働時間,欠勤日数,残業時間,休日出勤日数) "
				+ "VALUES(?,?,?,?,?,?,?)", hsh.get("key0") ,20 ,20,"160:00",0,0,0);
	}
		
	
	//オーバーロード(削除機能)
	//引数:配列 data[] … 社員情報(社員ID)
	//戻り値:voidのため無し
	public void dbAccess(String data[]) throws SQLException
	{	
		//DB接続処理の呼び出し
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		
		//削除処理の呼び出し
		//社員マスタから該当社員のデータを消去
		jdbcTemplate.update("DELETE FROM syainmst WHERE syainID = ?",data[0]);
		
		//給与マスタから該当社員のデータを削除
		jdbcTemplate.update("DELETE FROM salary_mst WHERE syainID = ?",data[0]);
		
		//勤怠マスタから該当社員のデータを削除
		jdbcTemplate.update("DELETE FROM attendance_mst WHERE syainID = ?",data[0]);
	}
	
	//オーバーライド(検索機能)
	//引数:HashTable condition … 社員情報(検索条件[検索条件,検索カラム])
	//引数:HashTable search_Char … 検索文字情報(検索文字[検索文字])
	//戻り値:searchList … 検索結果
	public List<String> dbAccess(Hashtable<String, Object> condition,Hashtable<String, Object> search_Char) throws SQLException
	{
		//DB接続処理の呼び出し
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		List <String>search_List = new ArrayList<String>();
		
		//変数宣言
		String front="";
		String rear ="";
		
		//社員マスタ検索条件の判定
		if(condition.get("key0").equals("front"))
		{
			front = "";
			rear = "%";
		}
		else if(condition.get("key0").equals("back"))
		{
			front = "%";
			rear = "";
		}
		else if(condition.get("key0").equals("all"))
		{
			front = "";
			rear = "";
		}
		else if(condition.get("key0").equals("part"))
		{
			front = "%";
			rear = "%";
		}
		
		//検索処理の呼び出し
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM syainmst where "
				+ ""+condition.get("key1")+" like ? ",front+search_Char.get("key0")+rear);
		
		//検索結果をListに追加する処理
		for(int l = 0;l < list.size();l++)
		{
			search_List.add(list.get(l).toString().replaceAll("^\\{|\\}$", ""));
		}
		return search_List;
	}

	//検索機能(社員情報[社員マスタ,給与マスタ,勤怠マスタ])
	//引数:syainID … 入力された社員ID
	//戻り値:searchList … 検索結果
	public List<String> dbAccess_Private(String syainID) throws SQLException
	{
		//DB接続処理の呼び出し
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		List <String>search_List = new ArrayList<String>();
		
		
		//検索処理の呼び出し
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM syainmst s INNER JOIN salary_mst l ON "
				+ "s.syainID = l.syainID INNER JOIN attendance_mst a ON s.syainID = a.syainID where s.syainID = ?",syainID);
		
		//検索結果をListに追加する処理
		for(int l = 0;l < list.size();l++)
		{
			search_List.add(list.get(l).toString().replaceAll("^\\{|\\}$", ""));
		}
		return search_List;
	}
	
	//検索機能(税金マスタ)
	//戻り値:searchList … 検索結果
	public List<String> dbAccess_tax() throws SQLException
	{
		//DB接続処理の呼び出し
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		List <String>search_List = new ArrayList<String>();
		
		
		//検索処理の呼び出し
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM deduction_mst");
		
		//検索結果をListに追加する処理
		for(int l = 0;l < list.size();l++)
		{
			search_List.add(list.get(l).toString().replaceAll("^\\{|\\}$", ""));
		}
		return search_List;
	}
	
}			

