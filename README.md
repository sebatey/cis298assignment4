# Assignment 4 - Add Implicit Intents and HTTP data loading to Beverage app.
## Due 12-12-2016
## Author



## Description

This assignment will require you to either update my version of assignment 3, or your own version. This repository comes with my finished version of assignment 3. If you would rather use your version, you must replace all of the files in this project with your files. This way you can still make a pull request to submit the assignment.

Once a user is on the detail page for a beverage item, there should be two additional buttons added that can be pressed. A Select Contact Button, and a Send Email button. The Select Contact button will allow a user to select a contact from the contacts application. The Send Email button will allow the user to send an email to the contact that was selected.

If the device has a default Contacts App, then the Select Contact button should be enabled, and the Send Email button disabled until a contact is selected. If the application does not have a default Contacts App, the application should disable the Select Contacts button and enable the Send Email button. If it is the case where a contacts app does not exist, the user should still be able to send an email, however there will be no way to pre-populate the 'to' field nor personalize it.

When clicking the Select Contact button, the app should launch the default contacts app and allow the user to select a contact to send a email to.
When clicking the Send Email button, the app should take the email address of the selected user, and use that as the recipient for the email. The email should have a Subject auto-generated based on a pre-defined message. The body of the email should address the contact that was selected by using the contacts name. This will help personalize the message. Lastly, the email body should also contain all of the information about the specific item.

In addition to adding the Implicit Intents to select a contact and send an email, the app must now load it's data from a remote web service vs. loading it from the CSV file.

The exact same data can be pulled by making a HTTP request to the following URL. If you would like to see what gets returned, you can simply type the following URL into a web browser and see what gets returned.

http://barnesbrothers.homeserver.com/beverageapi

The data comes back as JSON data that must be parsed into the collection of Beverages. Some of the work that was being done in the BeverageCollection (BeverageLab depending on what you named it), may need to move to the fragment that hosts the RecyclerView. That way onPostExecute can easily finish wiring up the RecyclerView after the data gets returned.

In order to get the returned JSON string from the web service into something that can begin to be parsed, the following code must be used to turn the JSONString into an object. The string returns an Array. It does NOT return a Object like the JSON from the book does. Therefore the inital parse from string to something must be to a JSONArray and not a JSONObject.

    JSONArray jsonBeverages = new JSONArray(jsonString);

Here are some screen shots showing what the new details page and email should look like.

![Application Detail Portrait Layout](http://barnesbrothers.homeserver.com/cis298/assignmentImages/assignment4a.jpg "Application Detail Portrait Layout")
![Application Email Message Layout](http://barnesbrothers.homeserver.com/cis298/assignmentImages/assignment4b.jpg "Application Email Message Layout")

Solution Requirements:

* Add button for Selecting a Contact
* Add button for Sending and Email
* Proper disabled / enable of buttons based on above description
* Pull email address from contact
* Auto-generate the email to, subject, and body
* Load Beverage data from HTTP web service

Optional Extra Credit:

* Use an SQLite database as the data source for the application
* Pre-load SQLite database with data from Web Service

### Notes

You will need to add a permission to use contacts to the Android Manifest file. The book only fetches the users name, and therefore does not need to ask permission. We would like to get the email for the user, so permission is needed. The permission is called:

    <uses-permission android:name="android.permission.READ_CONTACTS" />

The book does not cover how to pull the email address of a contact. It is a little more work than just the name. It requires making two queries to the contacts database. This stackoverflow answer should help you figure out what you need to do to get the email address for a contact. Because we are requesting data from a contacts 'database', a lot of the syntax is similar to what is done when interacting with SQLite.

http://stackoverflow.com/questions/10117049/get-only-email-address-from-contact-list-android

You will also need to add permission to the manifest to allow access to the Internet. The book DOES cover this.

You will probably need to add an email account and a contact to your virtual device in order to test this functionality.

## Grading
| Feature                           | Points |
|-----------------------------------|--------|
| Looks Like Pictures               | 5      |
| Select Contact                    | 15     |
| Send Email                        | 15     |
| Enable / Disable                  | 10     |
| Email to Contact                  | 15     |
| Email From/Subject/Body           | 10     |
| Pull JSON                         | 10     |
| Parse JSON                        | 10     |
| Documentation                     | 5      |
| README                            | 5      |
| Extra Credit                      | 20     |
| Total                             | 100    |

### Extra Credit

For an extra 20 points of assignment extra credit you can do the following:

Setup a SQLite database to maintain the information for the Beverages. When the database is initially setup, the database should pre-populate it's data by accessing the HTTP web URL mentioned above. There are 2 changes here. The collection of data used to 'run' the application will pull from the database and NOT the web service. The database will initially pull from the web service when it is being setup for the first time, and then never use the web service again. (Unless the app is uninstalled and reinstalled).

The application must do all of the above work to get the extra credit. In order to satisfy the web service grading part of the assignment, the application must either load the Beverage data from the web service when the application starts up (no database), or load the Beverage data when the database is created (with database). If there is no web service functionality for one of those two features, then you can not get any points for that section of grading.

## Outside Resources Used



## Known Problems, Issues, And/Or Errors in the Program



# Original Assignment README

## Description

Create a master detail application to view the contents of a beverage list.
The master part will display a list of all of the beverage items in the list.
The Detail part will display the details of a specific beverage item.
The User should be able to go up and down items in the list by swiping left or right on a detail page (View Pager).
The App should look like the screen shots I have provided.
There is no need to save any information changed in the application to the CSV file.

The properties of the CSV file are as follows:

1. Item Number
2. Item Description
3. Item Pack Size
4. Item Case Price
5. Whether the item is currently active. This is represented by a string that either says True, or False.

Solution Requirements:

* Master layout (You can use the same layout for both Portrait and Landscape unless you feel the need to make one for each)
* Detail layout (You can use the same layout for both Portrait and Landscape unless you feel the need to make one for each)
* Fragments for all of the layouts (Except ViewPager. That one is a little different.)
* Ability to click on a list item and start a new Activity that displays the details of the list item.
* Updating information in the detail view should be reflected in the list view when returning to the list.
* Class to represent a Beverage Item (POJO)
* ArrayList for the list of Beverage Items
* RecyclerView to display the master view
* Read in CSV file and create ArrayList of Beverage Items from the data in the CSV file
* Use View Pager to swipe left and right on detail page to go up and down in the list
* Should look like the screen shots provided
* Remember Documentation, and README

![Application Master Portrait Layout](http://barnesbrothers.homeserver.com/cis298/assignmentImages/assignment3a.jpg "Application Master Portrait Layout")
![Application Detail Portrait Layout](http://barnesbrothers.homeserver.com/cis298/assignmentImages/assignment3b.jpg "Application Detail Portrait Layout")

### Notes

The book does not cover how to read in a CSV file. We will try/plan to do something in class to demonstrate how to get the CSV read in.
You should also do your own research into how to read in a csv file in android
The location of the file to be read in is in the following paths:
Inside Android Studio: app/res/raw/
Through File System:   app/src/main/res/raw/
