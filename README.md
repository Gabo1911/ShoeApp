# ShoeApp

ShoeApp is an Android application designed to provide users with a seamless shoe shopping experience. The app allows users to sign up, log in, browse available shoes, add items to a cart, and complete their purchase through a checkout process.

## Features

- **User Authentication:** Sign up and log in securely using your email and password.
- **User Management:** Store user details like name, email, and mobile number.
- **Shopping Cart:** Add shoes to your cart, review items, and proceed to checkout.
- **Order Management:** Place and track orders, save delivery details including address and payment method.
- **Persistent Storage:** Stores user and order information locally using SQLite database.

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on your Android device or emulator.

## Structure

- `MainActivity` – Entry point with navigation to login/signup.
- `LoginPage` – Handles user login and session management.
- `SignUpPage` – New user registration.
- `CheckoutPage` – Review cart, enter address, and complete the order.
- `DatabaseHelper` – Manages local storage of user and order information.

## Requirements

- Android Studio (latest version recommended)
- Minimum SDK: (as set in the project)
- Internet connection (if real products or payment integration is added)

---

Welcome to ShoeApp – your companion for fast and easy shoe shopping!
