# Wyatt Conrad's ePortfolio

## Introduction
My name is Wyatt Conrad, and the completion of my Bachelor of Science in Computer Science at Southern New Hampshire University marks a significant milestone in both my academic and professional journey. With over 25 years of experience in the BPO/CX industry, I have always embraced a “see a need, fill a need” approach by identifying challenges, designing solutions, and taking full ownership of outcomes. Throughout the Computer Science program and the development of my capstone ePortfolio, I have strengthened this mindset through formal education, advanced technical skills, and practical experience, which together showcase my readiness to advance within the field of computer science.

## Coursework and the ePortfolio Process
Working through SNHU’s Computer Science coursework has sharpened my ability to analyze problems, design technical solutions, and apply industry-standard practices. Courses in algorithms, software engineering, full-stack development, databases, and artificial intelligence expanded my skill set from self-taught capability to academically validated competence. Developing this ePortfolio allowed me to intentionally reflect on these skills and present a curated collection of strengths that demonstrate not only technical proficiency but also creativity, adaptability, and a commitment to continuous learning.

The capstone project allowed me to showcase the skills I had self-learned, as well as the expanded knowledge I gained at SNHU. By bringing together skills in architecture, development, testing, documentation, and user-centered design, I was able to significantly enhance my project. This project reinforced my confidence in delivering complete, maintainable solutions that meet modern industry standards.

## Professional Values and Career Direction
My experience in the BPO/CX industry has shaped my professional values of taking ownership, maintaining integrity, and striving for excellence. SNHU’s computer science program reinforced these values by emphasizing structured problem-solving, effective communication, and maintaining academic integrity. My goal is to leverage both my technical foundation and seasoned industry perspective to positively impact an organization, both technically and financially.

Outside of my formal coursework, I have continued to improve my skills through real-world application. One example is the comprehensive swim league management system I developed for a local community program. Since 2016, this application has supported approximately 800 swimmers and more than 30 teams each year. More recently, I extended the system with a cross-platform mobile app built in .NET MAUI for parents, athletes, coaches, and administrators. This project exemplifies my ability to independently architect, develop, and deploy solutions that meet real organizational needs.

## Skills Across Key Computer Science Domains
Throughout the program, I have gained experience across a range of essential disciplines:

### Collaboration
Working on team projects, particularly during software engineering coursework and agile-focused classes, taught me how to collaborate effectively using version control, shared documentation, iterative development, and constructive feedback. These experiences reinforced my ability to contribute to a team environment much like the agile scrum practices I use professionally.

### Communication
My professional background naturally strengthened this skill, but the program honed and refined my technical communication. Writing design documents, preparing UML diagrams, presenting solutions, and translating user requirements into development tasks are skills I previously had not practiced. These experiences have already helped me communicate technical concepts clearly across both technical and non-technical audiences.

### Data Structures and Algorithms
The math courses that served as the foundation for many algorithms deepened my understanding of efficiency, complexity, and the importance of selecting the appropriate problem structure. Applying these concepts in projects has enabled me to design code that is both reliable and efficient.

### Software Engineering and Databases
The object-oriented courses focused on modular design, maintainability, testing, and documentation. I created a full-stack application using established design patterns, REST principles, databases, and secure coding practices. This advanced my ability to design systems that scale and integrate seamlessly in both professional and personal development projects.

### Security
Cybersecurity concepts such as encryption, secure authentication, and safe coding practices have strengthened my awareness of vulnerabilities and best practices. These principles are now critical concepts I use when designing software and handling sensitive data. This is especially true in applications that rely on user accounts, personal information, or cloud-based communication.

### My ePortfolio Artifact
The weight-tracking mobile app in my ePortfolio demonstrates how my academic experience and professional background work together to form a comprehensive foundation in computer science. Each version of the application, the Java-based original version and the new Kotlin-based version, demonstrates my strengths in software development, algorithmic thinking, and database development. They also demonstrate my ability to quickly convert an application from one language to another using an iterative process while applying industry best practices.

As a whole, my portfolio represents not only my competency but also my growth from a self-taught problem-solver to a formally trained computer science professional with a strong technical portfolio and a long history of delivering real solutions. I am excited to leverage these skills to create meaningful impact in my next role and continue evolving as a developer, engineer, and technology professional.

## Artifact Details
To showcase my software development capabilities, I chose my Android application, which I created during my CS-360 course at SNHU. It is a mobile app built initially using Java that lets users set a goal and track their weight. It also includes a login feature to manage multiple users and enables notifications via Toast and SMS when a user reaches their goal. The app for the course was created during the 2025 C-2 (March-April) term, which ran from March 3, 2025, to April 27, 2025. The completed app used for that course was uploaded to GitHub on April 20, 2025, which was the version submitted as the final project of CS-360.

The original mobile app code can be viewed or cloned from here: :iphone: [Original Artifact](https://github.com/WyattConrad/eportfolio/tree/PreEnhancement)

The app has been converted to Kotlin and Jetpack Compose to follow Google's recommendations for current Android applications. It now also includes dependency injection with the Dagger/Hilt library, along with a Room database for managing the application's data.

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

        // Assert the correct status text is displayed
        composeTestRule.onNodeWithText("Weight Lost:").assertIsDisplayed()

        // Assert the correct absolute and rounded value is displayed
        val expectedValue = positiveChange.toString()
        composeTestRule.onNodeWithText(expectedValue, substring = true).assertIsDisplayed()

        // Assert the unit is displayed
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
  - Throughout this phase of development, documentation was created through commit messages as well as the initial enhancement plan and follow-up status updates provided weekly.
- Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals.
  - Converting the application from Java to Kotlin, implementing dependency injection, and building tests to validate functionality all demonstrate well-founded techniques and skills required as a developer.


## Algorithms and Data Structures
This enhancement focused on adding a trend analysis algorithm based on the user's entered weights and their goal. 

### Trend Analysis
The analysis forecasts the date a user will reach their goal weight, helping set expectations and determine whether they are on track. The algorithm uses a linear regression to model the weight trends over time.

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
To test and demonstrate trend analysis, I developed an algorithm that generates a realistic series of weight entries, allowing me to better test the trend analysis algorithm. The previous test data consisted of a simple linear set of weights that decreased by a fixed amount for each entry. The new algorithm is still somewhat predictable, but it provides enough variation to demonstrate the trend analysis.

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

- Design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices and standards appropriate to its solution, while managing the trade-offs involved in design choices.
  - The trend analysis algorithm and the test data generation algorithm demonstrate using algorithmic principles to solve a given problem.
- Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals.
  - The combination of the trend analysis, the testing data, and the chart to display the results on the home screen of the app demonstrates my ability to use innovative techniques to deliver value


## Databases
Enhancing the weight-tracking mobile app’s database required several modifications.

### Additional Fields
First, additional fields were added to the user table to properly manage a user. An email address field was added, and the password field was replaced with a hashed password field to increase security.

### Hashed Password
The original database stored passwords in plain text, making them easily accessible outside the app. A hashing algorithm was used to improve password security. The database now only stores a hashed version of the password.

### Implement Database Migrations
As part of the database modifications, a database migration process was added to ensure that any existing data remains intact even after a database change. During one of these migrations, the hashed password field needed to be populated, which required a manual migration to process each user's clear-text password, hash it, and save it in the new field. Other auto migrations were used for the additional changes such as adding the extra fields and removing the plain text password field.

### Filtering of Tracked Weights
Finally, new queries were added to the app to filter the logged weights displayed in the home screen chart. The initial app design did not allow filtering or displaying data by the date the weight was logged. This caused a large number of weights to be queried and displayed whenever the current home page loaded. Filtering the data by month or week allows the app to load data much faster, increasing performance.

The pseudocode below demonstrates an example query added to the database to show the last 30 days of entries:
```
SELECT *
FROM weights
WHERE user_id = :userId
ORDER BY date_time_logged DESC
LIMIT 30
```

### Course Outcomes
The following course outcomes were achieved during this phase of development:
- Design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices and standards appropriate to its solution, while managing the trade-offs involved in design choices.
  - Utilizing a hashing algorithm to secure a user's password, along with queries designed to filter and limit data, demonstrates designing and evaluating solutions to solve given problems.
- Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals.
  - Utilizing buttons on the home screen to enable a user to quickly and easily filter data demonstrates innovative techniques and skills to deliver value to a user or organization.
- Develop a security mindset that anticipates adversarial exploits in software architecture and designs to expose potential vulnerabilities, mitigate design flaws, and ensure privacy and enhanced security of data and resources.
  - Hashing a user's password before storing, and comparing an entered password during login to the hashed password, demonstrates a security mindset. Even though this application stores all data on the device, ensuring that the password is protected is a basic security requirement.

