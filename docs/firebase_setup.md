# Firebase Setup Guide
## Create Firebase Account
https://firebase.google.com/

## Pricing
https://firebase.google.com/pricing

The "Spark Plan" is free and should be sufficient
* Authentication (Free except Phone Auth, but it is not necessary)
* Cloud Firestore 
(Free up to 1 GiB Data Storage and 10GiB/month Network egress )
* Cloud Messaging (Free without limits)

## Setup 
Click in Google Cloud Console on "Project Settings", gearwheel next to "Project Overview"

* Use value of "Project ID" for the environment variable FIREBASE_PROJECT_ID
* Use value of "Web API key" for the environment FIREBASE_PUBLIC_API_KEY

Click in Setttings on "Cloud Messaging" 
* Use value of "Server key" for the environment variable FIREBASE_PUSH_AUTHORIZATION_HEADER

### Enable Authentication
Go to Authentication in Google Cloud Console

* Activate Sign-in method "Email/Password"

* Add the domain where the app is hosted to the list of authorised domains

* Set the Advanced Option "One account per email address"

#### Add Server User 
This user is used by the server to read the tokens

Go to Authentication in Google Cloud Console
* In Tab Users add a "Email/Password-User"

Go to Database in Google Cloud Console
* Create a collection "users"
* Add a new document
* Add the boolean field 'SubscriptionTokensReader' and set it to true
* Add the string field 'email' and set it to the new registered user

Set the Environment Variables
* Set the FIREBASE_USERNAME variable to the username of the new registered user
* Set the FIREBASE_PASSWORD variable to the password of the new registered user

### Database
Go to Database in Google Cloud Console
* Create a collection "subscriptiontokens"

Go to Rules and set the rules to
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
  
    function signedInAndOwnDoc(userId) {
        return request.auth != null && 
        	request.auth.uid == userId;
    }
    
    function containsOwnMail() {
        return request.auth.token.email != null &&
          "email" in request.resource.data &&
          request.resource.data.email != null &&
        	request.auth.token.email == request.resource.data.email;
    }
    
    function isSubscriptionTokenReader(userId) {
        return userId != null && 
        exists(/databases/$(database)/documents/users/$(userId)) &&
        "SubscriptionTokensReader" in get(/databases/$(database)/documents/users/$(userId)).data &&
        get(/databases/$(database)/documents/users/$(userId)).data.SubscriptionTokensReader == true;
    }
    
    match /subscriptiontokens/{userId} {
      allow create, update: if signedInAndOwnDoc(userId) && containsOwnMail();
    }
    
    match /subscriptiontokens/{userId} {
      allow read: if signedInAndOwnDoc(userId);
    }
    
    match /subscriptiontokens/{userId} {
      allow read: if isSubscriptionTokenReader(request.auth.uid);
    }    
  }
}
```
* [Documentation](https://firebase.google.com/docs/firestore/)
* [Video Introduction](https://www.youtube.com/watch?v=v_hR4K4auoQ&list=PLl-K7zZEsYLluG5MCVEzXAQ7ACZBCuZgZ)

* [Security Rules Documentation](https://firebase.google.com/docs/firestore/security/get-started)
* [Security Rules Video Introduction](https://www.youtube.com/watch?v=eW5MdE3ZcAw)
