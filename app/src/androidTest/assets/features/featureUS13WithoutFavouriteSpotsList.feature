Feature: As an autheticated user
  If I have no favourite spots list I get message inform me of this

  Scenario: Authenticated user see the message saying he has no favourite spots
    Given I am an authenticated user with no favourite spots
    When  I am in the profile screen
    And I click the button "My Favourite Spots"
    Then I see an error message displayed saying "Your favourite spots list is empty"