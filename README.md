# PetStore
1.	Download and install BlueJ, MySQ, Java files and the sql file.
2.	Download MySQL Connector J (Independent Platform) and extract.
3.	Open BlueJ and find Tools  References  Libraries  Add the file .jar in the folder of MySQL Connector.
4.	Add all the files into the project of BlueJ

5.	Go to the bin folder of MySQL Server, such as:
•	C:\Program Files\MySQL\MySQL Server 8.0\bin
-	Copy the bin path, open Environment Variables in the System Properties of Windows, find the variable Path, click Edit, click New and place the bin path there.
-	Import the sql file by opening the Command Prompt and type in this:
o	mysql -u your_username -p -e "CREATE DATABASE your_database_name;"
// replace the your_username with your username (typically root). The part your_database_name should be replaced with petstore

6.	Find the ConnectDatabase java file, replace the password variable with your own password.

7.	Compile and Run the file LoginScene (username and password should be found in the database, or you can create your own account).
