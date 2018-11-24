Feature: Choose a find me a spot option when don't have a preference defined
  As a user I want to choose a option to find a spot when I select the option Find me a spot

  Scenario: Choose a Find me a Spot preference with favourites
    Given I am an authenticated user 
    And I don't have a Find me a Spot preference
    And I have favourites spots
    When I press the Find Me a Spot on the menu 
    Then I see buttons with the options The best spot available, The spot closer to me and One of my favourite spot available