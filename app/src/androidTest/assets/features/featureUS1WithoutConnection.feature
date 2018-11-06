# new feature
# Tags: optional

Feature: Screen to anonymous user without internet connection
  As an anonymous user and without internet connection
  I see the information that was obtained the last time I opened the application with internet connection

  Background: I don't have any tokens of credentials (I'm an anonymous user)

  Scenario: No internet connection
    Given I am an anonymous user
    When I open the application 
    And I don't have internet connection 
    Then I see the date that the information was last updated
