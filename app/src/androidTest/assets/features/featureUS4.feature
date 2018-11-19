Feature: Dashboard for authenticated users

  Scenario: An authenticated user can see the dashboard for authenticated users
    Given I am an authenticated user
    When I enter the application
    And I see the authenticated user dashboard
    And I see the free spots markers on the map
    And I see the information of total free spots, total of ocupied spots and the date of update
    And I see the hamburger button for the menu 

  Scenario: An authenticated user can see the menu options on the authenticated dashboard
    Given I am an authenticated user
    When I click on the hambuguer button
    Then I see the options: Profile, Find me a spot, My favourite Spots, My Spot, Statistics, Change My Password, Logout, Dashboard

  Scenario: An authenticated user can change park on the authenticated dashboard
    Given I am an authenticated user
    And I see the authenticated user dashboard
    When I choose other park on the spinner
    Then I see that park on the map
    And I see the free spots markers on the map of the other park
    And the information of total free spots and total of ocupied spots for that park