# HTT Frontend - Next.js

A professional login and dashboard application built with Next.js.

## Features

- 🎨 Professional UI with gradient design
- 🔐 Login page with authentication
- 📊 Dashboard with welcome section
- 📱 Fully responsive design
- ⚡ Built with Next.js 14

## Getting Started

### Prerequisites

- Node.js 16.x or higher
- npm or yarn

### Installation

1. Navigate to the frontend folder:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Run the development server:
   ```bash
   npm run dev
   ```

4. Open your browser and go to:
   ```
   http://localhost:3000
   ```

## Project Structure

```
frontend/
├── app/
│   ├── layout.js              # Root layout
│   ├── globals.css            # Global styles
│   ├── page.js                # Login page
│   ├── page.module.css        # Login page styles
│   └── dashboard/
│       ├── page.js            # Dashboard home page
│       └── dashboard.module.css # Dashboard styles
├── package.json               # Project dependencies
├── next.config.js             # Next.js configuration
├── jsconfig.json              # JavaScript config
└── .gitignore                 # Git ignore rules
```

## Usage

### Login Page
1. Navigate to the home page (http://localhost:3000)
2. Enter any email and password
3. Click "Sign In" to proceed to the dashboard

### Dashboard
- Shows a welcome message with the user's name
- Displays four dashboard cards (Analytics, Settings, Profile, Support)
- Click "Logout" to return to the login page

## Authentication

Currently uses client-side localStorage for session management. To integrate with your Spring Boot backend:

1. Update the login form to send credentials to your backend API
2. Store the JWT token in localStorage
3. Add token validation for protected routes

## Building for Production

```bash
npm run build
npm start
```

## License

MIT
