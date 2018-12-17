Feature: Park automatically
  As an authenticated user I want park on a spot and get a notification

  Scenario: User park on a Spot Automatically
  Given the application detects that a spot has changed from free to occupied
  When I am an autenticathed user
  Then I am in the proximity of that spot
  And The application asks if I am parked on that spot
  And I select the option yes
  And the application updates the user spot on database.
 

  Given the application detects that a spot has changed from free to occupied
  When I am an autenticathed user
  And I am in the proximity of that spot
  And The application asks if I am parked on that spot
  Then I select the option "No"