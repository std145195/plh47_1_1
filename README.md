# Password Character Frequency – Multithreaded Java Project

## Project Description
This Java program calculates the frequency of each Latin character (both uppercase and lowercase letters) across an array of randomly generated passwords. The program leverages multithreading (using multiple threads) to efficiently process large datasets and accelerate computation of character frequencies.

## Features
- Generates random passwords with lengths ranging from 8 to 16 characters, composed exclusively of Latin letters.
- Allows execution with different numbers of threads (1, 2, 4, 8) to perform parallel processing.
- Counts and displays the frequency of all lowercase (a-z) and uppercase (A-Z) letters.
- Measures and reports the execution time for each thread count configuration.

## How to Run
1. Compile and run the `erotima_1.java` file.
2. The program automatically generates a set of passwords of size \(2^n\) where \(n\) is randomly chosen.
3. Passwords not meeting the length criteria (8–16 characters) are filtered out.
4. For each configured thread count, the program counts character frequencies and outputs a summary along with execution time.

## Requirements
- Java 8 or later.
- No external libraries needed.

## File Structure
- `erotima_1.java`: Main source file implementing the logic.
- `README.md`: This documentation file.
