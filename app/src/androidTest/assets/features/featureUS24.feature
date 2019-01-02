Feature: An authenticated user can upload a image on the incident report

  Scenario: User wants to upload a image
    Given I am an authenticated user
    When I press the menu option "Report an Incident"
    And I insert the description on the description field
    And I see a button to get my current location and to choose a park and spot
    And I press the button to get my current location
    And I don't see the button to choose a park and a spot
    And I see a message saying that my current location was obtained
    And I press the button to upload a image


