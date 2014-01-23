package com.example.sqlitesample;

import com.example.sqlitesample.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	TextView _tvContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.setClick(R.id.btnCreateDB);
		this.setClick(R.id.btnCreateTable);
		this.setClick(R.id.btnInsertDB);
		this.setClick(R.id.btnUpdateDB);
		this.setClick(R.id.btnSelectDB);
		this.setClick(R.id.btnDeleteDB);		

		_tvContent = (TextView) this.findViewById(R.id.tvContent);
		createDB();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void setClick(int id) {
		Button btn = (Button) this.findViewById(id);
		btn.setOnClickListener(this);
	}

	public void onClick(View view) {

		int id = view.getId();

		switch (id) {
		case R.id.btnCreateDB: {
			createDB();
			break;
		}
		case R.id.btnCreateTable: {
			createTable();
			break;
		}
		case R.id.btnInsertDB: {
			insertTable();
			break;
		}
		case R.id.btnUpdateDB: {
			this.updateDB();
			break;
		}
		case R.id.btnSelectDB: {
			selectDB();
			break;
		}
		case R.id.btnDeleteDB:{
			deleteDB();
			break;
		}

		}
	}

	SQLiteDatabase _database;
	String _dbPath = "test";
	String TAG = "sqlitesample";

	void createDB() {
		try {

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory()
						.getCanonicalPath() +"/"+ _dbPath;
				//must be absolute path
				 _database = SQLiteDatabase.openOrCreateDatabase(path, null);
			}	
			  
 	/*	_database = this.openOrCreateDatabase(_dbPath,
					Context.MODE_PRIVATE, null);*/
 		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void createTable() {
		try {
			String _sql_check_table =  "select count(*) as tableCount from sqlite_master where name='users'";

			Cursor dataCursor = _database.query("sqlite_master", null, "name = 'users'",  null,
					null, null, null);
			int total = dataCursor.getCount();
			if (total > 0){
				String _sql_drop_table = "drop table users";
				_database.execSQL(_sql_drop_table);
			}
			
			
			String _sql_create_table = "create table users(id integer primary key AutoIncrement,uname varchar(20),age integer)";
			_database.execSQL(_sql_create_table);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void insertTable() {
		try {
			String _sql_insert_table = "insert into  users(uname,age) values ('abc',30)";
			_database.execSQL(_sql_insert_table);

			ContentValues values = new ContentValues();
			values.put("uname", "张三");
			values.put("age", 40);
			_database.insert("users", null, values);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void updateDB() {
		try {
			String _sql_update_table = "update users set uname = '李四', age = 50 where id = 4";
			_database.execSQL(_sql_update_table);

			ContentValues values = new ContentValues();
			values.put("uname", "李三");
			values.put("age", 70);
			_database.update("users", values,  "id=" +5, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void selectDB() {
		try {
			String _sql_Select_All = "select * from users";
			String[] fields = new String[] { "id","uname", "age" };
			Cursor dataCursor = _database.query("users", fields, null, null,
					null, null, null);
			int total = dataCursor.getCount();
			Log.i(TAG, "total:" + Integer.toString(total));

			StringBuilder builder = new StringBuilder();
			dataCursor.moveToFirst();
			while (dataCursor.moveToNext()) {
				String resultFormat = "id:%d uname:%s age:%d";
				int id = dataCursor.getInt(0);
				int idindex = dataCursor.getColumnIndex("id");
				String uName = dataCursor.getString(dataCursor
						.getColumnIndex("uname"));
				int age = dataCursor.getInt(dataCursor.getColumnIndex("age"));

				String str = String.format(resultFormat, id, uName, age);
				builder.append(str + "\r\n");
				Log.i(TAG, str);
			}

			_tvContent.setText(builder.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void deleteDB() {

		try {
			String _sql_delete_table = "delete from users where id%2==0";
			_database.execSQL(_sql_delete_table);

			_database.delete("users", "id>1", null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
