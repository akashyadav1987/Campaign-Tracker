package com.pulp.campaigntracker.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class UserLoginStatusDatabase {

	/**----------------------------------------------------------------
	Global Variables                                                
	  ----------------------------------------------------------------*/ 
	private Context context;                                                                                                     
	private DatabaseHelper locationDbHelper;                                                                                                   
	private SQLiteDatabase db;    

	public final String KEY_ROWID = "_id";  
	public final String KEY_TIME = "TIME";     
	public final String KEY_IMAGE = "IMAGE";
	public final String KEY_LOGIN_STATUS = "LOGIN_STATUS";
	private final String DATABASE_NAME = "LoginStatus";                                                                               
	private final String DATABASE_TABLE = "LogIn"; 

	private final int DATABASE_VERSION = 1;         

	// Create the database for Tracking Location
	private final String DATABASE_CREATE =                                                                                      
			"create table "+DATABASE_TABLE+" ("
					+ KEY_ROWID + " integer  primary key autoincrement , "                                                                 
					+ KEY_TIME + " TEXT  not null," 
					+ KEY_IMAGE + " BLOB not null,"
					+ KEY_LOGIN_STATUS + " INTEGER not null)";  


	public UserLoginStatusDatabase()                                                                                                      
	{       
	} 

	public UserLoginStatusDatabase(Context ctx)                                                                                                      
	{     
		this.context = ctx;                                                                                                       
		locationDbHelper = new DatabaseHelper(context);                                                                                            
	}   


	private  class DatabaseHelper extends SQLiteOpenHelper                                                                 
	{   
		DatabaseHelper(Context context)                                                                                           
		{                                                                                                                              
			super(context, DATABASE_NAME, null, DATABASE_VERSION);                                                                         
		}
		/**----------------------------------------------------------------
	    TO CRETAE THE DATABASE                                               
	   ----------------------------------------------------------------*/
		@Override                                                                                                                          
		public void onCreate(SQLiteDatabase db)                                                                                            
		{            db.execSQL(DATABASE_CREATE); 

		} 
		/**----------------------------------------------------------------
	    Method to update the database                                                
	   ----------------------------------------------------------------*/
		@Override                                                                                                                          
		public void onUpgrade(SQLiteDatabase db,                                                                                           
				int oldVersion,                                                                                                            
				int newVersion)                                                                                                            
		{                                                                                                                                  
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE); 
			onCreate(db);                                                                                                                      
		}                                                                                                                                  
	}                                                                                                                                  
	/**----------------------------------------------------------------
	    Method to open the database                                                
	   ----------------------------------------------------------------*/                                                                                                        
	public UserLoginStatusDatabase open() throws SQLException                                                                                        
	{    
		if(locationDbHelper==null)
		{
			locationDbHelper = new DatabaseHelper(this.context);  

		}

		db = locationDbHelper.getWritableDatabase();                                                                                      
		return this;    }                                                                                                                  
	/**----------------------------------------------------------------
	    Method to close the database                                                
	   ----------------------------------------------------------------*/                                                                                                        
	public void close()                                                                                                                
	{                                                                                                                                  
		locationDbHelper.close();                                                                                                              
	} 
	
	/*****************************************************************************
	 * Function Definitions
	 *****************************************************************************/
	/**
	 * 
	 * @param time String
	 * @param image byte[]
	 * @param loginStatus  int : 0/1
	 * @return
	 */
	public long insertInfo(String time,byte[] image,int loginStatus)                                                  
	{                                                                                                                                  
		ContentValues initialValues = new ContentValues();        

		initialValues.put(KEY_TIME, time);   
		initialValues.put(KEY_IMAGE, image);         
		initialValues.put(KEY_LOGIN_STATUS, loginStatus);         

		return db.insert(DATABASE_TABLE, null, initialValues);                                                                             
	} 



	/*****************************************************************************
	 * Function Definitions
	 *****************************************************************************/

	/*****************************************************************************
	 * DESCRIPTION
	 *  get all the info from database in descending order of inserted id if sent status is not true
	 *  @return long
	 *  @author dh.udit
	 *****************************************************************************/                                                                                                  
	public Cursor getAllInfo()                                                                                                       
	{      		
		return db.query(DATABASE_TABLE, new String[]                                                                              
				{   KEY_ROWID ,KEY_TIME,KEY_IMAGE,KEY_LOGIN_STATUS},                                                          
				null,                                                                                                                      
				new String[]{"false"} ,                                                                                                                     
				null,                                                                                                                      
				null,                                                                                                                      
				KEY_ROWID +" DESC" );  
	}     

	public  void updateSentStatus(String rowId)                                                  
	{                                                                                                                                  
		db.delete(DATABASE_TABLE,KEY_ROWID + " = ?" , new String[]{rowId});
	}

}
