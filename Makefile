run: compile
        java Frontend 

compile: Airport.class AirportInterface.class DataReaderInterface.class DataReader.class Backend.class BackendInterface.class Frontend.class CS400Graph.class GraphADT.class 
        
BackendInterface.class: BackendInterface.java
	javac BackendInterface.java
        
Backend.class: Backend.java
	javac Backend.java

Frontend.class: Frontend.java
	javac Frontend.java

CS400Graph.class: CS400Graph.java
	javac CS400Graph.java

GraphADT.class: GraphADT.java
	javac GraphADT.java

Airport.class: Airport.java
	javac Airport.java

DataReader.class: DataReader.java
	javac DataReader.java

AirportInterface.class: AirportInterface.java
	javac AirportInterface.java

DataReaderInterface.class: DataReaderInterface.java
	javac DataReaderInterface.java

test: testData testBackend testFrontend

testData: DataReader.class
	java DataReader
	
testBackend: TestBackend.class
	java TestBackend
	
testFrontend: TestFrontend.class
	java TestFrontend
	
DataReader.class: DataReader.java
	javac DataReader.java	

TestBackend.class: TestBackend.java
	javac TestBackend.java

TestFrontend.class: TestFrontend.java
	javac TestFrontend.java

clean:
        $(RM) *.class
                     
