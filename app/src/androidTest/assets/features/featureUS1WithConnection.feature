# new feature
# Tags: optional

Feature: Screen to anonymous user
  As an anonymous user
  I have internet connection
  I want see the dashboard with the parking lot and free spots marked

  Background: I don't have any tokens of credentials (I'm an anonymous user)

  Scenario: I have internet and database connection   
    Given I am an anonymous user 
    When I open the application 
    Then I see the parking lot map of the Campus 2 - Park A
    And I see the number of free and occupied sports 
    And I see the markers of free spots displayed on the map
    And I see the last update date
    And I see the button to Login



