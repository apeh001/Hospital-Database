/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		DBproject esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new DBproject (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Doctor");
				System.out.println("2. Add Patient");
				System.out.println("3. Add Appointment");
				System.out.println("4. Make an Appointment");
				System.out.println("5. List appointments of a given doctor");
				System.out.println("6. List all available appointments of a given department");
				System.out.println("7. List total number of different types of appointments per doctor in descending order");
				System.out.println("8. Find total number of patients per doctor with a given status");
				System.out.println("9. < EXIT");
				
				switch (readChoice()){
					case 1: AddDoctor(esql); break;
					case 2: AddPatient(esql); break;
					case 3: AddAppointment(esql); break;
					case 4: MakeAppointment(esql); break;
					case 5: ListAppointmentsOfDoctor(esql); break;
					case 6: ListAvailableAppointmentsOfDepartment(esql); break;
					case 7: ListStatusNumberOfAppointmentsPerDoctor(esql); break;
					case 8: FindPatientsCountWithStatus(esql); break;
					case 9: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice
 public static void AddDoctor(DBproject esql) {
       try{
         String query = "INSERT INTO Doctor SELECT ";
         String docid = "SELECT COUNT(*) FROM Doctor";
         int doctorID = esql.executeQuery(docid) + 1;
         System.out.print("\tName: ");
         String name = in.readLine();
         System.out.print("\tSpecialty: ");
         String specialty = in.readLine();
         System.out.print("\tDepartment ID: ");
         String did = in.readLine();
         query += doctorID + ", " + "'" + name + "', '" + specialty + "', " + did + " WHERE NOT EXISTS (SELECT * FROM Doctor WHERE doctor_ID = " + doctorID + ")";

         esql.executeUpdate(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }

//1
        }

        public static void AddPatient(DBproject esql) {try{
         String query = "INSERT INTO Patient SELECT ";
         String patid = "SELECT COUNT(*) FROM Patient";
         int pID = esql.executeQuery(patid);
         System.out.print("\tPatient Name: ");
         String pname = in.readLine();
         System.out.print("\tGender: ");
         String gtype = in.readLine();
         System.out.print("\tAge: ");
         int age = Integer.parseInt(in.readLine());
         System.out.print("\tAddress: ");
         String address = in.readLine();
         System.out.print("\tNumber Of Appointments: ");
                 int num_of_appts = Integer.parseInt(in.readLine());
         query += pID + ", '" + pname + "', '" + gtype + "', " + age + ", '" + address + "', " + num_of_appts + " WHERE NOT EXISTS (SELECT * FROM Patient WHERE patient_ID = " + pID + ")";

         esql.executeUpdate(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
//2
        }

        public static void AddAppointment(DBproject esql) {try{
         String query = "INSERT INTO Appointment SELECT ";
         String aptid = "SELECT COUNT(*) FROM Appointment";
         int appntID = esql.executeQuery(aptid) + 1;
         System.out.print("\tAppointment date: ");
         String adate = in.readLine(); System.out.print("\tTime Slot: ");
         String time_slot = in.readLine();
         System.out.print("\tStatus: ");
         String status = in.readLine();
         query += appntID + ", ‘" + adate + "', '" + time_slot + "', '" + status + "' WHERE NOT EXISTS (SELECT * FROM Appointment WHERE appnt_ID = " + appntID + ")";

         esql.executeUpdate(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
//3
        }


        public static void MakeAppointment(DBproject esql) {try{
        System.out.print("\tPatient ID: ");
        int pID = Integer.parseInt(in.readLine());
        System.out.print("\tPatient Name: ");
        String pname = in.readLine();
        System.out.print("\tGender: ");
        String gtype = in.readLine();
        System.out.print("\tAge: ");
        int age = Integer.parseInt(in.readLine());
        System.out.print("\tAddress: ");
        String address = in.readLine();
        System.out.print("\tNumber Of Appointments: ");
        int num_of_appts = Integer.parseInt(in.readLine());
        System.out.print("\tAppointment ID: ");
        int aID = Integer.parseInt(in.readLine());
        System.out.print("\tDoctor ID: ");
        int dID = Integer.parseInt(in.readLine());

        String query = "UPDATE Patient SET number_of_appts = number_of_appts + 1 WHERE EXISTS (SELECT * FROM Patient WHERE patient_ID = ";
        query += pID + ")";

        esql.executeUpdate(query);

        String query1 = "INSERT INTO Patient SELECT " + pID + ", '" + pname + "', '" + gtype + "', " + age + ", '" + address + "', " + num_of_appts + " WHERE NOT EXISTS (SELECT * FROM Patient WHERE patient_ID = " + pID + ") AND EXISTS (SELECT A.status FROM Appointment A, Doctor D, has_appointment H WHERE A.status = 'AV' AND A.appnt_ID = H.appt_id AND D.doctor_ID = H.doctor_id AND A.appnt_ID = " + aID + " AND D.doctor_ID = " + dID + ") OR EXISTS (SELECT AP.status FROM Appointment AP, Doctor DOC, has_appointment HA WHERE AP.status = 'AC' AND AP.appnt_ID = HA.appt_id AND DOC.doctor_ID = HA.doctor_id AND AP.appnt_ID = " + aID + " AND DOC.doctor_ID = " + dID + ") OR EXISTS (SELECT APP.status FROM Appointment APP, Doctor DOCT, has_appointment HAS WHERE APP.status = 'WL' AND APP.appnt_ID = HAS.appt_id AND DOCT.doctor_ID = HAS.doctor_id AND APP.appnt_ID = " + aID + " AND DOCT.doctor_ID = " + dID + ")";

        esql.executeUpdate(query1);

        String query2 = "UPDATE Appointment SET status = 'WL' WHERE status = 'AC' AND EXISTS (SELECT * FROM Appointment WHERE appnt_ID = " + aID + ")";

        esql.executeUpdate(query2);
	
	String query3 = "UPDATE Appointment SET status = 'AC' WHERE status = 'AV' AND EXISTS (SELECT * FROM Appointment WHERE appnt_ID = " + aID + ")";

        esql.executeUpdate(query3);
        }catch(Exception e) {
          System.err.println (e.getMessage());
        }


//4
                // Given a patient, a doctor and an appointment of the doctor that s/he wants to take, add an appointment to the DB
        }

        public static void ListAppointmentsOfDoctor(DBproject esql) {
        try{
         String query = "SELECT D.doctor_ID, A.appnt_ID, A.status FROM Appointment A, Doctor D, has_appointment H WHERE ((A.status = 'AV') OR (A.status = 'AC')) AND A.appnt_ID = H.appt_id AND D.doctor_ID = H.doctor_id AND D.doctor_ID = ";
        System.out.print("\tDoctor ID: ");
        int dID = Integer.parseInt(in.readLine());
        System.out.print("\tDate 1: ");
        String date1 = in.readLine();
        System.out.print("\tDate 2: ");
        String date2 = in.readLine();
        query += dID + " AND (A.adate BETWEEN '" + date1 + "' AND '" + date2 + "') GROUP BY D.doctor_ID, A.appnt_ID";

        esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }

//5
                // For a doctor ID and a date range, find the list of active and available appointments of the doctor
        }

        public static void ListAvailableAppointmentsOfDepartment(DBproject esql) {try{
         String query = "SELECT D.name, A.adate, A.appnt_ID, A.time_slot FROM Appointment A, Doctor D, Department DE, has_appointment H WHERE D.did = DE.dept_ID AND status = 'AV' AND A.appnt_ID = H.appt_id AND D.doctor_ID = H.doctor_id AND DE.name = '";
         System.out.print("\tDepartment name: ");
         String deptname = in.readLine();
         System.out.print("\tAppointment date: ");
         String apptdate = in.readLine();
         query += deptname + "' AND A.adate = '" + apptdate + "'";

         esql.executeQueryAndPrintResult(query);

      }catch(Exception e){
         System.err.println (e.getMessage());
      }
//6
                // For a department name and a specific date, find the list of available appointments of the department

        public static void ListStatusNumberOfAppointmentsPerDoctor(DBproject esql) {try{
         String query = "SELECT D.name, A.status, COUNT(A.appnt_ID) FROM Doctor D, Appointment A, has_appointment H WHERE D.doctor_ID = H.doctor_id AND A.appnt_ID = H.appt_id GROUP BY D.name, A.status ORDER BY COUNT(A.appnt_ID) DESC";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
//7
                // Count number of different types of appointments per doctors and list them in descending order
        }


        public static void FindPatientsCountWithStatus(DBproject esql) {try{
                System.out.print("\tStatus: ");
                String status = in.readLine();
                String query = "SELECT D.name, COUNT(P.patient_ID) FROM Appointment A, Patient P, searches S, Doctor D, has_appointment HA WHERE A.status = '";
                 query += status + "' AND A.appnt_ID = S.aid AND P.patient_ID = S.pid AND D.doctor_ID = HA.doctor_id GROUP BY D.name";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
//8
                // Find how many patients per doctor there are with a given status (i.e. PA, AC, AV, WL) and list that number per doctor.
        }

        public static void Index1(DBproject esql) {
                try{
                        String index = "DROP INDEX IF EXISTS patient_id_index;";
                        esql.executeUpdate(index);
                        String index1 = "CREATE INDEX IF EXISTS patient_id_index ON Patient USING BTREE (patient_ID);";
                        esql.executeUpdate(index1);
                }catch (Exception e){
                  System.err.println (e.getMessage());
                }
        }

        public static void Index2(DBproject esql) {
                try{
                        String index = "DROP INDEX IF EXISTS appointment_id_index;";
                        esql.executeUpdate(index);
                        String index1 = "CREATE INDEX IF EXISTS appointment_id_index ON Appointment USING BTREE (appnt_ID);";
                        esql.executeUpdate(index1);
                }catch (Exception e){
                  System.err.println (e.getMessage());
                }
        }

        public static void Index3(DBproject esql) {
                try{
                        String index = "DROP INDEX IF EXISTS doctor_id_index;";
                        esql.executeUpdate(index);
                        String index1 = "CREATE INDEX IF EXISTS doctor_id_index ON Doctor USING BTREE (doctor_ID);";
                        esql.executeUpdate(index1);
                }catch (Exception e){
                  System.err.println (e.getMessage());
                }
        }

        public static void Index4(DBproject esql) {
                try{
                        String index = "DROP INDEX IF EXISTS appointment_status_index;";
                        esql.executeUpdate(index);
                        String index1 = "CREATE INDEX IF EXISTS appointment_status_index ON Appointment USING BTREE (status);";
                        esql.executeUpdate(index1);
                }catch (Exception e){
                  System.err.println (e.getMessage());
                }
        }
}


                                                       