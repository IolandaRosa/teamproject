Feature: Updated Profile for an authenticate user
  As a user I want to update my profile information

  Background:
  Given I am an authenticate user
    When I am in my profile screen
    And I click on the "Update my profile" button
    Then I am on update my profile screen
    And I see all the fields displaying my current information

  Scenario Outline: Assert the option on the spinner match as the expected
    When I see the spinner preferences dispalyed
    And  I click to open the spinner
    Then I see the option <preference> displayed

    Examples:
      | preference                |
      | None                      |
      | Closer To My Location     |
      | Best Rated Place          |
      | My Favourite Spots        |

  Scenario: Update profile with empty fields
    When I see the name is empty
    And I see the email is empty
    And I press the "Save" button
    Then I see an error message displayed saying "The fields can't be empty"

  Scenario: Update profile with empty name
    When I see the name is empty
    And I see the email not empty
    And I press the "Save" button
    Then I see an error message on the name field saying "The name can't be empty"

  Scenario: Update profile with numeric name
    When I type "1234567" on the name field
    And I see the email not empty
    And I press the "Save" button
    Then I see an error message on the name field saying "The name can't contain only digits"

  Scenario: Update profile with empty email
    When I see the name not empty
    And I see the email is empty
    And I press the "Save" button
    Then I see an error message on the email field saying "The email can't be empty"

  Scenario: Update profile with invalid email format
    When I see the name not empty
    And I type "mariaemail.pt" on email
    And I press the "Save" button
    Then I see an error message on the email field saying "The email must have a valid format"

  Scenario: Update profile with no changes on the fields
    When I press the "Save" button
    Then I see an error message displayed saying "Nothing was changed"

  Scenario: Update profile change only the name (Happy path)
    When I type "Maria Jesus" on the name field
    And I press the "Save" button
    Then I see the profile activity with my new name displayed

  Scenario: Update profile change email with empty password confirmation
    When I type "maria_jesus@email.com" on email
    And I press the "Save" button
    And I see the password confirmation displayed
    And I press the Ok button
    Then I see an error message displayed saying "The password can't be empty"

  Scenario: Update profile change email with invalid password confirmation
    When I type "maria_jesus@email.com" on email
    And I press the "Save" button
    And I see the password confirmation displayed
    And I type "111111111" on password field
    And I press the Ok button
    Then I see an error message displayed saying "The password does not match"

  Scenario: Update profile change email (Happy Path)
    When I type "maria_jesus@email.com" on email
    And I press the "Save" button
    And I see the password confirmation displayed
    And I type "12345678" on password field
    And I press the Ok button
    Then I see the profile activity with my new email dispalyed
    And I confirm the authentication email is now "maria_jesus@email.com"

  Scenario: Update profile change preference with empty password confirmation
    When I click on the spinner
    And I choose the option "Closer To My Location"
    And I press the "Save" button
    And I see the password confirmation displayed
    And I press the Ok button
    Then I see an error message displayed saying "The password can't be empty"

  Scenario: Update profile change preference with invalid password confirmation
    When I click on the spinner
    And I choose the option "Best Rated Place"
    And I press the "Save" button
    And I see the password confirmation displayed
    And I type "111111111" on password field
    And I press the Ok button
    Then I see an error message displayed saying "The password does not match"

  Scenario: Update profile preference (Happy Path)
    When I click on the spinner
    And I choose the option "My Favourite Spots"
    And I press the "Save" button
    And I see the password confirmation displayed
    And I type "12345678" on password field
    And I press the Ok button
    Then I see the profile activity with my new preference dispalyed

  Scenario: Change email, name and preference (Happy path)
    When I type "Maria Juventina" on the name field
    And I type "maria_juventina@email.com" on email
    And I click on the spinner
    And I choose the option "None"
    And I press the "Save" button
    And I see the password confirmation displayed
    And I type "12345678" on password field
    And I press the Ok button
    Then I see the profile activity with my new information dispalyed
    And I confirm the authentication email is now "maria_juventina@email.com"

  Scenario: Update profile cancel
      And I press the Cancel button
      Then I see the profile activity displayed