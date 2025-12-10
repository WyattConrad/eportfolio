# Wyatt Conrad's EPortfolio
SNHU CS-499 Eportfolio

This site showcases the culmination of my work from my Computer Science coursework at Southern New Hampshire University (SNHU).

## Introduction
Welcome, my name is Wyatt Conrad. I have over 25 years of experience in the BPO/CX industry, and I have consistently embraced a "see a need, fill a need" approach to my work. Throughout my career, I have earned a reputation among my managers as the go-to person for finding or developing solutions to problems. I thrive on finding these solutions and taking ownership of tasks, ensuring they are completed efficiently and effectively.

One aspect I take great pride in is my ability to self-teach and acquire new skills. I have proactively sought opportunities to expand my knowledge and expertise, resulting in a diverse skill set across the BPO/CX industry. This self-driven approach has allowed me to adapt and thrive in an ever-evolving work environment. In addition, I recently completed my bachelor's degree in computer science at SNHU to legitimize my self-taught skillset.

Throughout my professional career, I have progressed from an agent role to training and training development, to marketing operations, and most recently sales engineering within the BPO/CX space. This progression highlights my dedication to personal and professional growth and my ability to take on increasingly challenging responsibilities.

In addition to my professional endeavors, I am passionate about giving back to the community. I volunteered for a swim league and took the initiative to develop a comprehensive web application to manage all aspects of the league. This system has been successfully utilized since 2016, earning praise from coaches and participants alike. The application manages approximately 800 swimmers and 30+ teams each year. I have also recently added a mobile app written in .NET Maui to support iOS and Android devices for swimmers, parents, coaches, and administrators.

Even though I am known for my expertise in BPO/CX and have a proven track record of delivering exceptional results, I am excited to explore new opportunities to leverage my skills and experience and drive meaningful impact for any organization.

## Artifact 
To showcase my software development capabilities, I chose my Android application, which I created during my CS-360 course at SNHU. It is a mobile app built initially using Java that lets users set a goal and track their weight. It also includes a login feature to manage multiple users and enables notifications via Toast and SMS when a user reaches their goal. The app for the course was created during the 2025 C-2 (March-April) term, which ran from March 3, 2025, to April 27, 2025. The completed app used for that course was uploaded to GitHub on April 20, 2025, which was the version submitted as the final project of CS-360.

The original mobile app code can be viewed or cloned from here: :iphone: [Original Artifact](https://github.com/WyattConrad/eportfolio/tree/PreEnhancement)

The app has been converted to Kotlin and Jetpack Compose to follow Google's recommendations. It now also includes dependency injection with the Dagger/Hilt library, along with a Room database to manage the application's data.

The enhanced mobile app code can be viewed or cloned from here: :iphone: [Enhanced Artifact](https://github.com/WyattConrad/eportfolio/tree/PostEnhancement)

## Code Review
Below is a code review completed before converting the mobile app, in which I discuss the shortcomings of the original app and the plans to update it to the latest version available below.
{% include youtube.html id="BV55c9mEkLA" %}

## Software Design and Engineering
Multiple enhancements were completed in this phase of development to demonstrate a strong software design and engineering skill set.

### Convert Application from Java to Kotlin and Jetpack Compose
The most significant enhancement to the original application was converting it to Kotlin, Google's current recommended language for Android development. All existing pages were also converted to use Jetpack Compose, replacing the traditional XML-formatted pages.

### Implement Dependency Injection
All supporting code was converted to Kotlin, and, where appropriate, dependency injection was used with the Dagger/Hilt framework.

### Build and Run Tests
Finally, I added testing to validate the app's core functionality and ensure that key components rendered data correctly on the screen. Additional tests were also added to ensure database migrations processed successfully. These changes resulted in a near-complete rewrite of the entire application.

A sample test in pseudocode, written in Kotlin:
```
@Test
    fun weightChangeCard_displaysWeightLost_whenChangeIsPositive() {
        val positiveChange = 1.2

        // Set the content with the component under test
        composeTestRule.setContent {
            WeightChangeCard(weightChange = positiveChange)
        }

        // 1. Assert the correct status text is displayed
        composeTestRule.onNodeWithText("Weight Lost:").assertIsDisplayed()

        // 2. Assert the correct absolute and rounded value is displayed
        val expectedValue = positiveChange.toString()
        composeTestRule.onNodeWithText(expectedValue, substring = true).assertIsDisplayed()

        // 3. Assert the unit is displayed
        composeTestRule.onNodeWithText("lbs.").assertIsDisplayed()
    }
```
### Enable Collaboration
GitHub was used to manage version control and project management throughout this process. Each significant enhancement was tracked using separate branches and commits. The commit messages provided short, concise details articulating the specific changes made. In a scenario involving multiple developers, this approach enables a collaborative development process that addresses numerous enhancements simultaneously.

### Course Outcomes
The following course outcomes were achieved during this phase of development:

- Employ strategies for building collaborative environments that enable diverse audiences to support organizational decision-making in the field of computer science.
  - GitHub was instrumental in achieving this outcome, providing the collaborative environment and documentation necessary to enable multiple developers to update the application simultaneously.
- Design, develop, and deliver professional-quality oral, written, and visual communications that are coherent, technically sound, and appropriately adapted to specific audiences and contexts.
  - Throughout this phase of development, documentation was created through commit messages as well as the initial enhancement plan and follow-up status updates provided on a weekly basis.
- Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals.
  - Converting the application from Java to Kotlin, implementing dependency injection, and building tests to validate functionality all demonstrate well-founded techniques and skills required as a developer.


## Algorithms and Data Structures
This enhancement focused on adding a trend analysis algorithm based on the user's entered weights and their goal. 

### Trend Analysis
The analysis forecasts the date a user should reach their goal weight to help set expectations and determine whether they are on track. The algorithm uses a linear regression to model the weight trends over time.

The time complexity for this algorithm is O(n). Each weight will need to be analyzed once to calculate the necessary sums, then combined to determine the slope and intercept.

For the trend analysis, I’ve developed the following pseudocode for the algorithm:
```
(double, double) calculateDaysToGoal(List<Weight> weights){
	int numberOfEntries = weights.size()
	If numberOfEntries is less than 3, return a zero result

	Double sumDaysTotal, sumWeightsTotal, sumDaysAndWeightsTotal, sumDaysSquaredTotal
	For each weight in weights {
		sumDaysTotal = Add all the days since weigh tracking started
		sumWeightsTotal = Add all the weights tracked
		sumDaysAndWeightsTotal = Add the days since tracked times the weight value
		sumDaysSquaredTotal = Add the square value of the days tracked
	}
	Double slope = (numberOfEntries * sumDaysAndWeightsTotal – sumDaysTotal * sumWeightsTotal) / (number of Entries * sumDaysSquaredTotal – sumDaysTotal * sumDaysTotal);
	Double intercept = (sumWeightsTotal – slope * sumDaysTotal) / numberOfEntries;
	
return (slope, intercept);
}
```

### Test Data Algorithm
To test and demonstrate trend analysis, I developed an algorithm that generates a realistic series of weight entries, allowing me to better test the trend analysis algorithm. My current test data is a very simple, linear set of weights that decreases by a set amount for each entry.

The following pseudocode demonstrates how the test data algorithm provides a variation of tracked weights:
```
val WEIGHTSTOCREATE = 200
for (i in 0 until WEIGHTSTOCREATE) {
        // set a slow decline in weight 
        weight -= 0.25

        // Set a min/max weight change during a week period + or - 6.8 pounds
        val wave = kotlin.math.sin(i / 7.0) * 6.8

        // Add some noise to the values for randomization + or - 1.2 pounds
        val noise = (random.nextDouble() * 0.4) - 1.2

        // Calculate the daily weight
        val dailyWeight = weight + wave + noise
		// Insert the weight into the database
        weightDao.insertWeight(
            Weight(
                id = 0L,
                weight = dailyWeight.roundTo2(),
                dateTimeLogged = startDate.plusDays(i.toLong()),
                userId = userId
            )
        )
    }
```

### Course Outcomes
The following course outcomes were achieved during this phase of development:

- Design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices and standards appropriate to its solution while managing the trade-offs involved in design choices.
  - The trend analysis algorithm and the test data generation algorithm demonstrate using algorithmic principles to solve a given problem.
- Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals.
  - The combination of the trend analysis, the testing data, and the chart to display the results on the home screen of the app demonstrates my ability to use innovative techniques to deliver value


## Databases
The enhancement of the weight-tracking mobile app’s database will involve several modifications.

### Additional Fields
First, additional fields were added to the user table to properly manage a user. An email address field was added, and the password field was replaced with a hashed password field to increase security.

### Hashed Password
An additional change to the database was to hash users' passwords. The original database stored passwords in plain text, which could be easily accessed outside the app. A hashing algorithm will be used, along with a salt, to secure passwords better.

### Implement Database Migrations
As part of the database modification, a database migration process was added to ensure that any existing data remains intact even after a database change. During this migration, the hashed password field needed to be populated, which required a manual migration to process each user's clear-text password, hash it, and save it in the new field. Other auto migrations were used for the additional changes.

### Filtering of Tracked Weights
Finally, new queries were added to the app to filter the logged weights displayed in the home screen chart. The initial app design did not allow for filtering or displaying data based on the date the weight was taken. This caused a large number of weights to be queried and displayed whenever the current home page loaded. Filtering the data by month or week allows the app to load data much faster, increasing performance.

The pseudocode below demonstrates an example query added to the database to show the last 30 days of entries:
```
SELECT *
FROM weight
WHERE user_id = userid
	AND date_time_logged >= date(now, -30 days)
ORDER BY date_time_logged DESC
```

### Course Outcomes
The following course outcomes were achieved during this phase of development:
- Design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices and standards appropriate to its solution, while managing the trade-offs involved in design choices.
  - Utilizing a hashing algorithm to secure a user's password, along with queries designed to filter and limit data, demonstrates designing and evaluating solutions to solve given problems.
- Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals.
  - Utilizing buttons on the home screen to enable a user to quickly and easily filter data demonstrates innovative techniques and skills to deliver value to a user or organization.
- Develop a security mindset that anticipates adversarial exploits in software architecture and designs to expose potential vulnerabilities, mitigate design flaws, and ensure privacy and enhanced security of data and resources.
  - Hashing a user's password before storing, and comparing an entered password during login to the hashed password, demonstrates a security mindset. Even though this application stores all data on the device, ensuring that the password is protected is a basic security requirement.

