# The Oktanauts
A JavaFX application connects to a FHIR server for health practitioners to monitor patients’ measurements.

### Pre-requisites
* Java 11
* Maven

### How to Run
1. Run Maven to install project dependencies, e.g. `mvn install`
2. Build the project
3. Run it

### Examples
1. Enter in the identifier of the desired practitioner
2. Select the patients to be monitored using the list on the left
3. Click on a patient and clicking `View patient detail` to view their details
4. The refresh rate of the table can be changed by using the spinner on the top-right side of the application
5. Red highlights indicates patients whose cholesterol level is higher than the average monitored patients' cholesterol level value
6. Set x (systolic blood pressure) and y (diastolic blood pressure), patients whose blood pressure are higher than one of these values will be highlighted in purple and displayed in the tracking view
7. These high blood pressure patients can be selected to track their historical blood pressure value by clicking `add`
8. You can go back to the login screen by clicking `Log out`


![img](https://github.com/meiyalian/TheOktanauts/blob/master/Documentation/uiscreenshot.png)
