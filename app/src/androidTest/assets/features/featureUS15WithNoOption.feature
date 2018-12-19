Feature: Park automatically
As an authenticated user I want park on a spot and get a notification



  Scenario: User park does not parked with Spot Automatically
    Given The application detects that a spot has changed from free to occupied
    And I am an authenticated user
    When I am in the proximity of that spot
    And The application asks if I am parked on that spot
    Then I select the option "No"

