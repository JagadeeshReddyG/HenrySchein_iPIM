cd..
cd..
cd..
mvn -P xmlFile clean compile test -DsuiteXMLFile=target\surefire-reports\testng-failed.xml -Drunmode=local
pause