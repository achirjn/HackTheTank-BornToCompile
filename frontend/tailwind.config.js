/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: '#667eea',
        secondary: '#764ba2',
        danger: '#ff6b6b',
        dangerDark: '#ee5a52',
      },
    },
  },
  plugins: [],
}
