/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      fontFamily: {
        lexend: ['Lexend', 'sans-serif'],
      },
      colors: {
        primary: {
          DEFAULT: '#172E54',
        }
      }
    },
  },
  plugins: [
    require('flowbite/plugin')
  ],
}

