Feature: Authenticated user password update
  As an authenticated user I want to change my password

  Background: Authenticated user wants to change password
    Given I am an authenticated user
    When I select the menu option "Change my password" the dashboard autheticated screen
    And I see the update password screen displayed
    Then I see al the fields empty

  Scenario: Change password with empty fields
    When I click the Save button
    Then I see an error message saying "The fields can't be empty"

  Scenario: Change password with empty current password field
    When I type my new password "12345677" on new password input field
    And I type my confirmation password "12345677" on new confirmation password field
    And I click the Save button
    Then I see an error message on the current password input field saying "The password can't be empty"

  Scenario: Change password with invalid current password length
    Given I type my current password "123456" on current password input field
    When I type my new password "12345677" on new password input field
    And I type my confirmation password "12345677" on new confirmation password field
    And I click the Save button
    Then I see an error message on the current password input field saying "The password must have at least 8 characters"

  Scenario: Change password with empty new password field
    Given I type my current password "12345678" on current password input field
    When I type my confirmation password "12345677" on new confirmation password field
    And I click the Save button
    Then I see an error message on the new password input field saying "The password can't be empty"

  Scenario: Change password with invalid new password length
    Given I type my current password "12345678" on current password input field
    When I type my new password "123456" on new password input field
    And I type my confirmation password "12345677" on new confirmation password field
    And I click the Save button
    Then I see an error message on the new password input field saying "The password must have at least 8 characters"

  Scenario: Change password with empty new confirmation password field
    Given I type my current password "12345678" on current password input field
    When I type my new password "12345677" on new password input field
    And I click the Save button
    Then I see an error message on the new password confirmation input field saying "The confirmation password can't be empty"

  Scenario: Change password with empty new confirmation password field
    Given I type my current password "12345678" on current password input field
    When I type my new password "12345677" on new password input field
    And I type my confirmation password "12345678" on new confirmation password field
    And I click the Save button
    Then I see an error message saying "The confirmation password must be equal to the password"

  Scenario: Change password with mismatch between new password and new confirmation password
    Given I type my current password "12345678" on current password input field
    When I type my new password "12345677" on new password input field
    And I type my confirmation password "12345678" on new confirmation password field
    And I click the Save button
    Then I see an error message saying "The confirmation password must be equal to the password"

  Scenario: Change password with equal current password and new password
    Given I type my current password "12345678" on current password input field
    When I type my new password "12345678" on new password input field
    And I type my confirmation password "12345678" on new confirmation password field
    And I click the Save button
    Then I see an error message saying "The new password must be different from the current password"

  Scenario: Change password with wrong password credential
    Given I type my current password "12345677" on current password input field
    When I type my new password "12345678" on new password input field
    And I type my confirmation password "12345678" on new confirmation password field
    And I click the Save button
    Then I see an error message saying "The password inserted does not match with your current password"

  Scenario: Change password (Happy Path)
    Given I type my current password "12345678" on current password input field
    When I type my new password "12345677" on new password input field
    And I type my confirmation password "12345677" on new confirmation password field
    And I click the Save button
    Then I was redirected to the dashboard authenticated screen

  Scenario: Change password (Happy Path)
    Given I click the Cancel button
    Then I was redirected to the dashboard authenticated screen