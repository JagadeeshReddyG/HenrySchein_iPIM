
cd..
mvn -P xmlFile clean compile test -DsuiteXMLFile=testng-failed.xml  -Drunmode=local  -Dlocale=US
pause
pause