package com.pulp.campaigntracker.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class UserFormUploadDatabase {

	/**----------------------------------------------------------------
	Global Variables                                                
	  ----------------------------------------------------------------*/ 
	private Context context;                                                                                                     
	private DatabaseHelper locationDbHelper;                                                                                                   
	private SQLiteDatabase db;    

	public final String KEY_ROWID = "_id";  
	public final String KEY_TIME = "TIME";     
	public final String KEY_IMAGE = "IMAGE";
	public final String KEY_FORM_KEY = "FORM_KEY";
	public final String KEY_FORM_VALUE = "FORM_VALUE";
	private final String DATABASE_NAME = "UserForm";                                                                               
	private final String DATABASE_TABLE = "Form"; 

	private final int DATABASE_VERSION = 1;         

	// Create the database for Tracking Location
	private final String DATABASE_CREATE =                                                                                      
			"create table "+DATABASE_TABLE+" ("
					+ KEY_ROWID + " integer  primary key autoincrement , "                                                                 
					+ KEY_TIME + " TEXT  not null," 
					+ KEY_IMAGE + " BLOB not null,"
					+ KEY_FORM_KEY + " INTEGER not null,"
					+ KEY_FORM_VALUE + " TEXT not null)";  

	
	public UserFormUploadDatabase()                                                                                                      
	{       
	} 

	public UserFormUploadDatabase(Context ctx)                                                                                                      
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
	public UserFormUploadDatabase open() throws SQLException                                                                                        
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
	 * @param time
	 * @param image
	 * @param formName
	 * @param formValue
	 * @return long : If insertion is successful.
	 */
	public long insertInfo(String time,byte[] image,int formName,String formValue)                                                  
	{                                                                                                                                  
		ContentValues initialValues = new ContentValues();        
		
		initialValues.put(KEY_TIME, time);   
		initialValues.put(KEY_IMAGE, image);         
		initialValues.put(KEY_FORM_KEY, formName);         
		initialValues.put(KEY_FORM_VALUE, formValue);     

		return db.insert(DATABASE_TABLE, null, initialValues);                                                                             
	} 



	/*****************************************************************************
	 * Function Definitions
	 *****************************************************************************/

	/*****************************************************************************
	 * DESCRIPTION
	 *  get all the info from database in descending order of inserted id 
	 *  @return long
	 *  @author dh.udit
	 *****************************************************************************/                                                                                                  
	public Cursor getAllInfo()                                                                                                       
	{      		
		return db.query(DATABASE_TABLE, new String[]                                                                              
				{   KEY_ROWID ,KEY_TIME,KEY_IMAGE,KEY_FORM_KEY,KEY_FORM_VALUE},                                                          
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
