Feature: As an autheticated user
  If I have no spots I can see message inform of this

  Scenario: Authenticated User See the message saying that has no favourite spots list
    Given I am an authenticated user with no favourite spots
    When  I am in the profile screen
    And I click the button "My Favourite Spots"
    Then I see an error message displayed saying "Your favourite spots list is empty"