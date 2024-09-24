/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
    "./node_modules/flowbite/**/*.js"
  ],
  theme: {
    extend: {
      fontFamily: {
        lexend: ['Lexend', 'sans-serif'],
      },
      colors: {
        primary: {
          DEFAULT: '#172E54',
          dark: '#122443'
        }
      }
    },
  },
  plugins: [
    require('flowbite/plugin')
  ],
}

