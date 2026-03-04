# OOP TV Platform Project

## Context
The goal of this project is to implement a platform for streaming movies and TV series.  
Initially, we considered several core functionalities commonly found in such platforms, including: **register, login, logout, search, view movie details, rating**, and other similar features.

## Project Execution
The program operates according to the following workflow:

- Data is read from the input test file (in **JSON format**) and loaded into objects.
- Initially, the platform provides:
  - a list of users already registered on the platform (which can only be modified through the **register** operation described later);
  - a list of movies available on the platform.  
  Each entity is identified based on its own attributes.
- The program receives actions sequentially (either **"change page"** or **"on page"**) and executes them in the order they are received. Each action modifies the platform state at a specific moment in time.
- After certain actions are executed, the results are written to the **output JSON file**.
- Once all actions are processed, the program execution ends and the next test set is executed.

## Loading Data from Files into Objects
To load data into objects, a similar approach to the one used in the first assignment was implemented.

The input file names are passed as arguments to the `main` method, after which the output file is created. The `action` function is then called, where an `ObjectMapper` instance is created. The input data is converted into an `Input` object, after which the main program logic is executed. Finally, the results are written to the output file in **JSON format**.

Using the `Input` class, the input data is transformed into objects of type **User**, **Movie**, or **Action**.

## Project Structure
The project contains the following components:

- **Checker** (`Test.java`)
- **Input & reference test files**
- **fileio** – package containing classes used to convert input data into objects
- **management** – package responsible for the application flow and execution of user commands received from the input
- **pages** – package containing different page types; it also includes the `PageFactory` class responsible for handling page transitions within the application
- **utils** – package containing helper classes:
  - `Constants` – defines numeric constants used throughout the program
  - `Errors` – handles the output displayed when an error occurs

## Program Execution
An object of type **AppManager** is created to process the commands received from the input.  
There are two possible types of actions:

- **"on page"**
- **"change page"**

These actions are executed depending on the request and the user permissions. The actions are parsed and handled using **switch-based control structures**.

## Design Patterns Used
- **Singleton (Lazy Initialization)** – implemented in the `Errors` class
- **Factory Method Pattern** – implemented through `PageFactory`, which creates the next required page. Each page type (such as `Movie`, `Login`, `Register`, etc.) extends the abstract class `Page`.

## Description of Key Functions
The functions **purchaseMovie**, **watchMovie**, **likeMovie**, and **rateMovie** are closely related, as the execution of each action depends on the completion of previous actions.

For example:
- A movie cannot be **watched** unless it has been **purchased**.
- A movie cannot be **liked** or **rated** unless it has been **watched**.

Therefore, whenever one of these actions is requested, the program verifies whether the necessary previous actions have already been performed by the current user.

In the **FilterAction** class, filtering and sorting operations are implemented. Filtering by **actors** or **genre** is performed by checking the corresponding structures in the movie list. Sorting is done based on specific criteria (such as **movie duration** or **rating**, in ascending or descending order) using **lambda expressions**.

## User Actions
For users, only two main actions are available:

- **login** – changes the current active user
- **register** – adds a new user to the database and sets them as the current user
