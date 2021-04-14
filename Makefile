run: compile
        java Frontend 

compile: Airport.class AirportInterface.class DataReaderInterface.class DataReader.class Backend.class BackendInterface.class Frontend.class CS400Graph.class GraphADT.class 

Airport.class: Airport.java
	javac Airport.java

AirportInterface.class: AirportInterface.java
	javac AirportInterface.java

DataReader.class: DataReader.java
	javac DataReader.java

DataReaderInterface.class: DataReaderInterface.java
	javac DataReaderInterface.java
	
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
	
test: testData testBackend testFrontend

testData: DataWranglerTests.class
	java DataWranglerTests
	
testBackend: TestBackend.class
	java TestBackend
	
testFrontend: FrontEndDeveloperTests.class
	java FrontEndDeveloperTests
	
DataWranglerTests.class: DataWranglerTests.java
	javac DataWranglerTests.java	

TestBackend.class: TestBackend.java
	javac TestBackend.java

FrontEndDeveloperTests.class: FrontEndDeveloperTests.java
	javac FrontEndDeveloperTests.java

clean:
        $(RM) *.class
                     
