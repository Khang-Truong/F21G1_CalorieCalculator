# F21G1_CalorieCalculator

# General installation
Download Android Studio here: https://developer.android.com/studio

Open the project in Android Studio
- Buil the app
- Run the app (Simulator/ Real device - Targeted for Nexus 6 API 30)

# About the app
This is a project for CSIS3175 - Mobile Application Development I course.

Project's name: Calorie Calculator 

Description: Users can track the calorie that they need, add the food’s calorie they consume every day and follow some exercise videos.

This app uses Sharepreferences to save and retrieve data from database and SQLiteOpenHelper to manage database. 

All data will store in LoginDB.db. 

The database has 3 tables
1.	Calendardata: stores userId, date, TDEE, food calorie, exercise calorie.
2.	Mealdata: stores userId, date, meal’s name, calories
3.	Users: stores userId, username, password, gender, age, height (inches), weight (Lbs), TDEE (Total Daily Energy Expenditure)
