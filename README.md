# automation-tests

### Technologies used

1. [Java SE Development Kit](https://www.oracle.com/technetwork/java/javase/downloads/index.html)  
2. [Maven](https://maven.apache.org/install.html).   
3. [Selenium](https://www.seleniumhq.org/)
4. [TestNG](https://testng.org/doc/index.html)
5. [Allure Report](https://github.com/allure-framework/allure-java)
6. [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)
7. [Selenoid](https://github.com/aerokube/selenoid)
### Framework overview

The framework is a selenium wrapper where every page is represented with a java Class (Page object Model), and selenium methods are sitting in one place where they can be reused.

The tests are run can be run locally DriverManager#getDriver() or on remote server using Selenoid by calling DriverManager#getDriver(String url)

### Requirements

1. Java JDK
2. Maven
3. Docker
4. [Configuration Manager](https://aerokube.com/cm/latest/)


### Test case

Purchase a moisturizers successfully :

1. Navigate to https://weathershopper.pythonanywhere.com/
2. Click on buy moisturizers button
3. Choose an item => payment frame is shown
4. Fill out the payment details 
5. Verify the payment was successful

The tests are run in parallel on both Firefox and Chrome browsers.

### How to execute tests

1.Clone the project

 ```bash  
git@github.com:mohcine1789/ui-selenium-tests.git
```

2. Make sure docker is run on your machine

3. Start Selenoid
 
 - On Linux and Mac :
 
 ```bash  
./cm selenoid start --vnc

```
- On Windows :
 ```bash  
/cm.exe selenoid start --vnc

```

4. Make sure to be on the same level as pom.xml file. Run the command line to execute the tests
```bash  
mvn clean test
```

#### Reporting

Allure is used to generate cute and clear reports with description of test steps. In case of failed test, a screenshot is attached to the report.

Run the command line to generate allure report and open it in the browser

```bash  
mvn allure:serve
```
