Feature: As an autheticated user
  If I have a favourite spots list
  Assert I can see the favourite spots list
  From both dashboard authenticated option menu and my profile option button

  Background: Authenticated user wants to see the favourite spots list
    Given I am an authenticate user
    When I am in dashboard authenticated screen
    Then I open the menu

  Scenario: See the list from the dashboard
    Given I press the "My favourite spots" option
    Then I see the favourite spots list displayed

  Scenario Outline: See the list from the profile option
    Given I press the "Profile" option
    When  I am in the profile screen
    And I click the button "My Favourite Spots"
    Then I see the favourite spots list displayed
    And I see the name <id>, park <park>, status <status> and the rate <rating> displayed on the list

  Examples:
    | id  | park | status   | rating|
    | A-1 | D    | Free     | 4     |
    | A-2 | D    | Occupied | 0     |