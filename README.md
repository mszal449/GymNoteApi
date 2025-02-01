# Auth pipelines:

## Registration
1. Client -> Registration controller
User provides credentials for authentication:
- username
- email
- password

2. Registration controller -> Registration Service
- Checks if user exists
- Encodes password
- Saves user to database


## Login
1. Client -> Controller
- User provides credentials

2. Controller -> AuthenticationManager
- Uses credentials to log in

3. AuthenticationManager -> Client
- JWT token generation

## JWT Authentication pipeline
1. Request -> JwtAuthenticationFilter

2. JwtAuthenticationFilter-> decoder
- JWT token get's decoded to be used

3. JwtAuthenticationFilter -> JwtToPrincipalConverter
- Principals are getting extracted from token

4. JwtAuthenticationFilter -> ContextHandler
- If principals are not null, user for current request is getting set
