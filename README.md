# GymNoteAPI
**`In Development`** **`API`** **`Springboot`**

### Overview
GymNote API is a robust Spring Boot application designed to provide comprehensive workout and fitness tracking functionality. It serves as the backend infrastructure for GymNote mobile applications, offering a complete solution for fitness enthusiasts to manage their workout routines.

## 💻️ Core Features

### 🏋️‍♀️ Workout Management
- Workout Templates: Create, customize, and manage reusable workout templates
- Live Workout Tracking: Real-time workout session management with start/end functionality
- Exercise Library: Extensive database of exercises with custom exercise creation
- Set Tracking: Detailed tracking of sets, repetitions, weights, distance, and other metrics


### 🔒 User System
- JWT Authentication: Secure authentication system with refresh token support
- Role-based Authorization: Granular access control with user-specific permissions
- Profile Management: User profile and preferences management

### 💿 Data Structure
- Hierarchical Organization:
    - Templates → Workouts → Exercises → Sets
    - Exercises → Categories → Types
- Flexible Exercise Types: Support for various exercise metrics (reps, time, distance)
- Ordered Collections: Maintained exercise order within workouts

### 🛠️ Technical Features
- REST-ful Architecture: Well-structured REST API endpoints
- Data Validation: Comprehensive input validation and error handling
- Entity Relationships: Complex database relationships with referential integrity
- Security Layer: Multiple security layers with JWT token management


## 🧰 Future Enhancements
- **OAuth integration** for social login and third-party authentication
- **Workout Analytics** with detailed workout statistics and insights
- **Mobile app integrations** with Swift-based iOS application
