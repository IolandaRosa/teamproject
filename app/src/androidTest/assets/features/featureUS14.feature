Feature: Authenticated user can delete a spot
  From his favourite spots list

  Background: Authenticated user can delete a favourite spot from his list
    Given I am an authenticated user
    When I select the menu option "My favourite spots" on dashboard authenticated
    And I see the spots favourite list with the delete option

  Scenario: I can delete a spot from the favourite spots list
    Given I see the spot on first position with information "Name: A-1", "Status: Free    ", "Rate: 4"
    When I see the spot on second position with information "Name: A-2", "Status: Occupied", "Rate: 0"
    And I press the option delete on the first position of the list
    Then I see the spot on the first position is "Name: A-2", "Status: Occupied", "Rate: 0"

  Scenario: I delete all the spots from the favourite spots list
    When I press the option delete on the first position of the list
    Then I see an error message displayed saying "Your favourite spots list is empty"