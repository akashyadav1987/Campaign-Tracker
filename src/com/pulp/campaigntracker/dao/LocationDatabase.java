/**
 * This class is used to create all the connection for creating the .
 * 
 * @author udit.gupta
 */

package com.pulp.campaigntracker.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LocationDatabase {

	/**----------------------------------------------------------------
	Global Variables                                                
	  ----------------------------------------------------------------*/ 
	private Context context;                                                                                                     
	private DatabaseHelper locationDbHelper;                                                                                                   
	private SQLiteDatabase db;    

	public final String KEY_ROWID = "_id";  
	public final String KEY_TIME = "TIME";     
	public final String KEY_ADMIN = "ADMIN";
	public final String KEY_SUB_ADMIN = "SUBADMIN";
	public final String KEY_LOCALITY = "LOCALITY";
	public final String KEY_ADDRESS = "ADDRESS";
	public final String KEY_LATITUDE = "LATITUDE";
	public final String KEY_LONGITUDE = "LONGITUDE";
	private final String DATABASE_NAME = "LocationDatabase";                                                                               
	private final String DATABASE_TABLE = "Location"; 
	private final int DATABASE_VERSION = 1;         

	// Create the database for Tracking Location
	private final String DATABASE_CREATE =                                                                                      
			"create table "+DATABASE_TABLE+"("
					+ KEY_ROWID + " integer  primary key autoincrement , "                                                                 
					+ KEY_TIME + " text UNIQUE not null," 
					+ KEY_ADMIN + " text not null,"
					+ KEY_LOCALITY + " text not null,"
					+ KEY_SUB_ADMIN + " text not null,"
					+ KEY_ADDRESS + " text not null,"
					+ KEY_LATITUDE + " REAL not null,"
					+ KEY_LONGITUDE + " REAL not null);";  

	public LocationDatabase()                                                                                                      
	{       
	} 

	public LocationDatabase(Context ctx)                                                                                                      
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
	public LocationDatabase open() throws SQLException                                                                                        
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

	/**
	 * 
	 * @param time : String
	 * @param admin : String
	 * @param subAdmin : String
	 * @param address : String
	 * @param locality : double
	 * @param latitude : double
	 * @param longitude : String
	 * @return id if inserted : long 
	 */
	public long insertInfo(String time,String admin,String subAdmin,String address,String locality,double latitude,double longitude)                                                  
	{                                                                                                                                  
		ContentValues initialValues = new ContentValues();                                                                             
		initialValues.put(KEY_TIME, time);   
		initialValues.put(KEY_ADMIN, admin);   
		initialValues.put(KEY_SUB_ADMIN, subAdmin);   
		initialValues.put(KEY_ADDRESS, address); 
		initialValues.put(KEY_LOCALITY, locality);   
		initialValues.put(KEY_LATITUDE, latitude);                                                                                         
		initialValues.put(KEY_LONGITUDE, longitude);                                                                                         

		return db.insert(DATABASE_TABLE, null, initialValues);                                                                             
	} 



	/*****************************************************************************
	 * Function Definitions
	 *****************************************************************************/

	/*****************************************************************************
	 * DESCRIPTION
	 *  get all the info from database in descending order of inserted id.
	 *  @return long
	 *  @author dh.udit
	 *****************************************************************************/                                                                                                  
	public Cursor getAllInfo()                                                                                                       
	{      		
		return db.query(DATABASE_TABLE, new String[]                                                                              
				{   KEY_ROWID ,KEY_TIME,KEY_ADMIN,KEY_SUB_ADMIN,KEY_ADDRESS,KEY_LOCALITY,KEY_LATITUDE,KEY_LONGITUDE},         
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
