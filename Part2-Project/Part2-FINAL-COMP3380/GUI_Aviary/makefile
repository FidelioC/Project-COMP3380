build: Main.class QueryPage.class

Main.class: Main.java
	javac Main.java

QueryPage.class: QueryPage.java	
	javac QueryPage.java

run: Main.class
	java -cp .:mssql-jdbc-11.2.0.jre11.jar Main

clean:
	rm Main.class